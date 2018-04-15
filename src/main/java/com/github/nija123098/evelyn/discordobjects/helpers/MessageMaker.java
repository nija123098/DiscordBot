package com.github.nija123098.evelyn.discordobjects.helpers;

import com.github.nija123098.evelyn.botconfiguration.ConfigProvider;
import com.github.nija123098.evelyn.command.ProcessingHandler;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.config.configs.guild.GuildLanguageConfig;
import com.github.nija123098.evelyn.config.configs.user.UserLanguageConfig;
import com.github.nija123098.evelyn.discordobjects.ExceptionWrapper;
import com.github.nija123098.evelyn.discordobjects.helpers.guildaudiomanager.GuildAudioManager;
import com.github.nija123098.evelyn.discordobjects.wrappers.*;
import com.github.nija123098.evelyn.exception.DevelopmentException;
import com.github.nija123098.evelyn.launcher.Launcher;
import com.github.nija123098.evelyn.moderation.logging.VoiceCommandPrintChannelConfig;
import com.github.nija123098.evelyn.util.*;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.Triple;
import sx.blah.discord.handle.impl.obj.ReactionEmoji;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.Permissions;
import sx.blah.discord.util.EmbedBuilder;
import sx.blah.discord.util.MessageBuilder;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A utility class to help with message responses and sending
 * messages in the proper format dependent on how it is used.
 *
 * Editing the values of an instance after send and
 * calling {@link MessageMaker#forceCompile()} will
 * result in the editing of the previously sent message.
 *
 * @author nija13098
 * @since 1.0.0
 * @see ReactionBehavior
 */
public class MessageMaker {
    static {
        Launcher.registerShutdown(ReactionBehavior::deregisterAll);
    }
    private static final ScheduledExecutorService EXECUTOR_SERVICE = Executors.newSingleThreadScheduledExecutor(r -> ThreadHelper.getDemonThreadSingle(r, "Message-Maker-Thread"));
    private static final Color DEFAULT_COLOR = new Color(116, 32, 196);
    private static final int CHAR_LIMIT = 2000;
    private static final int EMBED_LIMIT = 1000;
    private TextPart authorName, title, header, footer, note, external;
    private List<TextPart> textList = new ArrayList<>();
    private String[] textVals;
    private List<FieldPart> fieldList = new ArrayList<>();
    private Triple<String, String, Boolean>[][] fieldIndices;
    private EmbedBuilder embed = new EmbedBuilder();
    private MessageBuilder builder;
    private String lang;
    private User user;
    private Channel channel;
    private AtomicInteger currentPage = new AtomicInteger();
    private Map<String, ReactionBehavior> reactionBehaviors = new LinkedHashMap<>();
    private IMessage message, origin;
    private Message ourMessage;
    private final Set<String> reactions = new HashSet<>(1);
    private Long deleteDelay;
    private File file;
    private boolean okHand, maySend, mustEmbed, forceCompile, compiled, colored, messageError, autoSend = true, translate;

    /**
     * Builds the message maker and sets it up.
     *
     * @param user the user that the message is intended for.
     * @param channel the channel the message is intended for.
     * @param message the message the build message is intended as response to.
     */
    private MessageMaker(User user, Channel channel, Message message) {
        this.authorName = new TextPart(this);
        this.title = new TextPart(this);
        this.header = new TextPart(this);
        this.footer = new TextPart(this);
        this.note = new TextPart(this);
        this.external = new TextPart(this);
        this.user = user;
        this.channel = channel;
        this.origin = message == null ? null : message.message();
    }
    public MessageMaker(User user, Channel origin) {
        this(user, origin, null);
    }
    public MessageMaker(Channel origin) {
        this(null, origin, null);
    }
    public MessageMaker(User user, Message origin) {
        this(user, origin.getChannel(), origin);
    }
    public MessageMaker(Message message) {
        this(message.getAuthor(), message.getChannel(), message);
    }
    public MessageMaker(User user) {
        this(user, user.getOrCreatePMChannel(), null);
    }
    public MessageMaker(MessageMaker maker) {
        this(maker.user, maker.channel, Message.getMessage(maker.origin));
    }
    // setup methods

    /**
     * Removes the previously sent message from history,
     * usually to prevent editing of that message.
     *
     * @return the instance.
     */
    public MessageMaker clearMessage() {
        this.message = null;
        this.ourMessage = null;
        return this;
    }

    /**
     * Sets if the message should be sent automatically.
     *
     * @param autoSend if the message should be sent automatically.
     * @return the instance.
     */
    public MessageMaker withAutoSend(boolean autoSend) {
        this.autoSend = autoSend;
        return this;
    }

    /**
     * Forces the recompiling of the message content for this builder.
     *
     * @return the instance.
     */
    public MessageMaker forceCompile() {
        this.forceCompile = true;
        return this;
    }

    /**
     * Forces the non-embedding of the building message.
     *
     * @return the instance.
     */
    public MessageMaker asNormalMessage() {
        this.embed = null;
        return this;
    }

    /**
     * Forces the embeding of the building message.
     *
     * @return the instance.
     */
    public MessageMaker mustEmbed() {
        return this.mustEmbed(true);
    }

    /**
     * Sets if the embeding of the building message should be forced to embed.
     *
     * @param mustEmbed if the building message should be forced to embed.
     * @return the instance.
     */
    public MessageMaker mustEmbed(boolean mustEmbed) {
        this.mustEmbed = mustEmbed;
        return this;
    }

    /**
     * Returns if the building message could be sent as a non-embeded message.
     *
     * @return if the building message could be sent as a non-embeded message.
     */
    public boolean couldNormalize() {
        return !this.mustEmbed && this.fieldList.size() == 0 && this.textList.size() == 0 && !this.authorName.appended && !this.title.appended && !this.footer.appended && !this.note.appended;
    }

    /**
     * Sets the {@link Channel} the building message should be sent in.
     *
     * @param channel the {@link Channel} the building message should be sent in.
     * @return the instance.
     */
    public MessageMaker withChannel(Channel channel) {
        if (channel.equals(this.channel)) return this;
        ProcessingHandler.swapProcess(this.channel, channel);
        this.channel = channel;
        return this;
    }

    /**
     * Sets a {@link ReactionBehavior} which only is invoked to the user
     * specified by {@link MessageMaker#user} if a {@link Reaction}
     * by the given name is done on the built message.
     *
     * A call to this is ignored in the case that a reaction behavior
     * has already been set for the specified {@link Reaction} name.
     *
     * @param reactionName the {@link Reaction} to activate on specified by name for the intended user.
     * @param behavior the behavior to preform when the built message has been reacted to.
     * @return the instance.
     */
    public MessageMaker withReactionBehavior(String reactionName, ReactionBehavior behavior) {
        if (!this.reactionBehaviors.containsKey(reactionName)) this.reactionBehaviors.put(reactionName, (add, reaction, user) -> {
            if (user.equals(this.user)) behavior.behave(add, reaction, user);
        });
        return this;
    }

    /**
     * Sets a {@link ReactionBehavior} which is invoked if a
     * {@link Reaction} by the given name is done on the built message.
     *
     * @param reactionName the {@link Reaction} to activate on specified by name.
     * @param behavior the behavior to preform when the built message has been reacted to.
     * @return the instance.
     */
    public MessageMaker withPublicReactionBehavior(String reactionName, ReactionBehavior behavior) {
        if (!this.reactionBehaviors.containsKey(reactionName)) this.reactionBehaviors.put(reactionName, behavior);
        return this;
    }

    /**
     * Removes a {@link ReactionBehavior} for the specified
     * {@link Reaction} specified by name if one exists.
     *
     * @param reactionName the name specification for.
     * @return the instance.
     */
    public MessageMaker withoutReactionBehavior(String reactionName) {
        if (this.reactionBehaviors.remove(reactionName) != null) ReactionBehavior.deregisterListener(this.ourMessage, reactionName);
        return this;
    }

    /**
     * Deregisters all {@link ReactionBehavior} for the building message.
     *
     * @return the instance
     */
    public MessageMaker clearReactionBehaviors() {
        this.getReactionBehaved().forEach(this::withoutReactionBehavior);
        return this;
    }

    /**
     * Reacts to the building message when the message is sent.
     *
     * @param name the {@link Reaction} to react with on the building message specified by name.
     * @return the instance.
     */
    public MessageMaker withReaction(String name) {
        String chars = EmoticonHelper.getChars(name, false);
        if (chars == null) throw new DevelopmentException("Invalid emoticon name name");
        this.reactions.add(chars.endsWith("\u200B") ? chars.substring(0, chars.length() - 1) : chars);
        return this;
    }

    /**
     * Makes the building message send a response to a {@link User} as a direct message.
     *
     * @return the instance.
     */
    public MessageMaker withDM() {
        if (this.channel.isPrivate()) return this;
        return this.withChannel(this.user.getOrCreatePMChannel());
    }

    /**
     * If the message this is responding to should be reacted with a :ok_hand: {@link Reaction}.
     *
     * @param ok if the message this is responding to should be reacted with a ok_hand {@link Reaction}
     * @return the instance.
     */
    public MessageMaker withOK(boolean ok) {
        this.okHand = ok;
        return this;
    }

    /**
     * Sets that the message this is responding to should be reacted with a ok_hand {@link Reaction}.
     *
     * @return the instance.
     */
    public MessageMaker withOK() {
        this.okHand = true;
        return this;
    }
    public MessageMaker ensureListSize(int size) {
        ((ArrayList<TextPart>) this.textList).ensureCapacity(size);
        return this;
    }
    public MessageMaker ensureFieldSize(int size) {
        ((ArrayList<FieldPart>) this.fieldList).ensureCapacity(size);
        return this;
    }

    /**
     * Sets the delay in millis to delete the building message.
     * The message will not be deleted by this builder otherwise.
     *
     * @param deleteDelay the millis from sending when the building message should be deleted.
     * @return the instance.
     */
    public MessageMaker withDeleteDelay(Long deleteDelay) {
        this.deleteDelay = deleteDelay;
        return this;
    }

    /**
     * Sets if the building message may be sent.
     *
     * @param maySend if the building message may be sent.
     * @return the instance.
     */
    public MessageMaker maySend(boolean maySend) {
        this.maySend = maySend;
        return this;
    }

    /**
     * Sets that the building message may be sent.
     *
     * @return the instance.
     */
    public MessageMaker maySend() {
        return this.maySend(true);
    }
    // text methods

    /**
     * Sets the given {@link User} as the author to make all
     * author fields with the relevant information of that user.
     *
     * @param author the {@link User} to set all author fields for.
     * @return the instance.
     */
    public MessageMaker withAuthor(User author) {
        this.getAuthorName().appendRaw(author.getNameAndDiscrim() + " " + author.getID());
        return this.withAuthorIcon(author.getAvatarURL());
    }

    /**
     * Gets the {@link TextPart} for altering the embed field for the author name.
     *
     * @return the {@link TextPart} for altering the embed field for the author name.
     */
    public TextPart getAuthorName() {
        return this.authorName;
    }

    /**
     * Gets the {@link TextPart} for altering the embed field for the title.
     *
     * @return the {@link TextPart} for altering the embed field for the title.
     */
    public TextPart getTitle() {
        return this.title;
    }

    /**
     * Gets the {@link TextPart} for altering the embed field for the normal
     * text or if a embed is not nessisary or forced the normal text.
     *
     * @return the {@link TextPart} for altering the embed field for the
     * normal text or if a embed is not nessisary or forced the normal text.
     */
    public TextPart getHeader() {
        return this.header;
    }

    /**
     * Gets the {@link TextPart} for the content to appear below listings.
     *
     * @return the {@link TextPart} for the content to appear below listings.
     */
    public TextPart getFooter() {
        return this.footer;
    }

    /**
     * Gets the {@link TextPart} for the note content when the bulding message is to be embed.
     *
     * @return the {@link TextPart} for the note content when the bulding message is to be embed.
     */
    public TextPart getNote() {
        return this.note;
    }

    /**
     * Gets the {@link TextPart} for the content to be written outside an embed.
     *
     * @return the {@link TextPart} for the content to be written outside an embed.
     */
    public TextPart getExternal() {
        return this.external;
    }

    /**
     * Gets a {@link FieldPart} for the content of a single field part.
     *
     * @return a {@link FieldPart} for the content of a single field part.
     */
    public FieldPart getNewFieldPart() {
        return new FieldPart(this);// adds self in the constructor
    }

    /**
     * Removes all entries for {@link FieldPart}s associated with this maker.
     *
     * @return the instance.
     */
    public MessageMaker clearFieldParts() {
        this.fieldList.clear();
        return this;
    }

    /**
     * Gets a {@link TextPart} for a list in the embed.
     *
     * @return a {@link TextPart} for a list in the embed.
     */
    public TextPart getNewListPart() {
        TextPart part = new TextPart(this);
        this.textList.add(part);
        return part;
    }

    /**
     * Makes a new page when listing.
     *
     * @return the instance.
     */
    public MessageMaker guaranteeNewListPage() {
        this.textList.add(null);
        return this;
    }

    /**
     * Makes a new page when listing.
     *
     * @return the instance.
     */
    public MessageMaker guaranteeNewFieldPage() {
        this.fieldList.add(null);
        return this;
    }

    /**
     * A shortcut method for {@link MessageMaker#header#appendRaw(String)}.
     *
     * @param s the thing to {@link LangString#appendRaw(String)} to {@link MessageMaker#header}.
     * @return the instance.
     */
    public MessageMaker appendRaw(String s) {
        this.header.appendRaw(s);
        return this;
    }

    /**
     * A shortcut method for {@link MessageMaker#header#append(boolean, String)}.
     *
     * @param s the thing to {@link LangString#append(boolean, String)} to {@link MessageMaker#header}.
     * @return the instance.
     */
    public MessageMaker append(String s) {
        this.header.append(s);
        return this;
    }

    /**
     * A shortcut method for {@link MessageMaker#header#appendAlternate(boolean, String...)}.
     *
     * @param s the thing to {@link LangString#appendRaw(String)} to {@link MessageMaker#header}.
     * @return the instance.
     */
    public MessageMaker appendAlternate(boolean raw, String...s) {
        this.header.appendAlternate(raw, s);
        return this;
    }

    /**
     * A shortcut method for {@link MessageMaker#header#append(boolean, String)}.
     *
     * @param s the thing to {@link LangString#append(boolean, String)} to {@link MessageMaker#header}.
     * @return the instance.
     */
    public MessageMaker append(boolean raw, String s) {
        this.header.append(!raw, s);
        return this;
    }
    // embed methods

    /**
     * Returns if the associated embed has been colored.
     *
     * @return if the instance's color has been set.
     */
    public boolean isColored() {
        return this.colored;
    }

    /**
     * Sets a {@link Color} for the building message's embed color.
     *
     * @param color the color for the building message's embed color.
     * @return the instance.
     */
    public MessageMaker withColor(Color color) {
        this.embed.withColor(color);
        this.colored = true;
        return this;
    }

    /**
     * Sets the {@link Color} for the embed color based on the average color of the url's image.
     *
     * @param url the url to get a image average color from.
     * @return the instance.
     */
    public MessageMaker withColor(String url) {
        this.withColor(GraphicsHelper.getColor(url));
        return this;
    }

    /**
     * Sets the embed's {@link Color} as the {@link MessageMaker#user}'s avatar average color.
     *
     * @return the instance.
     */
    public MessageMaker withColor() {
        return withColor(this.user);
    }

    /**
     * Sets the embed color to a random color.
     *
     * @return the instance.
     */
    public MessageMaker withRandomColor() {
        this.withColor(new Color(Rand.getRand(16777216)));
        return this;
    }

    /**
     * Sets the embed color as the {@link User}'s average avatar color.
     *
     * @param user the user whose color portrait.
     * @return the instance.
     */
    public MessageMaker withColor(User user) {
        if (user != null) this.withColor(user.getAvatarURL());
        return this;
    }

    /**
     * Sets an embed's color to the {@link Role}'s color.
     *
     * @param color the role whose color should be used in the embed.
     * @return the instance.
     */
    public MessageMaker withColor(Role color) {
        return this.withColor(color.getColor());
    }

    /**
     * Sets the footer icon of an embed.
     *
     * @param url the url of the icon.
     * @return the instance.
     */
    public MessageMaker withFooterIcon(String url) {
        this.embed.withFooterIcon(url);
        this.mustEmbed = true;
        return this.maySend();
    }

    /**
     * Sets the author icon of an embed.
     *
     * @param url the author's icon.
     * @return the instance.
     */
    public MessageMaker withAuthorIcon(String url) {
        this.embed.withAuthorIcon(url);
        this.mustEmbed = true;
        return this.maySend();
    }

    /**
     * Sets the url clicking on the header/author causes redirection to.
     *
     * @param url the url to set the header/author icon link to.
     * @return the instance.
     */
    public MessageMaker withUrl(String url) {
        if (url != null) this.embed.withUrl(url);
        this.mustEmbed = true;
        return this;
    }

    /**
     * Sets the thumb image in an embed.
     *
     * @param url the url the thumb.
     * @return the instance.
     */
    public MessageMaker withThumb(String url) {
        this.embed.withThumbnail(url);
        this.mustEmbed = true;
        return this.maySend();
    }

    /**
     * Sets the embed image to the one pointed to by the url.
     *
     * @param url the image to add to the embed.
     * @return the instance.
     */
    public MessageMaker withImage(String url) {
        this.embed.withImage(url);
        this.mustEmbed = true;
        return this.maySend();
    }

    /**
     * Sets a file to be attached to the building message.
     *
     * @param file the file to be attached to the building message.
     * @return the instance.
     */
    public MessageMaker withFile(File file) {
        this.file = file;
        return this.appendRaw("");
    }

    /**
     * Sets the embed timestamp.
     *
     * @param millis the millis to set the embed timestamp to.
     * @return the instance.
     */
    public MessageMaker withTimestamp(long millis) {
        this.embed.withTimestamp(millis);
        this.mustEmbed = true;
        return this;
    }

    /**
     * Forces the translation of the resulting message instance.
     *
     * This may be useful for when messages may contain arbitrary text not set by the receiver.
     *
     * @return the instance.
     */
    public MessageMaker withForceTranslate() {
        this.translate = true;
        return this;
    }

    /**
     * Sets the language that the message should be sent in.
     *
     * @param lang the language code to set the message to translate as.
     * @return the instance.
     */
    public MessageMaker withLang(String lang) {
        this.lang = lang;
        return this;
    }

    /**
     * Sets the page to start the message maker on, or the closest one.
     *
     * @param page the page to start on.
     * @return the instance.
     */
    public MessageMaker withPage(int page) {
        this.currentPage.set(page);
        return this;
    }

    /**
     * Sets the page to start the message maker on, or the closest one, but paging starts at 1.
     *
     * @param page the page to start on.
     * @return the instance.
     */
    public MessageMaker withPageFromOne(int page) {
        return this.withPage(page - 1);
    }

    // getting

    /**
     * Gets the message that was built and sent, otherwise null.
     *
     * @return the message that was built and sent, otherwise null.
     */
    public Message sentMessage() {
        return Message.getMessage(this.message);
    }

    public MessageMaker setMessage(Message message) {
        this.message = message.message();
        return this;
    }

    /**
     * Gets the string representations of {@link Reaction}s which have associated {@link ReactionBehavior}s.
     *
     * @return the string representations of {@link Reaction}s which have associated {@link ReactionBehavior}s.
     */
    public Set<String> getReactionBehaved() {
        return this.reactionBehaviors.keySet();
    }
    // building

    /**
     * Sends the message if it should depending on if it is being sent aromatically.
     *
     * @param auto if this is being sent automatically.
     */
    public void send(boolean auto) {
        if (!(!this.autoSend && auto)) send();
    }

    /**
     * Sends the building message or edits the message
     * if it has already been sent and has not been cleared.
     */
    public void send() {
        try{send(0);
        } catch (Exception e) {
            Log.log("Error while sending " + (this.messageError ? "internal message error" : "") + " message", e);
            if (!this.messageError) {
                MessageMaker maker = new DevelopmentException(e).makeMessage(this.channel);
                maker.messageError = true;
                maker.send();
            }
        }
    }

    /**
     * Sends the building message dependent on the page.
     *
     * @param page the page to sent.
     */
    private void send(int page) {
        if (ConfigProvider.BOT_SETTINGS.ghostModeEnabled() && !DiscordClient.getApplicationOwner().equals(this.user)) return;
        if (!this.maySend) {
            if (this.origin != null && this.origin.getChannel().getModifiedPermissions(DiscordClient.getOurUser().user()).contains(Permissions.ADD_REACTIONS)) ExceptionWrapper.wrap(() -> this.origin.addReaction(ReactionEmoji.of(EmoticonHelper.getChars("ok_hand", false))));
            return;
        }
        if (!this.channel.getModifiedPermissions(DiscordClient.getOurUser()).contains(DiscordPermission.SEND_MESSAGES)) return;// only will effect emoticon commands, normal commands are already checked
        this.builder = new MessageBuilder(DiscordClient.getClientForShard(this.channel.getShard()));
        if (this.file != null) {
            try{this.builder.withFile(this.file);
            } catch (FileNotFoundException e) {
                throw new DevelopmentException("File not made by time of sending", e);
            }
        }
        if (this.origin != null && this.okHand) ExceptionWrapper.wrap(() -> null);
        this.compile();// page must be 0 if fieldIndices is null, which indicates no pages, I think.  It's been a while.
        if (this.embed != null) {
            if (page < 0 || page >= this.fieldIndices.length) throw new DevelopmentException("Attempted to get a page that doesn't exit");
            this.embed.clearFields().withDesc((this.header.langString.translate(this.lang, this.translate) + "\n\n" + (page >= textVals.length ? "" : textVals[page]) + "\n\n" + this.footer.langString.translate(lang, this.translate)).replace("\n\n\n\n", "\n\n"));
            for (Triple<String, String, Boolean> ind : fieldIndices[page]) {
                this.embed.appendField(ind.getLeft(), ind.getMiddle(), ind.getRight());
            }
            this.builder.withEmbed(this.embed.build());
            if (this.external.appended) this.builder.appendContent("\n" + this.external.translate(this.lang));
        }
        if (this.message == null) {
            if (this.channel instanceof VoiceChannel) {
                GuildAudioManager.getManager(this.channel.getGuild()).interrupt(this.header.langString);
                return;
            }
            this.builder.withChannel(this.channel.channel());
            this.message = ExceptionWrapper.wrap((ExceptionWrapper.Request<IMessage>) () -> this.builder.send());
            this.ourMessage = Message.getMessage(this.message);
            this.reactions.forEach(s -> ExceptionWrapper.wrap(() -> this.message.addReaction(ReactionEmoji.of(s))));
            if (this.deleteDelay != null) EXECUTOR_SERVICE.schedule(() -> ExceptionWrapper.wrap(this.message::delete), this.deleteDelay, TimeUnit.MILLISECONDS);
        } else {
            if (this.embed == null) ExceptionWrapper.wrap(() -> this.message.edit(this.builder.getContent()));
            else ExceptionWrapper.wrap(() -> this.message.edit(this.embed.build()));
        }

        for (Map.Entry<String, ReactionBehavior> behavior : this.reactionBehaviors.entrySet()) {
            ReactionBehavior.registerListener(this.ourMessage, behavior.getKey(), behavior.getValue());
        }

        ProcessingHandler.endProcess(this.channel);
    }

    /**
     * Gets the language the message should be sent in.
     *
     * @param user the user to consider getting the language for.
     * @param channel the channel to consider getting the language for.
     * @return the language code to send the message as.
     */
    public static String getLang(User user, Channel channel) {
        String lang = null;
        if (user != null) lang = ConfigHandler.getSetting(UserLanguageConfig.class, user);
        if (!channel.isPrivate() && lang == null) lang = ConfigHandler.getSetting(GuildLanguageConfig.class, channel.getGuild());
        return lang == null ? "en" : lang;
    }

    /**
     * Compiles how the building message should be sent.
     */
    private void compile() {
        if (this.compiled && !this.forceCompile) return;
        this.compiled = true;
        if (!this.colored) this.withColor(DEFAULT_COLOR);
        if (this.lang == null) this.lang = getLang(this.user, this.channel);
        this.translate = this.translate || !this.lang.equals("en");
        // message
        if (this.couldNormalize()) this.asNormalMessage();
        else if (this.channel instanceof VoiceChannel) this.channel = ConfigHandler.getSetting(VoiceCommandPrintChannelConfig.class, this.channel.getGuild());
        if (this.embed == null) this.builder.withContent(this.header.langString.translate(this.lang, translate));
        else {// embed
            this.embed.withAuthorName(authorName.langString.translate(lang, this.translate));
            this.embed.withTitle(title.langString.translate(lang, this.translate));
            int starterChars = this.embed.getTotalVisibleCharacters() + header.langString.translate(this.lang, translate).length() + footer.langString.translate(this.lang, translate).length();
            if (CHAR_LIMIT < starterChars) {
                throw new DevelopmentException("Header and footer are too big.");
            }
            if (textList.size() != 0) {
                int index = -1;
                String s = "";
                List<String> strings = new ArrayList<>();
                int newLines = 0;
                while (true) {
                    if (++index >= textList.size()) {
                        strings.add(s);
                        break;
                    }// make new page compatible with recompile
                    if (textList.get(index) == null || starterChars + s.length() + textList.get(index).langString.translate(lang, translate).length() > CHAR_LIMIT || (newLines += StringHelper.instances(textList.get(index).langString.translate(lang, translate), '\n')) > 21) {
                        if (textList.get(index) == null) textList.remove(index);
                        newLines = 0;
                        --index;
                        strings.add(s);
                        s = "";
                    } else {
                        s += "\n" + textList.get(index).langString.translate(lang, translate);
                    }
                }
                textVals = new String[strings.size()];
                for (int i = 0; i < strings.size(); i++) {
                    textVals[i] = strings.get(i);
                }
            } else {
                textVals = new String[]{""};
            }
            if (fieldList.size() != 0) {
                boolean embedsOfFront = this.embed.getTotalVisibleCharacters() + (this.textVals.length > 0 ? this.textVals[0].length() : 0) < EMBED_LIMIT;
                int index = -1, size = 0, page = 0;
                List<List<Triple<String, String, Boolean>>> vals = new ArrayList<>(this.fieldList.size());
                vals.add(new ArrayList<>());
                boolean newPage = false;
                while (true) {
                    if (++index >= fieldList.size()) {
                        break;
                    }
                    if (starterChars + size > CHAR_LIMIT || vals.get(page).size() > 21 || newPage) {
                        newPage = false;
                        --index;
                        size = 0;
                        ++page;
                        vals.add(new ArrayList<>());
                    } else {
                        if (fieldList.get(index) == null) newPage = true;
                        else {
                            size = fieldList.get(index).title.langString.translate(lang, translate).length() + fieldList.get(index).value.langString.translate(lang, translate).length();
                            vals.get(page).add(new ImmutableTriple<>(fieldList.get(index).title.langString.translate(lang, translate), fieldList.get(index).value.langString.translate(lang, translate), fieldList.get(index).inline)
                            );
                        }
                    }
                }
                fieldIndices = new Triple[textVals.length + vals.size() - (embedsOfFront ? 1 : 0)][];
                for (int i = 0; i < textVals.length - (embedsOfFront ? 1 : 0); i++) {
                    fieldIndices[i] = new Triple[0];
                }
                int k = 0;
                for (int i = textVals.length - (embedsOfFront ? 1 : 0); i < fieldIndices.length; ++i, ++k) {
                    fieldIndices[i] = new Triple[vals.get(k).size()];
                    for (int j = 0; j < fieldIndices[i].length; j++) {
                        fieldIndices[i][j] = vals.get(k).get(j);
                    }
                }
            } else {
                fieldIndices = new Triple[textVals.length][];
                Arrays.fill(fieldIndices, new Triple[0]);
            }
            if (this.embed != null) {
                if (this.fieldIndices.length > 1) {
                    this.withReactionBehavior("arrow_left", (add, reaction, user) -> {
                        if (currentPage.get() == 0) return;
                        this.embed.withFooterText(generateNote(currentPage.decrementAndGet()));
                        this.send(currentPage.get());
                    });
                    this.withReactionBehavior("arrow_right", (add, reaction, user) -> {
                        if (currentPage.get() == fieldIndices.length - 1) return;
                        this.embed.withFooterText(generateNote(currentPage.incrementAndGet()));
                        this.send(currentPage.get());
                    });
                }
                this.embed.withFooterText(generateNote(this.currentPage.get()));
            }
        }
    }

    /**
     * Determines what the message note should say dependent on page.
     *
     * @param page the page to consider.
     * @return the content of the message.
     */
    private String generateNote(int page) {
        String note = this.note.langString.translate(lang, translate);
        if (this.fieldIndices.length > 1) {
            if (!note.isEmpty()) note = " - " + note;
            note = "Page " + (page + 1) + " of " + this.fieldIndices.length + note;
        }
        return note;
    }

    /**
     * The wrapping of two {@link LangString}s which wraps a field content.
     */
    public class FieldPart {
        private MessageMaker maker;
        private FieldTextPart title, value;
        private boolean inline;

        private FieldPart(MessageMaker maker) {
            this.maker = maker;
            this.maker.maySend();
            this.maker.fieldList.add(this);
            this.inline = true;
            title = new FieldTextPart(maker, this);
            value = new FieldTextPart(maker, this);
        }

        public FieldPart withInline(boolean inline) {
            this.inline = inline;
            return this;
        }

        /**
         * Gets the {@link FieldTextPart} which wraps the field title.
         *
         * @return the {@link FieldTextPart} which wraps the field title.
         */
        public FieldTextPart getTitle() {
            return this.title;
        }

        /**
         * Gets the {@link FieldTextPart} which wraps the field value.
         *
         * @return the {@link FieldTextPart} which wraps the field value.
         */
        public FieldTextPart getValue() {
            return this.value;
        }

        /**
         * Sets the raw content of the {@link FieldPart}.
         *
         * @param title the raw title content.
         * @param value teh raw value content.
         * @return the instance.
         */
        public FieldPart withBoth(String title, String value) {
            this.title.append(title);
            this.value.append(value);
            return this;
        }

        /**
         * The maker instance this {@link FieldPart} came from.
         *
         * @return maker instance this {@link FieldPart} came from.
         */
        public MessageMaker getMaker() {
            return this.maker;
        }
    }

    /**
     * A wrapping of {@link LangString} which
     * contains a reference to the parent maker.
     */
    public class TextPart {
        private MessageMaker maker;
        LangString langString;
        private boolean appended;
        private TextPart(MessageMaker maker) {
            this.maker = maker;
            this.langString = new LangString();
        }

        /**
         * Appends text that will not be translated to the given field.
         *
         * @param s text that will not be translated to the given field.
         * @return the instance.
         */
        public TextPart appendRaw(String s) {
            this.appended = true;
            this.append(true, s);
            return this;
        }

        /**
         * Appends text that will be translated to the given field.
         *
         * @param s text that will be translated to the given field.
         * @return the instance.
         */
        public TextPart append(String s) {
            this.appended = true;
            this.append(false, s);
            return this;
        }

        /**
         * Appends text that will append raw/not raw in alternating fashion.
         *
         * @param raw if the first text should not be translated.
         * @param s the strings to append in raw/not raw in alternating fashion.
         * @return the instance.
         */
        public TextPart appendAlternate(boolean raw, String...s) {
            this.appended = true;
            this.maker.maySend();
            this.langString.appendToggle(raw, s);
            return this;
        }

        /**
         * Appends content to the given field and translates dependent on raw.
         *
         * @param raw if the text to append should not be translated.
         * @param s the text to append.
         * @return the instance.
         */
        public TextPart append(boolean raw, String s) {
            this.appended = true;
            this.maker.maySend();
            this.langString.append(!raw, s);
            return this;
        }

        /**
         * Appends a {@link LangString} to the content of the represented field.
         *
         * @param langString the lang string to append.
         * @return the instance.
         */
        public TextPart append(LangString langString) {
            this.appended = true;
            this.maker.maySend();
            this.langString.append(langString);
            return this;
        }

        /**
         * Gets the parent maker.
         *
         * @return the parent maker.
         */
        public MessageMaker getMaker() {
            return this.maker;
        }

        /**
         * Clears the content of the maker.
         *
         * @return the instance.
         */
        public TextPart clear() {
            this.langString = new LangString();
            this.appended = false;
            return this;
        }

        /**
         * Gets a translation of the {@link LangString} content.
         *
         * @param langCode the language code to translate the {@link LangString} to.
         * @return a translation of the {@link LangString} content.
         */
        public String translate(String langCode) {
            return this.langString.translate(langCode, translate);
        }
    }

    /**
     * Represents a field value or title for a {@link FieldPart}.
     */
    public class FieldTextPart extends TextPart {
        private FieldPart part;
        private FieldTextPart(MessageMaker helper, FieldPart part) {
            super(helper);
            this.part = part;
        }

        public FieldPart getFieldPart() {
            return this.part;
        }

        public FieldTextPart appendRaw(String s) {
            super.appendRaw(s);
            return this;
        }

        public FieldTextPart append(String s) {
            super.append(s);
            return this;
        }

        public FieldTextPart appendAlternate(boolean raw, String...s) {
            super.appendAlternate(raw, s);
            return this;
        }

        public FieldTextPart append(boolean raw, String s) {
            super.append(!raw, s);
            return this;
        }
    }
}
