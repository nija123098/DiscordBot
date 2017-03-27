package com.github.kaaz.emily.discordobjects.wrappers;

import sx.blah.discord.handle.obj.IMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * Made by nija123098 on 3/25/2017.
 */
public class Attachment {// probably don't want to store this at all
    static List<Attachment> getAttachments(List<IMessage.Attachment> attachments){
        List<Attachment> attaches = new ArrayList<>();
        attachments.forEach(attachment -> attaches.add(getAttachment(attachment)));
        return attaches;
    }
    static Attachment getAttachment(IMessage.Attachment attachment){
        return new Attachment(attachment);
    }
    private IMessage.Attachment attachment;
    private Attachment(IMessage.Attachment attachment) {
        this.attachment = attachment;
    }
    public String getFilename() {
        return this.attachment.getFilename();
    }

    public int getFilesize() {
        return this.attachment.getFilesize();
    }

    public String getId() {
        return this.attachment.getId();
    }

    public String getUrl() {
        return this.attachment.getUrl();
    }
}
