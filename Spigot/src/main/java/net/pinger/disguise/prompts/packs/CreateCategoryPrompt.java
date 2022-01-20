package net.pinger.disguise.prompts.packs;

import net.pinger.disguise.DisguisePlus;
import net.pinger.disguise.factory.SimpleSkinFactory;
import net.pinger.disguise.user.User;
import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CreateCategoryPrompt extends StringPrompt {

    private final DisguisePlus dp;

    public CreateCategoryPrompt(DisguisePlus dp) {
        this.dp = dp;
    }

    @Override
    public @NotNull String getPromptText(@NotNull ConversationContext conversationContext) {
        return this.dp.getConfiguration().of("categories.create");
    }

    @Override
    public @Nullable Prompt acceptInput(@NotNull ConversationContext conversationContext, @Nullable String s) {
        Player p = (Player) conversationContext.getForWhom();
        User user = this.dp.getUserManager().getUser(p);

        if (s.isEmpty())
            return this;

        // Create the category
        if (!(this.dp.getSkinFactory() instanceof SimpleSkinFactory))
            return Prompt.END_OF_CONVERSATION;

        user.sendRawMessage("categories.success-create", s);
        SimpleSkinFactory factory = (SimpleSkinFactory) this.dp.getSkinFactory();
        factory.createCategory(s);
        this.dp.getInventoryManager().getSkinPacksProvider().open(p);

        return Prompt.END_OF_CONVERSATION;
    }
}
