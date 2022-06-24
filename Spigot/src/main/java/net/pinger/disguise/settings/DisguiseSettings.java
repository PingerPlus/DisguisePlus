package net.pinger.disguise.settings;

import com.google.common.collect.Sets;
import net.pinger.common.lang.Lists;
import net.pinger.disguise.DisguisePlus;
import net.pinger.disguise.settings.display.DisplaySettings;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Set;

public class DisguiseSettings {

    private final DisguisePlus dp;

    private Set<String> disabledWorlds;
    private boolean online;
    private final DisplaySettings ds;
    private boolean update;
    private boolean metrics;

    private int min, max;

    public DisguiseSettings(DisguisePlus dp) {
        this.dp = dp;

        // Get the configuration
        FileConfiguration cfg = dp.getConfig();

        // Update the other values
        this.online = cfg.getBoolean("online-mode");
        this.update = cfg.getBoolean("update");
        this.metrics = cfg.getBoolean("bstats");
        this.disabledWorlds = Sets.newHashSet(cfg.getStringList("disabled-worlds"));
        this.min = cfg.getInt("nick.min-length");
        this.max = cfg.getInt("nick.max-length");

        // According to the rules it must be between 3 and 16
        this.min = Math.max(3, Math.min(this.min, 16));
        this.max = Math.max(3, Math.max(this.max, 16));

        // Update classes
        this.ds = new DisplaySettings(cfg);
    }

    /**
     * This method adds a certain world to the list of worlds
     * that will be banned from executions of disguise commands.
     *
     * @param world the world
     */

    public void banWorld(World world) {
        this.disabledWorlds.add(world.getName());
    }

    /**
     * This method adds a certain world to the list of worlds
     * that will be banned from executions of disguise commands.
     *
     * @param world the world
     */

    public void banWorld(String world) {
        this.disabledWorlds.add(world);
    }

    /**
     * Removes the world from the ban queue.
     *
     * @param world the world that gets removed
     */

    public void removeWorld(World world) {
        this.disabledWorlds.remove(world.getName());
    }

    /**
     * Removes the world with a specific name from the ban queue.
     *
     * @param name the name of the world
     */

    public void removeWorld(String name) {
        this.disabledWorlds.remove(name);
    }

    /**
     * Checks if this world is currently banned from usage of disguise commands.
     *
     * @param world the world
     * @return if the world is banned
     */

    public boolean isWorldDisabled(World world) {
        return this.disabledWorlds.contains(world.getName());
    }


    /**
     * Checks if this world is currently banned from usage of disguise commands.
     *
     * @param name the world name
     * @return if the world is banned
     */

    public boolean isWorldDisabled(String name) {
        return this.disabledWorlds.contains(name);
    }

    /**
     * Checks if a nick is valid for use.
     *
     * @param s the nick
     * @return if it's valid
     */

    public boolean isNickValid(String s) {
        s = ChatColor.translateAlternateColorCodes('&', s);

        return s.length() >= this.min
                && s.length() <= this.max;
    }

    /**
     * Checks if the plugin is running in offline mode.
     *
     * @return if the plugin is running offline mode
     */

    public boolean isOnlineMode() {
        return this.online;
    }

    /**
     * This method reverses the current state of the online-mode.
     */

    public void reverseOnline() {
        this.online = !this.online;
    }

    /**
     * Returns a set of all worlds that are currently banned.
     *
     * @return the worlds
     */

    public Set<String> getWorlds() {
        return disabledWorlds;
    }

    /**
     * Checks if the plugin is supposed to do regular update checks.
     *
     * @return if the plugin is doing update checks
     */

    public boolean isUpdate() {
        return update;
    }

    /**
     * This method reverses the current value of update.
     */

    public void reverseUpdate() {
        this.update = !this.update;
    }

    /**
     * Checks if the metrics should be used.
     *
     * @return if the metrics should be used
     */

    public boolean isMetrics() {
        return metrics;
    }

    /**
     * This method reverses the current value of metrics.
     */

    public void reverseMetrics() {
        this.metrics = !this.metrics;
    }

    /**
     * Checks if we're doing custom chat override.
     *
     * @return if we're doing the custom override
     */

    public boolean isOverride() {
        return this.ds.isOverride();
    }

    /**
     * This method reverses the current value of chat override.
     */

    public void reverseOverride() {
        this.ds.reverseOverride();
    }

    /**
     * Sets the chat override format.
     *
     * @param format the format of the chat
     */

    public void setFormat(String format) {
        this.ds.setFormat(format);
    }

    /**
     * Returns the current override format value.
     *
     * @return the current format
     */

    public String getFormat() {
        return this.ds.getFormat();
    }

    /**
     * Returns the display settings of this settings.
     *
     * @return the display settings
     */

    public DisplaySettings getDisplaySettings() {
        return ds;
    }

    public int getMax() {
        return max;
    }

    public int getMin() {
        return min;
    }

    /**
     * This method saves the current config configuration
     */

    public void saveConfig() {
        // Get the config
        FileConfiguration cfg = this.dp.getConfig();

        // Save the settings
        cfg.set("online-mode", this.online);
        cfg.set("bstats", this.metrics);
        cfg.set("update", this.update);

        cfg.set("disabled-worlds", Lists.newArrayList(this.disabledWorlds.toArray()));

        // Update nick creator
        this.ds.saveToConfig(cfg);
        this.dp.saveConfig();
    }
}
