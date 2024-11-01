package net.pinger.disguiseplus.rank;

public class Rank {
    private final String name;
    private final String displayName;
    private final String permission;

    public Rank(String name, String displayName, String permission) {
        this.name = name;
        this.displayName = displayName;
        this.permission = permission;
    }

    public static Rank of(String name, String displayName, String permission) {
        return new Rank(name, displayName, permission);
    }

    public String getName() {
        return name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getPermission() {
        return permission;
    }
}
