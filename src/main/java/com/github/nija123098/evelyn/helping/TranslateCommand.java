package com.github.nija123098.evelyn.helping;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.command.annotations.Context;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.config.configs.guild.GuildLanguageConfig;
import com.github.nija123098.evelyn.config.configs.user.UserLanguageConfig;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.Reaction;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.perms.BotRole;

import java.util.Objects;

import static com.github.nija123098.evelyn.command.ModuleLevel.HELPER;
import static com.github.nija123098.evelyn.config.ConfigHandler.getSetting;
import static com.github.nija123098.evelyn.util.EmoticonHelper.getChars;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class TranslateCommand extends AbstractCommand {
    public TranslateCommand() {
        super("translate", HELPER, null, "speech_balloon, speech_left", "Translates a message to the language of your choice based on the user_language config. This is a reaction based command");
    }

    @Command
    public void command(@Context(softFail = true) Reaction reaction, MessageMaker maker, User user, Guild guild) {
        if (reaction == null) {
            maker.append("Please react using a " + getChars("speech_balloon", false) + " on a message to translate it to your language!");
            return;
        }
        String userLang = getSetting(UserLanguageConfig.class, user), guildLang = getSetting(GuildLanguageConfig.class, guild);
        if (userLang == null && guildLang == null) {
            maker.withDM().append("Please set your language using `@Evelyn cfg set user_language myLanguage`\nI will now default to English as your personal language\n \n");
            ConfigHandler.setSetting(UserLanguageConfig.class, user, "en");
            userLang = "en";
            if (BotRole.GUILD_TRUSTEE.hasRequiredRole(user, guild)) maker.append("You can also set the guild language using `@Evelyn cfg set guild_language myLanguage\n \n");
        }
        maker.withForceTranslate();
        maker.append(reaction.getMessage().getContent());
        if (!Objects.equals(userLang, guildLang)) maker.withDM();
    }

    @Override
    protected String getLocalUsages() {
        return "react to a message to translate it";
    }

    @Override
    public boolean useReactions() {
        return true;
    }
}
