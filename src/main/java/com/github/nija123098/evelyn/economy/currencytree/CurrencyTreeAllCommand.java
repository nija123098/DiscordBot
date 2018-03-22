package com.github.nija123098.evelyn.economy.currencytree;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.wrappers.DiscordClient;

public class CurrencyTreeAllCommand extends AbstractCommand {
    public CurrencyTreeAllCommand() {
        super(CurrencyTreeCommand.class, "all", null, null, null, "Currency tree for all users");
    }
    @Command
    public void command(@Argument Integer amount, @Argument(optional = true, info = "currency type") String type) {
        DiscordClient.getUsers().parallelStream().forEach(user -> CurrencyTreeCommand.command(null, null, amount, user, type));
    }
}
