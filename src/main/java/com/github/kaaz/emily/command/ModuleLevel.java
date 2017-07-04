package com.github.kaaz.emily.command;

import com.github.kaaz.emily.command.annotations.LaymanName;
import com.github.kaaz.emily.perms.BotRole;
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
    DEVELOPMENT(BotRole.CONTRIBUTOR, "computer"),
    BOT_ADMINISTRATIVE(BotRole.BOT_ADMIN, "monkey"),
    ADMINISTRATIVE(BotRole.GUILD_TRUSTEE, "oncoming_police_car"),
    ECONOMY("moneybag"),
    INFO("chart_with_upwards_trend"),
    HELPER("hand_splayed"),
    FUN("game_die"),
    MUSIC("headphones"),
    NONE("grey_question"),;
    private String icon;
    private BotRole botRole;
    private List<AbstractCommand> commands = new ArrayList<>();
    void addCommand(AbstractCommand command){
        this.commands.add(command);
    }
    ModuleLevel(String icon) {
        this(BotRole.USER, icon);
    }
    ModuleLevel(BotRole role, String icon) {
        this.botRole = role;
        this.icon = EmoticonHelper.getChars(icon);
    }
    public String getIcon() {
        return this.icon;
    }
    public BotRole getDefaultRole(){
        return this.botRole;
    }
    public List<AbstractCommand> getCommands() {
        return this.commands;
    }
}
