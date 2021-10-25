package net.pinger.disguise.prompts.chat;

import net.pinger.disguise.DisguisePlus;
import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;

public class SetFormatPrompt extends StringPrompt {

    private final DisguisePlus dp;

    public SetFormatPrompt(DisguisePlus dp) {
        this.dp = dp;
    }

    @Override
    public String getPromptText(ConversationContext conversationContext) {
        return ChatColor.GREEN + "Specify the format.";
    }

    @Override
    public Prompt acceptInput(ConversationContext conversationContext, String s) {
        Player p = (Player) conversationContext.getForWhom();

        if (!s.contains("%player_name%") || !s.contains("%message%")) {
            p.sendRawMessage(ChatColor.RED + "The message must contain %player_name% and %message%.");
            return this;
        }

        this.dp.getSettings().setFormat(s);
        this.dp.getInventoryManager().getDisplayProvider().open(p);
        return Prompt.END_OF_CONVERSATION;
    }
}
