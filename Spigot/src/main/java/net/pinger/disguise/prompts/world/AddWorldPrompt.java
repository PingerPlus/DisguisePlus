package net.pinger.disguise.prompts.world;

import net.pinger.disguise.DisguisePlus;
import net.pinger.disguise.user.User;
import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

public class AddWorldPrompt extends StringPrompt {

    private final DisguisePlus dp;

    public AddWorldPrompt(DisguisePlus dp) {
        this.dp = dp;
    }

    @Override
    public String getPromptText(@Nonnull ConversationContext conversationContext) {
        return this.dp.getConfiguration().of("worlds.specify-name");
    }

    @Override
    public Prompt acceptInput(@Nonnull ConversationContext conversationContext, String s) {
        // Casting to the player
        Player sender = (Player) conversationContext.getForWhom();
        User user = this.dp.getUserManager().getUser(sender);

        if (s.isEmpty())
            return this;

        if (this.dp.getSettings().isWorldDisabled(s)) {
            return this;
        }

        this.dp.getSettings().banWorld(s);
        this.dp.getInventoryManager().getWorldListProvider().open(sender);
        user.sendRawMessage("worlds.success", s);
        return Prompt.END_OF_CONVERSATION;
    }
}
