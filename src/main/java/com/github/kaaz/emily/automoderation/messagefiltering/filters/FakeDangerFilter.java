package com.github.kaaz.emily.automoderation.messagefiltering.filters;

import com.github.kaaz.emily.automoderation.messagefiltering.MessageFilter;
import com.github.kaaz.emily.automoderation.messagefiltering.MessageMonitoringType;
import com.github.kaaz.emily.discordobjects.wrappers.event.events.DiscordMessageReceived;
import com.github.kaaz.emily.launcher.BotConfig;
import com.github.kaaz.emily.util.FormatHelper;
import com.github.kaaz.emily.util.Log;
import com.github.kaaz.emily.util.StringChecker;
import com.github.kaaz.emily.util.StringIterator;
import com.google.common.base.Joiner;
import com.google.common.util.concurrent.AtomicDouble;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Made by nija123098 on 8/10/2017.
 */
public class FakeDangerFilter implements MessageFilter {
    private static final float REQUIREMENT = .05F;
    private static final Map<String, Double> DANGER_COMPONENTS;
    static {
        Map<String, Double> map = new HashMap<>();
        try {
            Files.readAllLines(Paths.get(BotConfig.FAKE_DANGER_PATH)).forEach(s -> {
                if (s.isEmpty()) return;
                String[] split = s.split(" ");
                map.put(Joiner.on(' ').join(Arrays.copyOfRange(split, 1, split.length)), Double.parseDouble(split[0]));
            });
        } catch (IOException e) {
            Log.log("Unable to load fake danger file", e);
        }
        DANGER_COMPONENTS = new HashMap<>(map.size() + 1, 1);
        DANGER_COMPONENTS.putAll(map);
    }
    @Override
    public void checkFilter(DiscordMessageReceived event) {
        AtomicDouble score = new AtomicDouble();
        Consumer<String> policy = s -> score.addAndGet(DANGER_COMPONENTS.get(s));
        String reduced = FormatHelper.reduceRepeats(FormatHelper.reformat(event.getMessage().getContent(), c -> c == '\n' || c == '\r' ? ' ' : c), ' ');
        if (reduced.length() < 60) return;
        StringChecker.checkoutString(reduced, DANGER_COMPONENTS.keySet(), policy);
        if (score.get() / reduced.length() >= REQUIREMENT) System.out.println(score.get() + " " + event.getMessage().getContent());//throw new MessageMonitoringException("Dake danger identified.  Do not spread these.");
    }
    @Override
    public MessageMonitoringType getType() {
        return MessageMonitoringType.FAKE_DANGER;
    }
    private static String reduce(String in){
        StringBuilder builder = new StringBuilder(in.length());
        new StringIterator(in).forEachRemaining(c -> {
            switch (c){
                case '\n':
                case '\r':
                    builder.append(' ');
                    break;
                default:
                    if (Character.isLetter(c)) builder.append(c);
            }
        });
        return FormatHelper.reduceRepeats(builder.toString(), ' ').toLowerCase();
    }
}
