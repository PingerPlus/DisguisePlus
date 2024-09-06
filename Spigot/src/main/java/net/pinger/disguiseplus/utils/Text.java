package net.pinger.disguiseplus.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.md_5.bungee.api.ChatColor;
import net.pinger.disguise.server.MinecraftServer;

public class Text {
    private static final String DEFAULT_CODE = "§x";
    private static final Pattern HEX_PATTERN =  Pattern.compile("&#([A-Fa-f0-9]{6})");

    public static String translate(String message) {
        if (MinecraftServer.atLeast("1.16")) {
            final Matcher matcher = HEX_PATTERN.matcher(message);
            final StringBuffer buffer = new StringBuffer(message.length() + 4 * 8);
            while (matcher.find()) {
                final String group = matcher.group(1);
                matcher.appendReplacement(buffer, code(group));
            }

            return ChatColor.translateAlternateColorCodes('&', matcher.appendTail(buffer).toString());
        }

        return ChatColor.translateAlternateColorCodes('&', message);
    }
    private static String code(String hexCode) {
        final StringBuilder builder = new StringBuilder(Text.DEFAULT_CODE);
        for (int i = 0; i <= 5; i++) {
            builder.append(ChatColor.COLOR_CHAR).append(hexCode.charAt(i));
        }
        return builder.toString();
    }

}
