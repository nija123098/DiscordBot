package com.github.nija123098.evelyn.helping;

import com.github.nija123098.evelyn.BotConfig.BotConfig;
import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.exeption.ArgumentException;
import com.github.nija123098.evelyn.util.NetworkHelper;
import net.swisstech.bitly.BitlyClient;

/**
 * Written by Soarnir 10/9/17
 */

public class ShortenCommand extends AbstractCommand {
    private BitlyClient client = new BitlyClient(BotConfig.BITLY_TOKEN);
    public ShortenCommand(){
        super("shorten", ModuleLevel.HELPER, "shrt", "scissors", "shorten links with Bit.ly");
    }

    @Command
    public void command(@Argument String arg, MessageMaker maker) {
        if (NetworkHelper.isValid(arg)) {
            maker.appendRaw(client.shorten().setLongUrl((arg.startsWith("http") ? "" : "https://") + arg).call().data.url);
        } else {
            throw new ArgumentException("Use a valid URL");
        }
    }
}
