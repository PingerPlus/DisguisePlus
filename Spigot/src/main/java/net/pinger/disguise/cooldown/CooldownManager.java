package net.pinger.disguise.cooldown;

import org.bukkit.configuration.file.FileConfiguration;

public class CooldownManager {

    private boolean enabled;
    private String permission;
    private int interval;



    public void save(FileConfiguration cfg) {
        cfg.set("cooldowns.enabled", this.enabled);
        cfg.set("cooldowns.interval", this.interval);
        cfg.set("cooldowns.bypass", this.permission);
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public int getInterval() {
        return interval;
    }

    public String getPermission() {
        return permission;
    }
}
