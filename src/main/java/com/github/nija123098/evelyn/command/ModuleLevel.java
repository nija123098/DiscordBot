package com.github.nija123098.evelyn.command;

import com.github.nija123098.evelyn.command.annotations.LaymanName;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.perms.BotRole;
import com.github.nija123098.evelyn.util.EmoticonHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * An enum for every command module which assists in defining
 * default values for commands which are in the module.
 *
 * @author nija123098
 * @since 1.0.0
 */
@LaymanName(value = "Module", help = "A group of commands.  MUSIC, FUN, BOT_ADMINISTRATIVE, ADMINISTRATIVE, ECONOMY, DEVELOPMENT, INFO, HELPER, NONE")
public enum ModuleLevel {
    SYSTEM_LEVEL(BotRole.SYSTEM, "floppy_disk"),
    DEVELOPMENT(BotRole.CONTRIBUTOR, "computer"),
    BOT_ADMINISTRATIVE(BotRole.BOT_ADMIN, "monkey"),
    ADMINISTRATIVE(BotRole.GUILD_TRUSTEE, "oncoming_police_car"),
    ECONOMY("moneybag"),
    INFO("chart_with_upwards_trend"),
    HELPER("hand_splayed"),
    FUN("game_die"),
    MUSIC("headphones"),
    NONE("grey_question"),;
    private String icon, iconName;
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
        this.iconName = icon;
        this.icon = EmoticonHelper.getChars(icon, true);
    }
    public String getIcon() {
        return this.icon;
    }
    public String getIconName(){
        return this.iconName;
    }
    public BotRole getDefaultRole(){
        return this.botRole;
    }
    public List<AbstractCommand> getCommands() {
        return this.commands;
    }
    public static List<ModuleLevel> getGeneralApproved(User user, Guild guild){
        return Stream.of(values()).filter(level -> level.getDefaultRole().hasRequiredRole(user, guild)).collect(Collectors.toList());
    }
}
