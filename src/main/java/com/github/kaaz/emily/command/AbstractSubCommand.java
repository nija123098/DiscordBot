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
    public AbstractSubCommand(String name, BotRole botRole, String absoluteAliases, String emoticonAliases, String relativeAliases) {
        super(name, botRole, absoluteAliases, emoticonAliases);
        if (relativeAliases != null){
            String[] rAliases = relativeAliases.split(", ");
            this.relativeAliases = new HashSet<>(rAliases.length);
            Collections.addAll(this.relativeAliases, rAliases);
        }else{
            this.relativeAliases = new HashSet<>(0);
        }
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
        this.getNames().add(this.name);
        this.superCommand.getNames().forEach(abs -> new HashSet<>(this.relativeAliases).forEach(rel -> this.relativeAliases.add(abs + " " + rel)));
        this.getNames().addAll(this.relativeAliases);
        if (this.botRole == null){
            this.botRole = superCommand.botRole;
        }
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
        return this.superCommand.getModule();
    }
}
