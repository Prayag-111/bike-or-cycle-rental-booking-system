<%@ page contentType="text/html;charset=UTF-8" language="java" session="false" %>
<%@ page import="com.bikerental.util.Html" %>
<%@ include file="/WEB-INF/jspf/guard.jspf" %>
<%
    String pageTitle = "My bookings";
    String activePage = "bookings";
%>
<!DOCTYPE html>
<html lang="en">
<head>
<%@ include file="/WEB-INF/jspf/head.jspf" %>
</head>
<body>
<%@ include file="/WEB-INF/jspf/sprites.jspf" %>
<%@ include file="/WEB-INF/jspf/nav.jspf" %>

<main class="dash">
  <section class="hero bookings-hero" aria-labelledby="hero-title">
    <svg class="hero-art" viewBox="0 0 640 240" preserveAspectRatio="xMidYMid slice" aria-hidden="true"><use href="#scene"></use></svg>
    <div class="hero-text">
      <p class="hero-eyebrow">YOUR RIDES</p>
      <h2 id="hero-title">My bookings</h2>
      <p>Keep track of your active rides, check the time remaining, or find your next cycle.</p>
      <dl class="hero-stats">
        <div><dt>Active bookings</dt><dd id="stat-mine">&ndash;</dd></div>
        <div><dt>Cycles available</dt><dd id="stat-available">&ndash;</dd></div>
      </dl>
    </div>
  </section>

  <div class="page-grid">
    <section class="booking-panel" aria-labelledby="bookings-title">
      <div class="panel-heading">
        <div>
          <p class="panel-kicker">BOOKING OVERVIEW</p>
          <h3 id="bookings-title">Active rides</h3>
        </div>
        <a class="text-link" href="dashboard.jsp">Browse cycles <span aria-hidden="true">&rarr;</span></a>
      </div>
      <p class="panel-description">Your ride details and live countdowns appear here.</p>
      <div id="bookings"></div>
    </section>

    <aside class="activity-panel" aria-labelledby="activity-title">
      <div class="panel-heading">
        <div>
          <p class="panel-kicker">WHAT'S HAPPENING</p>
          <h3 id="activity-title">Live activity</h3>
        </div>
        <span class="activity-indicator" aria-hidden="true"></span>
      </div>
      <p class="panel-description">Recent booking updates from your rental fleet.</p>
      <ul class="activity" id="activity"></ul>
      <div class="activity-note">
        <strong>Need another ride?</strong>
        <p>Explore the available cycles and choose a ride that fits your plans.</p>
        <a class="text-link" href="dashboard.jsp">Explore cycles <span aria-hidden="true">&rarr;</span></a>
      </div>
    </aside>
  </div>
</main>

<%@ include file="/WEB-INF/jspf/foot.jspf" %>
</body>
</html>
