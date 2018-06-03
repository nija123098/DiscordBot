package com.github.nija123098.evelyn.moderation;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ContextType;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.*;
import com.github.nija123098.evelyn.exception.ContextException;
import com.github.nija123098.evelyn.exception.DevelopmentException;
import com.github.nija123098.evelyn.exception.PermissionsException;
import com.github.nija123098.evelyn.perms.BotRole;
import com.github.nija123098.evelyn.util.FileHelper;
import com.github.nija123098.evelyn.util.Log;
import com.github.nija123098.evelyn.util.Time;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class ArchiveCommand extends AbstractCommand {
    private static final EnumSet<DiscordPermission> USER_PERMISSIONS_REQUIRED = EnumSet.of(DiscordPermission.READ_MESSAGES, DiscordPermission.READ_MESSAGE_HISTORY);

    public ArchiveCommand() {
        super("archive", BotRole.USER, ModuleLevel.ADMINISTRATIVE, null, null, "Archives all data to a txt file");
    }

    @Command
    public void command(@Argument(optional = true, replacement = ContextType.NONE) Time time, Channel channel, @Argument(optional = true, info = "timezone") ZoneId zoneId, String linux, MessageMaker maker, User user) {
        if (channel.isPrivate()) throw new ContextException("You must use this command in a server");
        if (!channel.getModifiedPermissions(user).containsAll(USER_PERMISSIONS_REQUIRED)) {
            throw new PermissionsException("You do not have read access to that channel");
        }
        List<Message> messages = time == null ? channel.getMessages() : channel.getMessagesTo(System.currentTimeMillis() - time.timeUntil());
        Collections.reverse(messages);
        Charset charset = Charset.forName("UTF-8");
        File file = FileHelper.getTempFile("archive", "txt");// may want to put to hastebin
        boolean nEnding = linux.equalsIgnoreCase("linux") || linux.equalsIgnoreCase("unix");
        String ending = nEnding ? "\n" : "\r\n";
        try {
            OutputStream outputStream = new FileOutputStream(file, true);
            outputStream.write(("ARCHIVE for channel " + channel.getName() + " ID: " + channel.getID()).getBytes(charset));
            messages.forEach(message -> {// write messages to file
                StringBuilder builder = new StringBuilder(message.getContent().length() + 200);
                builder.append(ending).append("ID: ").append(message.getID())
                        .append(" TIME: ").append(message.getCreationDate().atZone(zoneId).format(DateTimeFormatter.ISO_DATE_TIME))
                        .append(" COM_TIME: ").append(message.getCreationDate().toEpochMilli())
                        .append(" AUTHOR: ").append(message.getAuthor().getNameAndDiscrim())
                        .append(" AUTHOR_ID: ").append(message.getAuthor().getID());
                message.getAttachments().forEach(attachment -> builder.append(" ATTACHMENT: ").append(attachment.getUrl()));
                builder.append(" MESSAGE:").append(ending).append(nEnding ? message.getContent() : message.getContent().replace("\n", ending));
                try {
                    outputStream.write(builder.toString().getBytes(charset));
                } catch (IOException e) {
                    throw new DevelopmentException("Unable to write messages to file", e);
                }
            });
        } catch (IOException e) {
            Log.log("Unable to write to file", e);
        }
        maker.withFile(file);
    }
}
