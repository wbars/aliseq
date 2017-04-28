package me.wbars.aliseq.utils;

public class Pair<T, S> {
    private final T first;
    private final S second;

    public Pair(T first, S second) {
        this.first = first;
        this.second = second;
    }

    public T first() {
        return first;
    }

    public S second() {
        return second;
    }
}
