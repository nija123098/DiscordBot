package com.github.nija123098.evelyn.command;

import java.util.function.Consumer;

/**
 * A object representing a module or abstract command.
 *
 * @author nija123098
 * @since 1.0.0
 */
public class CommandGroup {
    private final ModuleLevel moduleLevel;
    private final AbstractCommand command;
    public CommandGroup(ModuleLevel moduleLevel) {
        this.moduleLevel = moduleLevel;
        this.command = null;
    }
    public CommandGroup(AbstractCommand command) {
        this.command = command;
        this.moduleLevel = null;
    }
    public ModuleLevel getModuleLevel() {
        return this.moduleLevel;
    }
    public AbstractCommand getCommand() {
        return this.command;
    }
    public boolean isModuleGroup() {
        return this.command == null;
    }
    public void act(Consumer<ModuleLevel> moduleLevelConsumer, Consumer<AbstractCommand> commandConsumer) {
        if (this.command == null) moduleLevelConsumer.accept(this.moduleLevel);
        else commandConsumer.accept(this.command);
    }
}
