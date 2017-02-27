package com.github.kaaz.discordbot.command;

import com.github.kaaz.discordbot.perms.BotRole;

/**
 * Made by nija123098 on 2/20/2017.
 */
public abstract class AbstractSuperCommand extends AbstractCommand {
    AbstractSuperCommand(String name, BotRole botRole, String[] absoluteAliases, String[] emoticonAliases) {
        super(name, botRole, absoluteAliases, emoticonAliases);
    }
}
