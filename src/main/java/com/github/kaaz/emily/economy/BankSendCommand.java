package com.github.kaaz.emily.economy;

import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.ContextType;
import com.github.kaaz.emily.command.annotations.Argument;
import com.github.kaaz.emily.command.annotations.Command;
import com.github.kaaz.emily.command.annotations.Context;
import com.github.kaaz.emily.config.Configurable;
import com.github.kaaz.emily.discordobjects.wrappers.Guild;
import com.github.kaaz.emily.discordobjects.wrappers.User;
import com.github.kaaz.emily.exeption.ArgumentException;

/**
 * Made by nija123098 on 7/5/2017.
 */
public class BankSendCommand extends AbstractCommand {
    public BankSendCommand() {
        super(BankCommand.class, "send", null, null, null, "Sends currency to another user, guild, or guild user");
    }
    @Command
    public void command(@Argument Configurable one, @Argument(optional = true, replacement = ContextType.NONE) Configurable two, @Argument Integer integer, User user, @Context(softFail = true) Guild guild){
        if (integer < 0) throw new ArgumentException("You can't take money from other users without their consent");
        if (integer == 0) throw new ArgumentException("You can't send them nothing");
        Configurable reciver, sender;
        if (two != null){
            sender = one;
            reciver = two;
        }else{
            reciver = one;
            sender = user;
        }
        sender.checkPermissionToEdit(user, guild);
        MoneyTransfer.transact(sender, reciver, 0, integer, "Sending");
    }
}
