package com.github.nija123098.evelyn.command;

import com.github.nija123098.evelyn.command.annotations.LaymanName;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.perms.BotRole;
import com.github.nija123098.evelyn.util.EmoticonHelper;

import java.awt.*;
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
    SYSTEM_LEVEL(Color.GRAY, BotRole.SYSTEM, "floppy_disk"),
    DEVELOPMENT(Color.BLACK, BotRole.CONTRIBUTOR, "computer"),
    BOT_ADMINISTRATIVE(new Color(175, 30, 5), BotRole.BOT_ADMIN, "monkey"),
    ADMINISTRATIVE(Color.BLUE, BotRole.GUILD_TRUSTEE, "oncoming_police_car"),
    ECONOMY(Color.YELLOW, "moneybag"),
    INFO(new Color(39, 209, 110), "chart_with_upwards_trend"),
    HELPER(Color.CYAN,"hand_splayed"),
    FUN(Color.MAGENTA, "game_die"),
    MUSIC(Color.BLUE, "headphones"),
    NONE(Color.WHITE, "grey_question"),;
    private final String icon, iconName;
    private final BotRole botRole;
    private final List<AbstractCommand> commands = new ArrayList<>();
    private final Color color;
    void addCommand(AbstractCommand command) {
        this.commands.add(command);
    }
    ModuleLevel(Color color, String icon) {
        this(color, BotRole.USER, icon);
    }
    ModuleLevel(Color color, BotRole role, String icon) {
        this.color = color;
        this.botRole = role;
        this.iconName = icon;
        this.icon = EmoticonHelper.getChars(icon, true);
    }
    public String getIcon() {
        return this.icon;
    }
    public String getIconName() {
        return this.iconName;
    }
    public BotRole getDefaultRole() {
        return this.botRole;
    }
    public List<AbstractCommand> getCommands() {
        return this.commands;
    }
    public Color getColor() {
        return this.color;
    }
    public static List<ModuleLevel> getGeneralApproved(User user, Guild guild) {
        return Stream.of(values()).filter(level -> level.getDefaultRole().hasRequiredRole(user, guild)).collect(Collectors.toList());
    }
}
