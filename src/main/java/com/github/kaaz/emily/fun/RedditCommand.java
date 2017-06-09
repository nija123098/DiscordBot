package com.github.kaaz.emily.fun;

import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.ModuleLevel;
import com.github.kaaz.emily.command.anotations.Command;
import com.github.kaaz.emily.discordobjects.helpers.MessageMaker;
import com.github.kaaz.emily.launcher.Reference;
import com.github.kaaz.emily.util.Rand;
import com.github.kaaz.emily.util.RedditLink;
import ga.dryco.redditjerk.implementation.RedditApi;
import ga.dryco.redditjerk.wrappers.Link;
import ga.dryco.redditjerk.wrappers.Subreddit;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Made by nija123098 on 6/2/2017.
 */
public class RedditCommand extends AbstractCommand {
    static {
        RedditApi.getRedditInstance(Reference.USER_AGENT);
    }
    public RedditCommand() {
        super("reddit", ModuleLevel.FUN, null, null, "Posts something from Reddit");
    }
    @Command
    public void command(String[] args, MessageMaker maker) {
        RedditApi api = RedditApi.getRedditInstance();
        Subreddit subReddit = api.getSubreddit(args.length > 0 ? args[0] : "funny");
        List<Link> list = subReddit.getTop(50).stream().filter(link -> !link.getOver18()).collect(Collectors.toList());
        RedditLink redditLink = new RedditLink(list.get(Rand.getRand(list.size() - 1)));
        if (redditLink.getContent() == null) {
            if (redditLink.getFileType() == null) maker.append(redditLink.getTitle()).appendRaw("\n" + redditLink.getPointerUrl());
            else maker.withFile(redditLink.getFile()).getTitle().append(redditLink.getTitle());
        } else maker.append(redditLink.getTitle()).append(redditLink.getContent());
    }
}
