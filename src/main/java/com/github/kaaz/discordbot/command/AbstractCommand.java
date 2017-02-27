package com.github.kaaz.discordbot.command;

import com.github.kaaz.discordbot.discordwrapperobjects.Guild;
import com.github.kaaz.discordbot.discordwrapperobjects.User;
import com.github.kaaz.discordbot.perms.BotRole;
import com.github.kaaz.discordbot.util.Log;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Made by nija123098 on 2/20/2017.
 */
public abstract class AbstractCommand {
    String name;
    private Method method;
    private Class<?>[] args;
    private BotRole botRole;
    private Set<String> absoluteAliases, emoticonAliases, allAliases;
    AbstractCommand(String name, BotRole botRole, String[] absoluteAliases, String[] emoticonAliases){
        this.name = name;
        this.botRole = botRole;
        this.absoluteAliases = new HashSet<>(absoluteAliases.length);
        Collections.addAll(this.absoluteAliases, absoluteAliases);
        this.emoticonAliases = new HashSet<>(emoticonAliases.length);
        Collections.addAll(this.emoticonAliases, emoticonAliases);
        Method[] methods = this.getClass().getMethods();
        for (Method m : methods) {
            if (m.isAnnotationPresent(Command.class)) {
                this.method = m;
                break;
            }
        }
        if (this.method == null){
            Log.log("No method annotated " + Command.class.getSimpleName() + " in command: " + this.getClass().getName());
            return;
        }
        this.args = this.method.getParameterTypes();
        this.allAliases = new HashSet<>();
    }
    public Set<String> getAbsoluteAliases() {
        return this.absoluteAliases;
    }
    public Set<String> getEmoticonAliases() {
        return this.emoticonAliases;
    }
    public abstract ModuleLevel getModule();
    public Set<String> getAliases() {
        return this.allAliases;
    }
    public boolean mayUse(User user, Guild guild){
        return BotRole.hasRequiredBotRole(this.botRole, user, guild);
    }
}
