package com.github.nija123098.evelyn.template;

import com.github.nija123098.evelyn.command.ContextRequirement;
import com.github.nija123098.evelyn.exception.ArgumentException;
import com.github.nija123098.evelyn.exception.ContextException;
import com.github.nija123098.evelyn.exception.DevelopmentException;
import com.github.nija123098.evelyn.util.EnumHelper;

import java.util.Set;

/**
 * Used as an object representation of arguments for a
 * custom command and context requirements for postprocessing.
 *
 * This is essentially the argument part
 * of a method contract for custom commands.
 *
 * @author nija123098
 * @since 1.0.0
 */
public class CustomCommandDefinition {
    private String name;
    private Class<?>[] argTypes;
    private Set<ContextRequirement> availableContext;
    public CustomCommandDefinition(String name, ContextRequirement[] required, Class<?>... argTypes) {
        this.name = name;
        this.availableContext = EnumHelper.getSet(ContextRequirement.class, required);
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
