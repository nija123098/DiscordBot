package com.github.kaaz.emily.automoderation.modaction.support;

import com.github.kaaz.emily.config.AbstractConfig;
import com.github.kaaz.emily.config.ConfigHandler;
import com.github.kaaz.emily.discordobjects.wrappers.Guild;
import com.github.kaaz.emily.perms.BotRole;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Made by nija123098 on 5/10/2017.
 */
public class CaseNumberConfig extends AbstractConfig<Integer, Guild> {
    public CaseNumberConfig() {
        super("case_number", BotRole.SYSTEM, 0, "The case number for moderation actions");
    }
    public static int incrament(Guild guild){
        AtomicInteger i = new AtomicInteger();
        ConfigHandler.changeSetting(CaseNumberConfig.class, guild, integer -> i.addAndGet(1));
        return i.get();
    }
}
