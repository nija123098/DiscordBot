package com.github.nija123098.evelyn.fun;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.exception.ArgumentException;
import com.github.nija123098.evelyn.util.Rand;
import com.github.nija123098.evelyn.util.RedditLink;
import ga.dryco.redditjerk.implementation.RedditApi;
import ga.dryco.redditjerk.wrappers.Link;
import ga.dryco.redditjerk.wrappers.Subreddit;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class RedditCommand extends AbstractCommand {
    static {
        RedditApi.getRedditInstance("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.71 Safari/537.36");
    }
    public RedditCommand() {
        super("reddit", ModuleLevel.FUN, null, null, "Posts something from Reddit");
    }
    @Command
    public static void command(String[] args, MessageMaker maker) {
        RedditApi api = RedditApi.getRedditInstance();
        Subreddit subReddit = api.getSubreddit(args.length > 0 ? args[0] : "funny");
        List<Link> list = subReddit.getTop(50).stream().filter(link -> !link.getOver18()).collect(Collectors.toList());
        if (list.isEmpty()) throw new ArgumentException("That doesn't seem to be a valid subreddit");
        RedditLink redditLink;
        while (!(redditLink = new RedditLink(Rand.getRand(list, false))).getLinkApproved() && redditLink.getLinkApproved() && redditLink.getFileURL().endsWith(".figv")){}
        if (redditLink.getContent() == null) {
            if (redditLink.getFileType() == null) maker.append(redditLink.getTitle()).appendRaw("\n" + redditLink.getPointerUrl());
            else maker.withImage(redditLink.getFileURL()).getTitle().append(redditLink.getTitle());
        } else {
            String s = redditLink.getContent();
            if (s.length() > 1500) s = s.substring(0, 1497) + "...";
            maker.getTitle().append(redditLink.getTitle()).getMaker().append(s);
        }
        maker.withUrl(redditLink.getPointerUrl());
    }

    @Override
    protected String getLocalUsages() {
        return "#  reddit <search> // Get a random post from reddit based on <search>";
    }
}
