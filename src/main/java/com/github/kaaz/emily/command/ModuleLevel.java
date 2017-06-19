package com.github.kaaz.emily.command;

import com.github.kaaz.emily.command.anotations.LaymanName;
import com.github.kaaz.emily.util.EmoticonHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * An enum for every command module
 *
 * @author nija123098
 * @since 2.0.0
 */
@LaymanName(value = "Module", help = "A group of commands.  MUSIC, FUN, BOT_ADMINISTRATIVE, ADMINISTRATIVE, ECONOMY, DEVELOPMENT, INFO, HELPER, NONE")
public enum ModuleLevel {
    DEVELOPMENT("computer"),
    BOT_ADMINISTRATIVE("monkey"),
    ADMINISTRATIVE("oncoming_police_car"),
    ECONOMY("moneybag"),
    INFO("chart_with_upwards_trend"),
    HELPER("hand_splayed"),
    FUN("game_die"),
    MUSIC("headphones"),
    NONE("grey_question"),;
    private String icon;
    private List<AbstractCommand> commands = new ArrayList<>();
    void addCommand(AbstractCommand command){
        this.commands.add(command);
    }
    ModuleLevel(String icon) {
        this.icon = EmoticonHelper.getChars(icon);
    }
    public String getIcon() {
        return this.icon;
    }
    public List<AbstractCommand> getCommands() {
        return this.commands;
    }
}
