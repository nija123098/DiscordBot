package com.github.nija123098.evelyn.botmanagement;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;


import java.net.*;
import java.io.*;

public class ServerIPCommand extends AbstractCommand{
    public ServerIPCommand() {
        super("serverip", ModuleLevel.BOT_ADMINISTRATIVE, null, null, "Gets the current server ip address");
    }
    @Command
    public void command(MessageMaker maker) throws IOException {
        maker.append("The current server IP address is: " + getIP());
    }

    private String getIP() throws IOException {
        String ip;

        URL checkIP = new URL("http://checkip.amazonaws.com");
        BufferedReader in = new BufferedReader(new InputStreamReader(checkIP.openStream()));
        ip = in.readLine();

        return ip;
    }
}
