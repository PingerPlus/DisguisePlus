package net.pinger.disguise.prompts.packs;

import net.pinger.disguise.DisguisePlus;
import net.pinger.disguise.factory.SimpleSkinFactory;
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
        return ChatColor.GREEN + "Specify the category name.";
    }

    @Override
    public @Nullable Prompt acceptInput(@NotNull ConversationContext conversationContext, @Nullable String s) {
        Player p = (Player) conversationContext.getForWhom();

        if (s.isEmpty())
            return this;

        // Create the category
        if (!(this.dp.getSkinFactory() instanceof SimpleSkinFactory))
            return Prompt.END_OF_CONVERSATION;

        SimpleSkinFactory factory = (SimpleSkinFactory) this.dp.getSkinFactory();
        factory.createCategory(s);
        this.dp.getInventoryManager().getSkinPacksProvider().open(p);

        return Prompt.END_OF_CONVERSATION;
    }
}
