package net.pinger.disguiseplus.utils;

import java.security.SecureRandom;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class RandomUtil {
    private static final Random RANDOM = new SecureRandom();

    /**
     * This method returns a random element from the collection, with the specific
     * condition the element has to be at.
     *
     * @param collection the collection
     * @param predicate the condition
     * @param remove whether it should be removed after retrieving
     * @param <T> the type of element
     * @return a random element, with a specific condition and with option to remove it from the list
     */

    public static <T> T getRandomElement(List<T> collection, Predicate<T> predicate, boolean remove) {
        final List<T> list = collection.stream().filter(predicate).collect(Collectors.toList());
        if (list.isEmpty()) {
            return null;
        }

        return RandomUtil.getRandomElement(list, remove);
    }

    /**
     * This method returns a random element from the collection, with the specific
     * condition the element has to be at.
     *
     * @param collection the collection
     * @param predicate the condition
     * @param <T> the type of element
     * @return a random element, with a specific condition
     */

    public static <T> T getRandomElement(List<T> collection, Predicate<T> predicate) {
        final List<T> list = collection.stream().filter(predicate).collect(Collectors.toList());
        if (list.isEmpty()) {
            return null;
        }

        return RandomUtil.getRandomElement(list, predicate, false);
    }

    /**
     * This method returns a random element from a collection, with option
     * to delete it from the collection.
     *
     * @param collection the collection
     * @param remove whether it should be removed after retrieved
     * @param <T> the type of element
     * @return a random element, with option to remove it from the list
     */

    public static <T> T getRandomElement(List<T> collection, boolean remove) {
        if (collection.isEmpty()) {
            return null;
        }

        final T t = collection.get(RandomUtil.RANDOM.nextInt(collection.size()));
        if (remove) {
            collection.remove(t);
        }

        return t;
    }

    /**
     * This method returns a random element from a collection.
     *
     * @param collection the collection
     * @param <T> the type of element
     * @return a random element
     */

    public static <T> T getRandomElement(List<T> collection) {
        return RandomUtil.getRandomElement(collection, false);
    }

}
