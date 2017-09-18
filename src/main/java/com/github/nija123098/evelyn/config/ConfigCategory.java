package com.github.nija123098.evelyn.config;

import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.perms.BotRole;

import java.util.HashSet;
import java.util.Set;

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
    ConfigCategory(ModuleLevel level, BotRole botRole) {
        this.level = level;
        this.botRole = botRole;
    }
    ConfigCategory(ModuleLevel level) {
        this(level, level.getDefaultRole());
    }
    public ModuleLevel getLevel() {
        return this.level;
    }
    public BotRole getBotRole(){
        return this.botRole;
    }
    public Set<AbstractConfig<? extends Configurable, ?>> getConfigs() {
        return this.configs;
    }
    public void addConfig(AbstractConfig abstractConfig){
        this.configs.add(abstractConfig);
    }
}
