package net.pinger.disguise.prompts.chat.suffix;

import net.pinger.disguise.DisguisePlus;
import net.pinger.disguise.settings.display.DisplaySettings;
import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;

public class SetDisguisedSuffixPrompt extends StringPrompt {

    private final DisguisePlus dp;

    public SetDisguisedSuffixPrompt(DisguisePlus dp) {
        this.dp = dp;
    }

    @Override
    public String getPromptText(ConversationContext conversationContext) {
        return ChatColor.GREEN + "Specify the disguised suffix";
    }

    @Override
    public Prompt acceptInput(ConversationContext conversationContext, String s) {
        Player p = (Player) conversationContext.getForWhom();

        DisplaySettings settings = this.dp.getSettings().getDisplaySettings();
        if (s.isEmpty() || s.equalsIgnoreCase(settings.getSuffix().getDisguised())) {
            return this;
        }

        settings.getSuffix().setDisguised(s);
        this.dp.getInventoryManager().getDisplayProvider().open(p);
        return Prompt.END_OF_CONVERSATION;
    }

}
