package net.pinger.disguise.settings.display;

import me.clip.placeholderapi.PlaceholderAPI;
import net.pinger.disguise.user.User;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

public class DisplaySettings {

    // Whether the chat should be overriden
    private boolean override;
    private String format;

    private final Entry prefix, suffix;

    public DisplaySettings(FileConfiguration cfg) {
        ConfigurationSection map = cfg.getConfigurationSection("display");

        // Chat
        this.override = map.getBoolean("chat.override");
        this.format = this.formatObject(map.getString("chat.format"));

        // Prefix
        this.prefix = new Entry(this.formatObject(map.getString("prefix.default")),
                this.formatObject(map.getString("prefix.disguised")));

        // Suffix
        this.suffix = new Entry(this.formatObject(map.getString("suffix.default")),
                this.formatObject(map.getString("suffix.disguised")));
    }

    private String formatObject(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public void reverseOverride() {
        this.override = !this.override;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getFormat() {
        return format;
    }

    public Entry getPrefix() {
        return prefix;
    }

    public Entry getSuffix() {
        return suffix;
    }

    public boolean isOverride() {
        return override;
    }

    public void saveToConfig(FileConfiguration cfg) {
        ConfigurationSection map = cfg.getConfigurationSection("display");

        // Chat
        map.set("chat.override", this.override);
        map.set("chat.format", this.format);

        // Prefix
        map.set("prefix.default", this.prefix.getDef());
        map.set("prefix.disguised", this.prefix.getDisguised());

        // Suffix
        map.set("suffix.default", this.suffix.getDef());
        map.set("suffix.disguised", this.suffix.getDisguised());
    }

    public static class Entry {

        private String def, disguised;

        private Entry(String def, String disguised) {
            this.def = def;
            this.disguised = disguised;
        }

        public String toEntry(User user) {
            return PlaceholderAPI.setPlaceholders(user.transform(),
                    user.isDisguised() ? this.def : this.disguised);
        }

        public void setDef(String def) {
            this.def = ChatColor.translateAlternateColorCodes('&', def);
        }

        public void setDisguised(String disguised) {
            this.disguised = ChatColor.translateAlternateColorCodes('&', disguised);
        }

        public String getDef() {
            return def;
        }

        public String getDisguised() {
            return disguised;
        }
    }
}
