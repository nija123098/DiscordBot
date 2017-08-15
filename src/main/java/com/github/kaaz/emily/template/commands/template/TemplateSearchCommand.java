package com.github.kaaz.emily.template.commands.template;

import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.annotations.Command;
import com.github.kaaz.emily.discordobjects.helpers.MessageMaker;
import com.github.kaaz.emily.template.KeyPhrase;
import com.github.kaaz.emily.util.StringHelper;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Made by nija123098 on 8/8/2017.
 */
public class TemplateSearchCommand extends AbstractCommand {
    public TemplateSearchCommand() {
        super(TemplateCommand.class, "search", null, null, null, "Searches for template key phrases closest to your input");
    }
    @Command
    public void command(MessageMaker maker, String s){// won't use enum identification here due to requiring a brauder search
        List<String> results = StringHelper.getGoodMatch(s, Stream.of(KeyPhrase.values()).map(KeyPhrase::name).collect(Collectors.toList()), StringUtils::getLevenshteinDistance, true, true);
        if (results.isEmpty()) {
            maker.append("I couldn't find anything matching that!  Your options are as follows:");
            TemplateListCommand.command(maker);
        } else if (results.size() == 1) maker.append("You are looking for ").appendRaw(results.get(0));
        else {
            maker.append("You may be looking for one of these, all options are listed at `@Emily template list`");
            results.forEach(s1 -> maker.getNewFieldPart().withBoth(s1, ""));
        }
    }
}
