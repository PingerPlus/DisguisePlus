package net.pinger.disguiseplus.prompts;

import net.pinger.disguiseplus.DisguisePlus;
import net.pinger.disguiseplus.skin.SkinPack;
import net.pinger.disguiseplus.user.User;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ConfirmDeletePackPrompt extends StringPrompt {

    private final DisguisePlus dp;
    private final SkinPack pack;

    public ConfirmDeletePackPrompt(DisguisePlus dp, SkinPack pack) {
        this.dp = dp;
        this.pack = pack;
    }

    @Override
    public String getPromptText(@Nonnull ConversationContext conversationContext) {
        return this.dp.getConfiguration().of("skin-packs.confirm-delete");
    }

    @Override
    public Prompt acceptInput( ConversationContext conversationContext, @Nullable String s) {
        Player p = (Player) conversationContext.getForWhom();
        User user = this.dp.getUserManager().getUser(p.getUniqueId());

        if (s == null || !s.equalsIgnoreCase("confirm"))
            return this;

        // Delete the skin pack
        this.dp.getSkinFactory().deleteSkinPack(this.pack);
        user.sendRawMessage("skin-packs.success-delete", this.pack.getName());

        // Reopen the category provider
        this.dp.getInventoryManager().getCategoryProvider(this.pack.getCategory()).open(p);
        return Prompt.END_OF_CONVERSATION;
    }
}
