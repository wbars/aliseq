package me.wbars.aliseq.core;

import me.wbars.aliseq.utils.StringUtils;

import static java.lang.String.format;

public class Alignment {
    public static final Alignment EMPTY = new Alignment("", "");
    private final String first;
    private final String second;

    public Alignment(String first, String second) {
        int maxLength = Math.max(first.length(), second.length());
        this.first = appendGapsToLength(first, maxLength);
        this.second = appendGapsToLength(second, maxLength);
    }

    private String appendGapsToLength(String s, int len) {
        return StringUtils.repeat("-", len - s.length()) + s;
    }

    public String first() {
        return first;
    }

    public String second() {
        return second;
    }

    @Override
    public String toString() {
        return format("First: %s%nSecond: %s", first, second);
    }

    public int length() {
        return Math.min(first.length(), second.length());
    }

    public boolean sameChars(int i) {
        if (i < 0 || i > length()) throw new IllegalArgumentException();
        return first.charAt(i) == second.charAt(i);
    }

    public boolean hasGap(int i) {
        if (i < 0 || i > length()) throw new IllegalArgumentException();
        return first.charAt(i) == '-' || second.charAt(i) == '-';
    }
}
