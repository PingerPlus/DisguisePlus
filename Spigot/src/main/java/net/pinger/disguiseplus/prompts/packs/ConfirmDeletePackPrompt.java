package net.pinger.disguiseplus.prompts.packs;

import net.pinger.disguiseplus.DisguisePlus;
import net.pinger.disguiseplus.internal.SkinFactoryImpl;
import net.pinger.disguiseplus.SkinPack;
import net.pinger.disguiseplus.user.User;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;

public class ConfirmDeletePackPrompt extends StringPrompt {

    private final DisguisePlus dp;
    private final net.pinger.disguiseplus.SkinPack pack;

    public ConfirmDeletePackPrompt(DisguisePlus dp, SkinPack pack) {
        this.dp = dp;
        this.pack = pack;
    }

    @Override
    public String getPromptText(ConversationContext conversationContext) {
        return this.dp.getConfiguration().of("skin-packs.confirm-delete");
    }

    @Override
    public Prompt acceptInput( ConversationContext conversationContext, String s) {
        Player p = (Player) conversationContext.getForWhom();
        User user = this.dp.getUserManager().getUser(p.getUniqueId());

        if (!s.equalsIgnoreCase("confirm"))
            return this;

        ((SkinFactoryImpl) this.dp.getSkinFactory()).deleteSkinPack(this.pack);
        user.sendRawMessage("skin-packs.success-delete", this.pack.getName());

        this.dp.getInventoryManager().getCategoryProvider(this.pack.getCategory()).open(p);
        return Prompt.END_OF_CONVERSATION;
    }
}