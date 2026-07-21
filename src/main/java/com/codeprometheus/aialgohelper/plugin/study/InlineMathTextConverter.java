package com.codeprometheus.aialgohelper.plugin.study;

import java.util.Map;
import java.util.Set;

final class InlineMathTextConverter {

    private static final Set<String> TEXT_COMMANDS = Set.of(
            "mathcal", "mathrm", "mathbf", "mathit", "text", "operatorname"
    );

    private static final Map<String, String> SYMBOLS = Map.ofEntries(
            Map.entry("le", "≤"),
            Map.entry("leq", "≤"),
            Map.entry("ge", "≥"),
            Map.entry("geq", "≥"),
            Map.entry("ne", "≠"),
            Map.entry("neq", "≠"),
            Map.entry("times", "×"),
            Map.entry("cdot", "·"),
            Map.entry("infty", "∞"),
            Map.entry("to", "→"),
            Map.entry("rightarrow", "→"),
            Map.entry("leftarrow", "←"),
            Map.entry("log", "log"),
            Map.entry("ln", "ln")
    );

    private InlineMathTextConverter() {
    }

    static String toPlainText(String text) {
        StringBuilder result = new StringBuilder(text.length());
        int index = 0;
        while (index < text.length()) {
            if (text.charAt(index) != '$' || isEscaped(text, index)) {
                result.append(text.charAt(index++));
                continue;
            }

            int end = findClosingDelimiter(text, index + 1);
            if (end < 0) {
                result.append(text.charAt(index++));
                continue;
            }

            result.append(new LatexReader(text.substring(index + 1, end)).read());
            index = end + 1;
        }
        return result.toString();
    }

    private static int findClosingDelimiter(String text, int fromIndex) {
        for (int index = fromIndex; index < text.length(); index++) {
            if (text.charAt(index) == '$' && !isEscaped(text, index)) {
                return index;
            }
        }
        return -1;
    }

    private static boolean isEscaped(String text, int index) {
        int slashCount = 0;
        for (int cursor = index - 1; cursor >= 0 && text.charAt(cursor) == '\\'; cursor--) {
            slashCount++;
        }
        return slashCount % 2 != 0;
    }

    private static final class LatexReader {

        private final String source;
        private int index;

        private LatexReader(String source) {
            this.source = source;
        }

        private String read() {
            return readUntil('\0');
        }

        private String readUntil(char terminator) {
            StringBuilder result = new StringBuilder();
            while (index < source.length()) {
                char current = source.charAt(index++);
                if (current == terminator) {
                    break;
                }
                if (current == '\\') {
                    appendCommand(result);
                } else if (current == '{') {
                    result.append(readUntil('}'));
                } else if (current != '}') {
                    result.append(current == '~' ? ' ' : current);
                }
            }
            return result.toString();
        }

        private void appendCommand(StringBuilder result) {
            if (index >= source.length()) {
                return;
            }
            if (!Character.isLetter(source.charAt(index))) {
                result.append(source.charAt(index++));
                return;
            }

            int start = index;
            while (index < source.length() && Character.isLetter(source.charAt(index))) {
                index++;
            }
            String command = source.substring(start, index);
            if ("left".equals(command) || "right".equals(command)) {
                return;
            }
            if (TEXT_COMMANDS.contains(command)) {
                result.append(readGroup());
                return;
            }
            if ("frac".equals(command)) {
                result.append(readGroup()).append('/').append(readGroup());
                return;
            }
            if ("sqrt".equals(command)) {
                result.append('√').append(readGroup());
                return;
            }
            result.append(SYMBOLS.getOrDefault(command, command));
        }

        private String readGroup() {
            while (index < source.length() && Character.isWhitespace(source.charAt(index))) {
                index++;
            }
            if (index >= source.length() || source.charAt(index) != '{') {
                return "";
            }
            index++;
            return readUntil('}');
        }
    }
}
