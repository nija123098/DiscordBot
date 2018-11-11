package com.github.nija123098.evelyn.fun;

import com.github.nija123098.evelyn.botconfiguration.ConfigProvider;
import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Channel;
import com.github.nija123098.evelyn.exception.ContextException;
import com.github.nija123098.evelyn.exception.DevelopmentException;
import com.github.nija123098.evelyn.util.FormatHelper;
import com.github.nija123098.evelyn.util.ThreadHelper;
import org.apache.commons.text.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class FMLCommand extends AbstractCommand {
    private static final ExecutorService EXECUTOR_SERVICE = Executors.newSingleThreadExecutor(r -> ThreadHelper.getDemonThreadSingle(r, "FML-Getter-Thread"));
    private final List<String> items = new ArrayList<>();
    public FMLCommand() {
        super("fml", ModuleLevel.FUN, "fmylife", null, "Gets a random entry from fmylife.com");
        getItems();
    }
    @Command
    public void command(MessageMaker maker, Channel channel) {
        if (channel.isPrivate()) {
            //order important since isNSFW throws Unsupported Action Exception on DMs
        } else if (!channel.isNSFW()) {
            throw new ContextException("This command needs to be used within an NSFW channel or DM due to its possible content.");
        }
        if (this.items.size() < 10) EXECUTOR_SERVICE.submit(this::getItems);
        if (this.items.isEmpty()) {
            maker.append("There are no more fmls at this time, please check back later");
            return;
        }
        maker.append(this.items.remove(0));
    }
    private void getItems() {
        try {
            Document document = Jsoup.connect("http://fmylife.com/random").timeout(30_000).userAgent(ConfigProvider.BOT_SETTINGS.userAgent()).get();
            if (document != null) items.addAll(document.select("p.block a[href^=/article/]").stream().map(Element::text).map(String::trim).map(s -> FormatHelper.limitLength(s, 2000)).filter(s -> s.endsWith("FML")).map(StringEscapeUtils::unescapeHtml4).collect(Collectors.toSet()));
            else throw new DevelopmentException("Could not load FML items");
        } catch (IOException e) {
            throw new DevelopmentException(e);
        }
    }
}
