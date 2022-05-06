package net.pinger.disguise.prompts.packs;

import net.pinger.disguise.DisguisePlus;
import net.pinger.disguise.manager.skin.SkinFetcher;
import net.pinger.disguise.internal.SkinPackImpl;
import net.pinger.disguise.Skin;
import net.pinger.disguise.SkinPack;
import net.pinger.disguise.user.User;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CreateSkinNamePrompt extends StringPrompt {

    private final DisguisePlus dp;
    private final SkinPack pack;

    public CreateSkinNamePrompt(DisguisePlus dp, SkinPack pack) {
        this.dp = dp;
        this.pack = pack;
    }

    @Override
    public @NotNull String getPromptText(@NotNull ConversationContext conversationContext) {
        return this.dp.getConfiguration().of("skins.player-name");
    }

    @Override
    public @Nullable Prompt acceptInput(@NotNull ConversationContext conversationContext, @Nullable String s) {
        Player p = (Player) conversationContext.getForWhom();
        User user = this.dp.getUserManager().getUser(p);

        if (s.isEmpty())
            return this;

        Skin skin = SkinFetcher.getSkin(s);

        if (skin == null) {
            user.sendRawMessage("skins.error-name", s);
        } else {
            user.sendRawMessage("skins.success-name", s);

            // Add the skin
            ((SkinPackImpl) this.pack).addSkin(skin);
        }

        // Check for error here

        this.dp.getInventoryManager().getExactPackProvider(this.pack).open(p);
        return Prompt.END_OF_CONVERSATION;
    }
}
