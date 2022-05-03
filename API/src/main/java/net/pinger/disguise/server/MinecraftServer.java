package net.pinger.disguise.server;

import org.bukkit.Bukkit;

public final class MinecraftServer {


    /**
     * The current version of the {@link org.bukkit.Server} server
     */

    private static final String BUKKIT_VERSION = Bukkit.getVersion();

    /**
     * Striped version of the Bukkit version
     */

    public static final String STRIPPED_VERSION;

    static {
        STRIPPED_VERSION = BUKKIT_VERSION.substring(BUKKIT_VERSION.indexOf("(MC: ") + 5, BUKKIT_VERSION.length() - 1);
    }

    /**
     * Checks if the version is a subversion of the current {@link org.bukkit.Server} version
     *
     * @param version the version we are assuring for
     * @return if the version is a subversion of the server
     */

    public static boolean isVersion(String version) {
        return STRIPPED_VERSION.equalsIgnoreCase(version);
    }

    /**
     * Checks if the current bukkit version is at least the version specified in the parameters
     *
     * @param version the version
     * @return if the version is at least the one specified
     */

    public static boolean atLeast(String version) {
        return compareTo(STRIPPED_VERSION, version) >= 0;
    }

    /**
     * Compares two Bukkit versions by splitting them with the character dot.
     *
     * @param a the first version
     * @param b the second version
     * @return the comparison
     */

    public static int compareTo(String a, String b) {
        if (a.equalsIgnoreCase(b))
            return 0;

        String[] at = a.split("\\.");
        String[] bt = b.split("\\.");

        for (int i = 0; i < Math.min(at.length, bt.length); i++) {
            Integer ax = Integer.valueOf(at[i]);
            Integer bx = Integer.valueOf(bt[i]);

            if (ax.compareTo(bx) != 0) {
                return ax.compareTo(bx);
            }
        }

        return at.length > bt.length ? 1 : -1;
    }

}
