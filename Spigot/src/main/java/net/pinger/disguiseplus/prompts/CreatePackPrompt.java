package net.pinger.disguiseplus.prompts;

import net.pinger.disguiseplus.DisguisePlus;
import net.pinger.disguiseplus.skin.SkinFactory;
import net.pinger.disguiseplus.user.DisguiseUser;
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
    public Prompt acceptInput(@Nonnull ConversationContext conversationContext, @Nullable String in) {
        Player p = (Player) conversationContext.getForWhom();
        DisguiseUser user = this.dm.getUserManager().getUser(p);
        SkinFactory factory = this.dm.getSkinFactory();
        if (in == null || factory.getSkinPack(this.category, in) != null) {
            return this;
        }

        // Create the skin pack
        factory.createSkinPack(this.category, in);

        // Send message and return
        this.dm.getInventoryManager().getCategoryProvider(this.category).open(p);
        user.sendRawMessage("skin-packs.success-create", in);
        return Prompt.END_OF_CONVERSATION;
    }
}
