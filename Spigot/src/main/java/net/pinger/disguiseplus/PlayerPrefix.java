package net.pinger.disguiseplus;

import com.mojang.authlib.GameProfile;
import me.clip.placeholderapi.PlaceholderAPI;
import net.pinger.disguiseplus.user.User;
import org.bukkit.configuration.ConfigurationSection;

public class PlayerPrefix extends DisguiseFeature implements Prefixable<User> {

    private String prefixDefault;
    private String prefixDisguised;

    public PlayerPrefix(DisguisePlus disguise) {
        super(disguise);
    }

    public String getPrefixDefault() {
        return prefixDefault;
    }

    public String getPrefixDisguised() {
        return prefixDisguised;
    }

    @Override
    public String toPrefix(User user) {
        if (user.isDisguised()) {
            // Get the disguised prefix
            return PlaceholderAPI.setPlaceholders(user.transform(), getPrefixDisguised());
        }

        // Otherwise, return the default prefix
        return PlaceholderAPI.setPlaceholders(user.transform(), getPrefixDefault());
    }

    @Override
    protected void load() {
        ConfigurationSection section =
                this.plugin.getConfig().getConfigurationSection("display.prefix");

        // Set the values
        this.prefixDefault = section.getString("default");
        this.prefixDisguised = section.getString("disguised");
    }

    @Override
    protected void reload() {
        this.load();
    }
}
