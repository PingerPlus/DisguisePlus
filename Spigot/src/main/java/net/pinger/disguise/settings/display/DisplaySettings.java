package net.pinger.disguise.settings.display;

import net.pinger.disguise.utils.SimplePair;

public class DisplaySettings {

    // Whether the chat should be overriden
    private boolean override = false;
    private String format = "%player_name% %message%";
    private final SimplePair<String, String> prefix = new SimplePair<>(),
            suffix = new SimplePair<>();


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
}
