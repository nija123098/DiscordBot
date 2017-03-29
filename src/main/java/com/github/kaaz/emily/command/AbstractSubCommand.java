package com.github.kaaz.emily.command;

import com.github.kaaz.emily.perms.BotRole;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * A implementation of AbstractCommand that
 * represents a command that is a sub command
 * of another command.  All sub commands
 * have a single super command.
 *
 * For example:
 * !stats activity
 *
 * @author nija123098
 * @since 2.0.0
 * @see AbstractCommand
 * @see AbstractSuperCommand
 */
public abstract class AbstractSubCommand extends AbstractCommand {
    private AbstractSuperCommand superCommand;
    private Set<String> relativeAliases;
    public AbstractSubCommand(String name, BotRole botRole, String[] absoluteAliases, String[] emoticonAliases, String[] relativeAliases) {
        super(name, botRole, absoluteAliases, emoticonAliases);
        this.relativeAliases = new HashSet<>(relativeAliases.length);
        Collections.addAll(this.relativeAliases, relativeAliases);
    }

    /**
     * Sets the super command of this command
     *
     * @param superCommand the super command to be set
     *                     as this command's super command
     */
    void setSuperCommand(AbstractSuperCommand superCommand){
        this.superCommand = superCommand;
        this.name = this.superCommand.name + " " + this.name;
        Set<String> relativeAliases = new HashSet<>(this.relativeAliases);
        this.superCommand.getAbsoluteAliases().forEach(abs -> relativeAliases.forEach(rel -> this.relativeAliases.add(abs + " " + rel)));
        this.getNames().addAll(this.relativeAliases);
    }

    /**
     * The method to get a HashSet to get all
     * command names that require a super command
     * name to be called.
     *
     * @return the aliases that require a
     * super command name to be called
     */
    public Set<String> getRelativeAliases() {
        return this.relativeAliases;
    }
    @Override
    public ModuleLevel getModule() {
        return superCommand.getModule();
    }
}
