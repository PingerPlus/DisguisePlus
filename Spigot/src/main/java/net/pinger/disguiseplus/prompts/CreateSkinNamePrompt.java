package net.pinger.disguiseplus.prompts;

import net.pinger.disguise.Skin;
import net.pinger.disguiseplus.DisguisePlus;
import net.pinger.disguiseplus.SkinPack;
import net.pinger.disguiseplus.user.User;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CreateSkinNamePrompt extends StringPrompt {

    private final DisguisePlus dp;
    private final net.pinger.disguiseplus.SkinPack pack;

    public CreateSkinNamePrompt(DisguisePlus dp, SkinPack pack) {
        this.dp = dp;
        this.pack = pack;
    }

    @Override
    @Nonnull
    public String getPromptText(@Nonnull ConversationContext conversationContext) {
        return this.dp.getConfiguration().of("skins.player-name");
    }

    @Override
    @Nullable
    public Prompt acceptInput(@Nonnull ConversationContext conversationContext, @Nullable String s) {
        Player p = (Player) conversationContext.getForWhom();
        User user = this.dp.getUserManager().getUser(p);

        // Check if input is empty
        if (s == null) {
            return this;
        }

        // Fetch skin here
        Skin skin = null;

        if (skin == null) {
            user.sendRawMessage("skins.error-name", s);
        } else {
            user.sendRawMessage("skins.success-name", s);

            // Add the skin
            this.pack.addSkin(skin);
        }

        // Check for error here
        this.dp.getInventoryManager().getExactPackProvider(this.pack).open(p);
        return Prompt.END_OF_CONVERSATION;
    }
}
