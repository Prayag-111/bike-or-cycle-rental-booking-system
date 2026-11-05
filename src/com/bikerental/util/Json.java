package com.bikerental.util;

/**
 * Json.java
 * Phase 4 - Server-Side Integration and Execution
 *
 * Writes text as a JSON string. The characters &lt; &gt; and &amp; are also escaped, so the
 * result is safe to place inside an HTML page as well as in a JSON response.
 */
public final class Json {

    private Json() {
    }

    public static String quote(String text) {
        if (text == null) {
            return "null";
        }
        StringBuilder out = new StringBuilder(text.length() + 2);
        out.append('"');
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            switch (c) {
                case '"':  out.append("\\\""); break;
                case '\\': out.append("\\\\"); break;
                case '\n': out.append("\\n");  break;
                case '\r': out.append("\\r");  break;
                case '\t': out.append("\\t");  break;
                case '<':
                case '>':
                case '&':
                case 0x2028:
                case 0x2029:
                    out.append(String.format("\\u%04x", (int) c));
                    break;
                default:
                    if (c < 0x20) {
                        out.append(String.format("\\u%04x", (int) c));
                    } else {
                        out.append(c);
                    }
            }
        }
        out.append('"');
        return out.toString();
    }
}
