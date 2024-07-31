package net.pinger.disguiseplus.placeholders;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.pinger.disguise.DisguiseAPI;
import net.pinger.disguiseplus.DisguisePlus;
import net.pinger.disguiseplus.meta.PlayerMeta;
import net.pinger.disguiseplus.user.DisguiseUser;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DisguisePlusExpansion extends PlaceholderExpansion {

    private final DisguisePlus disguisePlus;

    public DisguisePlusExpansion(DisguisePlus disguisePlus) {
        this.disguisePlus = disguisePlus;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "dp";
    }

    @Override
    public @NotNull String getAuthor() {
        return "itspinger";
    }

    @Override
    public @NotNull String getVersion() {
        return "2.0.0";
    }

    @Override
    public @Nullable String onPlaceholderRequest(Player player, @NotNull String params) {
        final DisguiseUser user = this.disguisePlus.getUserManager().getUser(player);
        if (user == null) {
            return "";
        }

        // Switch from the params
        if (params.equalsIgnoreCase("name")) {
            return user.getName();
        }

        if (params.equalsIgnoreCase("original_name")) {
            return DisguiseAPI.getDisguisePlayer(player).getDefaultName();
        }

        if (params.equalsIgnoreCase("rank")) {
            final PlayerMeta meta = user.getActiveMeta();
            return meta == null || meta.getRank() == null ? "" : meta.getRank().getDisplayName();
        }

        return "";
    }
}
