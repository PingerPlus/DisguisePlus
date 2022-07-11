package net.pinger.disguiseplus.prompts;

import net.pinger.disguise.DisguiseAPI;
import net.pinger.disguiseplus.DisguisePlus;
import net.pinger.disguiseplus.SkinPack;
import net.pinger.disguiseplus.user.User;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CreateSkinImagePrompt extends StringPrompt {

    private final DisguisePlus dp;
    private final SkinPack pack;

    public CreateSkinImagePrompt(DisguisePlus dp, SkinPack pack) {
        this.dp = dp;
        this.pack = pack;
    }

    @Override
    @Nonnull
    public String getPromptText(@Nonnull ConversationContext conversationContext) {
        return this.dp.getConfiguration().of("skins.image-url");
    }

    @Override
    @Nullable
    public Prompt acceptInput(@Nonnull ConversationContext conversationContext, @Nullable String s) {
        Player p = (Player) conversationContext.getForWhom();
        User user = this.dp.getUserManager().getUser(p.getUniqueId());

        // Check if the input is null
        // If so break this prompt
        if (s == null) {
            return this;
        }

        DisguiseAPI.getSkinManager().getFromImage(s, skin -> {
            if (!skin.success()) {
                user.sendRawMessage("skins.error-url");
                return;
            }

            // Add the skin if there was no error
            this.pack.addSkin(skin.get());

            // Send the success message
            user.sendRawMessage("skins.success-url");

            // Reopen the inventory
            // For this player
            this.dp.getInventoryManager().getExactPackProvider(pack).open(p);
        });

        return Prompt.END_OF_CONVERSATION;
    }
}
