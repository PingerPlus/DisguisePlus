package net.pinger.disguise.settings;

import com.google.common.collect.Sets;
import net.pinger.disguise.DisguisePlus;
import net.pinger.disguise.nick.SimpleNickCreator;
import net.pinger.disguise.settings.display.DisplaySettings;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Set;

public class DisguiseSettings {

    private final DisguisePlus dp;

    private Set<String> disabledWorlds = Sets.newHashSet();
    private boolean online = true;
    private SimpleNickCreator creator;
    private final DisplaySettings ds;
    private boolean update = true;
    private boolean metrics = true;
    private final int min = 5, max = 16;

//    private boolean override;
//    private String format = "%player_name% %message%";

    public DisguiseSettings(DisguisePlus dp) {
        this.dp = dp;
        this.creator = SimpleNickCreator.createFrom(this.dp.getConfig().getString("nick.pattern"));
        this.ds = new DisplaySettings(this.dp.getConfig());
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
     * Returns the nick creator.
     *
     * @return the nick creator
     */

    public SimpleNickCreator getCreator() {
        return creator;
    }

    /**
     * Returns the display settings of this settings.
     *
     * @return the display settings
     */

    public DisplaySettings getDisplaySettings() {
        return ds;
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

        // Update nick creator
        this.creator.saveToConfig(cfg);
        this.dp.saveConfig();
    }
}
