package com.github.nija123098.evelyn.moderation;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ContextType;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.*;
import com.github.nija123098.evelyn.exception.ArgumentException;
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
import java.time.Instant;
import java.time.LocalDate;
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
        super("archive", BotRole.USER, ModuleLevel.ADMINISTRATIVE, null, null, "Archives all channel messages to a txt file");
    }

    @Command
    public void command(@Argument(optional = true, replacement = ContextType.NONE) Time time, Channel channel, Guild guild, @Argument(optional = true, info = "timezone") ZoneId zoneId, @Argument(optional = true, info = "send dm", replacement = ContextType.NONE) Boolean sendDm, @Argument(optional = true, info = "detailed") String string, String linux, MessageMaker maker, User user) {
        boolean detailed = false;
        if (channel.isPrivate()) throw new ContextException("You must use this command in a server");
        if (!channel.getModifiedPermissions(user).containsAll(USER_PERMISSIONS_REQUIRED)) {
            throw new PermissionsException("You do not have read access to that channel");
        }
        List<Message> messages = time == null ? channel.getMessages() : channel.getMessagesTo(System.currentTimeMillis() - time.timeUntil());
        Collections.reverse(messages);
        Charset charset = Charset.forName("UTF-8");

        //create file
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        File file = FileHelper.getTempFile("archive","txt",channel.getName() + "-" + guild.getName() + "-" + date);

        boolean nEnding = linux.equalsIgnoreCase("linux") || linux.equalsIgnoreCase("unix");
        String ending = nEnding ? "\n" : "\r\n";
        try {
            if (!string.isEmpty()) {
                if (string.equalsIgnoreCase("detailed")) {
                    detailed = true;
                } else {
                    throw new ArgumentException("type out `detailed` to create a detailed report with message ids");
                }
            }
            OutputStream outputStream = new FileOutputStream(file, true);
            outputStream.write(("Archive for channel #" + channel.getName() + " ID: " + channel.getID()).getBytes(charset));
            outputStream.write(("\nGuild: " + guild.getName() +  " ID: " + guild.getID()).getBytes(charset));
            outputStream.write(("\nReport taken: " + Instant.now().atZone(zoneId).format(DateTimeFormatter.ISO_DATE_TIME) + "\n").getBytes(charset));
            boolean finalDetailed = detailed;
            messages.forEach(message -> {// write messages to file
                StringBuilder builder = new StringBuilder(message.getContent().length() + 200);
                if (finalDetailed) {
                    builder.append(ending).append(message.getAuthor().getNameAndDiscrim())
                            .append(" user_id: ").append(message.getID())
                            .append("\ntime: ").append(message.getCreationDate().atZone(zoneId).format(DateTimeFormatter.ISO_DATE_TIME))
                            .append(" message_id: ").append(message.getAuthor().getID());
                } else {
                    builder.append(ending).append(message.getAuthor().getNameAndDiscrim())
                            .append(" at: ").append(message.getCreationDate().atZone(zoneId).format(DateTimeFormatter.ISO_DATE_TIME));
                }
                message.getAttachments().forEach(attachment -> builder.append("\nATTACHMENT: ").append(attachment.getUrl()));
                builder.append("\nMESSAGE:").append(ending).append(nEnding ? message.getContent() : message.getContent().replace("\n", ending)).append("\n");
                try {
                    outputStream.write(builder.toString().getBytes(charset));
                } catch (IOException e) {
                    throw new DevelopmentException("Unable to write messages to file", e);
                }
            });
        } catch (IOException e) {
            Log.log("Unable to write to file", e);
        }
        if (sendDm) {
            new MessageMaker(user.getOrCreatePMChannel()).shouldEmbed(false).withFile(file).send();
        } else {
            maker.withFile(file).send();
        }
    }
}

