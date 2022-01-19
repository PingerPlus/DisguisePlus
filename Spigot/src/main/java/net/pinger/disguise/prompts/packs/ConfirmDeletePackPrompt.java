package net.pinger.disguise.prompts.packs;

import net.pinger.disguise.DisguisePlus;
import net.pinger.disguise.factory.SimpleSkinFactory;
import net.pinger.disguise.skin.SkinPack;
import net.pinger.disguise.user.User;
import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;

public class ConfirmDeletePackPrompt extends StringPrompt {

    private final DisguisePlus dp;
    private final SkinPack pack;

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

        ((SimpleSkinFactory) this.dp.getSkinFactory()).deleteSkinPack(this.pack);
        user.sendRawMessage("skin-packs.success-delete", this.pack.getName());

        this.dp.getInventoryManager().getCategoryProvider(this.pack.getCategory()).open(p);
        return Prompt.END_OF_CONVERSATION;
    }
}
