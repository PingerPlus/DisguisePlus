package net.pinger.disguiseplus.prompts.chat;

import net.pinger.disguiseplus.DisguisePlus;
import net.pinger.disguiseplus.user.User;
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
        return this.dp.getConfiguration().of("chat.format.specify");
    }

    @Override
    public Prompt acceptInput(ConversationContext conversationContext, String s) {
        Player p = (Player) conversationContext.getForWhom();
        User user = this.dp.getUserManager().getUser(p);

        if (!s.contains("%player_name%") || !s.contains("%message%")) {
            p.sendRawMessage(ChatColor.RED + "The message must contain %player_name% and %message%.");
            return this;
        }

        this.dp.getSettings().setFormat(s);
        this.dp.getInventoryManager().getDisplayProvider().open(p);
        user.sendRawMessage("chat.format.success");
        return Prompt.END_OF_CONVERSATION;
    }
}
