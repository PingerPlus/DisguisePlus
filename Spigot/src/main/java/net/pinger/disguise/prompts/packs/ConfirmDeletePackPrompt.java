package net.pinger.disguise.prompts.packs;

import net.pinger.disguise.DisguisePlus;
import net.pinger.disguise.factory.SimpleSkinFactory;
import net.pinger.disguise.skin.SkinPack;
import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ConfirmDeletePackPrompt extends StringPrompt {

    private final DisguisePlus dp;
    private final SkinPack pack;

    public ConfirmDeletePackPrompt(DisguisePlus dp, SkinPack pack) {
        this.dp = dp;
        this.pack = pack;
    }

    @Override
    public @NotNull String getPromptText(@NotNull ConversationContext conversationContext) {
        return ChatColor.GREEN + "Type confirm to confirm the deletion of this skin pack.";
    }

    @Override
    public @Nullable Prompt acceptInput(@NotNull ConversationContext conversationContext, @Nullable String s) {
        Player p = (Player) conversationContext.getForWhom();

        if (!s.equalsIgnoreCase("confirm"))
            return this;

        if (((SimpleSkinFactory) this.dp.getSkinFactory()).deleteSkinPack(this.pack)) {
            p.sendRawMessage(ChatColor.GREEN + "Successfully deleted skin pack -> " + pack.getName());
        } else {
            p.sendRawMessage(ChatColor.RED + "Failed to delete the pack with the name -> " + pack.getName());
        }

        this.dp.getInventoryManager().getCategoryProvider(this.pack.getCategory()).open(p);
        return Prompt.END_OF_CONVERSATION;
    }
}
