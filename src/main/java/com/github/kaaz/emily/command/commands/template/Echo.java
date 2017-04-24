package com.github.kaaz.emily.command.commands.template;

import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.anotations.Command;
import com.github.kaaz.emily.command.anotations.Convert;
import com.github.kaaz.emily.command.anotations.TemplateCommand;

/**
 * Made by nija123098 on 4/23/2017.
 */
@TemplateCommand
public class Echo extends AbstractCommand {
    public Echo() {
        super(null, "echo", null, null, null);
    }
    @Command
    public String echo(@Convert String s){
        return s;
    }
}
