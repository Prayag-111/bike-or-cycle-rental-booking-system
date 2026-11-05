/**
 * register.js
 * Phase 4 - Server-Side Integration and Execution
 *
 * Checks the form in the browser first (fast feedback). If everything is valid the
 * form is submitted to RegisterServlet (POST /register). The server validates again,
 * because browser checks can be bypassed.
 */

const form = document.getElementById("registerForm");

const fields = {
  fullName: document.getElementById("fullName"),
  username: document.getElementById("username"),
  email: document.getElementById("email"),
  phoneNumber: document.getElementById("phoneNumber"),
  password: document.getElementById("password"),
  confirmPassword: document.getElementById("confirmPassword")
};

const rules = {
  fullName: () => Validator.fullName(fields.fullName.value),
  username: () => Validator.username(fields.username.value),
  email: () => Validator.email(fields.email.value),
  phoneNumber: () => Validator.phone(fields.phoneNumber.value),
  password: () => Validator.password(fields.password.value),
  confirmPassword: () =>
    Validator.confirmPassword(fields.confirmPassword.value, fields.password.value)
};

Object.keys(fields).forEach((name) => FormUI.watch(fields[name], rules[name]));

form.addEventListener("submit", (event) => {
  event.preventDefault();

  let firstInvalid = null;
  Object.keys(fields).forEach((name) => {
    const valid = FormUI.check(fields[name], rules[name]);
    if (!valid && firstInvalid === null) firstInvalid = fields[name];
  });

  if (firstInvalid !== null) {
    firstInvalid.focus();
    return;
  }

  form.submit();   // all fields are valid: send the data to the servlet
});
