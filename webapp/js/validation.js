/**
 * validation.js
 * Phase 3 - Web Interface and Client Interaction
 *
 * Client-side validation (empty fields, format checks).
 * Each rule returns an error message, or "" when the value is valid.
 * The rules mirror the Java validation from Phase 1 and 2, so the browser
 * and the server agree on what a valid user looks like.
 */

const Validator = {

  fullName(value) {
    const v = value.trim();
    if (v === "") return "Enter your full name.";
    if (v.length < 3) return "Full name must be at least 3 characters.";
    if (!/^[A-Za-z][A-Za-z .'-]*$/.test(v)) return "Use letters and spaces only.";
    return "";
  },

  username(value) {
    const v = value.trim();
    if (v === "") return "Enter a username.";
    if (!/^[a-zA-Z0-9_]{4,}$/.test(v)) {
      return "Username needs at least 4 letters, digits or underscores.";
    }
    return "";
  },

  email(value) {
    const v = value.trim();
    if (v === "") return "Enter your email address.";
    if (!/^[\w.+-]+@[\w-]+\.[a-zA-Z]{2,}$/.test(v)) {
      return "Enter an email like name@example.com.";
    }
    return "";
  },

  phone(value) {
    const v = value.trim();
    if (v === "") return "Enter your phone number.";
    if (!/^[0-9]{10}$/.test(v)) return "Phone number must be exactly 10 digits.";
    return "";
  },

  password(value) {
    if (value === "") return "Enter a password.";
    if (value.length < 6 || !/[a-zA-Z]/.test(value) || !/[0-9]/.test(value)) {
      return "Password needs at least 6 characters with a letter and a digit.";
    }
    return "";
  },

  confirmPassword(value, original) {
    if (value === "") return "Re-enter your password.";
    if (value !== original) return "Passwords do not match.";
    return "";
  },

  // Login only checks that something was entered; the server decides if it is correct.
  required(value, label) {
    return value.trim() === "" ? "Enter your " + label + "." : "";
  }
};

/**
 * Small helpers for showing and clearing error messages next to a field.
 * Each input needs a sibling element with the id "<input id>-error".
 */
const FormUI = {

  setError(input, message) {
    const holder = input.closest(".field");
    const errorBox = document.getElementById(input.id + "-error");
    errorBox.textContent = message;
    holder.classList.toggle("has-error", message !== "");
    input.setAttribute("aria-invalid", message !== "" ? "true" : "false");
  },

  // Runs a rule on one field, shows the result, and returns true when valid.
  check(input, rule) {
    const message = rule();
    FormUI.setError(input, message);
    return message === "";
  },

  // Validate when the user leaves a field, and clear the error once they fix it.
  watch(input, rule) {
    input.addEventListener("blur", () => FormUI.check(input, rule));
    input.addEventListener("input", () => {
      if (input.getAttribute("aria-invalid") === "true") FormUI.check(input, rule);
    });
  }
};

// Allows the rules to be unit-tested in Node without a browser.
if (typeof module !== "undefined") {
  module.exports = { Validator };
}
