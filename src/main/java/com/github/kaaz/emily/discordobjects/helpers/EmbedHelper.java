package com.github.kaaz.emily.discordobjects.helpers;

import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.util.EmbedBuilder;

import java.awt.*;
import java.time.LocalDateTime;

/**
 * Made by nija123098 on 3/23/2017.
 */
public class EmbedHelper {
    private MessageHelper helper;
    private EmbedBuilder builder = new EmbedBuilder();
    public EmbedHelper(MessageHelper helper) {
        this.helper = helper;
    }
    public MessageHelper getMessageHelper(){
        return this.helper;
    }
    public EmbedHelper setLenient(boolean lenient) {
        this.builder.setLenient(lenient);
        return this;
    }
    public EmbedHelper withTitle(String title) {
        this.builder.withTitle(title);
        return this;
    }
    public EmbedHelper withDescription(String desc) {
        this.builder.appendDescription(desc);
        return this;
    }
    public EmbedHelper withTimestamp(LocalDateTime ldt) {
        this.builder.withTimestamp(ldt);
        return this;
    }
    public EmbedHelper withTimestamp(long millis) {
        this.builder.withTimestamp(millis);
        return this;
    }
    public EmbedHelper withColor(Color color) {
        this.builder.withColor(color);
        return this;
    }
    public EmbedHelper withFooterText(String footer) {
        this.builder.withFooterText(footer);
        return this;
    }
    public EmbedHelper withImage(String imageUrl) {
        this.builder.withImage(imageUrl);
        return this;
    }
    public EmbedHelper withThumbnail(String url) {
        this.builder.withThumbnail(url);
        return this;
    }
    public EmbedHelper withAuthorIcon(String url) {
        this.builder.withAuthorIcon(url);
        return this;
    }
    public EmbedHelper withAuthorName(String name) {
        this.builder.withAuthorName(name);
        return this;
    }
    public EmbedHelper withAuthorUrl(String url) {
        this.builder.withAuthorUrl(url);
        return this;
    }
    public EmbedHelper withUrl(String url) {
        this.builder.withUrl(url);
        return this;
    }
    public int getFieldCount() {
        return this.builder.getFieldCount();
    }
    public EmbedObject build() {
        return this.builder.build();
    }
}
