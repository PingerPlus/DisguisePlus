package net.pinger.disguise.prompts;

import net.pinger.disguise.DisguisePlus;
import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

public class SetUsernamePrompt extends StringPrompt {

    // Instance of the main
    private final DisguisePlus dp;

    public SetUsernamePrompt(DisguisePlus dp) {
        this.dp = dp;
    }

    @Override @Nonnull
    public String getPromptText(@Nonnull ConversationContext conversationContext) {
        return ChatColor.GREEN + "Specify the database username.";
    }

    @Override
    public Prompt acceptInput(@Nonnull ConversationContext conversationContext, String s) {
        // Get the player
        Player sender = (Player) conversationContext.getForWhom();

        // Setting the host
        this.dp.getSQLDatabase().setUsername(s);

        // Returning to the inventory
        this.dp.getInventoryManager().getDatabaseProvider().open(sender);
        return Prompt.END_OF_CONVERSATION;
    }
}
