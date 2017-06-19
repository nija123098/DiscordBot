package com.github.kaaz.emily.chatbot;

import com.github.kaaz.emily.discordobjects.wrappers.Channel;
import com.github.kaaz.emily.util.Log;
import com.google.code.chatterbotapi.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Made by nija123098 on 6/14/2017.
 */
public class ChatBot {
    private static final ChatterBot BOT;
    private static final Map<Channel, ChatBot> BOTS = new HashMap<>();
    static {
        ChatterBot bot = null;
        try{bot = new ChatterBotFactory().create(ChatterBotType.JABBERWACKY);// is broken
        } catch (Exception e) {
            e.printStackTrace();
        }
        BOT = bot;
    }
    public static ChatBot getChatBot(Channel channel){
        return BOTS.computeIfAbsent(channel, c -> new ChatBot(BOT.createSession()));
    }
    private ChatterBotSession session;
    private ChatBot(ChatterBotSession session) {
        this.session = session;
    }
    public String think(String s){
        try {
            ChatterBotThought thought = new ChatterBotThought();
            thought.setText(s);
            return this.session.think(thought).getText();
        } catch (Exception e) {
            Log.log("Error while thinking.  Stop it, it's dangerous.", e);
            return "The chat bot seems to be down right now.";
        }
    }
}
