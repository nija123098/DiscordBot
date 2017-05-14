package com.github.kaaz.emily.template;

import com.github.kaaz.emily.command.ContextRequirement;
import com.github.kaaz.emily.discordobjects.wrappers.User;
import com.github.kaaz.emily.exeption.ArgumentException;
import com.github.kaaz.emily.exeption.ContextException;
import com.github.kaaz.emily.util.EnumHelper;

import java.util.Set;

/**
 * Made by nija123098 on 4/17/2017.
 */
public enum KeyPhrase {
    TEST// no requirements or arguments for testing purposes
            (new ContextRequirement[0]),
    PLAY_TEXT//                       location
            (new ContextRequirement[]{ContextRequirement.SHARD}),
    USER_JOIN//                       location,                 location,                 joiner,                   first time,    Emily favor
            (new ContextRequirement[]{ContextRequirement.GUILD, ContextRequirement.SHARD, ContextRequirement.USER}, Boolean.class, Float.class),
    USER_LEAVE//              location,                 location,                 kicker,                   target  invoker     banned
            (new ContextRequirement[]{ContextRequirement.GUILD, ContextRequirement.SHARD, ContextRequirement.USER}, User.class, Boolean.class),;
    private Set<ContextRequirement> availableContext;
    private Class<?>[] argTypes;
    KeyPhrase(ContextRequirement[] availableContext, Class<?>...argTypes) {
        this.availableContext = EnumHelper.getSet(ContextRequirement.class, availableContext);
        this.argTypes = argTypes;
    }
    public Class<?>[] getArgTypes(){
        return this.argTypes;
    }
    public void checkArgTypes(Object...objects){
        if (this.argTypes.length < objects.length){
            throw new ArgumentException("To many arguments for KeyPhrase: " + this.name());
        }
        for (int i = 0; i < objects.length; i++) {
            if (objects[i] != null || !this.argTypes[i].isAssignableFrom(objects.getClass())){
                throw new ArgumentException("Argument type mismatch: index: " + i + " got: " + objects[i].getClass() + " for: " + this.argTypes[i]);
            }
        }
    }
    public void checkAvailableContext(Set<ContextRequirement> requirements){
        if (!this.availableContext.containsAll(requirements)){// todo clarify
            throw new ContextException("KeyPhrase only provides context: " + availableContext + " required " + requirements);
        }
    }
}
