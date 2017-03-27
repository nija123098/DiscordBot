package com.github.kaaz.emily.discordobjects.wrappers;

import com.github.kaaz.emily.discordobjects.exception.ErrorWrapper;
import com.github.kaaz.emily.service.services.MemoryManagementService;
import sx.blah.discord.handle.obj.IMessage;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Made by nija123098 on 3/4/2017.
 */
public class Message {// should not be kept stored, too many are made
    private static final Map<String, Message> MAP = new MemoryManagementService.ManagedMap<>(120000);
    public static Message getMessage(IMessage iMessage){
        return MAP.computeIfAbsent(iMessage.getID(), s -> new Message(iMessage));
    }
    static Message getMessage(String id){
        return getMessage(DiscordClient.client().getMessageByID(id));
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
    IMessage message() {
        return iMessage;
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
        return Reaction.getReaction(message().getReactionByName(s));
    }

    public Reaction addReaction(String s) {
        ErrorWrapper.wrap(() -> message().addReaction(s));
        return getReaction(s);
    }

    public void removeReaction(Reaction reaction) {
        ErrorWrapper.wrap(() -> message().removeReaction(reaction.reaction()));
    }

    public void removeReaction(String s) {
        ErrorWrapper.wrap(() -> message().removeReaction(getReaction(s).reaction()));
    }

    public boolean isDeleted() {
        return message().isDeleted();
    }

    public String getID() {
        return message().getID();
    }

    public Shard getShard() {
        return Shard.getShard(message().getShard());
    }

    public LocalDateTime getCreationDate() {
        return message().getCreationDate();
    }
}
