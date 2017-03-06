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
 * The representative class for a command
 * that is subclassed directly by
 * AbstractSubCommand and AbstractSuperCommand,
 * as well as all commands used by the robot.
 *
 * All invocation and identification of commands
 * are contained within this class.
 *
 * @author nija123098
 * @since 2.0.0
 * @see AbstractSubCommand
 * @see AbstractSuperCommand
 */
public abstract class AbstractCommand {
    String name;
    private Method method;
    private Class<?>[] args;
    private BotRole botRole;
    private Set<String> absoluteAliases, emoticonAliases, allNames;
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
        this.allNames = new HashSet<>(this.absoluteAliases.size() + this.emoticonAliases.size() + 1);
        this.allNames.add(this.name);
        this.allNames.addAll(this.absoluteAliases);
        this.allNames.addAll(this.emoticonAliases);
    }

    /**
     * The method to get the set of all strings
     * that can be used without using a super
     * command to call it.
     *
     * @return the set of all aliases
     * that require no super command
     */
    public Set<String> getAbsoluteAliases() {
        return this.absoluteAliases;
    }

    /**
     * The method to get a set of all emoticon chars
     * by which the command can be called
     *
     * @return the HashSet of the emoticon's chars
     * that can represent this command
     */
    public Set<String> getEmoticonAliases() {
        return this.emoticonAliases;
    }

    /**
     * The method to get the set off all names
     * by which the command goes by
     *
     * @return the HashSet of all names
     * by which the command goes by
     */
    public Set<String> getNames() {
        return this.allNames;
    }

    /**
     * A check if the user can use a command in the context
     *
     * @param user the user that is being checked for permission
     * @param guild the guild in which permissions are being checked,
     *              null if there is no guild in the context
     * @return if the user can use this command
     * in the guild, if one exists
     */
    public boolean mayUse(User user, Guild guild){
        return BotRole.hasRequiredBotRole(this.botRole, user, guild);
    }

    /**
     * Returns the command's module
     *
     * @return the module of which the command is part of
     */
    public abstract ModuleLevel getModule();
}
