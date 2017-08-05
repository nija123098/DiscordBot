package com.github.kaaz.emily.discordobjects.wrappers;

import com.github.kaaz.emily.discordobjects.exception.ErrorWrapper;
import com.github.kaaz.emily.exeption.GhostException;
import com.github.kaaz.emily.launcher.BotConfig;
import com.github.kaaz.emily.service.services.MemoryManagementService;
import com.github.kaaz.emily.util.EmoticonHelper;
import sx.blah.discord.handle.impl.obj.ReactionEmoji;
import sx.blah.discord.handle.obj.IMessage;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Made by nija123098 on 3/4/2017.
 */
public class Message {// should not be kept stored, too many are made
    private static final Map<String, Message> MAP = new MemoryManagementService.ManagedMap<>(120000);
    public static Message getMessage(IMessage iMessage){
        if (iMessage == null) return null;
        return MAP.computeIfAbsent(iMessage.getStringID(), s -> new Message(iMessage));
    }
    public static Message getMessage(String id){
        try {
            return getMessage(DiscordClient.client().getMessageByID(id));
        } catch (NumberFormatException e) {
            return null;
        }
    }
    static List<Message> getMessages(List<IMessage> iMessages){
        List<Message> messages = new ArrayList<>(iMessages.size());
        iMessages.forEach(iMessage -> messages.add(getMessage(iMessage)));
        return messages;
    }
    static List<IMessage> getIMessages(List<Message> messages){
        List<IMessage> iMessages = new ArrayList<>(messages.size());
        messages.forEach(message -> iMessages.add(message.message()));
        return iMessages;
    }
    private final IMessage iMessage;
    private Message(IMessage message){
        iMessage = message;
    }
    public IMessage message() {
        return iMessage;
    }

    @Override
    public boolean equals(Object o) {
        return o == this || o instanceof Message && ((Message) o).getID().equals(this.getID());
    }

    @Override
    public int hashCode() {
        return this.getID().hashCode();
    }

    public String getContent() {
        return message().getContent();
    }

    public Channel getChannel() {
        return Channel.getChannel(message().getChannel());
    }

    public User getAuthor() {
        return User.getUser(message().getAuthor());
    }

    public LocalDateTime getTimestamp() {
        return message().getTimestamp();
    }

    public List<User> getMentions() {
        return User.getUsers(message().getMentions());
    }

    public List<Role> getRoleMentions() {
        return Role.getRoles(message().getRoleMentions());
    }

    public List<Channel> getChannelMentions() {
        return Channel.getChannels(message().getChannelMentions());
    }

    public List<Attachment> getAttachments() {
        return Attachment.getAttachments(message().getAttachments());
    }

    public IMessage edit(String s) {
        return ErrorWrapper.wrap((ErrorWrapper.Request<IMessage>)() -> message().edit(s));
    }

    public boolean mentionsEveryone() {
        return message().mentionsEveryone();
    }

    public boolean mentionsHere() {
        return message().mentionsHere();
    }

    public void delete() {
        if (BotConfig.GHOST_MODE) return;
        ErrorWrapper.wrap(() -> message().delete());
    }

    public Optional<LocalDateTime> getEditedTimestamp() {
        return message().getEditedTimestamp();
    }

    public boolean isPinned() {
        return message().isPinned();
    }

    public Guild getGuild() {
        return Guild.getGuild(message().getGuild());
    }

    public String getFormattedContent() {
        return message().getFormattedContent();
    }

    public List<Reaction> getReactions() {
        return Reaction.getReactions(message().getReactions());
    }

    public Reaction getReaction(String s){
        return Reaction.getReaction(message().getReactionByUnicode(s));
    }

    public Reaction getReactionByName(String name){
        return getReaction(EmoticonHelper.getChars(name, false));
    }

    public Reaction addReaction(String s) {
        if (BotConfig.GHOST_MODE) throw new GhostException();
        AtomicReference<String> reference = new AtomicReference<>(s);
        if (s.endsWith("\u200B")) reference.set(s.substring(0, s.length() - 1));
        ErrorWrapper.wrap(() -> message().addReaction(ReactionEmoji.of(reference.get())));
        return getReaction(s);
    }

    public Reaction addReactionByName(String name) {
        return addReaction(EmoticonHelper.getChars(name, false));
    }

    public void removeReaction(Reaction reaction) {
        ErrorWrapper.wrap(() -> message().removeReaction(DiscordClient.getOurUser().user(), reaction.reaction()));
    }

    public void removeReaction(String s) {
        ErrorWrapper.wrap(() -> message().removeReaction(DiscordClient.getOurUser().user(), getReaction(s).reaction()));
    }

    public void removeReactionByName(String name) {
        removeReaction(EmoticonHelper.getChars(name, false));
    }

    public boolean isDeleted() {
        return message().isDeleted();
    }

    public String getID() {
        return message().getStringID();
    }

    public Shard getShard() {
        return Shard.getShard(message().getShard());
    }

    public LocalDateTime getCreationDate() {
        return message().getCreationDate();
    }
}
