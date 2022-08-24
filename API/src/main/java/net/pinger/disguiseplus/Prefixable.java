package net.pinger.disguiseplus;

public interface Prefixable<T> {

    /**
     * This method returns a prefix based on the type
     * of the object specified in the arguments.
     *
     * @param t the type of object to prefix
     * @return the prefixed string
     */

    String toPrefix(T t);

}
