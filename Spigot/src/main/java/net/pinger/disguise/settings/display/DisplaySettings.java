package net.pinger.disguise.settings.display;

import me.clip.placeholderapi.PlaceholderAPI;
import net.pinger.disguise.utils.SimplePair;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Map;

public class DisplaySettings {

    // Whether the chat should be overriden
    private boolean override;
    private String format;

    private final SimplePair<String, String> prefix = new SimplePair<>(),
            suffix = new SimplePair<>();

    @SuppressWarnings("unchecked")
    public DisplaySettings(FileConfiguration cfg) {
        ConfigurationSection map = cfg.getConfigurationSection("display");

        // Chat
        this.override = map.getBoolean("chat.override");
        this.format = this.formatObject(map.getString("chat.format"));

        // Prefix
        this.prefix.setFirst(this.formatObject(map.getString("prefix.default")));
        this.prefix.setSecond(this.formatObject(map.getString("prefix.disguised")));

        // Suffix
        this.suffix.setFirst(this.formatObject(map.getString("suffix.default")));
        this.suffix.setSecond(this.formatObject(map.getString("suffix.disguised")));
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

    public SimplePair<String, String> getPrefix() {
        return prefix;
    }

    public SimplePair<String, String> getSuffix() {
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
        map.set("prefix.default", this.prefix.getFirst());
        map.set("prefix.disguised", this.prefix.getSecond());

        // Suffix
        map.set("suffix.default", this.suffix.getFirst());
        map.set("suffix.disguised", this.suffix.getSecond());
    }
}
