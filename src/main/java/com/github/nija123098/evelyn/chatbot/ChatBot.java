package com.github.nija123098.evelyn.chatbot;

import com.github.nija123098.evelyn.command.CommandHandler;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.discordobjects.wrappers.Channel;

import java.util.HashMap;
import java.util.Map;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class ChatBot {
    private static final Map<Channel, ChatBot> BOTS = new HashMap<>();
    public static boolean mayChat(Channel channel, String s){
        return !(s == null || s.isEmpty() || !Character.isLetterOrDigit(s.charAt(0)) || !s.startsWith(CommandHandler.MENTION_NICK.get()) || !s.startsWith(CommandHandler.MENTION.get())) && ConfigHandler.getSetting(AllowChatBotConfig.class, channel);
    }
    public static ChatBot getChatBot(Channel channel){
        return BOTS.computeIfAbsent(channel, c -> new ChatBot());
    }
    private ChatBot() {}
    public String think(String thought){
        // if (CommandHandler.MENTION.get().startsWith(thought)) thought = thought.substring(CommandHandler.MENTION.get().length());
        // else if (CommandHandler.MENTION_NICK.get().startsWith(thought)) thought = thought.substring(CommandHandler.MENTION_NICK.get().length());
        return "ChatBot became a payed service at a price which we can not come close to afford, sorry for the inconvenience.  If you are wondering who's fault this is his name starts with  \"Soar\" and ends with \"nir\" and his discord ID is 187580838194577409";
    }
}
