package com.github.kaaz.emily.template;

import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.CommandHandler;
import com.github.kaaz.emily.command.ContextPack;
import com.github.kaaz.emily.command.ContextRequirement;
import com.github.kaaz.emily.command.anotations.Convert;
import com.github.kaaz.emily.discordobjects.wrappers.*;
import com.github.kaaz.emily.exeption.ArgumentException;
import com.github.kaaz.emily.exeption.ContextException;

import java.lang.reflect.Parameter;
import java.util.*;

/**
 * Made by nija123098 on 4/17/2017.
 */
public class Template {
    private Set<ContextRequirement> contextRequirements;
    private CombinedArg arg;
    private KeyPhrase keyPhrase;
    public Template(String template, KeyPhrase keyPhrase){
        this.keyPhrase = keyPhrase;
        this.arg = new CombinedArg(getCalculatedArgs(template, keyPhrase));
        this.contextRequirements = new HashSet<>();
    }
    public String interpret(ContextPack pack, Object...args){
        return this.interpret(pack.getUser(), pack.getShard(), pack.getChannel(), pack.getGuild(), pack.getMessage(), pack.getReaction(), args);
    }
    public String interpret(User user, Shard shard, Channel channel, Guild guild, Message message, Reaction reaction, Object...objects){
        this.keyPhrase.checkArgTypes(objects);// if is for testing since in testing it will be null
        Object[] contexts = new Object[]{user, shard, channel, guild, message, reaction};
        for (int i = 0; i < contexts.length; i++) {
            if (this.contextRequirements.contains(ContextRequirement.values()[i])) ContextException.checkRequirement(contexts[i], ContextRequirement.values()[i]);
        }
        return arg.calculate(user, shard, channel, guild, message, reaction, objects);
    }
    private abstract static class Arg{
        abstract Object calculate(User user, Shard shard, Channel channel, Guild guild, Message message, Reaction reaction, Object...objects);
        abstract Class<?> getReturnType();
    }
    private static class StaticArg extends Arg {
        private Object arg;
        StaticArg(Object arg) {
            this.arg = arg;
        }
        @Override
        Object calculate(User user, Shard shard, Channel channel, Guild guild, Message message, Reaction reaction, Object... objects) {
            return this.arg;
        }
        @Override
        Class getReturnType() {
            return arg.getClass();
        }
    }
    private static class GrantedArg extends Arg {
        private int i;
        private KeyPhrase keyPhrase;
        GrantedArg(int i, KeyPhrase keyPhrase){
            this.i = i;
            this.keyPhrase = keyPhrase;
        }
        @Override
        Object calculate(User user, Shard shard, Channel channel, Guild guild, Message message, Reaction reaction, Object...objects) {
            return objects[this.i];
        }
        @Override
        Class<?> getReturnType() {
            return this.keyPhrase.getArgTypes()[this.i];
        }
    }
    private static class CalculatedArg extends Arg {
        private AbstractCommand command;
        private Arg[] args;
        CalculatedArg(AbstractCommand command, String s, KeyPhrase keyPhrase){
            keyPhrase.checkAvalibleContext(command.getContextRequirements());
            this.command = command;
            if (!this.command.isTemplateCommand()){
                throw new ArgumentException("Command is not a valid template command: " + this.command.getName());
            }
            this.args = getCalculatedArgs(s, keyPhrase);
            int argTotal = 0;
            for (Parameter parameter : command.getParameters()){
                if (parameter.isAnnotationPresent(Convert.class)){
                    ++argTotal;
                }
            }
            if (this.args.length > argTotal){
                throw new ArgumentException("Too many arguments.  Expected arg count: " + argTotal);
            }
            int arg = 0;
            for (int i = 0; i < this.args.length; i++) {
                if (command.getParameters()[i].isAnnotationPresent(Convert.class)){
                    ++arg;
                }else continue;
                if (!command.getParameters()[i].getType().isAssignableFrom(this.args[i].getReturnType()) || command.getParameters()[i].getAnnotation(Convert.class).optional()){
                    throw new ArgumentException("Incorrect argument type for arg: " + arg + (this.args[i] instanceof CombinedArg ? "  Make sure you have argument splitters" : ""));
                }
            }
        }
        @Override
        Object calculate(User user, Shard shard, Channel channel, Guild guild, Message message, Reaction reaction, Object...objects) {
            Object[] obs = new Object[this.args.length];
            for (int i = 0; i < obs.length; i++) {
                obs[i] = args[i].calculate(user, shard, channel, guild, message, reaction, objects);
            }
            return this.command.invoke(user, shard, channel, guild, message, reaction, "", obs);
        }
        @Override
        Class<?> getReturnType() {
            return this.command.getReturnType();
        }
    }
    private static class CombinedArg extends Arg {
        private Arg[] args;
        CombinedArg(List<Arg> args) {
            this.args = args.toArray(new Arg[args.size()]);
        }
        CombinedArg(Arg[] args) {
            this.args = args;
        }
        @Override
        String calculate(User user, Shard shard, Channel channel, Guild guild, Message message, Reaction reaction, Object... objects) {
            String s = "";
            for (Arg arg : args) {
                s += arg.calculate(user, shard, channel, guild, message, reaction, objects);
            }
            return s;
        }
        @Override
        Class<String> getReturnType() {
            return String.class;
        }
    }
    private static class ArgBuilder{// might want to make a builder for this
        private KeyPhrase keyPhrase;
        private String template, wordBuilder = "", sectionBuilder = "";
        private List<Arg> args = new ArrayList<>();
        ArgBuilder(String template, KeyPhrase keyPhrase) {
            this.template = template;
            this.keyPhrase = keyPhrase;
        }
        void forEach(Iterator<Character> iterator){
            char c = iterator.next();
            if (TemplateHandler.ARGUMENT_SPLITTER == c){
                args.add(null);
            }
            switch (c){
                case TemplateHandler.LEFT_BRACE:
                    String command = this.wordBuilder;
                    this.sectionBuilder = this.sectionBuilder.substring(0, this.sectionBuilder.length() - command.length());
                    endSection();
                    int left = 1;
                    String comArgs = "";
                    while (true){
                        c = iterator.next();
                        if (TemplateHandler.RIGHT_BRACE == c){
                            if (--left == 0){
                                break;
                            }
                        }
                        comArgs += c;
                    }
                    args.add(new CalculatedArg(CommandHandler.getCommand(command.replace("_", " ")), comArgs, keyPhrase));
                    return;
                case TemplateHandler.ARGUMENT_CHARACTER:
                    endSection();
                    args.add(new GrantedArg(Integer.parseInt(iterator.next() + ""), keyPhrase));
                    break;
                case TemplateHandler.ARGUMENT_SPLITTER:
                    endSection();
                    args.add(null);
            }
            if (c == ' ') wordBuilder = ""; else wordBuilder += c;
            sectionBuilder += c;
        }
        void endSection(){
            if (!sectionBuilder.equals("")) args.add(new StaticArg(sectionBuilder));
            sectionBuilder = "";
            wordBuilder = "";
        }
        Arg[] build(){
            Iterator<Character> characterIterator = new Iterator<Character>() {
                int i;
                @Override
                public boolean hasNext() {
                    return i != template.length();
                }
                @Override
                public Character next() {
                    if (!hasNext()) throw new RuntimeException("No next");
                    return template.charAt(i++);
                }
            };
            while (characterIterator.hasNext()){
                forEach(characterIterator);
            }
            if (!sectionBuilder.equals("")){
                args.add(new StaticArg(sectionBuilder));
            }
            final List<Arg> newArgs = new ArrayList<>(args.size()), temp = new ArrayList<>(3);
            args.add(null);
            args.forEach(arg -> {
                if (arg == null){
                    if (temp.size() == 1){
                        newArgs.add(temp.get(0));
                    }else{
                        newArgs.add(new CombinedArg(temp));
                    }
                    temp.clear();
                }else{
                    temp.add(arg);
                }
            });

            return newArgs.toArray(new Arg[newArgs.size()]);
        }
    }
    private static Arg[] getCalculatedArgs(String s, KeyPhrase keyPhrase){
        return new ArgBuilder(s, keyPhrase).build();
    }
}
