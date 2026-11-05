/**
 * dashboard.js
 * Phase 4 - Server-Side Integration and Execution
 *
 * Makes the dashboard live, without reloading the page:
 *   - asks the server for the latest state every few seconds (api/state)
 *   - books and cancels with fetch() (api/book, api/cancel) and updates the page at once
 *   - counts down the time left on each booking every second
 *   - filters cycles, shows the cost as the hours change, and shows toast messages
 * The server checks every booking; this script only makes the page feel instant.
 */
(function () {
  "use strict";

  var V = DashboardView;
  var POLL_MS = 4000;

  // Each page shows only some of these parts, so a missing part becomes a hidden dummy element
  function pick(id) {
    return document.getElementById(id) || document.createElement("div");
  }

  var els = {
    chips: pick("chips"),
    cycles: pick("cycle-list"),
    bookings: pick("bookings"),
    activity: pick("activity"),
    available: pick("stat-available"),
    mine: pick("stat-mine"),
    navCount: pick("nav-count"),
    toasts: pick("toasts"),
    pill: pick("live-pill"),
    pillText: pick("live-text")
  };

  var state = null;          // latest state from the server
  var filter = "all";        // selected cycle type
  var hoursById = {};        // hours chosen for each cycle, kept between redraws
  var clockOffset = 0;       // server time minus this browser's time
  var last = { chips: null, cycles: null, bookings: null, activity: null };
  var polling = false;
  var lastEndedPoll = 0;

  function now() {
    return Date.now() + clockOffset;
  }

  function findCycle(s, id) {
    for (var i = 0; i < s.cycles.length; i++) {
      if (s.cycles[i].id === id) return s.cycles[i];
    }
    return null;
  }

  function byDataId(container, id) {
    var items = container.children;
    for (var i = 0; i < items.length; i++) {
      if (items[i].getAttribute("data-id") === id) return items[i];
    }
    return null;
  }

  function restartAnimation(el, className) {
    el.classList.remove(className);
    void el.offsetWidth;          // lets the browser notice the class was removed
    el.classList.add(className);
  }

  function setText(el, text) {
    if (el.textContent === text) return;
    el.textContent = text;
    restartAnimation(el, "bump");
  }

  // Draws a part of the page only when its HTML changed (so open drop-downs are not closed)
  function paint(key, el, html) {
    if (last[key] === html) return;
    el.innerHTML = html;
    last[key] = html;
  }

  function render(changedIds) {
    var t = now();

    setText(els.available, state.availableCount + " of " + state.totalCount);
    setText(els.mine, String(state.bookings.length));
    setText(els.navCount, state.bookings.length > 0 ? String(state.bookings.length) : "");

    var types = V.cycleTypes(state);
    if (filter !== "all" && types.indexOf(filter) === -1) filter = "all";

    paint("chips", els.chips, V.chipsHtml(types, filter));
    paint("cycles", els.cycles, V.cyclesHtml(state, filter, hoursById));

    changedIds.forEach(function (id) {
      var row = byDataId(els.cycles, id);
      if (row) restartAnimation(row, "flash");
    });

    // The countdown text changes every second, so compare only the booking details
    var signature = V.bookingsSignature(state);
    if (last.bookingsSignature !== signature) {
      els.bookings.innerHTML = V.bookingsHtml(state, t);
      last.bookingsSignature = signature;
    }

    paint("activity", els.activity, V.activityHtml(state, t));
  }

  function apply(newState) {
    if (!newState || !newState.cycles) throw new Error("Unexpected reply");

    clockOffset = newState.serverTime - Date.now();

    // Which cycles changed since the last picture (for example booked by someone else)
    var changed = [];
    if (state) {
      newState.cycles.forEach(function (c) {
        var old = findCycle(state, c.id);
        if (old && old.available !== c.available) changed.push(c.id);
      });
    }

    state = newState;
    render(changed);
  }

  // Runs every second: updates the time left on each booking
  function tick() {
    var nodes = document.querySelectorAll(".remaining");
    var anyEnded = false;

    for (var i = 0; i < nodes.length; i++) {
      var node = nodes[i];
      var left = Number(node.getAttribute("data-ends")) - now();
      var duration = Number(node.getAttribute("data-duration"));
      node.textContent = V.formatRemaining(left);

      var fill = node.parentNode.querySelector(".bar-fill");
      if (fill) fill.style.width = V.progressPercent(left, duration) + "%";
      if (left <= 0) anyEnded = true;
    }

    // A booking just ended: ask the server so the cycle shows as available again
    if (anyEnded && Date.now() - lastEndedPoll > 3000) {
      lastEndedPoll = Date.now();
      poll();
    }
  }

  function toast(message, kind) {
    while (els.toasts.children.length >= 4) {
      els.toasts.removeChild(els.toasts.children[0]);
    }
    var node = document.createElement("div");
    node.className = "toast toast-" + kind;
    node.textContent = message;

    function remove() {
      if (node.parentNode) node.parentNode.removeChild(node);
    }
    node.addEventListener("click", remove);
    els.toasts.appendChild(node);
    setTimeout(remove, kind === "error" ? 6000 : 4500);
  }

  function setLive(isLive) {
    if (isLive) {
      els.pill.classList.remove("is-offline");
      els.pillText.textContent = "Live";
    } else {
      els.pill.classList.add("is-offline");
      els.pillText.textContent = "Reconnecting...";
    }
  }

  function redrawCycles() {
    last.cycles = null;           // forces the buttons to be drawn again (and enabled)
    render([]);
  }

  function post(url, body) {
    return fetch(url, {
      method: "POST",
      credentials: "same-origin",
      headers: {
        "X-Requested-With": "XMLHttpRequest",
        "Content-Type": "application/x-www-form-urlencoded;charset=UTF-8"
      },
      body: body
    }).then(function (response) { return response.json(); });
  }

  function handleReply(data) {
    if (data.loggedOut) {
      window.location.href = "login.jsp?required=1";
      return;
    }
    if (data.state) {
      last.cycles = null;
      apply(data.state);
    }
    toast(data.message || "Done.", data.ok ? "ok" : "error");
  }

  function handleFailure() {
    toast("Could not reach the server. Please try again.", "error");
    setLive(false);
    if (state) redrawCycles();
  }

  function book(button) {
    var select = button.parentNode.querySelector("select");
    var hours = select ? select.value : "1";

    button.disabled = true;
    button.textContent = "Booking...";

    post("api/book", "cycleId=" + encodeURIComponent(button.getAttribute("data-id")) +
         "&hours=" + encodeURIComponent(hours))
      .then(handleReply)
      .catch(handleFailure);
  }

  function cancel(button) {
    // First click asks "Sure?", a second click within 3 seconds cancels
    if (button.getAttribute("data-confirm") !== "1") {
      button.setAttribute("data-confirm", "1");
      button.textContent = "Sure? Click again";
      button.classList.add("confirming");
      setTimeout(function () {
        if (button.getAttribute("data-confirm") === "1") {
          button.setAttribute("data-confirm", "0");
          button.textContent = "Cancel";
          button.classList.remove("confirming");
        }
      }, 3000);
      return;
    }

    button.disabled = true;
    button.textContent = "Cancelling...";

    post("api/cancel", "bookingId=" + encodeURIComponent(button.getAttribute("data-id")))
      .then(handleReply)
      .catch(handleFailure);
  }

  function poll() {
    if (polling) return;
    polling = true;

    fetch("api/state", {
      credentials: "same-origin",
      cache: "no-store",
      headers: { "X-Requested-With": "XMLHttpRequest" }
    })
      .then(function (response) { return response.json(); })
      .then(function (data) {
        if (data.loggedOut) {
          window.location.href = "login.jsp?required=1";
          return;
        }
        apply(data);
        setLive(true);
      })
      .catch(function () { setLive(false); })
      .then(function () { polling = false; });
  }

  // ---- events (one listener per area, so redrawn buttons keep working) ----

  els.chips.addEventListener("click", function (event) {
    var chip = event.target.closest("[data-filter]");
    if (!chip || !state) return;
    filter = chip.getAttribute("data-filter");
    render([]);
  });

  els.cycles.addEventListener("click", function (event) {
    var button = event.target.closest('[data-action="book"]');
    if (button) book(button);
  });

  els.cycles.addEventListener("change", function (event) {
    var select = event.target;
    var id = select.getAttribute("data-hours-for");
    if (!id || !state) return;

    hoursById[id] = Number(select.value);

    var cycle = findCycle(state, id);
    var row = select.closest(".cycle");
    var output = row ? row.querySelector(".cost") : null;
    if (cycle && output) output.textContent = V.money(cycle.rate * hoursById[id]);

    // The page already shows this choice, so the next refresh must not redraw it
    last.cycles = V.cyclesHtml(state, filter, hoursById);
  });

  els.bookings.addEventListener("click", function (event) {
    var button = event.target.closest('[data-action="cancel"]');
    if (button) cancel(button);
  });

  // ---- start ----

  try {
    apply(JSON.parse(document.getElementById("initial-state").textContent));
  } catch (e) {
    poll();
  }

  setInterval(tick, 1000);
  setInterval(function () { if (!document.hidden) poll(); }, POLL_MS);
  document.addEventListener("visibilitychange", function () { if (!document.hidden) poll(); });

})();
