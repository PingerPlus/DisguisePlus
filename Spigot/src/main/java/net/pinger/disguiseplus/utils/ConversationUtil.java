package net.pinger.disguiseplus.utils;

import net.pinger.disguiseplus.DisguisePlus;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ConversationUtil {

    private final List<Conversation> conversations = new ArrayList<>();
    private final ConversationFactory factory;

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
        this.createConversation(player, prompt, 10);
    }

    /**
     * This method creates a conversation for a certain player
     * with a specific timeout.
     *
     * @param player the player
     * @param prompt the prompt
     * @param timeout the timeout
     */

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
