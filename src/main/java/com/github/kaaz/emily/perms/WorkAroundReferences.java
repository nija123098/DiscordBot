package com.github.kaaz.emily.perms;

import java.util.concurrent.atomic.AtomicReference;

import static com.github.kaaz.emily.perms.BotRole.BOT_ADMIN;
import static com.github.kaaz.emily.perms.BotRole.BOT_OWNER;
import static com.github.kaaz.emily.perms.BotRole.GUILD_ADMIN;

/**
 * Made by nija123098 on 4/27/2017.
 */
class WorkAroundReferences {
    static final AtomicReference<BotRole> B_A = new AtomicReference<>();
    static final AtomicReference<BotRole> B_O = new AtomicReference<>();
    static final AtomicReference<BotRole> G_A = new AtomicReference<>();
    static void set(){
        B_A.set(BOT_ADMIN);
        B_O.set(BOT_OWNER);
        G_A.set(GUILD_ADMIN);
    }
}
