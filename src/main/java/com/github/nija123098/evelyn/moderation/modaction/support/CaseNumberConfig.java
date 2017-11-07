package com.github.nija123098.evelyn.moderation.modaction.support;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.perms.BotRole;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Made by nija123098 on 5/10/2017.
 */
public class CaseNumberConfig extends AbstractConfig<Integer, Guild> {
    public CaseNumberConfig() {
        super("case_number", ConfigCategory.STAT_TRACKING, 0, "The case number for moderation actions");
    }
    public static int incrament(Guild guild){
        AtomicInteger i = new AtomicInteger();
        ConfigHandler.changeSetting(CaseNumberConfig.class, guild, integer -> i.addAndGet(1));
        return i.get();
    }
}
