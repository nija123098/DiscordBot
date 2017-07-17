package com.github.kaaz.emily.discordobjects.helpers;

import com.github.kaaz.emily.automoderation.VoiceCommandPrintChannelConfig;
import com.github.kaaz.emily.command.ProcessingHandler;
import com.github.kaaz.emily.config.ConfigHandler;
import com.github.kaaz.emily.config.configs.guild.GuildLanguageConfig;
import com.github.kaaz.emily.config.configs.user.UserLanguageConfig;
import com.github.kaaz.emily.discordobjects.exception.ErrorWrapper;
import com.github.kaaz.emily.discordobjects.helpers.guildaudiomanager.GuildAudioManager;
import com.github.kaaz.emily.discordobjects.wrappers.*;
import com.github.kaaz.emily.exeption.BotException;
import com.github.kaaz.emily.exeption.DevelopmentException;
import com.github.kaaz.emily.launcher.BotConfig;
import com.github.kaaz.emily.service.services.ScheduleService;
import com.github.kaaz.emily.util.EmoticonHelper;
import com.github.kaaz.emily.util.ImageColorHelper;
import com.github.kaaz.emily.util.LangString;
import com.github.kaaz.emily.util.Rand;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.Triple;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.util.EmbedBuilder;
import sx.blah.discord.util.MessageBuilder;

import java.awt.*;
import java.io.File;
import java.time.LocalDateTime;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Made by nija123098 on 4/7/2017.
 */
public class MessageMaker {
    private static final int CHAR_LIMIT = 2000;
    private static final int EMBED_LIMIT = 1000;
    private TextPart authorName, title, header, footer, note, external;
    private List<TextPart> textList = new ArrayList<>();
    private String[] textVals;
    private List<FieldPart> fieldList = new ArrayList<>();
    private Triple<String, String, Boolean>[][] fieldIndices;
    private EmbedBuilder embed = new EmbedBuilder();
    private MessageBuilder builder = new MessageBuilder(DiscordClient.client());
    private String lang;
    private User user;
    private Channel channel;
    private AtomicInteger currentPage = new AtomicInteger();
    private Map<String, ReactionBehavior> reactionBehaviors = new HashMap<>();
    private IMessage message, origin;
    private Message ourMessage;
    private boolean okHand;
    private final Set<String> reactions = new HashSet<>(1);
    private Long deleteDelay;
    private boolean maySend, mustEmbed, forceCompile, autoSend = true;
    private MessageMaker(User user, Channel channel, Message message){
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
    public MessageMaker(Channel origin){
        this(null, origin, null);
    }
    public MessageMaker(User user, Message origin) {
        this(user, origin.getChannel(), origin);
    }
    public MessageMaker(Message message){
        this(message.getAuthor(), message.getChannel(), message);
    }
    public MessageMaker(User user) {
        this(user, user.getOrCreatePMChannel(), null);
    }
    // setup methods
    public MessageMaker clearMessage(){
        this.message = null;
        this.ourMessage = null;
        return this;
    }
    public MessageMaker withAutoSend(boolean autoSend){
        this.autoSend = autoSend;
        return this;
    }
    public MessageMaker forceCompile(){
        this.forceCompile = true;
        return this;
    }
    public MessageMaker asNormalMessage(){
        this.embed = null;
        return this;
    }
    public MessageMaker mustEmbed(){
        return this.mustEmbed(true);
    }
    public MessageMaker mustEmbed(boolean mustEmbed){
        this.mustEmbed = mustEmbed;
        return this;
    }
    public boolean couldNormalize(){
        return !this.mustEmbed && this.fieldList.size() == 0 && this.textList.size() == 0 && !this.authorName.appended && !this.title.appended && !this.footer.appended && !this.note.appended;
    }
    public MessageMaker withChannel(Channel channel){
        ProcessingHandler.swapProcess(this.channel, channel);
        this.channel = channel;
        return this;
    }
    public MessageMaker withReactionBehavior(String reactionName, ReactionBehavior behavior){
        ReactionBehavior.deregisterListener(this.ourMessage, reactionName);
        this.reactionBehaviors.put(reactionName, behavior);
        return this;
    }
    public MessageMaker withoutReactionBehavior(String reactionName){
        if (this.reactionBehaviors.remove(reactionName) != null) ReactionBehavior.deregisterListener(this.ourMessage, reactionName);
        return this;
    }
    public MessageMaker clearReactionBehaviors(){
        this.getReactionBehaved().forEach(this::withoutReactionBehavior);
        return this;
    }
    public MessageMaker withReaction(String name){
        this.reactions.add(EmoticonHelper.getChars(name));
        return this;
    }
    public MessageMaker withDM(){
        return this.withChannel(this.user.getOrCreatePMChannel());
    }
    public MessageMaker withOK(boolean ok){
        this.okHand = ok;
        return this;
    }
    public MessageMaker withOK(){
        this.okHand = true;
        return this;
    }
    public MessageMaker ensureListSize(int size){
        List<TextPart> textList = new ArrayList<>(size);
        textList.addAll(this.textList);
        this.textList = textList;
        return this;
    }
    public MessageMaker ensureFieldSize(int size){
        List<FieldPart> fieldList = new ArrayList<>(size);
        fieldList.addAll(this.fieldList);
        this.fieldList = fieldList;
        return this;
    }
    public MessageMaker withDeleteDelay(Long deleteDelay){
        this.deleteDelay = deleteDelay;
        return this;
    }
    public MessageMaker maySend(boolean maySend){
        this.maySend = maySend;
        return this;
    }
    public MessageMaker maySend(){
        return this.maySend(true);
    }
    // text methods
    public TextPart getAuthorName(){
        return this.authorName;
    }
    public TextPart getTitle(){
        return this.title;
    }
    public TextPart getHeader(){
        return this.header;
    }
    public TextPart getFooter(){
        return this.footer;
    }
    public TextPart getNote(){
        return this.note;
    }
    public TextPart getExternal(){
        return this.external;
    }
    public FieldPart getNewFieldPart(){
        return new FieldPart(this);// adds self in the constructor
    }
    public MessageMaker clearFieldParts(){
        this.fieldList.clear();
        return this;
    }
    public TextPart getNewListPart(){
        TextPart part = new TextPart(this);
        this.textList.add(part);
        return part;
    }
    public MessageMaker appendRaw(String s){
        this.header.appendRaw(s);
        return this;
    }
    public MessageMaker append(String s){
        this.header.append(s);
        return this;
    }
    public MessageMaker appendAlternate(boolean raw, String...s){
        this.header.appendAlternate(raw, s);
        return this;
    }
    public MessageMaker append(boolean raw, String s){
        this.header.append(!raw, s);
        return this;
    }
    public MessageMaker asExceptionMessage(BotException cause) {
        return this.maySend().withColor(Color.RED).getHeader().append(cause.getMessage()).getMaker().getTitle().appendRaw(cause.getClass().getSimpleName()).getMaker();
    }
    // embed methods
    public MessageMaker withColor(Color color){
        this.embed.withColor(color);
        return this;
    }
    public MessageMaker withUserColor(){
        this.embed.withColor(ImageColorHelper.getColor(this.user.getAvatarURL()));
        return this;
    }
    public MessageMaker withRandomColor(){
        this.embed.withColor(Rand.getRand(0xFFFFFF));
        return this;
    }
    public MessageMaker withUserColor(User user){
        this.embed.withColor(ImageColorHelper.getColor(user.getAvatarURL()));
        return this;
    }
    public MessageMaker withFooterIcon(String url){
        this.embed.withFooterIcon(url);
        this.mustEmbed = true;
        return this.maySend();
    }
    public MessageMaker withAuthorIcon(String url){
        this.embed.withAuthorIcon(url);
        this.mustEmbed = true;
        return this.maySend();
    }
    public MessageMaker withUrl(String url){
        if (url != null) this.embed.withUrl(url);
        this.mustEmbed = true;
        return this;
    }
    public MessageMaker withThumb(String url){
        this.embed.withThumbnail(url);
        this.mustEmbed = true;
        return this.maySend();
    }
    public MessageMaker withImage(String url){
        this.embed.withImage(url);
        this.mustEmbed = true;
        return this.maySend();
    }
    public MessageMaker withFile(File file){
        try{this.builder.withFile(file);
        }catch(Exception e) {
            throw new DevelopmentException(e);
        }
        return this.appendRaw("");
    }
    public MessageMaker withTimestamp(LocalDateTime time){
        this.embed.withTimestamp(time);
        this.mustEmbed = true;
        return this;
    }
    public MessageMaker withTimestamp(long millis){
        this.embed.withTimestamp(millis);
        this.mustEmbed = true;
        return this;
    }
    // getting
    public Message sentMessage(){
        return Message.getMessage(this.message);
    }
    public Set<String> getReactionBehaved(){
        return this.reactionBehaviors.keySet();
    }
    // building
    public void send(boolean auto){
        if (!(!this.autoSend && auto)) send();
    }
    public void send(){
        try {
            send(0);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    private void send(int page){
        if (BotConfig.GHOST_MODE) return;
        if (!this.maySend) {
            if (this.origin != null) ErrorWrapper.wrap(() -> this.origin.addReaction(EmoticonHelper.getChars("ok_hand")));
            return;
        }
        if (this.origin != null && this.okHand) ErrorWrapper.wrap(() -> this.origin.addReaction(EmoticonHelper.getChars("ok_hand")));
        this.compile();
        if (this.embed != null){
            if (page < 0 || page >= this.fieldIndices.length) throw new DevelopmentException("Attempted to get a page that doesn't exit");
            this.embed.clearFields().withDesc(this.header.langString.translate(this.lang) + "\n\n" + (page >= textVals.length ? "" : textVals[page]) + "\n\n" + this.footer.langString.translate(lang));
            for (Triple<String, String, Boolean> ind : fieldIndices[page]){
                this.embed.appendField(ind.getLeft(), ind.getMiddle(), ind.getRight());
            }
            this.builder.withEmbed(this.embed.build());
            if (this.external.appended) this.builder.appendContent("\n" + this.external.translate(this.lang));
        }
        if (this.message == null){
            if (this.channel instanceof VoiceChannel){
                GuildAudioManager.getManager(this.channel.getGuild()).interrupt(this.header.langString);
                return;
            }
            this.builder.withChannel(this.channel.channel());
            this.message = ErrorWrapper.wrap((ErrorWrapper.Request<IMessage>) () -> this.builder.send());
            this.ourMessage = Message.getMessage(this.message);
            this.reactions.forEach(this.message::addReaction);
            if (this.deleteDelay != null) ScheduleService.schedule(this.deleteDelay, () -> ErrorWrapper.wrap(this.message::delete));
        } else {
            if (this.embed == null) ErrorWrapper.wrap(() -> this.message.edit(this.builder.getContent()));
            else ErrorWrapper.wrap(() -> this.message.edit(this.embed.build()));
        }
        this.reactionBehaviors.forEach((s, behavior) -> ReactionBehavior.registerListener(this.ourMessage, s, behavior));
    }
    public static String getLang(User user, Channel channel){
        String lang = null;
        if (user != null) lang = ConfigHandler.getSetting(UserLanguageConfig.class, user);
        if (!channel.isPrivate() && lang == null) lang = ConfigHandler.getSetting(GuildLanguageConfig.class, channel.getGuild());
        return lang == null ? "en" : lang;
    }
    private void compile(){
        if (this.lang != null && !this.forceCompile) return;
        // message
        this.lang = getLang(this.user, this.channel);
        if (!this.channel.getModifiedPermissions(DiscordClient.getOurUser()).contains(DiscordPermission.SEND_MESSAGES)){
            if (this.channel.isPrivate()){
                throw new RuntimeException("Could not send message to user due to lacking permissions: " + this.user.getName());
            } else {
                this.channel = this.user.getOrCreatePMChannel();
                this.compile();
                return;
            }
        }
        if (this.couldNormalize()) this.asNormalMessage();
        else if (this.channel instanceof VoiceChannel) this.channel = ConfigHandler.getSetting(VoiceCommandPrintChannelConfig.class, this.channel.getGuild());
        if (this.embed == null) this.builder.withContent(this.header.langString.translate(this.lang));
        else {// embed
            this.embed.withAuthorName(authorName.langString.translate(lang));
            this.embed.withTitle(title.langString.translate(lang));
            int starterChars = this.embed.getTotalVisibleCharacters() + header.langString.translate(lang).length() + footer.langString.translate(lang).length();
            if (CHAR_LIMIT < starterChars){
                throw new RuntimeException("Header and footer are too big.");
            }
            if (textList.size() != 0){
                int index = -1;
                String s = "";
                List<String> strings = new ArrayList<>();
                while (true) {
                    if (++index >= textList.size()){
                        strings.add(s);
                        break;
                    }
                    if (starterChars + s.length() + textList.get(index).langString.translate(lang).length() > CHAR_LIMIT){
                        --index;
                        strings.add(s);
                        s = "";
                    } else {
                        s += "\n" + textList.get(index).langString.translate(lang);
                    }
                }
                textVals = new String[strings.size()];
                for (int i = 0; i < strings.size(); i++) {
                    textVals[i] = strings.get(i);
                }
            } else {
                textVals = new String[]{""};
            }
            if (fieldList.size() != 0){
                boolean embedsOfFront = this.embed.getTotalVisibleCharacters() + (this.textVals.length > 0 ? this.textVals[0].length() : 0) < EMBED_LIMIT;
                int index = -1, size = 0, page = 0;
                List<List<Triple<String, String, Boolean>>> vals = new ArrayList<>(this.fieldList.size());
                vals.add(new ArrayList<>());
                while (true) {
                    if (++index >= fieldList.size()){
                        break;
                    }
                    if (starterChars + size > CHAR_LIMIT || vals.get(page).size() > 21){
                        --index;
                        size = 0;
                        ++page;
                        vals.add(new ArrayList<>());
                    } else {
                        size = fieldList.get(index).title.langString.translate(lang).length() + fieldList.get(index).value.langString.translate(lang).length();
                        vals.get(page).add(new ImmutableTriple<>(fieldList.get(index).title.langString.translate(lang), fieldList.get(index).value.langString.translate(lang), fieldList.get(index).inline));
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
            if (this.embed != null && this.fieldIndices.length > 1){
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
                this.embed.withFooterText(generateNote(0));
            }
        }
    }
    private String generateNote(int page){
        String note = this.note.langString.translate(lang);
        if (note.length() != 0) note += " - ";
        return "Page " + ++page + " of " + this.fieldIndices.length + note;
    }
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

        public FieldPart withInline(boolean inline){
            this.inline = inline;
            return this;
        }

        public FieldTextPart getTitle(){
            return this.title;
        }
        public FieldTextPart getValue(){
            return this.value;
        }
        public FieldPart withBoth(String title, String value){
            this.title.append(title);
            this.value.append(value);
            return this;
        }
        public MessageMaker getMessageProducer(){
            return this.maker;
        }
    }
    public class TextPart {
        private MessageMaker maker;
        public LangString langString;
        private boolean appended;
        private TextPart(MessageMaker maker) {
            this.maker = maker;
            this.langString = new LangString();
        }
        public boolean isAppended(){
            return this.appended;
        }
        public TextPart appendRaw(String s){
            this.appended = true;
            this.append(true, s);
            return this;
        }
        public TextPart append(String s){
            this.appended = true;
            this.append(false, s);
            return this;
        }
        public TextPart appendAlternate(boolean raw, String...s){
            this.appended = true;
            this.maker.maySend();
            this.langString.appendToggle(raw, s);
            return this;
        }
        public TextPart append(boolean raw, String s){
            this.appended = true;
            this.maker.maySend();
            this.langString.append(!raw, s);
            return this;
        }
        public TextPart append(LangString langString){
            this.appended = true;
            this.maker.maySend();
            this.langString.append(langString);
            return this;
        }
        public MessageMaker getMaker(){
            return this.maker;
        }
        public TextPart clear() {
            this.langString = new LangString();
            this.appended = false;
            return this;
        }
        public String translate(String langCode) {
            return this.langString.translate(langCode);
        }
    }
    public class FieldTextPart extends TextPart {
        private FieldPart part;
        private FieldTextPart(MessageMaker helper, FieldPart part) {
            super(helper);
            this.part = part;
        }

        public FieldPart getFieldPart(){
            return this.part;
        }

        public FieldTextPart appendRaw(String s){
            super.appendRaw(s);
            return this;
        }

        public FieldTextPart append(String s){
            super.append(s);
            return this;
        }

        public FieldTextPart appendAlternate(boolean raw, String...s){
            super.appendAlternate(raw, s);
            return this;
        }

        public FieldTextPart append(boolean raw, String s){
            super.append(!raw, s);
            return this;
        }
    }
}
