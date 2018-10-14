package com.github.nija123098.evelyn.template;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.CommandHandler;
import com.github.nija123098.evelyn.command.ContextPack;
import com.github.nija123098.evelyn.command.ContextRequirement;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.config.Configurable;
import com.github.nija123098.evelyn.discordobjects.wrappers.*;
import com.github.nija123098.evelyn.exception.ArgumentException;
import com.github.nija123098.evelyn.util.FormatHelper;
import com.github.nija123098.evelyn.util.StringIterator;

import java.lang.reflect.Parameter;
import java.util.*;

/**
 * A object representation of text that compiles to text and
 * possibly nested function calls, which when interpreted,
 * produces a line of text based on the results of the calls.
 *
 * This can essentially be used as a custom command implementation
 * with guaranteed return as well as a light programing language.
 *
 * @author nija123098
 * @since 1.0.0
 */
public class Template {
    private static final char ARGUMENT_CHARACTER = '%', ARGUMENT_SPLITTER = '|', RIGHT_BRACE = '}', LEFT_BRACE = '{';
    private String text;
    private CustomCommandDefinition definition;
    private transient CombinedArg arg;
    public Template(String template, CustomCommandDefinition definition) {
        this.text = template;
        this.definition = definition;
        this.precompileCheck();
        this.ensureArgsInited();
    }
    public Template() {
        this.ensureArgsInited();//XSTREAM just doesn't like calling this
    }
    public CustomCommandDefinition getDefinition() {
        return this.definition;
    }
    public void precompileCheck() {
        int leftBraceCount = 0, rightBraceCount = 0;
        for (int i = 0; i < this.text.length(); i++) {
            char c = this.text.charAt(i);
            if (c == LEFT_BRACE) ++leftBraceCount;
            else if (c == RIGHT_BRACE) ++rightBraceCount;
        }
        if (leftBraceCount != rightBraceCount) throw new ArgumentException("Brace count miss-match, make sure every function is closed");
    }
    private void ensureArgsInited() {
        if (this.arg != null) return;
        this.arg = new CombinedArg(getCalculatedArgs(TemplatePrepossessing.substitute(this.text), this.definition));
    }
    public String interpret(ContextPack pack, Object...args) {
        return this.interpret(pack.getUser(), pack.getShard(), pack.getChannel(), pack.getGuild(), pack.getMessage(), pack.getReaction(), args);
    }
    public String interpret(User user, Shard shard, Channel channel, Guild guild, Message message, Reaction reaction, Object...objects) {
        this.definition.checkArgTypes(objects);// if is for testing since in testing it will be null
        Object[] contexts = new Object[]{user, shard, channel, guild, message, reaction};
        Set<ContextRequirement> requirements = new HashSet<>(contexts.length + 2, 1);
        requirements.add(ContextRequirement.STRING);// this may cause problems
        for (int i = 0; i < contexts.length; i++) if (contexts[i] != null) requirements.add(ContextRequirement.values()[i]);
        this.definition.checkAvailableContext(requirements);
        this.ensureArgsInited();
        return this.arg.calculate(user, shard, channel, guild, message, reaction, objects);
    }
    public String getText() {
        return this.text;
    }

    /**
     * An abstract utility which is used to calculate
     * the result of a {@link Template} interpretation.
     */
    private abstract static class Arg{
        abstract Object calculate(User user, Shard shard, Channel channel, Guild guild, Message message, Reaction reaction, Object...objects);
        abstract Class<?> getReturnType();
    }

    /**
     * An argument implementation which always calculates to a static {@link String}.
     */
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

    /**
     * An argument implementation which always calculates to a
     * argument based on the interpretation of the entire template.
     */
    private static class GrantedArg extends Arg {
        private int i;
        private CustomCommandDefinition keyPhrase;
        GrantedArg(int i, CustomCommandDefinition definition) {
            this.i = i;
            this.keyPhrase = definition;
        }
        @Override
        Object calculate(User user, Shard shard, Channel channel, Guild guild, Message message, Reaction reaction, Object...objects) {
            return objects[this.i];
        }
        @Override
        Class<?> getReturnType() {
            if (this.i > this.keyPhrase.getArgTypes().length) throw new ArgumentException("Too high of argument number, make sure your arguments are zero indexed");
            return this.keyPhrase.getArgTypes()[this.i];
        }
    }

    /**
     * An argument implementation which contains possibly
     * nested functions and invokes at least one command
     * and can be calculated to an object of any type.
     */
    private static class CalculatedArg extends Arg {
        private AbstractCommand command;
        private Arg[] args;
        CalculatedArg(AbstractCommand command, String s, CustomCommandDefinition keyPhrase) {
            Set<ContextRequirement> requirements = command.getContextRequirements();
            requirements.remove(ContextRequirement.STRING);
            keyPhrase.checkContextRequirements(requirements);
            this.command = command;
            if (!this.command.isTemplateCommand()) {
                throw new ArgumentException("Command is not a valid template command: " + this.command.getName());
            }
            this.args = getCalculatedArgs(s, keyPhrase);
            int argTotal = 0;
            for (Parameter parameter : command.getParameters()) {
                if (parameter.isAnnotationPresent(Argument.class)) {
                    ++argTotal;
                }
            }
            if (this.args.length > argTotal) {
                throw new ArgumentException("Too many arguments for " + command.getName() + " expected arg count: " + argTotal);
            }
            int arg = 0;
            for (int i = 0; i < this.args.length; i++) {
                if (command.getParameters()[i].isAnnotationPresent(Argument.class)) {
                    ++arg;
                }else continue;
                if (!command.getParameters()[i].getType().isAssignableFrom(this.args[i].getReturnType())) {
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

    /**
     * An argument implementation which calculates to a
     * {@link String} and is the result of multiple arguments.
     */
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
                s += Template.toString(arg.calculate(user, shard, channel, guild, message, reaction, objects));
            }
            return s;
        }
        @Override
        Class<String> getReturnType() {
            return String.class;
        }
    }
    private static String toString(Object o) {
        return o instanceof Configurable ? ((Configurable) o).getName() : String.valueOf(o);
    }

    /**
     * A utility class that compiles a {@link Template}.
     */
    private static class ArgBuilder{
        private CustomCommandDefinition commandDefinition;
        private String template, wordBuilder = "", sectionBuilder = "";
        private List<Arg> args = new ArrayList<>();
        ArgBuilder(String template, CustomCommandDefinition commandDefinition) {
            this.template = template;
            this.commandDefinition = commandDefinition;
        }
        void forEach(Iterator<Character> iterator) {
            char c = iterator.next();
            if (ARGUMENT_SPLITTER == c) {
                args.add(null);
            }
            switch (c) {
                case LEFT_BRACE:
                    String command = this.wordBuilder;
                    this.sectionBuilder = this.sectionBuilder.substring(0, this.sectionBuilder.length() - command.length());
                    endSection();
                    int left = 1;
                    String comArgs = "";
                    while (true) {
                        c = iterator.next();
                        if (RIGHT_BRACE == c) {
                            if (--left == 0) {
                                break;
                            }
                        } else if (LEFT_BRACE == c) ++left;
                        comArgs += c;
                    }
                    command = FormatHelper.filtering(command, character -> Character.isLetterOrDigit(character) || character == '_');
                    args.add(new CalculatedArg(CommandHandler.getCommand(command.replace("_", " ")), comArgs, commandDefinition));
                    return;
                case ARGUMENT_CHARACTER:
                    endSection();
                    if (!iterator.hasNext()) throw new ArgumentException("Improperly formed argument, please follow every " + ARGUMENT_CHARACTER + " with a argument number");
                    try{args.add(new GrantedArg(Integer.parseInt(iterator.next() + ""), commandDefinition));
                    } catch (NumberFormatException e) {
                        throw new ArgumentException("Improperly formed argument, please follow every " + ARGUMENT_CHARACTER + " with a argument number", e);
                    }
                    return;
                case ARGUMENT_SPLITTER:
                    endSection();
                    args.add(null);
            }
            if (c != ARGUMENT_SPLITTER) {
                if (c == ' ') wordBuilder = ""; else wordBuilder += c;
                sectionBuilder += c;
            }
        }
        void endSection() {
            if (!sectionBuilder.isEmpty()) args.add(new StaticArg(sectionBuilder));
            sectionBuilder = "";
            wordBuilder = "";
        }
        Arg[] build() {
            StringIterator iterator = new StringIterator(this.template);
            while (iterator.hasNext()) {
                forEach(iterator);
            }
            if (!sectionBuilder.equals("")) {
                args.add(new StaticArg(sectionBuilder));
            }
            final List<Arg> newArgs = new ArrayList<>(args.size()), temp = new ArrayList<>(3);
            args.add(null);
            args.forEach(arg -> {
                if (arg == null) {
                    if (temp.size() == 1) newArgs.add(temp.get(0));
                    else if (!temp.isEmpty()) newArgs.add(new CombinedArg(temp));
                    temp.clear();
                }else temp.add(arg);
            });

            return newArgs.toArray(new Arg[newArgs.size()]);
        }
    }
    private static Arg[] getCalculatedArgs(String s, CustomCommandDefinition definition) {
        return new ArgBuilder(s, definition).build();
    }
}
