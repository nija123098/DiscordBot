package com.github.nija123098.evelyn.botmanagement;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ContextType;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.DiscordAdapter;
import com.github.nija123098.evelyn.discordobjects.wrappers.DiscordClient;
import com.github.nija123098.evelyn.discordobjects.wrappers.Presence;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class PlayTextOverrideCommand extends AbstractCommand {
    public PlayTextOverrideCommand() {
        super("playtext", ModuleLevel.BOT_ADMINISTRATIVE, null, null, "Overrides the playtext of the bot");
    }
    @Command
    public void command(@Argument(optional = true, replacement = ContextType.NONE) Presence.Status stats, @Argument(optional = true, replacement = ContextType.NONE) Presence.Activity activity, @Argument String s) {
        DiscordAdapter.PLAY_TEXT_UPDATE.set(false);
        if (stats == null) stats = Presence.Status.ONLINE;
        if (activity == null) activity = Presence.Activity.PLAYING;
        DiscordClient.changePresence(stats, activity, s);
    }
}
