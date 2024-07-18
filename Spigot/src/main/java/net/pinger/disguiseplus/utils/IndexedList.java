package net.pinger.disguiseplus.utils;

import java.util.ArrayList;

public class IndexedList<E> extends ArrayList<E> {

    public E back(int fromEnd) {
        return this.get(this.size() - 1 - fromEnd);
    }

    public E back() {
        return this.back(0);
    }

    public E last() {
        return this.back();
    }

    public E lastSafe() {
        return this.isEmpty() ? null : last();
    }
}
