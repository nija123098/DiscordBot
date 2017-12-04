package com.github.nija123098.evelyn.perms.configs.specialperms;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.discordobjects.wrappers.Channel;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.Role;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.exception.ArgumentException;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static com.github.nija123098.evelyn.perms.BotRole.BOT_ADMIN;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyMap;
import static java.util.Collections.emptySet;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class SpecialPermsContainer {
    private Guild guild;
    private Channel channel;
    private final Map<Channel, Map<Role, Set<String>>> allowCommandMap = new HashMap<>();
    private final Map<Channel, Map<Role, Set<String>>> denyCommandMap = new HashMap<>();
    private final Map<Channel, Map<Role, Set<String>>> exemptCommandMap = new HashMap<>();
    private final Map<Channel, Map<Role, Set<ModuleLevel>>> denyModuleMap = new HashMap<>();
    private final Map<Channel, Map<Role, Set<ModuleLevel>>> allowModuleMap = new HashMap<>();

    public SpecialPermsContainer(Guild guild) {
        this.guild = guild;
    }

    protected SpecialPermsContainer() {
    }

    public Boolean getSpecialPermission(AbstractCommand command, Channel channel, User user) {
        if (this.channel != null && !this.channel.equals(channel)) return false;
        boolean deny = false;
        for (Role role : user.getRolesForGuild(this.guild)) {
            if (!this.exemptCommandMap.getOrDefault(channel, emptyMap()).getOrDefault(role, emptySet()).contains(command.getName())) {
                if (this.denyModuleMap.getOrDefault(channel, emptyMap()).getOrDefault(role, emptySet()).contains(command.getModule()))
                    deny = true;
                else if (this.allowModuleMap.getOrDefault(channel, emptyMap()).getOrDefault(role, emptySet()).contains(command.getModule()))
                    return true;
                else if (this.denyModuleMap.getOrDefault(channel, emptyMap()).getOrDefault(role, emptySet()).contains(command.getModule()))
                    deny = true;
                else if (this.allowModuleMap.getOrDefault(channel, emptyMap()).getOrDefault(role, emptySet()).contains(command.getModule()))
                    return true;
                else if (this.denyModuleMap.getOrDefault(channel, emptyMap()).getOrDefault(role, emptySet()).contains(command.getModule()))
                    deny = true;
                else if (this.allowModuleMap.getOrDefault(channel, emptyMap()).getOrDefault(role, emptySet()).contains(command.getModule()))
                    return true;
                else if (this.denyModuleMap.getOrDefault(channel, emptyMap()).getOrDefault(role, emptySet()).contains(command.getModule()))
                    deny = true;
                else if (this.allowModuleMap.getOrDefault(channel, emptyMap()).getOrDefault(role, emptySet()).contains(command.getModule()))
                    return true;
            }
            if (denyCommandMap.getOrDefault(channel, emptyMap()).getOrDefault(role, emptySet()).contains(command.getName()))
                deny = true;
            else if (this.allowCommandMap.getOrDefault(channel, emptyMap()).getOrDefault(role, emptySet()).contains(command.getName()))
                return true;
            else if (this.denyCommandMap.getOrDefault(channel, emptyMap()).getOrDefault(role, emptySet()).contains(command.getName()))
                deny = true;
            else if (this.allowCommandMap.getOrDefault(channel, emptyMap()).getOrDefault(role, emptySet()).contains(command.getName()))
                return true;
            else if (this.denyCommandMap.getOrDefault(channel, emptyMap()).getOrDefault(role, emptySet()).contains(command.getName()))
                deny = true;
            else if (this.allowCommandMap.getOrDefault(channel, emptyMap()).getOrDefault(role, emptySet()).contains(command.getName()))
                return true;
            else if (this.denyCommandMap.getOrDefault(channel, emptyMap()).getOrDefault(role, emptySet()).contains(command.getName()))
                deny = true;
            else if (this.allowCommandMap.getOrDefault(channel, emptyMap()).getOrDefault(role, emptySet()).contains(command.getName()))
                return true;
        }
        return deny ? false : null;
    }

    public void addModuleExcemption(Channel channel, Role role, AbstractCommand command) {
        this.exemptCommandMap.computeIfAbsent(channel, chan -> new ConcurrentHashMap<>()).get(role).add(command.getName());
        clean();
    }

    public void addCommand(boolean allow, Channel channel, Role role, AbstractCommand command) {
        if (command.getBotRole().ordinal() >= BOT_ADMIN.ordinal())
            throw new ArgumentException("That command is not able to be disabled");
        Map<Channel, Map<Role, Set<String>>> first = allow ? this.allowCommandMap : this.denyCommandMap, second = allow ? this.denyCommandMap : this.allowCommandMap;
        first.computeIfAbsent(channel, chan -> new ConcurrentHashMap<>()).get(role).add(command.getName());
        second.get(channel).get(role).remove(command.getName());
        clean();
    }

    public void addModule(boolean allow, Channel channel, Role role, ModuleLevel module) {
        Map<Channel, Map<Role, Set<ModuleLevel>>> first = allow ? this.allowModuleMap : this.denyModuleMap, second = allow ? this.allowModuleMap : this.denyModuleMap;
        first.computeIfAbsent(channel, chan -> new ConcurrentHashMap<>()).get(role).add(module);
        second.get(channel).get(role).remove(module);
        clean();
    }

    public void restrict(Channel channel) {
        if (channel != null) {
            asList(this.allowCommandMap, this.denyCommandMap, this.exemptCommandMap, this.denyModuleMap, this.allowModuleMap).forEach(channelMap -> {
                channelMap.forEach((chan, o) -> {
                    if (!channel.equals(chan)) channelMap.remove(chan);
                });
            });
        }
        this.channel = channel;
        clean();
    }

    private void clean() {
        clean(this.allowCommandMap);
        clean(this.denyCommandMap);
        clean(this.exemptCommandMap);
        clean(this.denyModuleMap);
        clean(this.allowModuleMap);
    }

    private <E> void clean(Map<Channel, Map<Role, Set<E>>> map) {
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

    public SpecialPermsContainer copy() {
        SpecialPermsContainer container = new SpecialPermsContainer(this.guild);
        container.allowCommandMap.putAll(this.allowCommandMap);
        container.denyCommandMap.putAll(this.denyCommandMap);
        container.exemptCommandMap.putAll(this.exemptCommandMap);
        container.denyModuleMap.putAll(this.denyModuleMap);
        container.allowModuleMap.putAll(this.allowModuleMap);
        return this;
    }

}
