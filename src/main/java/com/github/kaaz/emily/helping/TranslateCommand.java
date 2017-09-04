package com.github.kaaz.emily.helping;

import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.ModuleLevel;
import com.github.kaaz.emily.command.annotations.Command;
import com.github.kaaz.emily.command.annotations.Context;
import com.github.kaaz.emily.config.ConfigHandler;
import com.github.kaaz.emily.config.configs.user.UserLanguageConfig;
import com.github.kaaz.emily.discordobjects.helpers.MessageMaker;
import com.github.kaaz.emily.discordobjects.wrappers.Reaction;
import com.github.kaaz.emily.discordobjects.wrappers.User;
import com.github.kaaz.emily.util.EmoticonHelper;

/**
 * Made by nija123098 on 7/21/2017.
 */
public class TranslateCommand extends AbstractCommand {
    public TranslateCommand() {
        super("translate", ModuleLevel.HELPER, null, "speech_balloon, speech_left", "Translates a message to the language of your choice based on the user_language config. This is a reaction based command");
    }
    @Command
    public void command(@Context(softFail = true) Reaction reaction, MessageMaker maker, User user){
        if (reaction == null){
            maker.append("Please use " + EmoticonHelper.getChars("speech_balloon", false) + " on a message to translate it to your language!");
            return;
        }
        if (ConfigHandler.getSetting(UserLanguageConfig.class, user) == null) {
            maker.withDM().append("Please set your language using `@Emily cfg set user_language myLanguage`");
            return;
        }
        maker.append(reaction.getMessage().getContent());
    }

    @Override
    protected String getLocalUsages() {
        return "react to a message to translate it";
    }
}
