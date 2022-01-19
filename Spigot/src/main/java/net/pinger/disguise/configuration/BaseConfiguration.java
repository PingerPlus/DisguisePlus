package net.pinger.disguise.configuration;

import net.pinger.common.file.Reader;
import net.pinger.disguise.DisguisePlus;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.Map;

public class BaseConfiguration {

    private final FileConfiguration messages;

    public BaseConfiguration(DisguisePlus bj) {
        // Create the file
        File file = new File(bj.getDataFolder(), "messages.yml");
        this.messages = YamlConfiguration.loadConfiguration(file);

        // Keep
        this.messages.options().copyDefaults(true);

        // Read the source file
        String read = Reader.read(bj.getResource("messages.yml"));

        // Make the configuration
        YamlConfiguration configuration = new YamlConfiguration();

        try {
            // Load from the resource
            configuration.loadFromString(read);

            for (Map.Entry<String, ?> entry : configuration.getValues(true).entrySet()) {
                this.messages.addDefault(entry.getKey(), entry.getValue());
            }

            this.messages.save(file);
        } catch (Exception e) {
            bj.getLogger().warning("Failed to parse messages.yml file.");
            bj.getLogger().warning(e.getMessage());
        }
    }

    public boolean has(String key) {
        return this.messages.get(key) != null;
    }

    public String of(String key, boolean translate) {
        String value = this.messages.getString(key);

        if (!translate)
            return ChatColor.stripColor(value);

        return ChatColor.translateAlternateColorCodes('&', value);
    }

    public String of(String key) {
        return this.of(key, true);
    }

    private String ofFormatted(String key, boolean translate, Object... objects) {
        return String.format(this.of(key, translate), objects);
    }

    public String ofFormatted(String key, Object... objects) {
        return this.ofFormatted(key, true, objects);
    }


}
