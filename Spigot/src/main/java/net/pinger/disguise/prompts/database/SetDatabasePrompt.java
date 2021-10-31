package net.pinger.disguise.prompts.database;

import net.pinger.disguise.DisguisePlus;
import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

public class SetDatabasePrompt extends StringPrompt {

    // SQL object
    private final DisguisePlus dp;

    public SetDatabasePrompt(DisguisePlus dp) {
        this.dp = dp;
    }

    @Override
    public String getPromptText(@Nonnull ConversationContext conversationContext) {
        return ChatColor.GREEN + "Specify the database name.";
    }

    @Override
    public Prompt acceptInput(@Nonnull ConversationContext conversationContext, String s) {
        // Get the player
        Player sender = (Player) conversationContext.getForWhom();

        // Setting the host
        this.dp.getSQLDatabase().getSettings().setDatabase(s);

        // Returning to the inventory
        this.dp.getInventoryManager().getDatabaseProvider().open(sender);
        return Prompt.END_OF_CONVERSATION;
    }
}
