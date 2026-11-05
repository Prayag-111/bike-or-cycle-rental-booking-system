<%@ page contentType="text/html;charset=UTF-8" language="java" session="false" %>
<%@ page import="com.bikerental.util.Html" %>
<%@ include file="/WEB-INF/jspf/guard.jspf" %>
<%
    String pageTitle = "Cycles";
    String activePage = "cycles";

    int hour = java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY);
    String greeting = hour < 12 ? "Good morning" : (hour < 17 ? "Good afternoon" : "Good evening");
    String firstName = user.getFullName().trim().split("\\s+")[0];
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
  <noscript><div class="alert alert-error">This page needs JavaScript to show and book cycles.</div></noscript>

  <section class="hero" aria-labelledby="hero-title">
    <svg class="hero-art" viewBox="0 0 640 240" preserveAspectRatio="xMidYMid slice" aria-hidden="true"><use href="#scene"></use></svg>
    <div class="hero-text">
      <h2 id="hero-title"><%= greeting %>, <%= Html.escape(firstName) %></h2>
      <p>Pick a cycle, choose the hours and ride. This page updates live.</p>
      <dl class="hero-stats">
        <div><dt>Cycles available</dt><dd id="stat-available">&ndash;</dd></div>
        <div><dt>Your active bookings</dt><dd id="stat-mine">&ndash;</dd></div>
      </dl>
    </div>
  </section>

  <section aria-labelledby="cycles-title">
    <h3 id="cycles-title" class="section-title">Choose your ride</h3>
    <div class="chips" id="chips" role="group" aria-label="Filter cycles by type"></div>
    <ul class="cycle-grid" id="cycle-list"></ul>
  </section>
</main>

<%@ include file="/WEB-INF/jspf/foot.jspf" %>
</body>
</html>
