package com.github.nija123098.evelyn.template.commands.template;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.template.KeyPhrase;
import com.github.nija123098.evelyn.util.StringHelper;
import org.apache.commons.text.similarity.LevenshteinDistance;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class TemplateSearchCommand extends AbstractCommand {
    public TemplateSearchCommand() {
        super(TemplateCommand.class, "search", null, null, null, "Searches for template key phrases closest to your input");
    }
    @Command
    public void command(MessageMaker maker, String s) {// won't use enum identification here due to requiring a broad search
        List<String> results = StringHelper.getGoodMatch(s, Stream.of(KeyPhrase.values()).map(KeyPhrase::name).collect(Collectors.toList()), LevenshteinDistance.getDefaultInstance()::apply, true, true);
        if (results.isEmpty()) {
            maker.append("I couldn't find anything matching that!  Your options are as follows:");
            TemplateListCommand.command(maker);
        } else if (results.size() == 1) maker.append("You are looking for ").appendRaw(results.get(0));
        else {
            maker.append("You may be looking for one of these, all options are listed at `@Evelyn template list`");
            results.forEach(s1 -> maker.getNewFieldPart().withBoth(s1, ""));
        }
    }
}
