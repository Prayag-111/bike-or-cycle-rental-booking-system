<%@ page contentType="text/html;charset=UTF-8" language="java" session="false" %>
<%@ page import="com.bikerental.util.Html" %>
<%@ include file="/WEB-INF/jspf/guard.jspf" %>
<%
    String pageTitle = "Profile";
    String activePage = "profile";

    String[] nameParts = user.getFullName().trim().split("\\s+");
    String initials = nameParts[0].substring(0, 1).toUpperCase()
            + (nameParts.length > 1 ? nameParts[nameParts.length - 1].substring(0, 1).toUpperCase() : "");
%>
<!DOCTYPE html>
<html lang="en">
<head>
<%@ include file="/WEB-INF/jspf/head.jspf" %>
</head>
<body>
<%@ include file="/WEB-INF/jspf/nav.jspf" %>

<main class="dash">
  <h2 class="page-title">My profile</h2>
  <p class="lead">Your account details.</p>

  <div class="profile">
    <div class="profile-side">
      <div class="avatar" aria-hidden="true"><%= Html.escape(initials) %></div>
      <h3 class="profile-name"><%= Html.escape(user.getFullName()) %></h3>
      <p class="profile-user">@<%= Html.escape(user.getUsername()) %></p>
    </div>

    <div class="profile-card">
      <h3>Personal information</h3>
      <dl class="info">
        <div><dt>Full name</dt><dd><%= Html.escape(user.getFullName()) %></dd></div>
        <div><dt>Username</dt><dd><%= Html.escape(user.getUsername()) %></dd></div>
        <div><dt>Email</dt><dd><%= Html.escape(user.getEmail()) %></dd></div>
        <div><dt>Phone</dt><dd><%= Html.escape(user.getPhoneNumber()) %></dd></div>
        <div><dt>User ID</dt><dd><%= Html.escape(user.getUserId()) %></dd></div>
      </dl>

      <h3>This session</h3>
      <dl class="info">
        <div><dt>Signed in at</dt><dd><%= new java.util.Date(userSession.getCreationTime()) %></dd></div>
        <div><dt>Session timeout</dt><dd><%= userSession.getMaxInactiveInterval() / 60 %> minutes after you leave the site</dd></div>
        <div><dt>Active bookings</dt><dd id="stat-mine">&ndash;</dd></div>
      </dl>
    </div>
  </div>
</main>

<%@ include file="/WEB-INF/jspf/foot.jspf" %>
</body>
</html>
