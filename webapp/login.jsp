<%@ page contentType="text/html;charset=UTF-8" language="java" session="false" %>
<%@ page import="com.bikerental.util.Html" %>
<%
    // Message sent back by LoginServlet after a failed login
    String error = (String) request.getAttribute("error");

    // Messages shown after redirects (registered, logged out, dashboard needs login)
    String notice = null;
    if ("1".equals(request.getParameter("registered"))) {
        notice = "Registration successful. Please log in.";
    } else if ("1".equals(request.getParameter("loggedout"))) {
        notice = "You have been logged out.";
    } else if ("1".equals(request.getParameter("required"))) {
        notice = "Please log in to open the dashboard.";
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Log in | Bike Rental</title>
  <link rel="preconnect" href="https://fonts.googleapis.com">
  <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
  <link href="https://fonts.googleapis.com/css2?family=Bricolage+Grotesque:wght@600;700&family=Source+Sans+3:wght@400;600&display=swap" rel="stylesheet">
  <link rel="stylesheet" href="css/style.css?v=3">
</head>
<body>
<main class="split">

  <aside class="brand">
    <div>
      <h1>Welcome back. Your ride is waiting.</h1>
      <p>Log in to see available cycles and manage your bookings.</p>
    </div>
    <svg class="wheel" viewBox="0 0 400 400" role="img" aria-label="Bicycle wheel illustration">
      <circle class="rim" cx="200" cy="200" r="185"/>
      <circle class="rim" cx="200" cy="200" r="168" style="stroke-width:2"/>
      <line class="spoke" x1="200" y1="200" x2="200" y2="30" transform="rotate(0 200 200)"/>
      <line class="spoke" x1="200" y1="200" x2="200" y2="30" transform="rotate(22.5 200 200)"/>
      <line class="spoke" x1="200" y1="200" x2="200" y2="30" transform="rotate(45 200 200)"/>
      <line class="spoke" x1="200" y1="200" x2="200" y2="30" transform="rotate(67.5 200 200)"/>
      <line class="spoke" x1="200" y1="200" x2="200" y2="30" transform="rotate(90 200 200)"/>
      <line class="spoke" x1="200" y1="200" x2="200" y2="30" transform="rotate(112.5 200 200)"/>
      <line class="spoke" x1="200" y1="200" x2="200" y2="30" transform="rotate(135 200 200)"/>
      <line class="spoke" x1="200" y1="200" x2="200" y2="30" transform="rotate(157.5 200 200)"/>
      <line class="spoke" x1="200" y1="200" x2="200" y2="30" transform="rotate(180 200 200)"/>
      <line class="spoke" x1="200" y1="200" x2="200" y2="30" transform="rotate(202.5 200 200)"/>
      <line class="spoke" x1="200" y1="200" x2="200" y2="30" transform="rotate(225 200 200)"/>
      <line class="spoke" x1="200" y1="200" x2="200" y2="30" transform="rotate(247.5 200 200)"/>
      <line class="spoke" x1="200" y1="200" x2="200" y2="30" transform="rotate(270 200 200)"/>
      <line class="spoke" x1="200" y1="200" x2="200" y2="30" transform="rotate(292.5 200 200)"/>
      <line class="spoke" x1="200" y1="200" x2="200" y2="30" transform="rotate(315 200 200)"/>
      <line class="spoke" x1="200" y1="200" x2="200" y2="30" transform="rotate(337.5 200 200)"/>
      <circle class="hub" cx="200" cy="200" r="16"/>
    </svg>
  </aside>

  <section class="panel">
    <div class="panel-inner">
      <h2>Log in</h2>
      <p class="lead">Use the username and password you registered with.</p>

      <% if (error != null) { %>
      <div class="alert alert-error" role="alert"><%= Html.escape(error) %></div>
      <% } %>
      <% if (notice != null) { %>
      <div class="alert alert-ok" role="status"><%= Html.escape(notice) %></div>
      <% } %>

      <form id="loginForm" action="login" method="post" novalidate>
      <div class="field">
        <label for="username">Username</label>
        <input type="text" id="username" name="username" autocomplete="username" aria-describedby="username-error">
        <span class="error-text" id="username-error" aria-live="polite"></span>
      </div>
      <div class="field">
        <label for="password">Password</label>
        <input type="password" id="password" name="password" autocomplete="current-password" aria-describedby="password-error">
        <span class="error-text" id="password-error" aria-live="polite"></span>
      </div>
        <button type="submit" class="btn">Log in</button>
      </form>

      <p class="switch">New here? <a href="register.jsp">Create an account</a></p>
    </div>
  </section>

</main>
<script src="js/validation.js?v=3"></script>
<script src="js/login.js?v=3"></script>
</body>
</html>
