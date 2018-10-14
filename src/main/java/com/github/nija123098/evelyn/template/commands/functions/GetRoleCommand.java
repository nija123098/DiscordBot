package com.github.nija123098.evelyn.template.commands.functions;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.wrappers.Role;

public class GetRoleCommand extends AbstractCommand {
    public GetRoleCommand() {
        super("get role", ModuleLevel.NONE, "getr", null, "Gets a role object from a string");
    }
    @Command
    public Role command(@Argument String s) {
        return Role.getRole(s);
    }
}
