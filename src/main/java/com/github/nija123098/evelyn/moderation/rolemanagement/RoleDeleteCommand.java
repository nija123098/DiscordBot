package com.github.nija123098.evelyn.moderation.rolemanagement;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Channel;
import com.github.nija123098.evelyn.discordobjects.wrappers.Role;
import com.github.nija123098.evelyn.exception.PermissionsException;

import java.util.ConcurrentModificationException;

/**
 * @author Soarnir
 * @since 1.0.0
 */
public class RoleDeleteCommand extends AbstractCommand {

    public RoleDeleteCommand() {
        super(RoleCommand.class, "delete", "delrole", null, null, "Delete a role from the server");
    }

    @Command
    public void command(@Argument Role role, MessageMaker maker, Channel invokeChannel) {

        maker.mustEmbed();
        maker.getTitle().appendRaw("Role Deletion");
        maker.appendRaw("The `" + role.getName() + "` role will be deleted, this is an irreversible action and all permissions associated with the role will be deleted, use at your own risk");

        maker.withReactionBehavior("red_tick", ((add, reaction, u) -> {
            //remove reactions
            try {
                maker.clearReactionBehaviors();
            } catch (ConcurrentModificationException ignored) { }
            maker.sentMessage().getReactions().forEach(reactions -> maker.sentMessage().removeReaction(reactions));

        }));

        //if send
        maker.withReactionBehavior("green_tick", ((add, reaction, u) -> {

            //remove reactions
            try {
                maker.clearReactionBehaviors();
            } catch (ConcurrentModificationException ignored) {
            }
            maker.sentMessage().getReactions().forEach(reactions -> maker.sentMessage().removeReaction(reactions));

            MessageMaker maker2 = new MessageMaker(invokeChannel);
            try {
                String roleName = role.getName();
                role.delete();
                maker.mustEmbed().appendRaw(roleName + " deleted successfully");
            } catch (PermissionsException e) {
                maker2.appendRaw("I couldn't delete the `" + role.getName() + "` role, check your discord permissions to ensure my role is higher than the role I'm trying to delete.");
            }
            maker2.forceCompile().send();
        }));
        maker.forceCompile().send();


    }
}