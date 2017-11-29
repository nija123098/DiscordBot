package com.github.nija123098.evelyn.command.commands;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * @author Celestialdeath99
 */

public class ServerIPCommand extends AbstractCommand{
    public ServerIPCommand() {
        super("serverip", ModuleLevel.DEVELOPMENT, "ip", null, "Gets the current server ip address");
    }
    @Command
    public void command(MessageMaker maker) throws IOException {
        maker.append("The current server IP address is: " + getIP() + "\n" + "**Please do not share this IP as it may produce security concerns.**");
    }

    private String getIP() throws IOException {
        String ip;
        URL checkIP = new URL("http://checkip.amazonaws.com");
        BufferedReader in = new BufferedReader(new InputStreamReader(checkIP.openStream()));
        ip = in.readLine();
        return ip;
    }
}
