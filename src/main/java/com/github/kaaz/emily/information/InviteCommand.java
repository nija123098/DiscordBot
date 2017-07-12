package com.github.kaaz.emily.information;

import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.ModuleLevel;
import com.github.kaaz.emily.command.annotations.Command;
import com.github.kaaz.emily.discordobjects.helpers.MessageMaker;
import com.github.kaaz.emily.discordobjects.wrappers.DiscordClient;

/**
 * Made by nija123098 on 5/11/2017.
 */
public class InviteCommand extends AbstractCommand {
    public InviteCommand() {
        super("invite", ModuleLevel.INFO, "inv", null, "Provides an invite link to add the bot to your server.");
    }
    @Command
    public void command(MessageMaker maker){
        maker.append("I am honored you'd want to invite me! :hugging: \n" +
                "You can add me to your guild/server with the following link : \n" +
                "https://discordapp.com/oauth2/authorize?client_id=" + DiscordClient.getOurUser().getID() + "&scope=bot&permissions=70634560");
    }
}
