package net.pinger.disguiseplus.prompts;

import net.pinger.disguiseplus.DisguisePlus;
import net.pinger.disguiseplus.SkinFactory;
import net.pinger.disguiseplus.internal.SkinPackImpl;
import net.pinger.disguiseplus.user.User;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CreatePackPrompt extends StringPrompt {

    private final DisguisePlus dm;
    private final String category;

    public CreatePackPrompt(DisguisePlus dm, String category) {
        this.dm = dm;
        this.category = category;
    }

    @Override @Nonnull
    public String getPromptText(@Nonnull ConversationContext conversationContext) {
        return this.dm.getConfiguration().of("skin-packs.create");
    }

    @Override @Nullable
    public Prompt acceptInput(@Nonnull ConversationContext conversationContext, @Nullable String s) {
        Player p = (Player) conversationContext.getForWhom();
        User user = this.dm.getUserManager().getUser(p);
        SkinFactory factory = this.dm.getSkinFactory();


        if (s == null) {
            return this;
        }

        if (factory.getSkinPack(category, s) != null)
            return this;

        factory.createSkinPack(this.category, new SkinPackImpl(null, this.category, s, Lists.newArrayList()));
        this.dm.getInventoryManager().getCategoryProvider(this.category).open(p);
        user.sendRawMessage("skin-packs.success-create", s);
        return Prompt.END_OF_CONVERSATION;
    }
}
