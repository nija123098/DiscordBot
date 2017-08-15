package com.github.kaaz.emily.template;

import com.github.kaaz.emily.command.ContextRequirement;
import com.github.kaaz.emily.exeption.ArgumentException;
import com.github.kaaz.emily.exeption.ContextException;
import com.github.kaaz.emily.exeption.DevelopmentException;
import com.github.kaaz.emily.util.EnumHelper;

import java.util.Set;

/**
 * Made by nija123098 on 8/11/2017.
 */
public class CustomCommandDefinition {
    private String name;
    private Class<?>[] argTypes;
    private Set<ContextRequirement> availableContext;
    public CustomCommandDefinition(String name, ContextRequirement[] availableContext, Class<?>... argTypes) {
        this.name = name;
        this.availableContext = EnumHelper.getSet(ContextRequirement.class, availableContext);
        this.argTypes = argTypes;
    }
    public Class<?>[] getArgTypes(){
        return this.argTypes;
    }
    public Set<ContextRequirement> getAvailableContext() {
        return this.availableContext;
    }
    public void checkArgTypes(Object...objects){
        if (this.argTypes.length < objects.length){
            throw new ArgumentException("To many arguments for KeyPhrase: " + this.name);
        }
        for (int i = 0; i < objects.length; i++) {
            if (objects[i] == null || !this.argTypes[i].isAssignableFrom(objects[i].getClass())){
                throw new ArgumentException("Argument type mismatch: index: " + i + " got: " + objects[i].getClass() + " for: " + this.argTypes[i]);
            }
        }
    }
    public void checkContextRequirements(Set<ContextRequirement> requirements){
        if (!this.availableContext.containsAll(requirements)){
            throw new ContextException("KeyPhrase only provides context: " + this.availableContext + " required " + requirements);
        }
    }
    public void checkAvailableContext(Set<ContextRequirement> available){
        if (!available.containsAll(this.availableContext)){
            throw new DevelopmentException("Template system attempted to be used without proper context, required: " + this.availableContext + " provided: " + available);
        }
    }
}
