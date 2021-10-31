package net.pinger.disguise.prompts.database;

import net.pinger.disguise.DisguisePlus;
import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.NumericPrompt;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

public class SetPortPrompt extends NumericPrompt {

    // Instance of the main
    private final DisguisePlus dp;

    public SetPortPrompt(DisguisePlus dp) {
        this.dp = dp;
    }

    @Override @Nonnull
    public String getPromptText(@Nonnull ConversationContext conversationContext) {
        return ChatColor.GREEN + "Specify the database port.";
    }

    @Override
    protected Prompt acceptValidatedInput(@Nonnull ConversationContext conversationContext, @Nonnull Number number) {
        // Get the player
        Player sender = (Player) conversationContext.getForWhom();

        // Setting the host
        this.dp.getSQLDatabase().getSettings().setPort(number.intValue());

        // Returning to the inventory
        this.dp.getInventoryManager().getDatabaseProvider().open(sender);
        return Prompt.END_OF_CONVERSATION;
    }
}
