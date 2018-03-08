package com.github.nija123098.evelyn.botmanagement;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.DiscordAdapter;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class PlayTextResumeCommand extends AbstractCommand {
    public PlayTextResumeCommand() {
        super(PlayTextOverrideCommand.class, "resume", null, null, null, "Resumes normal playtext display");
    }
    @Command
    public void command(){
        DiscordAdapter.PLAY_TEXT_UPDATE.set(true);
    }
}
