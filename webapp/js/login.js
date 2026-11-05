/**
 * login.js
 * Phase 4 - Server-Side Integration and Execution
 *
 * Checks for empty fields in the browser, then submits the form to
 * LoginServlet (POST /login). The server decides if the credentials are correct.
 */

const form = document.getElementById("loginForm");
const username = document.getElementById("username");
const password = document.getElementById("password");

const rules = {
  username: () => Validator.required(username.value, "username"),
  password: () => Validator.required(password.value, "password")
};

FormUI.watch(username, rules.username);
FormUI.watch(password, rules.password);

form.addEventListener("submit", (event) => {
  event.preventDefault();

  const usernameOk = FormUI.check(username, rules.username);
  const passwordOk = FormUI.check(password, rules.password);

  if (!usernameOk || !passwordOk) {
    (usernameOk ? password : username).focus();
    return;
  }

  form.submit();   // send the credentials to the servlet
});
