package net.pinger.disguise.prompts.packs;

import net.pinger.disguise.DisguisePlus;
import net.pinger.disguise.manager.skin.SkinFetcher;
import net.pinger.disguise.skin.SimpleSkinPack;
import net.pinger.disguise.skin.SkinPack;
import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CreateSkinImagePrompt extends StringPrompt {

    private final DisguisePlus dp;
    private final SkinPack pack;

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

        if (s.isEmpty())
            return this;

        // Add the skin
        SkinFetcher.catchSkin(s, skin -> {
                    ((SimpleSkinPack) this.pack).addSkin(skin);
                    this.dp.getInventoryManager().getExactPackProvider(this.pack).open(p);
                },
                throwable -> {
                    p.sendMessage(ChatColor.RED + "Image URL is either invalid or you have been rate-limited.");
                    this.dp.getInventoryManager().getExactPackProvider(this.pack).open(p);
            }, this.dp);

        return Prompt.END_OF_CONVERSATION;
    }
}
