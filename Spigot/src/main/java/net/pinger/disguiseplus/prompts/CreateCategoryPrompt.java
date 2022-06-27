package net.pinger.disguiseplus.prompts;

import net.pinger.disguiseplus.DisguisePlus;
import net.pinger.disguiseplus.internal.SkinFactoryImpl;
import net.pinger.disguiseplus.user.User;
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
        if (!(this.dp.getSkinFactory() instanceof SkinFactoryImpl))
            return Prompt.END_OF_CONVERSATION;

        user.sendRawMessage("categories.success-create", s);
        SkinFactoryImpl factory = (SkinFactoryImpl) this.dp.getSkinFactory();
        factory.createCategory(s);
        this.dp.getInventoryManager().getSkinPacksProvider().open(p);

        return Prompt.END_OF_CONVERSATION;
    }
}
