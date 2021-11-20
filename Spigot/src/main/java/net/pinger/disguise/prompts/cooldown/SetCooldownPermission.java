package net.pinger.disguise.prompts.cooldown;

import net.pinger.disguise.DisguisePlus;
import net.pinger.disguise.cooldown.CooldownManager;
import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SetCooldownPermission extends StringPrompt {

    private final DisguisePlus dp;

    public SetCooldownPermission(DisguisePlus dp) {
        this.dp = dp;
    }

    @Override
    public @NotNull String getPromptText(@NotNull ConversationContext conversationContext) {
        return ChatColor.GREEN + "Specify the cooldown bypass permission.";
    }

    @Override
    public @Nullable Prompt acceptInput(@NotNull ConversationContext conversationContext, @Nullable String s) {
        Player p = (Player) conversationContext.getForWhom();
        CooldownManager cld = this.dp.getSettings().getCooldownManager();

        if (s.isEmpty() || s.equalsIgnoreCase(cld.getPermission())) {
            return this;
        }

        cld.setPermission(s);
        this.dp.getInventoryManager().getCooldownProvider().open(p);
        return Prompt.END_OF_CONVERSATION;
    }
}