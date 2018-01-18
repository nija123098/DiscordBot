package com.github.nija123098.evelyn.config.commands;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ContextType;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.command.annotations.Context;
import com.github.nija123098.evelyn.config.*;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.Message;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.perms.BotRole;
import com.github.nija123098.evelyn.util.FormatHelper;
import com.github.nija123098.evelyn.util.LanguageHelper;

import java.awt.*;
import java.util.stream.Stream;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class ConfigCommand extends AbstractCommand {
    public ConfigCommand() {
        super("config", BotRole.USER, ModuleLevel.ADMINISTRATIVE, "cfg", null, "Gets information on config values");
    }
    @Command
    public <C extends Configurable> void command(@Argument(optional = true, info = "config/user/channel/role") C configurable, @Argument(optional = true, replacement = ContextType.NONE) ConfigCategory configCategory, @Argument String s, User user, @Context(softFail = true) Guild guild, MessageMaker maker, @Context(softFail = true) Message message){
        maker.withColor(new Color(39, 209, 110));
        if (configurable != null || s == null || s.isEmpty()){
            if (configurable == null) configurable = (C) (guild == null ? user : guild);
            C finalConfigurable = configurable;
            maker.getAuthorName().appendRaw(LanguageHelper.makePossessive(configurable.getName()) + " ").append(" Settings");
            if (configurable instanceof User || configurable instanceof Guild) maker.withAuthorIcon(configurable instanceof User ? ((User) configurable).getAvatarURL() : ((Guild) configurable).getIconURL());
            maker.getNote().append("To view " + (configurable instanceof User ? "server" : "user") + " settings use this command in a " + (configurable instanceof User ? "server" : "DM with me"));
            if (configCategory != null) configCategory.getConfigs().stream().filter(AbstractConfig::isNormalViewing).filter(abstractConfig -> abstractConfig.getConfigLevel() == finalConfigurable.getConfigLevel()).filter(abstractConfig -> abstractConfig.getBotRole().hasRequiredRole(user, guild)).forEach(abstractConfig -> maker.getNewFieldPart().withBoth(abstractConfig.getConfigCommandDisplay(), ConfigHandler.getExteriorSetting(abstractConfig.getName(), finalConfigurable)));
            else Stream.of(ConfigCategory.values()).filter(category -> category.getBotRole().hasRequiredRole(user, guild)).forEach(category -> {
                if (finalConfigurable instanceof Guild) {
                    if (!category.getBotRole().name().contains("BOT") && !category.getBotRole().name().contains("ADMIN")) {
                        maker.getNewFieldPart().withInline(false).withBoth("\u200b", FormatHelper.embedLink(category.name(), ""));
                    }
                } else {
                    if (!category.getBotRole().name().contains("ADMIN")) {
                        maker.getNewFieldPart().withInline(false).withBoth("\u200b", FormatHelper.embedLink(category.name(), ""));
                    }
                }
                if (category.getConfigs().stream().filter(AbstractConfig::isNormalViewing).filter(abstractConfig -> abstractConfig.getConfigLevel() == finalConfigurable.getConfigLevel() || abstractConfig.getConfigLevel() == ConfigLevel.ALL).filter(abstractConfig -> abstractConfig.getBotRole().hasRequiredRole(user, guild)).peek(config -> maker.getNewFieldPart().withBoth(config.getConfigCommandDisplay(), ConfigHandler.getExteriorSetting(config.getName(), finalConfigurable))).count() > 0){
                    maker.guaranteeNewFieldPage();
                }
            });
            // ConfigHandler.getConfigs(configurable.getClass()).stream().filter(config -> config.getBotRole().hasRequiredRole(user, guild)).filter(AbstractConfig::isNormalViewing).forEach(config -> maker.getNewFieldPart().withBoth(config.getName(), ConfigHandler.getExteriorSetting(config.getName(), finalConfigurable)));
        }else maker.append("Please use ").appendRaw("`config get` ").append("or").appendRaw(" `config set`").append(" but you can do !cfg [user/guild] to show all configs now.");
    }
}
