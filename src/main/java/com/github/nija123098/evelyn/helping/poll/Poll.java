package com.github.nija123098.evelyn.helping.poll;

import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Message;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.exception.ArgumentException;
import com.github.nija123098.evelyn.util.LangString;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.github.nija123098.evelyn.config.ConfigHandler.getSetting;
import static com.github.nija123098.evelyn.config.ConfigHandler.setSetting;
import static com.github.nija123098.evelyn.config.GlobalConfigurable.GLOBAL;
import static com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker.getLang;
import static com.github.nija123098.evelyn.discordobjects.wrappers.event.EventDistributor.register;
import static com.github.nija123098.evelyn.helping.poll.PollIDCountConfig.getNewID;
import static com.github.nija123098.evelyn.launcher.Launcher.registerShutdown;
import static com.github.nija123098.evelyn.service.services.ScheduleService.schedule;
import static com.github.nija123098.evelyn.util.EmoticonHelper.getChars;
import static com.github.nija123098.evelyn.util.LanguageHelper.getInteger;
import static java.lang.System.currentTimeMillis;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class Poll {
    private static final long MAX_POLL_DURATION = 1_209_600_000;// 2 weeks
    private static final long MIN_POLL_DURATION = 60_000;// 1 min
    private static final Map<String, Poll> MESSAGE_ID_MAP = new ConcurrentHashMap<>();
    static final Map<Integer, Poll> POOL_ID_MAP = new ConcurrentHashMap<>();

    public static void load() {
        getSetting(PollStorageConfig.class, GLOBAL).forEach(poll -> MESSAGE_ID_MAP.put(poll.origin.getID(), poll));
        register(Poll.class);
        registerShutdown(() -> setSetting(PollStorageConfig.class, GLOBAL, new HashSet<>(MESSAGE_ID_MAP.values())));
    }

    private Integer pollID;
    private Message origin;
    private String question, lang;
    private String[] options;
    private Map<User, List<Integer>> votes;
    private long expiration;
    private int maxVotes;

    public Poll(String question, String[] options, long expiration, int maxVotes, Message origin) {
        if (options.length < 1) throw new ArgumentException("A poll must have more than two options");
        if (expiration - currentTimeMillis() < MIN_POLL_DURATION)
            throw new ArgumentException("Polls must be open for at least one minute");
        if (expiration - currentTimeMillis() > MAX_POLL_DURATION)
            throw new ArgumentException("That poll time exceeds two weeks");
        this.pollID = getNewID();
        this.question = question;
        this.options = options;
        this.expiration = expiration;
        this.maxVotes = maxVotes;
        this.origin = origin;
        this.lang = getLang(origin.getAuthor(), origin.getChannel());
        this.votes = new ConcurrentHashMap<>();
        setUp();
        MessageMaker maker = new MessageMaker(origin);
        maker.getHeader().append(this.getMessageContent());
        for (int i = 0; i < this.options.length; i++) {
            maker.withPublicReactionBehavior(getInteger(i + 1), (add, reaction, user) -> {
                try {
                    int val = getInteger(reaction.getName());
                    if (add) this.vote(user, val);
                    else this.retractVote(user, val);
                } catch (ArgumentException ignored) {
                }
            });
        }
        maker.send();
    }

    protected Poll() {
        if (this.expiration >= currentTimeMillis()) this.onExpiration();
        else setUp();
    }

    private void setUp() {
        schedule(this.expiration - currentTimeMillis(), this::onExpiration);
        MESSAGE_ID_MAP.put(this.origin.getID(), this);
        POOL_ID_MAP.put(this.pollID, this);
    }

    private int[] votes() {
        int[] votes = new int[this.options.length];
        this.votes.values().forEach(integers -> integers.forEach(integer -> ++votes[integer]));
        return votes;
    }

    public void vote(User user, int option) {
        List<Integer> list = this.votes.computeIfAbsent(user, u -> new CopyOnWriteArrayList<>());
        if (list.size() > this.maxVotes) list.remove(0);
        list.add(option);
        this.origin.edit(this.getMessageContent().translate(this.lang));
    }

    public void retractVote(User user, int option) {
        List<Integer> list = this.votes.get(user);
        if (list == null) return;
        list.remove(option);
        this.origin.edit(this.getMessageContent().translate(this.lang));
    }

    private LangString getMessageContent() {
        LangString string = new LangString();
        string.appendTranslation("Poll by ").appendRaw(this.origin.getAuthor().getDisplayName(this.origin.getGuild()) + "\n**" + this.question + "**\n");
        for (int i = 0; i < this.options.length; i++) {
            string.appendRaw(getChars(getInteger(i + 1), false) + " " + this.options[i] + "\n");
        }
        string.appendTranslation("Poll ID: " + this.pollID);
        return string;
    }

    private void onExpiration() {
        MessageMaker maker = new MessageMaker(this.origin.getAuthor()).append("The results of your poll are in:\n").appendRaw("**" + this.question + "**\n");
        int max = 0;
        int[] votes = this.votes();
        for (int i = 0; i < this.options.length; i++) {
            if (max > votes[i]) max = votes[i];
        }
        ++max;
        int[] option = new int[this.options.length];
        int op = -1;
        for (int i = 0; i < max; i++) {
            for (int j = 0; j < this.options.length; j++) {
                if (votes[j] == i) option[++op] = j;
            }
        }
        maker.append("Your poll has been resolved").appendRaw(":\n**" + this.question + "**");
        for (int i = option.length - 1; i > -1; --i) {
            maker.appendRaw(getChars(getInteger(option[i]), false) + this.options[option[i]] + " ").append("with " + votes[option[i]] + " votes\n");
        }
        maker.append(this.votes.size() + " unique users");
    }
}
