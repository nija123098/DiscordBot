package com.github.kaaz.emily.automoderation.modaction;

import com.github.kaaz.emily.automoderation.GuildUserJoinTimeConfig;
import com.github.kaaz.emily.automoderation.MessageDeleteService;
import com.github.kaaz.emily.automoderation.modaction.support.AbstractModAction;
import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.annotations.Argument;
import com.github.kaaz.emily.command.annotations.Command;
import com.github.kaaz.emily.config.ConfigHandler;
import com.github.kaaz.emily.config.Configurable;
import com.github.kaaz.emily.config.GuildUser;
import com.github.kaaz.emily.discordobjects.helpers.MessageMaker;
import com.github.kaaz.emily.discordobjects.wrappers.Guild;
import com.github.kaaz.emily.discordobjects.wrappers.Message;
import com.github.kaaz.emily.discordobjects.wrappers.User;
import com.github.kaaz.emily.information.configs.OldNicknameConfig;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Made by nija123098 on 5/13/2017.
 */
public class ExpungeModActionCommand extends AbstractCommand {
    public ExpungeModActionCommand() {
        super(ModActionCommand.class, "expunge", "expunge", null, "e", "Totally eradicates a user's history on a server");
    }
    @Command
    public void command(Guild guild, User user, @Argument(info = "The user to be kicked") User target, @Argument(optional = true, info = "The reason") String reason){
        guild.banUser(user, 7);// 7 is the max
        new AbstractModAction(guild, AbstractModAction.ModActionLevel.BAN, target, user, reason);
        MessageMaker maker = new MessageMaker(user).getTitle().append("Name Removal").getMaker()
                .append("The following names were used by the user while present in the guild.  " +
                        "Select the corresponding letter to remove the name from all message history.  " +
                        "Press the ok reaction to finalize these decisions.  " +
                        "This is irreversible, chose wisely.");
        List<String> names = new ArrayList<>(ConfigHandler.getSetting(OldNicknameConfig.class, GuildUser.getGuildUser(guild, target)));
        int setSize = names.size() / 20 + 1;
        List<List<String>> nameSet = new ArrayList<>();
        for (int i = 0; i < setSize; i++) nameSet.add(new ArrayList<>(i == setSize - 1 ? names.size() % 20 : 20));
        AtomicInteger count = new AtomicInteger();
        names.forEach(s -> nameSet.get(count.incrementAndGet() / 20).add(s));
        Set<String> removes = new HashSet<>();
        computeRound(maker, nameSet.get(0), removes);
        AtomicInteger set = new AtomicInteger(0);
        maker.withReactionBehavior("ok_hand", (add, reaction, u) -> {
            if (set.incrementAndGet() > setSize){
                maker.clearReactionBehaviors().withReactionBehavior("+1", (a, r, us) -> {
                    maker.clearReactionBehaviors().send();
                    long joinDate = GuildUserJoinTimeConfig.get(GuildUser.getGuildUser(guild, target));
                    guild.getChannels().forEach(channel -> {
                        Set<Message> messagesToRemove = new HashSet<>();
                        channel.getMessagesTo(joinDate).forEach(message -> {
                            if (message.getAuthor().equals(target) || message.getMentions().contains(target)) {
                                messagesToRemove.add(message);
                                return;
                            }
                            for (String rem : removes) {
                                if (message.getContent().contains(rem)) {
                                    messagesToRemove.add(message);
                                    return;
                                }
                            }
                        });
                        MessageDeleteService.delete(new ArrayList<>(messagesToRemove));
                    });
                });
            }else computeRound(maker, nameSet.get(set.incrementAndGet()), removes);
        });
    }
    @Override
    public long getCoolDown(Class<? extends Configurable> clazz){
        return 604_800_000;// 7 days
    }
    private static void computeRound(MessageMaker maker, List<String> names, Set<String> removes){
        maker.clearReactionBehaviors();
        int code = 97;
        names.forEach(s -> maker.getNewFieldPart().getTitle().append(String.valueOf(code)).getFieldPart().getValue().append(s).getMaker().withReactionBehavior("regional_indicator_" + String.valueOf(code), (add, reaction, u) -> {
            if (add) removes.add(s);
            else removes.remove(s);
        }));
        maker.forceCompile().send();
    }
}
