package net.pinger.disguiseplus.prompts;

import net.pinger.disguiseplus.DisguisePlus;
import net.pinger.disguiseplus.user.User;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CreateCategoryPrompt extends StringPrompt {

    private final DisguisePlus dp;

    public CreateCategoryPrompt(DisguisePlus dp) {
        this.dp = dp;
    }

    @Override
    @Nonnull
    public String getPromptText(@Nonnull ConversationContext conversationContext) {
        return this.dp.getConfiguration().of("categories.create");
    }

    @Override
    @Nullable
    public Prompt acceptInput(@Nonnull ConversationContext conversationContext, @Nullable String s) {
        Player p = (Player) conversationContext.getForWhom();
        User user = this.dp.getUserManager().getUser(p);

        // Break this prompt
        // If the input is null
        if (s == null)
            return this;

        // Create the category
        this.dp.getSkinFactory().createCategory(s);

        // Send message and return
        user.sendRawMessage("categories.success-create", s);
        this.dp.getInventoryManager().getSkinPacksProvider().open(p);
        return Prompt.END_OF_CONVERSATION;
    }
}
