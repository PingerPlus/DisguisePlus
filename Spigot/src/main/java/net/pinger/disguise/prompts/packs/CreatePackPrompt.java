package net.pinger.disguise.prompts.packs;

import net.pinger.common.lang.Lists;
import net.pinger.disguise.DisguisePlus;
import net.pinger.disguise.factory.SimpleSkinFactory;
import net.pinger.disguise.skin.SimpleSkinPack;
import net.pinger.disguise.user.User;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CreatePackPrompt extends StringPrompt {

    private final DisguisePlus dm;
    private final String category;

    public CreatePackPrompt(DisguisePlus dm, String category) {
        this.dm = dm;
        this.category = category;
    }

    @Override
    public @NotNull String getPromptText(@NotNull ConversationContext conversationContext) {
        return this.dm.getConfiguration().of("skin-packs.create");
    }

    @Override
    public @Nullable Prompt acceptInput(@NotNull ConversationContext conversationContext, @Nullable String s) {
        Player p = (Player) conversationContext.getForWhom();
        User user = this.dm.getUserManager().getUser(p);
        SimpleSkinFactory factory = (SimpleSkinFactory) this.dm.getSkinFactory();

        if (s.isEmpty()) {
            return this;
        }

        if (factory.getSkinPack(s) != null && factory.getSkinPack(s).getCategory().equalsIgnoreCase(this.category))
            return this;

        factory.createSkinPack(this.category, new SimpleSkinPack(null, this.category, s, Lists.newArrayList()));
        this.dm.getInventoryManager().getCategoryProvider(this.category).open(p);
        user.sendRawMessage("skin-packs.success-create", s);
        return Prompt.END_OF_CONVERSATION;
    }
}
