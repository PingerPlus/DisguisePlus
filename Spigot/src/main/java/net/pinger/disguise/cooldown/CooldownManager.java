package net.pinger.disguise.cooldown;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

public class CooldownManager {

    private boolean enabled;
    private String permission;
    private int interval;

    public CooldownManager(FileConfiguration cfg) {
        ConfigurationSection section = cfg.getConfigurationSection("cooldowns");

        // Update the values
        this.enabled = section.getBoolean("enabled");
        this.permission = section.getString("bypass");
        this.interval = section.getInt("interval");
    }

    public void saveToConfig(FileConfiguration cfg) {
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
        return this.enabled ?
                -1 : this.interval;
    }

    public String getPermission() {
        return permission;
    }
}
