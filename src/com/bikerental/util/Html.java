package com.bikerental.util;

/**
 * Html.java
 * Phase 4 - Server-Side Integration and Execution
 *
 * Escapes text before it is written into a web page, so that user input such as
 * "<script>" is shown as plain text instead of being run by the browser.
 */
public final class Html {

    private Html() {
    }

    public static String escape(String text) {
        if (text == null) {
            return "";
        }
        StringBuilder out = new StringBuilder(text.length() + 16);
        for (char c : text.toCharArray()) {
            switch (c) {
                case '&':  out.append("&amp;");  break;
                case '<':  out.append("&lt;");   break;
                case '>':  out.append("&gt;");   break;
                case '"':  out.append("&quot;"); break;
                case '\'': out.append("&#39;");  break;
                default:   out.append(c);
            }
        }
        return out.toString();
    }
}
