package com.github.nija123098.evelyn.command;

import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.command.annotations.Context;
import com.github.nija123098.evelyn.command.annotations.LaymanName;
import com.github.nija123098.evelyn.command.configs.CommandsUsedCountConfig;
import com.github.nija123098.evelyn.command.configs.DisabledCommandsConfig;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.config.Configurable;
import com.github.nija123098.evelyn.config.GlobalConfigurable;
import com.github.nija123098.evelyn.config.GuildUser;
import com.github.nija123098.evelyn.config.configs.guild.GuildLastCommandTimeConfig;
import com.github.nija123098.evelyn.config.configs.user.LastCommandTimeConfig;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.helpers.guildaudiomanager.GuildAudioManager;
import com.github.nija123098.evelyn.discordobjects.wrappers.*;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.EventDistributor;
import com.github.nija123098.evelyn.exception.BotException;
import com.github.nija123098.evelyn.exception.ContextException;
import com.github.nija123098.evelyn.exception.DevelopmentException;
import com.github.nija123098.evelyn.moderation.logging.BotLogConfig;
import com.github.nija123098.evelyn.perms.BotRole;
import com.github.nija123098.evelyn.perms.configs.specialperms.GuildSpecialPermsConfig;
import com.github.nija123098.evelyn.perms.configs.specialperms.SpecialPermsContainer;
import com.github.nija123098.evelyn.tag.Tag;
import com.github.nija123098.evelyn.tag.Tagable;
import com.github.nija123098.evelyn.tag.Tags;
import com.github.nija123098.evelyn.util.CacheHelper;
import com.github.nija123098.evelyn.util.EmoticonHelper;
import com.github.nija123098.evelyn.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.stream.Stream;

/**
 * The super class type for all class commands, even sub-commands.
 *
 * @author nija123098
 * @since 1.0.0
 */
@LaymanName(value = "Command", help = "The command represented by a string or some alias")
public class AbstractCommand implements Tagable {
    private final Class<? extends AbstractCommand> superCommand;
    private AbstractCommand highCommand;
    private String name, aAliases, eAliases, rAliases, help;
    private BotRole botRole;
    private ModuleLevel module;
    private Method method;
    private Parameter[] parameters;
    private Set<String> emoticonAliases, allNames;
    private long globalUseTime, globalCoolDownTime;
    private CacheHelper.ContainmentCache<Guild> guildCoolDowns;
    private CacheHelper.ContainmentCache<Channel> channelCoolDowns;
    private CacheHelper.ContainmentCache<User> userCoolDowns;
    private CacheHelper.ContainmentCache<GuildUser> guildUserCoolDowns;
    private Set<ContextRequirement> contextRequirements;
    private Set<AbstractCommand> subCommands;
    private final List<Tag> tags;
    private boolean prefixRequired = true;
    private boolean okOnSuccess = false;
    private boolean hasInLineTool = false;

    /**
     * The high constructor for a command which all other constructors call.
     *
     * This is most commonly used as a constructor for sub-commands.
     * All values not set here will default to the super-command's values.
     *
     * @param superCommand the type of the super command, or null.
     * @param name the name of the command, not including the super command name.
     * @param absoluteAliases the list of names which are separated by a comma and space.
     * @param emoticonAliases the list of emoticon names separated by a comma and space.
     * @param relativeAliases the list of names which the super command's names are prefixed before separated by a comma and space.
     * @param help the help text for this command.
     */
    public AbstractCommand(Class<? extends AbstractCommand> superCommand, String name, String absoluteAliases, String emoticonAliases, String relativeAliases, String help) {
        this.superCommand = superCommand;
        this.name = name;
        this.aAliases = absoluteAliases;
        this.eAliases = emoticonAliases;
        this.rAliases = relativeAliases;
        this.name = name;
        this.help = help != null ? help : "No provided help for this command";
        Tags tags = this.getClass().getAnnotation(Tags.class);
        this.tags = tags == null ? Collections.emptyList() : Arrays.asList(tags.value());
    }

    /**
     * The constructor for a super command.
     *
     * @param name the full name of a command.
     * @param module the module for the command.
     * @param absoluteAliases the names for which the command can also be called separated by a comma and space.
     * @param emoticonAliases the list of emoticon names separated by a comma and space.
     * @param help the help text for this command.
     */
    public AbstractCommand(String name, ModuleLevel module, String absoluteAliases, String emoticonAliases, String help) {
        this(null, name, absoluteAliases, emoticonAliases, null, help);
        this.module = module;
        this.name = name;
    }

    /**
     * The constructor for a super command.
     *
     * @param name the full name of a command.
     * @param botRole the override for the botrole of the command.
     * @param module the module for the command.
     * @param absoluteAliases the names for which the command can also be called separated by a comma and space.
     * @param emoticonAliases the list of emoticon names separated by a comma and space.
     * @param help the help text for this command.
     */
    public AbstractCommand(String name, BotRole botRole, ModuleLevel module, String absoluteAliases, String emoticonAliases, String help) {
        this(name, module, absoluteAliases, emoticonAliases, help);
        this.botRole = botRole;
    }

    /**
     * Called once after all configs are initialized to link
     * super-commands and sub-commands called hierarchically.
     */
    void load() {
        AbstractCommand superCommand = getSuperCommand();
        this.allNames = new HashSet<>();
        if (superCommand != null) this.getSuperCommand().allNames.forEach(s -> this.allNames.add(s + " " + this.name));
        this.name = superCommand == null ? this.name : superCommand.name + " " + this.name;
        this.module = this.getModule() == null ? superCommand == null ? ModuleLevel.NONE : superCommand.getModule() : this.module;
        this.module.addCommand(this);
        this.botRole = getBotRole() == null ? (superCommand == null ? this.getModule().getDefaultRole() : superCommand.getBotRole()) : this.getBotRole();
        this.prefixRequired = superCommand == null ? this.prefixRequired : superCommand.prefixRequired();
        this.allNames.add(this.name);
        if (aAliases != null) {
            Collections.addAll(this.allNames, aAliases.split(", "));
            if (this.allNames.remove("")) Log.log("Extra coma in " + this.getClass().getName());
        }
        if (eAliases != null) {
            String[] eAliases = this.eAliases.split(", ");
            this.emoticonAliases = new HashSet<>(eAliases.length);
            Collections.addAll(this.emoticonAliases, eAliases);
            if (this.emoticonAliases.remove("")) Log.log("Extra coma in " + this.getClass().getName());
        }else{
            this.emoticonAliases = new HashSet<>(0);
        }
        this.allNames.addAll(this.emoticonAliases);
        this.emoticonAliases.stream().map(s -> EmoticonHelper.getChars(s, false)).forEach(this.allNames::add);
        if (rAliases != null && superCommand != null) {
            for (String rel : rAliases.split(", ")) {
                if (rel.isEmpty()) Log.log("Extra coma in " + this.getClass().getName());
                else superCommand.getNames().forEach(s -> this.allNames.add(s + " " + rel));
            }
        }
        for (Method m : this.getClass().getMethods()) {
            if (m.isAnnotationPresent(Command.class)) {
                this.method = m;
                break;
            }
        }
        if (this.method == null) {
            throw new DevelopmentException("No method annotated " + Command.class.getSimpleName() + " in command: " + this.getClass().getName());
        }
        this.parameters = this.method.getParameters();
        if (!this.isTemplateCommand()) for (Parameter parameter : this.parameters) {
            if (parameter.isAnnotationPresent(Argument.class)) InvocationObjectGetter.checkConvertType(parameter.getType());
            else if (parameter.isAnnotationPresent(Argument.class) || parameter.getAnnotations().length == 0) InvocationObjectGetter.checkContextType(parameter.getType());
        }
        this.contextRequirements = new HashSet<>();
        for (Parameter parameter : this.parameters) {
            if (parameter.getAnnotations().length == 0 || (parameter.isAnnotationPresent(Context.class) && !parameter.getAnnotation(Context.class).softFail())) {
                this.contextRequirements.addAll(InvocationObjectGetter.getContextRequirements(parameter.getType(), parameter.isAnnotationPresent(Context.class) ? parameter.getAnnotation(Context.class).value() : ContextType.DEFAULT));
            }else if (parameter.isAnnotationPresent(Argument.class) && !parameter.getAnnotation(Argument.class).optional() && !this.isTemplateCommand()) {
                this.contextRequirements.addAll(InvocationObjectGetter.getConvertRequirements(parameter.getType(), parameter.getAnnotation(Argument.class).replacement()));
            }
            if (parameter.getType().equals(MessageMaker.class) || parameter.getType().equals(GuildAudioManager.class)) this.okOnSuccess = false;
            else if (parameter.getType().equals(InLineCommandTool.class)) this.hasInLineTool = true;
        }// makes it into a more efficient set
        this.contextRequirements = this.contextRequirements.isEmpty() ? Collections.emptySet() : EnumSet.copyOf(this.contextRequirements);
        this.subCommands = new HashSet<>();
        if (this.superCommand != null) this.getSuperCommand().subCommands.add(this);
        this.highCommand = this.getSuperCommand();
        if (this.highCommand == null) this.highCommand = this;
        else while (true) {
            AbstractCommand command = this.highCommand.getSuperCommand();
            if (command == null) break;
            this.highCommand = command;
        }
        this.globalCoolDownTime = this.getCoolDown(GlobalConfigurable.class);
        long persistence = this.getCoolDown(Guild.class);
        if (persistence != -1) {
            this.guildCoolDowns = new CacheHelper.ContainmentCache<>(persistence);
        }
        persistence = this.getCoolDown(Channel.class);
        if (persistence != -1) {
            this.channelCoolDowns = new CacheHelper.ContainmentCache<>(persistence);
        }
        persistence = this.getCoolDown(User.class);
        if (persistence != -1) {
            this.userCoolDowns = new CacheHelper.ContainmentCache<>(persistence);
        }
        persistence = this.getCoolDown(GuildUser.class);
        if (persistence != -1) {
            this.guildUserCoolDowns = new CacheHelper.ContainmentCache<>(persistence);
        }
        EventDistributor.register(this);
        this.aAliases = null;
        this.eAliases = null;
        this.rAliases = null;
    }

    /**
     * Return if the command has been loaded.
     *
     * @return if the command has been loaded.
     */
    public boolean isLoaded() {
        return this.allNames != null;
    }

    /**
     * A getter for the object representing the super command.
     * This gives the command directly above this command.
     *
     * @return this command's super command.
     */
    public AbstractCommand getSuperCommand() {
        return CommandHandler.getCommand(this.superCommand);
    }

    /**
     * Gets the highest command in the command's hierarchy.
     *
     * @return the command's high command.
     */
    public AbstractCommand getHighCommand() {
        return this.highCommand;
    }

    /**
     * Returns if this command is the highest
     * command in this command's hierarchy.
     *
     * @return if this has no higher command.
     */
    public boolean isHighCommand() {
        return this.highCommand == this;
    }

    /**
     * A standard getter.
     *
     * @return the type of the super command.
     */
    public Class<? extends AbstractCommand> getSuperCommandType() {
        return this.superCommand;
    }

    /**
     * The method to get a set of all emoticon chars
     * by which the command can be called
     *
     * @return the HashSet of the emoticon's chars
     * that can represent this command.
     */
    public Set<String> getEmoticonAliases() {
        return this.emoticonAliases;
    }

    /**
     * A standard getter.
     *
     * @return the name of the command.
     */
    public String getName() {
        return this.name;
    }

    @Override
    public String typeName() {
        return "command";
    }

    @Override
    public List<Tag> getTags() {
        return this.tags;
    }

    /**
     * The method to get the set off all names
     * by which the command goes by.
     *
     * @return the HashSet of all names
     * by which the command goes by.
     */
    public Set<String> getNames() {
        return this.allNames;
    }

    /**
     * Returns the command's module.
     *
     * @return the module of which the command is part of.
     */
    public ModuleLevel getModule() {
        return this.module;
    }

    /**
     * Gets the bot role required to run this command and defaults to USER.
     *
     * @return the required bot role to run this command.
     */
    public BotRole getBotRole() {
        return this.botRole;
    }

    /**
     * Gets if the current command is a template command.
     *
     * @return if the command is a template command.
     */
    public boolean isTemplateCommand() {
        return this.getClass().getPackage().getName().contains(".template.");
    }

    /**
     * Gets the return type of the command.
     *
     * @return the return type of the command.
     */
    public Class<?> getReturnType() {
        return this.method.getReturnType();
    }

    /**
     * A standard getter.
     *
     * @return the Java reflection results for getting the parameters.
     */
    public Parameter[] getParameters() {
        return this.parameters;
    }

    /**
     * A standard getter.
     *
     * @return gets the help text.
     */
    public String getHelp() {
        return this.help;
    }

    /**
     * Gets a string showing an example of the command's usage.
     *
     * @return a command's example.
     */
    public String getExample() {
        return null;
    }

    /**
     * This command and the sub-command's help text and usage.
     *
     * @return a string representing the sub-command's and this command's help text and usage.
     */
    protected String getLocalUsages() {
        StringBuilder builder = new StringBuilder("#  ").append(this.name).append(" ");
        Stream.of(this.parameters).filter(parameter -> parameter.isAnnotationPresent(Argument.class)).forEach(parameter -> {
            boolean optional = parameter.getAnnotation(Argument.class).optional();
            String info = parameter.getAnnotation(Argument.class).info();
            if (info.isEmpty()) info = parameter.getType().isAnnotationPresent(LaymanName.class) ? parameter.getType().getAnnotation(LaymanName.class).value() : parameter.getType().getSimpleName();
            info = info.toLowerCase();
            builder.append(optional ? "[" : "<").append(info).append(optional ? "] " : "> ");
        });
        builder.append("// ").append(this.help);
        return builder.toString();
    }

    /**
     * Gets the string representation of usages.
     * This is only called in instances of high commands.
     *
     * @return gets the string usages of a command.
     */
    public String getUsages() {
        StringBuilder builder = new StringBuilder();
        builder.append(getLocalUsages()).append("\n");
        this.subCommands.forEach(command -> builder.append(command.getUsages()).append("\n"));
        return builder.substring(0, builder.length() - 1);
    }

    /**
     * If a prefix is required for using this command.
     *
     * @return if a prefix is required for using this command.
     */
    public boolean prefixRequired() {
        return this.prefixRequired;
    }

    /**
     * Returns the set of all sub-commands.
     *
     * @return a set of all sub-commands.
     */
    public Set<AbstractCommand> getSubCommands() {
        return this.subCommands;
    }

    /**
     * The set of words to naturally trigger the command.
     *
     * @return the set of words to naturally trigger the command.
     */
    public Set<String> getNaturalTriggers() {
        return Collections.emptySet();
    }

    /**
     * Returns if the bot responds to reactions for this command.
     *
     * @return if the bot responds to reactions for this command.
     */
    public boolean useReactions() {
        return false;
    }

    /**
     * Returns if the bot should log the use of this command.
     *
     * @return if the bot should log the use of this command.
     */
    public boolean shouldLog() {
        return true;
    }

    /**
     * A check if the user can use a command in the context.
     *
     * @param user the user that is being checked for permission.
     * @param channel the channel in which permissions are being checked.
     * @return if the user can use this command in the guild, if one exists.
     */
    public boolean hasPermission(User user, Channel channel) {
        Set<String> blocked = ConfigHandler.getSetting(DisabledCommandsConfig.class, GlobalConfigurable.GLOBAL);
        AbstractCommand command = this;
        while (command != null) {
            if (blocked.contains(command.getName())) return false;
            command = command.getSuperCommand();
        }
        boolean hasNormalPerm = this.botRole.hasRequiredRole(user, channel.getGuild());
        if (!channel.isPrivate()) {
            SpecialPermsContainer container = ConfigHandler.getSetting(GuildSpecialPermsConfig.class, channel.getGuild());
            if (container != null && BotRole.GUILD_TRUSTEE.hasRequiredRole(user, channel.getGuild())) {
                Boolean allow = container.getSpecialPermission(this, channel, user);
                return allow == null ? hasNormalPerm : allow;
            }
        }
        return hasNormalPerm;
    }

    /**
     * A method to check the cool down on a command.
     *
     * @param channel the channel checked for rate limiting.
     * @param user the user checked for rate limiting.
     * @return if the command is not being rate limited.
     */
    boolean checkCoolDown(Channel channel, User user) {
        if (this.globalUseTime != -1 && this.globalUseTime > System.currentTimeMillis()) {
            return false;
        }
        if (this.guildCoolDowns != null && !channel.isPrivate() && this.guildCoolDowns.contains(channel.getGuild())) {
            return false;
        }
        if (this.channelCoolDowns != null && this.channelCoolDowns.contains(channel)) {
            return false;
        }
        if (this.userCoolDowns != null && this.userCoolDowns.contains(user)) {
            return false;
        }
        if (this.guildUserCoolDowns != null && !channel.isPrivate() && this.guildUserCoolDowns.contains(GuildUser.getGuildUser(channel.getGuild(), user))) {
            return false;
        }
        return true;
    }

    /**
     * Method to be called when the command is invoked.
     *
     * @param channel the channel checked for rate limiting.
     * @param user the user checked for rate limiting.
     */
    public void invoked(Channel channel, User user, Message message) {
        if (this.globalUseTime != -1) {
            this.globalUseTime = System.currentTimeMillis() + this.globalCoolDownTime;
        }
        if (this.guildCoolDowns != null && !channel.isPrivate()) {
            this.guildCoolDowns.add(channel.getGuild());
        }
        if (this.channelCoolDowns != null) {
            this.channelCoolDowns.add(channel);
        }
        if (this.userCoolDowns != null) {
            this.userCoolDowns.add(user);
        }
        if (this.guildUserCoolDowns != null && !channel.isPrivate()) {
            this.guildUserCoolDowns.add(GuildUser.getGuildUser(channel.getGuild(), user));
        }
        CommandsUsedCountConfig.increment(user);
        LastCommandTimeConfig.update(user);
        if (!channel.isPrivate()) GuildLastCommandTimeConfig.update(channel.getGuild());
        if (message != null) {
            if (this.okOnSuccess) message.addReactionByName("ok_hand");
            if (this.shouldLog() && !message.getChannel().isPrivate()) {
                Channel chan = ConfigHandler.getSetting(BotLogConfig.class, message.getGuild());
                if (chan == null || !chan.canPost()) return;
                new MessageMaker(chan).withAuthorIcon(user.getAvatarURL()).getAuthorName().appendRaw(message.getAuthor().getDisplayName(message.getGuild()) + (message.getAuthor().getNickname(message.getGuild()) == null ? "" : " AKA " + message.getAuthor().getNameAndDiscrim())).getMaker().append(message.getChannel().mention() + " - used command ***" + this.name + "***").appendRaw("\n" + message.getMentionCleanedContent()).getNote().appendRaw("ID: " + message.getID()).getMaker().withTimestamp(System.currentTimeMillis()).send();
            }
        }
    }

    /**
     * Returns the cool down in millis dependent on the
     * type of configurable which is being rate limited.
     *
     * @param clazz the configurable type.
     * @return the cool down in millis dependent on the type.
     */
    public long getCoolDown(Class<? extends Configurable> clazz) {
        if (clazz.equals(User.class)) return 200;
        return -1;
    }

    /**
     * A standard getter to get the set of context requirements for the command.
     *
     * @return The context requirements for the command.
     */
    public Set<ContextRequirement> getContextRequirements() {
        return this.contextRequirements;
    }

    /**
     * Differs to {@link InvocationObjectGetter} to get
     * objects for the command's arguments and invokes
     * the command and returns the object the command
     * returns or false if the command fails.
     *
     * @param user the user that invokes the command.
     * @param message the message sent or reacted to.
     * @param reaction the reaction if the invocation
     *                 was caused by reacting to a message.
     * @param args the user args for invocation.
     * @return if the command was successful.
     */
    public Object invoke(User user, Shard shard, Channel channel, Guild guild, Message message, Reaction reaction, String args, Object...argOverrides) {
        ProcessingHandler.startProcess(channel);
        Object[] contexts = new Object[]{user, shard, channel, guild, message, reaction, args};
        boolean[] overridden = new boolean[argOverrides.length];
        for (int i = 0; i < overridden.length; i++) {
            overridden[i] = argOverrides[i] != null;
        }
        for (int i = 0; i < contexts.length; i++) {
            if (this.contextRequirements.contains(ContextRequirement.values()[i])) ContextException.checkRequirement(contexts[i], ContextRequirement.values()[i]);
        }
        Object[] objects = InvocationObjectGetter.replace(this, this.parameters, new Object[this.parameters.length], user, shard, channel, guild, message, reaction, args, overridden);
        for (int i = 0; i < overridden.length; i++) {
            if (argOverrides[i] != null) objects[i] = argOverrides[i];
        }
        try {
            Object object = this.method.invoke(this, objects);
            if (!this.okOnSuccess) Stream.of(objects).filter(MessageMaker.class::isInstance).map(o -> ((MessageMaker) o)).forEach(o -> (this.getTags().isEmpty() ? o : o.withColor(this.getTags().get(0).getColor())).send(true));
            if (this.hasInLineTool) Stream.of(objects).filter(InLineCommandTool.class::isInstance).map(o -> ((InLineCommandTool) o)).forEach(InLineCommandTool::release);
            return object;
        } catch (IllegalAccessException e) {
            Log.log("Malformed command: " + getName(), e);
        } catch (InvocationTargetException e) {
            if (e.getCause() instanceof BotException) {
                ((BotException) e.getCause()).makeMessage(message.getChannel()).send();
                if (reaction != null) message.addReactionByName(CommandHandler.EXCEPTION_FOR_METHOD);
            }
            else if (e.getCause() instanceof DevelopmentException) Log.log("Exception during method execution: " + getName(), e);
            else new DevelopmentException(e.getCause()).makeMessage(message.getChannel()).send();
        }
        ProcessingHandler.endProcess(channel);
        return false;
    }

    /**
     * Returns if the command succeeded determined by it's output.
     *
     * @param o the output of the command's invocation.
     * @return if the command succeeded determined by it's output.
     */
    boolean interpretSuccess(Object o) {
        return !(o instanceof Boolean) || (boolean) o;
    }
}