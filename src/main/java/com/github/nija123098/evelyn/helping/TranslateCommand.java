package com.github.nija123098.evelyn.helping;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.command.annotations.Context;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.config.configs.guild.GuildLanguageConfig;
import com.github.nija123098.evelyn.config.configs.user.UserLanguageConfig;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.Message;
import com.github.nija123098.evelyn.discordobjects.wrappers.Reaction;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.service.services.MemoryManagementService;
import com.github.nija123098.evelyn.util.EmoticonHelper;

import java.util.Set;

/**
 * Made by nija123098 on 7/21/2017.
 */
public class TranslateCommand extends AbstractCommand {
    private static final Set<Message> NO_REPLICATION = new MemoryManagementService.ManagedSet<>(120_000);
    public TranslateCommand() {
        super("translate", ModuleLevel.HELPER, null, "speech_balloon, speech_left", "Translates a message to the language of your choice based on the user_language config. This is a reaction based command");
    }
    @Command
    public void command(@Context(softFail = true) Reaction reaction, MessageMaker maker, User user, Guild guild){
        if (reaction == null){
            maker.append("Please react using a " + EmoticonHelper.getChars("speech_balloon", false) + " on a message to translate it to your language!");
            return;
        }
        if (!NO_REPLICATION.add(reaction.getMessage())) return;
        if (ConfigHandler.getSetting(UserLanguageConfig.class, user) == null) {
            maker.withDM().append("Please set your language using `@Evelyn cfg set user_language myLanguage`");
            return;
        }
        maker.append(reaction.getMessage().getContent());
        String lang = ConfigHandler.getSetting(GuildLanguageConfig.class, guild);
        if (lang == null || !lang.equals(ConfigHandler.getSetting(UserLanguageConfig.class, user))) maker.withDM();
    }

    @Override
    protected String getLocalUsages() {
        return "react to a message to translate it";
    }
}
