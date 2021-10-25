package net.pinger.disguise.utils;

import net.pinger.common.lang.Lists;
import net.pinger.disguise.DisguisePlus;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;

import java.util.List;

public class ConversationUtil {

    private final ConversationFactory factory;

    // List of all conversations
    private final List<Conversation> conversations = Lists.newArrayList();

    public ConversationUtil(DisguisePlus disguisePlus) {
        this.factory = new ConversationFactory(disguisePlus);
    }

    /**
     * This method creates a conversation for a certain player.
     *
     * @param player the player
     * @param prompt the conversation prompt
     */

    public void createConversation(Player player, Prompt prompt) {
        Conversation conversation = this.factory.withFirstPrompt(prompt)
                .withLocalEcho(false)
                .withTimeout(10)
                .withEscapeSequence("return")
                .buildConversation(player);

        // Automatically do this
        player.closeInventory();

        this.conversations.add(conversation);
        // Start it
        conversation.begin();
    }

    public void createConversation(Player player, Prompt prompt, int timeout) {
        Conversation conversation = this.factory.withFirstPrompt(prompt)
                .withLocalEcho(false)
                .withTimeout(timeout)
                .withEscapeSequence("return")
                .buildConversation(player);

        // Automatically do this
        player.closeInventory();

        this.conversations.add(conversation);
        // Start it
        conversation.begin();
    }

    public void cancelAllConversations() {
        for (Conversation conversation : this.conversations) {
            // Abandon the conversations
            conversation.abandon();
        }
    }
}
