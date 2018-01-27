package com.github.nija123098.evelyn.moderation;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Channel;
import com.github.nija123098.evelyn.discordobjects.wrappers.Message;
import com.github.nija123098.evelyn.exception.DevelopmentException;
import com.github.nija123098.evelyn.util.Time;

import java.io.File;
import java.io.IOException;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import static com.github.nija123098.evelyn.command.ModuleLevel.ADMINISTRATIVE;
import static com.github.nija123098.evelyn.perms.BotRole.USER;
import static com.github.nija123098.evelyn.util.FileHelper.getTempFile;
import static com.google.common.collect.Lists.reverse;
import static java.lang.System.currentTimeMillis;
import static java.nio.file.Files.write;
import static java.nio.file.Paths.get;
import static java.time.format.DateTimeFormatter.ISO_DATE_TIME;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class ArchiveCommand extends AbstractCommand {
    private static final String NEW_LINE_SPLITTER = "     ";

    public ArchiveCommand() {
        super("archive", USER, ADMINISTRATIVE, null, null, "Archives all data to a txt file");
    }

    @Command
    public void command(@Argument Time time, MessageMaker maker, Channel channel, ZoneId zoneId) {
        List<Message> messages = channel.getMessagesTo(time == null ? 0 : currentTimeMillis() - time.timeUntil());
        List<String> strings = new ArrayList<>(messages.size());
        messages.forEach(message -> {
            String content = message.getContent();
            while (content.contains("\n")) content = content.replace("\n", NEW_LINE_SPLITTER);
            strings.add(message.getCreationDate().atZone(zoneId).format(ISO_DATE_TIME).replace("T", " | ") + " " + message.getAuthor().getNameAndDiscrim() + ": " + content);
        });
        try {
            File file = getTempFile("archive", "txt");// may want to put to hastebin
            write(get(file.getPath()), reverse(strings));
            maker.withFile(file);
        } catch (IOException e) {
            throw new DevelopmentException(e);
        }
    }
}
