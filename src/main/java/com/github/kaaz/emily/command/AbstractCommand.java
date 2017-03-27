package com.github.kaaz.emily.command;

import com.github.kaaz.emily.config.ConfigHandler;
import com.github.kaaz.emily.config.Configurable;
import com.github.kaaz.emily.config.configs.guild.SpecialPermsGuildEnabledConfig;
import com.github.kaaz.emily.config.configs.role.*;
import com.github.kaaz.emily.discordobjects.wrappers.Channel;
import com.github.kaaz.emily.discordobjects.wrappers.Guild;
import com.github.kaaz.emily.discordobjects.wrappers.Role;
import com.github.kaaz.emily.discordobjects.wrappers.User;
import com.github.kaaz.emily.perms.BotRole;
import com.github.kaaz.emily.service.services.MemoryManagementService;
import com.github.kaaz.emily.util.Log;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
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
    private String name;
    private Method method;
    private Class<?>[] args;
    private BotRole botRole;
    private Set<String> absoluteAliases, emoticonAliases, allNames;
    private long globalUseTime, globalCoolDownTime;
    private List<Guild> guildCoolDowns;
    private List<Channel> channelCoolDowns;
    private List<User> userCoolDowns;
    private List<Configurable.GuildUser> guildUserCoolDowns;
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
        this.globalCoolDownTime = this.getCoolDown(Configurable.GlobalConfigurable.class);
        // this.globalUseTime = 0;
        long persistence = this.getCoolDown(Guild.class);
        if (persistence != -1){
            this.guildCoolDowns = new MemoryManagementService.ManagedList<>(persistence);
        }
        persistence = this.getCoolDown(Channel.class);
        if (persistence != -1){
            this.channelCoolDowns = new MemoryManagementService.ManagedList<>(persistence);
        }
        persistence = this.getCoolDown(User.class);
        if (persistence != -1){
            this.userCoolDowns = new MemoryManagementService.ManagedList<>(persistence);
        }
        persistence = this.getCoolDown(Configurable.GuildUser.class);
        if (persistence != -1){
            this.guildUserCoolDowns = new MemoryManagementService.ManagedList<>(persistence);
        }
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
     * A standard getter.
     *
     * @return the name of the command
     */
    public String getName(){
        return this.name;
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
    public boolean hasPermission(User user, Guild guild) {
        boolean hasNormalPerm = BotRole.hasRequiredBotRole(this.botRole, user, guild);
        if (BotRole.getBestBotRole(user, guild).ordinal() < BotRole.GUILD_TRUSTEE.ordinal()){
            if (ConfigHandler.getSetting(SpecialPermsGuildEnabledConfig.class, guild)){
                return hasNormalPerm;
            }
            boolean disapproved = false;
            for (Role role : user.getRolesForGuild(guild)){
                if (!ConfigHandler.getSetting(SpecialPermsRoleEnable.class, role)){
                    continue;
                }
                if (ConfigHandler.getSetting(PermsCommandWhitelistConfig.class, role).contains(this.getName()) || ConfigHandler.getSetting(PermsModuleWhitelistConfig.class, role).contains(this.getName()) && !ConfigHandler.getSetting(PermsModuleWhitelistExemptionsConfig.class, role).contains(this.getName())){
                    return true;
                }
                if (ConfigHandler.getSetting(PermsCommandBlacklistConfig.class, role).contains(this.getName()) || ConfigHandler.getSetting(PermsModuleWhitelistConfig.class, role).contains(this.getName()) && !ConfigHandler.getSetting(PermsCommandBlacklistConfig.class, role).contains(this.getName())){
                    disapproved = true;
                }
            }
            return !disapproved && hasNormalPerm;
        }else{
            return hasNormalPerm;
        }
    }

    public boolean checkCoolDown(Guild guild, Channel channel, User user){
        if (this.globalUseTime != -1){
            if (this.globalUseTime < System.currentTimeMillis()){
                return false;
            } else {
                this.globalUseTime = System.currentTimeMillis() + this.globalCoolDownTime;
            }
        }
        if (this.guildCoolDowns != null){
            if (this.guildCoolDowns.contains(guild)){
                return false;
            }else{
                this.guildCoolDowns.add(guild);
            }
        }
        if (this.channelCoolDowns != null){
            if (this.channelCoolDowns.contains(channel)){
                return false;
            }else{
                this.channelCoolDowns.add(channel);
            }
        }
        if (this.userCoolDowns != null){
            if (this.userCoolDowns.contains(user)){
                return false;
            }else{
                this.userCoolDowns.add(user);
            }
        }
        Configurable.GuildUser guildUser = Configurable.getGuildUser(guild, user);
        if (this.guildUserCoolDowns != null){
            if (this.guildUserCoolDowns.contains(guildUser)){
                return false;
            }else{
                this.guildUserCoolDowns.add(guildUser);
            }
        }
        return true;
    }

    /**
     * Returns the command's module
     *
     * @return the module of which the command is part of
     */
    public abstract ModuleLevel getModule();

    /**
     * Returns the cool down in millis dependent
     * on the type of configurable which is being rate limited
     *
     * @param clazz the configurable type
     * @return the cool down in millis dependent on the tyoe
     */
    protected long getCoolDown(Class<? extends Configurable<?>> clazz){
        return -1;
    }
}
