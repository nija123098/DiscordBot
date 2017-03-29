package com.github.kaaz.emily.command;

import com.github.kaaz.emily.perms.BotRole;

/**
 * A implementation of AbstractCommand that
 * represents a command that may be a
 * super command of another command.
 * Some properties of sub command will
 * be inherited by their super commands.
 *
 * For example:
 * !stats
 * As a super command to:
 * !stats activity
 *
 * @author nija123098
 * @since 2.0.0
 * @see AbstractCommand
 */
public abstract class AbstractSuperCommand extends AbstractCommand {
    public AbstractSuperCommand(String name, BotRole botRole, String[] absoluteAliases, String[] emoticonAliases) {
        super(name, botRole, absoluteAliases, emoticonAliases);
    }
}
