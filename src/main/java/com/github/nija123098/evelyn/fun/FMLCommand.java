package com.github.nija123098.evelyn.fun;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Channel;
import com.github.nija123098.evelyn.exception.ContextException;
import com.github.nija123098.evelyn.exception.DevelopmentException;
import com.github.nija123098.evelyn.service.services.ScheduleService;
import org.apache.commons.lang3.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class FMLCommand extends AbstractCommand {
	private final List<String> items = new ArrayList<>();
	public FMLCommand() {
		super("fml", ModuleLevel.FUN, "fmylife", null, "fmylife! Returns a random entry from fmylife.com");
		getItems();
	}
	@Command
	public void command(MessageMaker maker, Channel channel) {
		if (this.items.size() < 10) ScheduleService.schedule(5, this::getItems);
		if (this.items.isEmpty()) {
			maker.append("There are no more fmls at this time, check back later");
			return;
		}
		if (channel.isNSFW()) {
			maker.append(this.items.remove(0));
		} else {
			throw new ContextException("This command needs to be used within an NSFW channel due to its content.");
		}
	}
	private void getItems(){
		try {
			Document document = Jsoup.connect("http://fmylife.com/random").timeout(30_000).userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.71 Safari/537.36").get();
			if (document != null) items.addAll(document.select("p.block a[href^=/article/]").stream().map(Element::text).map(String::trim).map(s -> s.length() > 2000 ? s.substring(0, 1999) : s).filter(s -> s.endsWith("FML")).map(StringEscapeUtils::unescapeHtml4).collect(Collectors.toSet()));
		} catch (IOException e) {
			throw new DevelopmentException(e);
		}
	}
}
