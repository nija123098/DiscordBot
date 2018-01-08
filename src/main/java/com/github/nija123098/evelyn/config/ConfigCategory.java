package com.github.nija123098.evelyn.config;

import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.perms.BotRole;

import java.util.HashSet;
import java.util.Set;

/**
 * An enum for representing the category of a
 * config which defines defaults for the config.
 */
public enum ConfigCategory {
    GAME_TEMPORARY_CHANNELS(ModuleLevel.ADMINISTRATIVE),
    STAT_TRACKING(ModuleLevel.SYSTEM_LEVEL),
    GUILD_PERSONALIZATION(ModuleLevel.ADMINISTRATIVE),
    MUSIC(ModuleLevel.MUSIC),
    ECONOMY(ModuleLevel.ECONOMY),
    LOGGING(ModuleLevel.ADMINISTRATIVE),
    FAVOR(ModuleLevel.ADMINISTRATIVE),
    PERSONAL_PERSONALIZATION(ModuleLevel.NONE, BotRole.BOT),
    MODERATION(ModuleLevel.ADMINISTRATIVE),
    GUILD_HIERARCHY(ModuleLevel.ADMINISTRATIVE, BotRole.GUILD_ADMIN);
    private final Set<AbstractConfig<? extends Configurable, ?>> configs = new HashSet<>();
    private final ModuleLevel level;
    private final BotRole botRole;

    /**
     * The override for setting the {@link BotRole} as the
     * default for setting configs which are under this category.
     *
     * @param level the module level this config category.
     * @param botRole the {@link BotRole} required to edit a config from the category.
     */
    ConfigCategory(ModuleLevel level, BotRole botRole) {
        this.level = level;
        this.botRole = botRole;
    }

    /**
     * A constructor which sets the {@link ModuleLevel} for the {@link ConfigCategory}.
     *
     * @param level the {@link ModuleLevel} this belongs to.
     */
    ConfigCategory(ModuleLevel level) {
        this(level, null);
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
    public void addConfig(AbstractConfig<? extends Configurable, ?> abstractConfig){
        this.configs.add(abstractConfig);
    }
}
