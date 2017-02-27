package com.github.kaaz.discordbot.command;

import com.github.kaaz.discordbot.perms.BotRole;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Made by nija123098 on 2/20/2017.
 */
public abstract class AbstractSubCommand extends AbstractCommand {
    private AbstractSuperCommand superCommand;
    private Set<String> relativeAliases;
    AbstractSubCommand(String name, BotRole botRole, String[] absoluteAliases, String[] emoticonAliases, String[] relativeAliases) {
        super(name, botRole, absoluteAliases, emoticonAliases);
        this.relativeAliases = new HashSet<>(relativeAliases.length);
        Collections.addAll(this.relativeAliases, relativeAliases);
    }
    void setSuperCommand(AbstractSuperCommand superCommand){
        this.superCommand = superCommand;
        this.name = this.superCommand.name + " " + this.name;
        Set<String> relativeAliases = new HashSet<>(this.relativeAliases);
        this.superCommand.getAbsoluteAliases().forEach(abs -> relativeAliases.forEach(rel -> this.relativeAliases.add(abs + " " + rel)));
        this.getAliases().addAll(this.relativeAliases);
    }
    public Set<String> getRelativeAliases() {
        return this.relativeAliases;
    }
    @Override
    public ModuleLevel getModule() {
        return superCommand.getModule();
    }
}
