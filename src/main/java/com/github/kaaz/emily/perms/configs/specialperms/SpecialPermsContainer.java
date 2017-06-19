package com.github.kaaz.emily.perms.configs.specialperms;

import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.ModuleLevel;
import com.github.kaaz.emily.discordobjects.wrappers.Channel;
import com.github.kaaz.emily.discordobjects.wrappers.Guild;
import com.github.kaaz.emily.discordobjects.wrappers.Role;
import com.github.kaaz.emily.discordobjects.wrappers.User;
import com.github.kaaz.emily.exeption.ArgumentException;
import com.github.kaaz.emily.perms.BotRole;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Made by nija123098 on 6/13/2017.
 */
public class SpecialPermsContainer {
    private Guild guild;
    private final Map<Channel, Map<Role, Set<String>>> allowCommandMap = new ConcurrentHashMap<>();
    private final Map<Channel, Map<Role, Set<String>>> denyCommandMap = new ConcurrentHashMap<>();
    private final Map<Channel, Map<Role, Set<String>>> exemptCommandMap = new ConcurrentHashMap<>();
    private final Map<Channel, Map<Role, Set<ModuleLevel>>> denyModuleMap = new ConcurrentHashMap<>();
    private final Map<Channel, Map<Role, Set<ModuleLevel>>> allowModuleMap = new ConcurrentHashMap<>();
    public SpecialPermsContainer(Guild guild){
        this.guild = guild;
    }
    protected SpecialPermsContainer() {}
    public Boolean getSpecialPermission(AbstractCommand command, Channel channel, User user){
        boolean deny = false;
        for (Role role : user.getRolesForGuild(this.guild)) {
            if (!this.exemptCommandMap.get(channel).get(role).contains(command.getName())) {
                if (this.denyModuleMap.get(channel).get(role).contains(command.getModule())) deny = true;
                else if (this.allowModuleMap.get(channel).get(role).contains(command.getModule())) return true;
                else if (this.denyModuleMap.get(null).get(role).contains(command.getModule())) deny = true;
                else if (this.allowModuleMap.get(null).get(role).contains(command.getModule())) return true;
                else if (this.denyModuleMap.get(channel).get(null).contains(command.getModule())) deny = true;
                else if (this.allowModuleMap.get(channel).get(null).contains(command.getModule())) return true;
                else if (this.denyModuleMap.get(null).get(null).contains(command.getModule())) deny = true;
                else if (this.allowModuleMap.get(null).get(null).contains(command.getModule())) return true;
            }
            if (denyCommandMap.get(channel).get(role).contains(command.getName())) deny = true;
            else if (this.allowCommandMap.get(channel).get(role).contains(command.getName())) return true;
            else if (this.denyCommandMap.get(null).get(role).contains(command.getName())) deny = true;
            else if (this.allowCommandMap.get(null).get(role).contains(command.getName())) return true;
            else if (this.denyCommandMap.get(channel).get(null).contains(command.getName())) deny = true;
            else if (this.allowCommandMap.get(channel).get(null).contains(command.getName())) return true;
            else if (this.denyCommandMap.get(null).get(null).contains(command.getName())) deny = true;
            else if (this.allowCommandMap.get(null).get(null).contains(command.getName())) return true;
        }
        return deny ? false : null;
    }
    public void addModuleExcemption(Channel channel, Role role, AbstractCommand command){
        this.exemptCommandMap.computeIfAbsent(channel, chan -> new ConcurrentHashMap<>()).get(role).add(command.getName());
    }
    public void addCommand(boolean allow, Channel channel, Role role, AbstractCommand command){
        if (command.getBotRole().ordinal() >= BotRole.BOT_ADMIN.ordinal() ) throw new ArgumentException("That command is not able to be disabled");
        Map<Channel, Map<Role, Set<String>>> first = allow ? this.allowCommandMap : this.denyCommandMap, second = allow ? this.denyCommandMap : this.allowCommandMap;
        first.computeIfAbsent(channel, chan -> new ConcurrentHashMap<>()).get(role).add(command.getName());
        second.get(channel).get(role).remove(command.getName());
    }
    public void addModule(boolean allow, Channel channel, Role role, ModuleLevel module){
        Map<Channel, Map<Role, Set<ModuleLevel>>> first = allow ? this.allowModuleMap : this.denyModuleMap, second = allow ? this.allowModuleMap: this.denyModuleMap;
        first.computeIfAbsent(channel, chan -> new ConcurrentHashMap<>()).get(role).add(module);
        second.get(channel).get(role).remove(module);
    }
    private void clean(){
        cleanCommands(this.allowCommandMap);
        cleanCommands(this.denyCommandMap);
        cleanCommands(this.exemptCommandMap);
        cleanModules(this.denyModuleMap);
        cleanModules(this.allowModuleMap);
    }
    private void cleanCommands(Map<Channel, Map<Role, Set<String>>> map){
        map.forEach((channel, roleMap) -> {
            if (roleMap.isEmpty()) map.remove(channel);
        });
        Collection<Channel> channels = map.keySet();
        channels.removeAll(this.guild.getChannels());
        channels.forEach(map::remove);
        map.forEach((channel, roleSetMap) -> {
            Collection<Role> roles = roleSetMap.keySet();
            roles.removeAll(this.guild.getRoles());
            roles.forEach(roleSetMap::remove);
        });
    }
    private void cleanModules(Map<Channel, Map<Role, Set<ModuleLevel>>> map){
        map.forEach((channel, roleMap) -> {
            if (roleMap.isEmpty()) map.remove(channel);
        });
        Collection<Channel> channels = map.keySet();
        channels.removeAll(this.guild.getChannels());
        channels.forEach(map::remove);
        map.forEach((channel, roleSetMap) -> {
            Collection<Role> roles = roleSetMap.keySet();
            roles.removeAll(this.guild.getRoles());
            roles.forEach(roleSetMap::remove);
        });
    }
    public SpecialPermsContainer copy(){
        SpecialPermsContainer container = new SpecialPermsContainer(this.guild);
        container.allowCommandMap.putAll(this.allowCommandMap);
        container.denyCommandMap.putAll(this.denyCommandMap);
        container.exemptCommandMap.putAll(this.exemptCommandMap);
        container.denyModuleMap.putAll(this.denyModuleMap);
        container.allowModuleMap.putAll(this.allowModuleMap);
        return this;
    }

}
