package com.github.nija123098.evelyn.discordobjects.wrappers;

import sx.blah.discord.handle.obj.IMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Wraps a Discord4J {@link IMessage.Attachment} object.
 *
 * @author nija123098
 * @since 1.0.0
 */
public class Attachment {// probably don't want to store this at all
    static List<Attachment> getAttachments(List<IMessage.Attachment> attachments) {
        List<Attachment> attaches = new ArrayList<>();
        attachments.forEach(attachment -> attaches.add(getAttachment(attachment)));
        return attaches;
    }
    private static Attachment getAttachment(IMessage.Attachment attachment) {
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
        return this.attachment.getStringID();
    }

    public String getUrl() {
        return this.attachment.getUrl();
    }

    public boolean equals(Object o) {
        return o instanceof Attachment && Objects.equals(((Attachment) o).getId(), this.getId());
    }
}
