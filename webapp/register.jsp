<%@ page contentType="text/html;charset=UTF-8" language="java" session="false" %>
<%@ page import="com.bikerental.util.Html" %>
<%
    // Message sent back by RegisterServlet when the server rejects the data
    String error = (String) request.getAttribute("error");
%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Create account | Bike Rental</title>
  <link rel="preconnect" href="https://fonts.googleapis.com">
  <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
  <link href="https://fonts.googleapis.com/css2?family=Bricolage+Grotesque:wght@600;700&family=Source+Sans+3:wght@400;600&display=swap" rel="stylesheet">
  <link rel="stylesheet" href="css/style.css?v=3">
</head>
<body>
<main class="split">

  <aside class="brand">
    <div>
      <h1>Book a cycle in a few taps.</h1>
      <p>Register once, then reserve a bike by the hour.</p>
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
      <h2>Create your account</h2>
      <p class="lead">It takes a minute. You will use these details to log in.</p>

      <% if (error != null) { %>
      <div class="alert alert-error" role="alert"><%= Html.escape(error) %></div>
      <% } %>

      <form id="registerForm" action="register" method="post" novalidate>
      <div class="field">
        <label for="fullName">Full name</label>
        <input type="text" id="fullName" name="fullName" autocomplete="name" aria-describedby="fullName-error">
        <span class="error-text" id="fullName-error" aria-live="polite"></span>
      </div>
      <div class="field">
        <label for="username">Username</label>
        <input type="text" id="username" name="username" autocomplete="username" aria-describedby="username-hint username-error">
        <span class="hint" id="username-hint">At least 4 letters, digits or underscores.</span>
        <span class="error-text" id="username-error" aria-live="polite"></span>
      </div>
      <div class="field">
        <label for="email">Email</label>
        <input type="email" id="email" name="email" autocomplete="email" aria-describedby="email-error">
        <span class="error-text" id="email-error" aria-live="polite"></span>
      </div>
      <div class="field">
        <label for="phoneNumber">Phone number</label>
        <input type="tel" id="phoneNumber" name="phoneNumber" autocomplete="tel" inputmode="numeric" aria-describedby="phoneNumber-hint phoneNumber-error">
        <span class="hint" id="phoneNumber-hint">10 digits, no spaces.</span>
        <span class="error-text" id="phoneNumber-error" aria-live="polite"></span>
      </div>
      <div class="field">
        <label for="password">Password</label>
        <input type="password" id="password" name="password" autocomplete="new-password" aria-describedby="password-hint password-error">
        <span class="hint" id="password-hint">At least 6 characters, with a letter and a digit.</span>
        <span class="error-text" id="password-error" aria-live="polite"></span>
      </div>
      <div class="field">
        <label for="confirmPassword">Confirm password</label>
        <input type="password" id="confirmPassword" name="confirmPassword" autocomplete="new-password" aria-describedby="confirmPassword-error">
        <span class="error-text" id="confirmPassword-error" aria-live="polite"></span>
      </div>
        <button type="submit" class="btn">Create account</button>
      </form>

      <p class="switch">Already registered? <a href="login.jsp">Log in</a></p>
    </div>
  </section>

</main>
<script src="js/validation.js?v=3"></script>
<script src="js/register.js?v=3"></script>
</body>
</html>
