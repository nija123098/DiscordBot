package com.github.nija123098.evelyn.config;

import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.perms.BotRole;

import java.util.HashSet;
import java.util.Set;

/**
 * An enum for representing the category of a
 * config which defines defaults for the config.
 *
 * @author nija123098
 * @since 1.0.0
 */
public enum ConfigCategory {
    GAME_TEMPORARY_CHANNELS("GTC", ModuleLevel.ADMINISTRATIVE),
    STAT_TRACKING("ST", ModuleLevel.SYSTEM_LEVEL),
    GUILD_PERSONALIZATION("GP", ModuleLevel.ADMINISTRATIVE),
    MUSIC("MS", ModuleLevel.MUSIC),
    ECONOMY("ECO", ModuleLevel.ECONOMY),
    LOGGING("L", ModuleLevel.ADMINISTRATIVE),
    FAVOR("FA", ModuleLevel.ADMINISTRATIVE),
    PERSONAL_PERSONALIZATION("PP", ModuleLevel.NONE, BotRole.BOT),
    MODERATION("MD", ModuleLevel.ADMINISTRATIVE),
    GUILD_HIERARCHY("HI", ModuleLevel.ADMINISTRATIVE, BotRole.GUILD_ADMIN);
    private final Set<AbstractConfig<? extends Configurable, ?>> configs = new HashSet<>();
    private final ModuleLevel level;
    private final BotRole botRole;
    private final String abbreviation;

    /**
     * The override for setting the {@link BotRole} as the
     * default for setting configs which are under this category.
     *
     * @param abbreviation the abbreviation for setting a {@link AbstractConfig} shortID.
     * @param level the module level this config category.
     * @param botRole the {@link BotRole} required to edit a config from the category.
     */
    ConfigCategory(String abbreviation, ModuleLevel level, BotRole botRole) {
        this.abbreviation = abbreviation;
        this.level = level;
        this.botRole = botRole;
    }

    /**
     * A constructor which sets the {@link ModuleLevel} for the {@link ConfigCategory}.
     *
     * @param abbreviation the abbreviation for setting a {@link AbstractConfig} shortID.
     * @param level the {@link ModuleLevel} this belongs to.
     */
    ConfigCategory(String abbreviation, ModuleLevel level) {
        this(abbreviation, level, null);
    }
    public ModuleLevel getLevel() {
        return this.level;
    }
    public BotRole getBotRole(){
        return botRole == null ? this.level.getDefaultRole() : this.botRole;
    }
    public Set<AbstractConfig<? extends Configurable, ?>> getConfigs() {
        return this.configs;
    }

    /**
     * Adds an {@link AbstractConfig} to the
     * list of {@link AbstractConfig}s which
     * are under this {@link ConfigCategory}.
     *
     * @param abstractConfig the config to add to the category.
     */
    public String addConfig(AbstractConfig<? extends Configurable, ?> abstractConfig){
        this.configs.add(abstractConfig);
        return this.abbreviation + this.configs.size();
    }
}
