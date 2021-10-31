package net.pinger.disguise.utils;

import net.pinger.common.lang.Pair;

public class SimplePair<T, R> implements Pair {

    private T first;
    private R second;

    public void setFirst(T first) {
        this.first = first;
    }

    public void setSecond(R second) {
        this.second = second;
    }

    @Override
    public T getFirst() {
        return this.first;
    }

    @Override
    public R getSecond() {
        return this.second;
    }
}
