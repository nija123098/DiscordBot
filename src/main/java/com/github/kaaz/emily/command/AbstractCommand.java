package com.github.kaaz.emily.command;

import com.github.kaaz.emily.command.anotations.Command;
import com.github.kaaz.emily.command.anotations.Context;
import com.github.kaaz.emily.command.anotations.Argument;
import com.github.kaaz.emily.command.anotations.LaymanName;
import com.github.kaaz.emily.config.ConfigHandler;
import com.github.kaaz.emily.config.Configurable;
import com.github.kaaz.emily.config.GlobalConfigurable;
import com.github.kaaz.emily.config.GuildUser;
import com.github.kaaz.emily.discordobjects.helpers.MessageMaker;
import com.github.kaaz.emily.discordobjects.wrappers.*;
import com.github.kaaz.emily.discordobjects.wrappers.event.EventDistributor;
import com.github.kaaz.emily.exeption.BotException;
import com.github.kaaz.emily.exeption.ContextException;
import com.github.kaaz.emily.exeption.DevelopmentException;
import com.github.kaaz.emily.perms.BotRole;
import com.github.kaaz.emily.perms.configs.specialperms.*;
import com.github.kaaz.emily.service.services.MemoryManagementService;
import com.github.kaaz.emily.util.EmoticonHelper;
import com.github.kaaz.emily.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.stream.Stream;

/**
 * @author nija123098
 * @since 2.0.0
 */
@LaymanName(value = "Command", help = "The command represented by a string or some alias")
public class AbstractCommand {
    private final Class<? extends AbstractCommand> superCommand;
    private String name, aAliases, eAliases, rAliases, help;
    private BotRole botRole;
    private ModuleLevel module;
    private Method method;
    private Parameter[] parameters;
    private Set<String> emoticonAliases, allNames;
    private long globalUseTime, globalCoolDownTime;
    private List<Guild> guildCoolDowns;
    private List<Channel> channelCoolDowns;
    private List<User> userCoolDowns;
    private List<GuildUser> guildUserCoolDowns;
    private Set<ContextRequirement> contextRequirements;
    public AbstractCommand(Class<? extends AbstractCommand> superCommand, String name, String absoluteAliases, String emoticonAliases, String relativeAliases, String help){
        this.superCommand = superCommand;
        this.name = name;
        this.aAliases = absoluteAliases;
        this.eAliases = emoticonAliases;
        this.rAliases = relativeAliases;
        this.name = name;
        this.help = help != null ? help : "No provided help for this command";
    }

    public AbstractCommand(String name, ModuleLevel module, String absoluteAliases, String emoticonAliases, String help){
        this(null, name, absoluteAliases, emoticonAliases, null, help);
        this.module = module;
        this.name = name;
    }

    public AbstractCommand(String name, BotRole botRole, ModuleLevel module, String absoluteAliases, String emoticonAliases, String help){
        this(name, module, absoluteAliases, emoticonAliases, help);
        this.botRole = botRole;
    }

    void load(){
        AbstractCommand superCommand = getSuperCommand();
        this.name = superCommand == null ? name : superCommand.name + " " + name;
        this.module = getModule() == null ? superCommand == null ? ModuleLevel.NONE : superCommand.getModule() : ModuleLevel.NONE;
        this.botRole = getBotRole() == null ? superCommand == null ? BotRole.USER : superCommand.getBotRole() : BotRole.USER;
        this.allNames = new HashSet<>();
        this.allNames.add(this.name);
        if (aAliases != null){
            Collections.addAll(this.allNames, aAliases.split(", "));
        }
        if (eAliases != null){
            String[] eAliases = this.eAliases.split(", ");
            this.emoticonAliases = new HashSet<>(eAliases.length);
            Collections.addAll(this.emoticonAliases, eAliases);
        }else{
            this.emoticonAliases = new HashSet<>(0);
        }
        this.allNames.addAll(this.emoticonAliases);
        this.emoticonAliases.stream().map(EmoticonHelper::getChars).forEach(this.allNames::add);
        if (rAliases != null && superCommand != null){
            for (String rel : rAliases.split(", ")){
                superCommand.getNames().forEach(s -> this.allNames.add(s + " " + rel));
            }
        }
        Method[] methods = this.getClass().getMethods();
        for (Method m : methods) {
            if (m.isAnnotationPresent(Command.class)) {
                this.method = m;
                break;
            }
        }
        if (this.method == null){
            throw new DevelopmentException("No method annotated " + Command.class.getSimpleName() + " in command: " + this.getClass().getName());
        }
        this.parameters = this.method.getParameters();
        this.contextRequirements = new HashSet<>();
        for (Parameter parameter : this.parameters) {
            if (parameter.getAnnotations().length == 0 || (parameter.isAnnotationPresent(Context.class) && !parameter.getAnnotation(Context.class).softFail())) {
                this.contextRequirements.addAll(InvocationObjectGetter.getContextRequirements(parameter.getType(), parameter.isAnnotationPresent(Context.class) ? parameter.getAnnotation(Context.class).value() : ContextType.DEFAULT));
            }else if (parameter.isAnnotationPresent(Argument.class) && !parameter.getAnnotation(Argument.class).optional()){
                this.contextRequirements.addAll(InvocationObjectGetter.getConvertRequirements(parameter.getType(), parameter.getAnnotation(Argument.class).replacement()));
            }
        }// makes it into a more efficient set
        this.contextRequirements = this.contextRequirements.isEmpty() ? Collections.emptySet() : EnumSet.copyOf(this.contextRequirements);
        this.globalCoolDownTime = this.getCoolDown(GlobalConfigurable.class);
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
        persistence = this.getCoolDown(GuildUser.class);
        if (persistence != -1){
            this.guildUserCoolDowns = new MemoryManagementService.ManagedList<>(persistence);
        }
        EventDistributor.register(this);
        this.aAliases = null;
        this.eAliases = null;
        this.rAliases = null;
    }

    /**
     * A getter for the object of the super command.
     *
     * @return this command's super command
     */
    public AbstractCommand getSuperCommand(){
        return CommandHandler.getCommand(this.superCommand);
    }

    /**
     * A standard getter.
     *
     * @return the type of the super command
     */
    public Class<? extends AbstractCommand> getSuperCommandType(){
        return this.superCommand;
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
     * Returns the command's module
     *
     * @return the module of which the command is part of
     */
    public ModuleLevel getModule(){
        return this.module;
    }

    /**
     * Gets the bot role required to run this command,
     * default USER
     *
     * @return the required bot role to run this command
     */
    public BotRole getBotRole(){
        return this.botRole;
    }

    /**
     * Gets if the current command is a template command
     *
     * @return if the command is a template command
     */
    public boolean isTemplateCommand(){
        return this.getClass().getPackage().getName().contains(".template.");
    }

    /**
     * Gets the return type of the command
     *
     * @return the return type of the command
     */
    public Class<?> getReturnType(){
        return this.method.getReturnType();
    }

    /**
     * A standard getter
     *
     * @return the Java reflection results for getting the paramaters
     */
    public Parameter[] getParameters(){
        return this.parameters;
    }

    /**
     * A standard getter.
     *
     * @return gets the help text.
     */
    public String getHelp(){
        return this.help;
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
        boolean hasNormalPerm = BotRole.hasRequiredRole(this.botRole, user, guild);
        if (guild != null && (!ConfigHandler.getSetting(GuildSpecialPermsEnabledConfig.class, guild) || !(this.botRole.ordinal() >= BotRole.GUILD_TRUSTEE.ordinal()))){
            boolean disapproved = false;
            for (Role role : user.getRolesForGuild(guild)){
                if (!ConfigHandler.getSetting(SpecialPermsRoleEnable.class, role)){
                    continue;
                }
                if (ConfigHandler.getSetting(PermsCommandWhitelistConfig.class, role).contains(this.getName())
                        || (ConfigHandler.getSetting(PermsModuleWhitelistConfig.class, role).contains(this.getName())
                        && !ConfigHandler.getSetting(PermsModuleWhitelistExemptionsConfig.class, role).contains(this.getName()))){
                    return true;
                }
                if (ConfigHandler.getSetting(PermsCommandBlacklistConfig.class, role).contains(this.getName())
                        || ConfigHandler.getSetting(PermsModuleBlacklistConfig.class, role).contains(this.getName())
                        && !ConfigHandler.getSetting(PermsCommandBlacklistConfig.class, role).contains(this.getName())){
                    disapproved = true;
                }
            }
            return !disapproved && hasNormalPerm;
        }else{
            return hasNormalPerm;
        }
    }

    /**
     * A method to check the cool down on a command.
     *
     * @param channel the channel checked for rate limiting
     * @param user the user checked for rate limiting
     * @return if the command is not being rate limited
     */
    boolean checkCoolDown(Channel channel, User user){
        if (this.globalUseTime != -1 && this.globalUseTime > System.currentTimeMillis()){
            return false;
        }
        if (this.guildCoolDowns != null && !channel.isPrivate() && this.guildCoolDowns.contains(channel.getGuild())){
            return false;
        }
        if (this.channelCoolDowns != null && this.channelCoolDowns.contains(channel)){
            return false;
        }
        if (this.userCoolDowns != null && this.userCoolDowns.contains(user)){
            return false;
        }
        if (this.guildUserCoolDowns != null && !channel.isPrivate() && this.guildUserCoolDowns.contains(GuildUser.getGuildUser(channel.getGuild(), user))){
            return false;
        }
        return true;
    }

    /**
     * Method to be called when the command is invoked
     *
     * @param channel the channel checked for rate limiting
     * @param user the user checked for rate limiting
     */
    public void invoked(Channel channel, User user){
        if (this.globalUseTime != -1){
            this.globalUseTime = System.currentTimeMillis() + this.globalCoolDownTime;
        }
        if (this.guildCoolDowns != null && !channel.isPrivate()){
            this.guildCoolDowns.add(channel.getGuild());
        }
        if (this.channelCoolDowns != null){
            this.channelCoolDowns.add(channel);
        }
        if (this.userCoolDowns != null){
            this.userCoolDowns.add(user);
        }
        if (this.guildUserCoolDowns != null && !channel.isPrivate()){
            this.guildUserCoolDowns.add(GuildUser.getGuildUser(channel.getGuild(), user));
        }
    }

    /**
     * Returns the cool down in millis dependent
     * on the type of configurable which is being rate limited
     *
     * @param clazz the configurable type
     * @return the cool down in millis dependent on the tyoe
     */
    public long getCoolDown(Class<? extends Configurable> clazz){
        return -1;
    }

    /**
     * A standard getter to get the set of context requirements for the command
     *
     * @return The context requirements for the command
     */
    public Set<ContextRequirement> getContextRequirements(){
        return this.contextRequirements;
    }

    protected void checkSetupRequirements(User user, Shard shard, Channel channel, Guild guild, Message message, Reaction reaction, String args){}

    /**
     *
     *
     * @param user the user that invokes the command
     * @param message the message sent or reacted to
     * @param reaction the reaction if the invocation
     *                 was caused by reacting to a message
     * @param args the user args for invocation
     * @return if the command was successful
     */
    public Object invoke(User user, Shard shard, Channel channel, Guild guild, Message message, Reaction reaction, String args, Object...argOverrides){
        ProcessingHandler.startProcess(channel);
        Object[] contexts = new Object[]{user, shard, channel, guild, message, reaction, args};
        try{this.checkSetupRequirements((User) contexts[0], (Shard) contexts[1], (Channel) contexts[2], (Guild) contexts[3], (Message) contexts[4], (Reaction) contexts[5], (String) contexts[6]);
        } catch (BotException e) {new MessageMaker((Message) contexts[4]);}
        boolean[] overridden = new boolean[argOverrides.length];
        for (int i = 0; i < overridden.length; i++) {
            overridden[i] = argOverrides[i] != null;
        }
        for (int i = 0; i < contexts.length; i++) {
            if (this.contextRequirements.contains(ContextRequirement.values()[i])) ContextException.checkRequirement(contexts[i], ContextRequirement.values()[i]);
        }
        Object[] objects = InvocationObjectGetter.replace(this.parameters, new Object[this.parameters.length], user, shard, channel, guild, message, reaction, args, overridden);
        for (int i = 0; i < overridden.length; i++) {
            if (argOverrides[i] != null) objects[i] = argOverrides[i];
        }
        try {
            Object object = this.method.invoke(this, objects);
            Stream.of(objects).filter(MessageMaker.class::isInstance).forEach(o -> ((MessageMaker) o).send());
            ProcessingHandler.endProcess(channel);
            return object;
        } catch (IllegalAccessException e) {
            Log.log("Malformed command: " + getName(), e);
        } catch (InvocationTargetException e) {
            if (e.getCause() instanceof BotException) new MessageMaker(user, message).asExceptionMessage(((BotException) e.getCause())).withReaction("grey_exclamation").send();
            Log.log("Exception during method execution: " + getName(), e);
        }
        ProcessingHandler.endProcess(channel);
        return false;
    }

    protected boolean interpretSuccess(Object o){
        return true;
    }
}
