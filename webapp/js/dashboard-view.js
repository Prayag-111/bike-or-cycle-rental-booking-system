/**
 * dashboard-view.js
 * Phase 4 - Server-Side Integration and Execution
 *
 * Turns the dashboard "state" (JSON from the server) into HTML.
 * These are plain functions with no page access, so they are easy to test.
 * Every piece of text from the server is escaped before it goes into the page.
 */

function escapeHtml(text) {
  return String(text).replace(/[&<>"']/g, function (ch) {
    return { "&": "&amp;", "<": "&lt;", ">": "&gt;", '"': "&quot;", "'": "&#39;" }[ch];
  });
}

function money(amount) {
  const n = Number(amount);
  return "Rs. " + (Number.isInteger(n) ? n : n.toFixed(2));
}

function formatRemaining(ms) {
  if (ms <= 0) return "Ended";
  const total = Math.floor(ms / 1000);
  const h = Math.floor(total / 3600);
  const m = Math.floor((total % 3600) / 60);
  const s = total % 60;
  if (h > 0) return h + "h " + m + "m " + s + "s";
  if (m > 0) return m + "m " + s + "s";
  return s + "s";
}

function progressPercent(leftMs, durationMs) {
  if (durationMs <= 0) return 100;
  const used = Math.round((1 - leftMs / durationMs) * 100);
  return Math.min(100, Math.max(0, used));
}

function timeAgo(ms) {
  const sec = Math.max(0, Math.floor(ms / 1000));
  if (sec < 10) return "just now";
  if (sec < 60) return sec + " s ago";
  const min = Math.floor(sec / 60);
  if (min < 60) return min + " min ago";
  return Math.floor(min / 60) + " h ago";
}

function cycleTypes(state) {
  const seen = [];
  state.cycles.forEach(function (c) {
    if (seen.indexOf(c.type) === -1) seen.push(c.type);
  });
  return seen;
}

function chipsHtml(types, active) {
  return ["all"].concat(types).map(function (t) {
    return '<button type="button" class="chip" data-filter="' + escapeHtml(t) +
      '" aria-pressed="' + (t === active) + '">' + (t === "all" ? "All" : escapeHtml(t)) + "</button>";
  }).join("");
}

function typeClass(type) {
  return "type-" + String(type).toLowerCase().replace(/[^a-z0-9]/g, "");
}

function bikeSymbol(type) {
  const known = { mountain: "bike-mountain", hybrid: "bike-hybrid", road: "bike-road", electric: "bike-electric" };
  return known[String(type).toLowerCase()] || "bike-hybrid";
}

function cyclesHtml(state, filter, hoursById) {
  return state.cycles
    .filter(function (c) { return filter === "all" || c.type === filter; })
    .map(function (c) {
      const id = escapeHtml(c.id);

      const art = '<div class="card-art"><svg class="bike-pic" viewBox="0 0 240 140" role="img" aria-label="' +
        escapeHtml(c.type) + ' cycle"><use href="#' + bikeSymbol(c.type) + '"></use></svg>' +
        (c.available ? "" : '<span class="ribbon">Booked</span>') + "</div>";

      const details = '<div class="card-title"><strong>' + escapeHtml(c.name) + '</strong><span class="tag">' +
        escapeHtml(c.type) + "</span></div>" +
        '<p class="price"><b>' + money(c.rate) + "</b> per hour</p>";

      let controls;
      if (!c.available) {
        controls = '<p class="unavailable">Not available right now</p>';
      } else {
        const chosen = Number((hoursById && hoursById[c.id]) || 1);
        let options = "";
        for (let h = 1; h <= 8; h++) {
          options += '<option value="' + h + '"' + (h === chosen ? " selected" : "") + ">" +
            h + (h === 1 ? " hour" : " hours") + "</option>";
        }
        controls = '<div class="book-form">' +
          '<label class="sr-only" for="hours-' + id + '">Hours for ' + escapeHtml(c.name) + "</label>" +
          '<select id="hours-' + id + '" data-hours-for="' + id + '">' + options + "</select>" +
          '<output class="cost" aria-live="polite">' + money(c.rate * chosen) + "</output>" +
          '<button type="button" class="btn btn-small" data-action="book" data-id="' + id + '">Book</button>' +
          "</div>";
      }

      return '<li class="cycle cycle-card ' + typeClass(c.type) + (c.available ? "" : " is-booked") +
        '" data-id="' + id + '">' + art + '<div class="card-body">' + details + controls + "</div></li>";
    }).join("");
}

// Used to decide if the bookings table must be drawn again (the countdown is updated separately)
function bookingsSignature(state) {
  return state.bookings.map(function (b) {
    return b.id + ":" + b.hours + ":" + b.endsAt;
  }).join("|");
}

function bookingsHtml(state, now) {
  if (state.bookings.length === 0) {
    return '<p class="empty">No active bookings yet. <a href="dashboard.jsp">Browse cycles</a> to start.</p>';
  }

  const rows = state.bookings.map(function (b) {
    const id = escapeHtml(b.id);
    const duration = Number(b.hours) * 3600000;
    const left = Number(b.endsAt) - now;
    return '<tr data-id="' + id + '"><td>' + id + "</td><td>" + escapeHtml(b.cycle) + "</td><td>" +
      Number(b.hours) + "</td><td>" + money(b.total) + "</td>" +
      '<td><span class="remaining" data-ends="' + Number(b.endsAt) + '" data-duration="' + duration + '">' +
      formatRemaining(left) + "</span>" +
      '<span class="bar"><span class="bar-fill" style="width:' + progressPercent(left, duration) + '%"></span></span></td>' +
      '<td><button type="button" class="btn btn-small btn-outline" data-action="cancel" data-id="' + id +
      '">Cancel</button></td></tr>';
  }).join("");

  return '<div class="table-wrap"><table class="cycles"><thead><tr><th>Booking</th><th>Cycle</th>' +
    '<th>Hours</th><th>Total</th><th>Time left</th><th><span class="sr-only">Action</span></th></tr></thead>' +
    "<tbody>" + rows + "</tbody></table></div>";
}

function activityHtml(state, now) {
  if (state.activity.length === 0) {
    return '<li class="activity-empty">No activity yet.</li>';
  }
  return state.activity.map(function (a) {
    return '<li><span class="who">' + escapeHtml(a.who) + "</span> " + escapeHtml(a.text) +
      '<span class="ago">' + timeAgo(now - Number(a.time)) + "</span></li>";
  }).join("");
}

const DashboardView = {
  escapeHtml: escapeHtml, money: money, formatRemaining: formatRemaining,
  progressPercent: progressPercent, timeAgo: timeAgo, cycleTypes: cycleTypes,
  chipsHtml: chipsHtml, cyclesHtml: cyclesHtml, bookingsSignature: bookingsSignature,
  bookingsHtml: bookingsHtml, activityHtml: activityHtml
};

// Allows the functions to be tested in Node without a browser.
if (typeof module !== "undefined") {
  module.exports = DashboardView;
}
