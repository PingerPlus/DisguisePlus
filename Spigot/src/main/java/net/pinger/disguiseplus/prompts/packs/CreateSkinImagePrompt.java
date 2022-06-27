package net.pinger.disguiseplus.prompts.packs;

import net.pinger.disguiseplus.DisguisePlus;
import net.pinger.disguiseplus.fetcher.SkinFetcher;
import net.pinger.disguiseplus.internal.SkinPackImpl;
import net.pinger.disguiseplus.SkinPack;
import net.pinger.disguiseplus.user.User;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CreateSkinImagePrompt extends StringPrompt {

    private final DisguisePlus dp;
    private final net.pinger.disguiseplus.SkinPack pack;

    public CreateSkinImagePrompt(DisguisePlus dp, SkinPack pack) {
        this.dp = dp;
        this.pack = pack;
    }

    @Override
    public @NotNull String getPromptText(@NotNull ConversationContext conversationContext) {
        return this.dp.getConfiguration().of("skins.image-url");
    }

    @Override
    public @Nullable Prompt acceptInput(@NotNull ConversationContext conversationContext, @Nullable String s) {
        Player p = (Player) conversationContext.getForWhom();
        User user = this.dp.getUserManager().getUser(p.getUniqueId());

        if (s.isEmpty())
            return this;

        SkinPackImpl simple = (SkinPackImpl) pack;

        // Add the skin
        SkinFetcher.catchSkin(s, skin -> {
            simple.addSkin(skin);
            user.sendRawMessage("skins.success-url", s);

            // Reopen the inv
            this.dp.getInventoryManager().getExactPackProvider(this.pack).open(p);
        }, err -> {
            user.sendRawMessage("skins.error-url", s);

            // Reopen the inv
            this.dp.getInventoryManager().getExactPackProvider(this.pack).open(p);
        });

        return Prompt.END_OF_CONVERSATION;
    }
}