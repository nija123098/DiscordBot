package com.github.kaaz.emily.economy;

import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.ContextType;
import com.github.kaaz.emily.command.annotations.Argument;
import com.github.kaaz.emily.command.annotations.Command;
import com.github.kaaz.emily.command.annotations.Context;
import com.github.kaaz.emily.config.Configurable;
import com.github.kaaz.emily.config.GuildUser;
import com.github.kaaz.emily.discordobjects.wrappers.User;

/**
 * Made by nija123098 on 7/5/2017.
 */
public class BankSendCommand extends AbstractCommand {
    public BankSendCommand() {
        super(BankCommand.class, "send", null, null, null, "Sends currency to another user, guild, or guild user");
    }
    @Command
    public void command(@Argument Configurable one, @Argument(optional = true, replacement = ContextType.NONE) Configurable two, @Argument Integer integer, @Context(softFail = true) GuildUser guildUser, User user){
        Configurable reciver, sender;
        if (two != null){
            sender = one;
            reciver = two;
        }else{
            reciver = one;
            sender = guildUser == null ? user : guildUser;
        }
        MoneyTransfer.transact(sender, reciver, 12, 0, "Sending");
    }
}
