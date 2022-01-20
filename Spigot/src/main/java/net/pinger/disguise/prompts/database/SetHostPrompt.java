package net.pinger.disguise.prompts.database;

import net.pinger.disguise.DisguisePlus;
import net.pinger.disguise.user.User;
import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

public class SetHostPrompt extends StringPrompt {

    // Instance of the main
    private final DisguisePlus dp;

    public SetHostPrompt(DisguisePlus dp) {
        this.dp = dp;
    }

    @Override @Nonnull
    public String getPromptText(@Nonnull ConversationContext conversationContext) {
        return this.dp.getConfiguration().of("database.specify-hostname");
    }

    @Override
    public Prompt acceptInput(@Nonnull ConversationContext conversationContext, String s) {
        // Get the player
        Player sender = (Player) conversationContext.getForWhom();
        User user = this.dp.getUserManager().getUser(sender);

        // Setting the host
        this.dp.getSQLDatabase().getSettings().setHost(s);

        // Returning to the inventory
        this.dp.getInventoryManager().getDatabaseProvider().open(sender);
        user.sendRawMessage("database.success-hostname", s);
        return Prompt.END_OF_CONVERSATION;
    }
}
