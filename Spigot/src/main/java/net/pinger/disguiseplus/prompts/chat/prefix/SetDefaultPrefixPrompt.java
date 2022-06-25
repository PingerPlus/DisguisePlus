package net.pinger.disguiseplus.prompts.chat.prefix;

import net.pinger.disguiseplus.DisguisePlus;
import net.pinger.disguiseplus.settings.display.DisplaySettings;
import net.pinger.disguiseplus.user.User;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;

public class SetDefaultPrefixPrompt extends StringPrompt {

    private final DisguisePlus dp;

    public SetDefaultPrefixPrompt(DisguisePlus dp) {
        this.dp = dp;
    }

    @Override
    public String getPromptText(ConversationContext conversationContext) {
        return this.dp.getConfiguration().of("chat.prefix.specify-normal");
    }

    @Override
    public Prompt acceptInput(ConversationContext conversationContext, String s) {
        Player p = (Player) conversationContext.getForWhom();
        User user = this.dp.getUserManager().getUser(p);

        DisplaySettings settings = this.dp.getSettings().getDisplaySettings();
        if (s.isEmpty() || s.equalsIgnoreCase(settings.getPrefix().getDef())) {
            return this;
        }

        settings.getPrefix().setDef(s);
        this.dp.getInventoryManager().getDisplayProvider().open(p);
        user.sendRawMessage("chat.prefix.success-normal", s);
        return Prompt.END_OF_CONVERSATION;
    }
}
