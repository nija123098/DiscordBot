package com.github.kaaz.emily.discordobjects.helpers;

import com.github.kaaz.emily.discordobjects.exception.ErrorWrapper;
import com.github.kaaz.emily.discordobjects.exception.MissingPermException;
import com.github.kaaz.emily.discordobjects.wrappers.Channel;
import com.github.kaaz.emily.discordobjects.wrappers.DiscordClient;
import com.github.kaaz.emily.discordobjects.wrappers.User;
import com.github.kaaz.emily.util.LangString;
import sx.blah.discord.util.MessageBuilder;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Made by nija123098 on 3/10/2017.
 */
public class MessageHelper {
    private LangString message = new LangString();
    private User user;
    private Channel channel;
    private boolean dmCheck, tts, forceTranslate;
    private File file;
    public MessageHelper(User user, Channel channel) {
        this.user = user;
        this.channel = channel;
    }
    public MessageHelper append(boolean translate, String content){
        message.append(translate, content);
        return this;
    }
    public MessageHelper appendTranslation(String content){
        message.appendTranslation(content);
        return this;
    }
    public MessageHelper appendRaw(String content){
        message.appendRaw(content);
        return this;
    }
    public MessageHelper appendToggle(boolean rawFirst, String...content){
        for (String c : content) {
            rawFirst = !rawFirst;
            message.append(rawFirst, c);
        }
        return this;
    }
    public MessageHelper withDMCheck(){
        this.dmCheck = true;
        this.withDM();
        return this;
    }
    public MessageHelper withDM(){
        this.channel = this.user.getOrCreatePMChannel();
        return this;
    }
    public MessageHelper withTTS(){
        this.tts = true;
        return this;
    }
    public MessageHelper withFile(File file){
        this.file = file;
        return this;
    }
    public void send(){
        if (this.dmCheck){
            ErrorWrapper.wrap(() -> new MessageBuilder(DiscordClient.client()).withChannel(this.channel.channel()).withContent(this.user.mention() + " check your DMs").send());
        }
        try {
            String message = this.message.asBuilt();// todo translate
            MessageBuilder builder = new MessageBuilder(DiscordClient.client()).withChannel(this.channel.channel()).withContent(message);
            if (file != null) {
                try {
                    builder.withFile(this.file);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
            if (tts) {
                builder.withTTS();
            }
            ErrorWrapper.wrap(builder::send);
        } catch (MissingPermException e){
            if (!this.channel.isPrivate()){
                this.withDM();
            }
        }
    }
}