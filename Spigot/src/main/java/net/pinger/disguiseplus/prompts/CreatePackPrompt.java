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

        // Check if the input is null
        // If so break this prompt
        if (s == null) {
            return this;
        }

        // Check if the skin pack already exists
        // If so then don't create another one
        if (factory.getSkinPack(this.category, s) != null)
            return this;

        // Create the skin pack
        factory.createSkinPack(this.category, s);

        // Send message and return
        this.dm.getInventoryManager().getCategoryProvider(this.category).open(p);
        user.sendRawMessage("skin-packs.success-create", s);
        return Prompt.END_OF_CONVERSATION;
    }
}
