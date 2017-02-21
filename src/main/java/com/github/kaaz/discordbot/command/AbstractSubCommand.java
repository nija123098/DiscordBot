package com.github.kaaz.discordbot.command;

/**
 * Made by nija123098 on 2/20/2017.
 */
public abstract class AbstractSubCommand extends AbstractCommand {
    private AbstractSuperCommand superCommand;
    protected void setSuperCommand(AbstractSuperCommand superCommand){
        this.superCommand = superCommand;
    }
    @Override
    public ModuleLevel getModule() {
        return superCommand.getModule();
    }
}
