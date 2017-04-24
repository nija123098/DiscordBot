package com.github.kaaz.emily.template;

import com.github.kaaz.emily.command.ContextPack;
import com.github.kaaz.emily.config.ConfigHandler;
import com.github.kaaz.emily.config.GlobalConfigurable;
import com.github.kaaz.emily.config.configs.global.GlobalTemplateConfig;
import com.github.kaaz.emily.config.configs.guild.GuildTemplatesConfig;
import com.github.kaaz.emily.discordobjects.wrappers.*;
import com.github.kaaz.emily.util.Log;
import com.github.kaaz.emily.util.Rand;

import java.util.List;

/**
 * Made by nija123098 on 2/27/2017.
 */
public class TemplateHandler {
    public static final char LEFT_BRACE = '<', RIGHT_BRACE = '>', ARGUMENT_SPLITTER = '|', ARGUMENT_CHARACTER = '%';

    /**
     * Forces the initialization of this class
     */
    public static void initialize(){
        Log.log("Template Handler initialize");
    }

    public static String interpret(KeyPhrase keyPhrase, ContextPack pack, Object...args){
        return interpret(keyPhrase, pack.getUser(), pack.getShard(), pack.getChannel(), pack.getGuild(), pack.getMessage(), pack.getReaction(), args);
    }

    public static String interpret(KeyPhrase keyPhrase, User user, Shard shard, Channel channel, Guild guild, Message message, Reaction reaction, Object...args){
        List<Template> templates = ConfigHandler.getSetting(GuildTemplatesConfig.class, guild).get(keyPhrase);
        if (templates == null || templates.size() == 0){
            templates = ConfigHandler.getSetting(GlobalTemplateConfig.class, GlobalConfigurable.GLOBAL).get(keyPhrase);
        }
        if (templates == null || templates.size() == 0){
            Log.log("No templates found for KeyPhrase: " + keyPhrase.name());
            return "No templates for KeyPhrase: " + keyPhrase.name();
        }// should not throw an exception since nothing failed
        return templates.get(Rand.getRand(templates.size() - 1)).interpret(user, shard, channel, guild, message, reaction, args);
    }
}
