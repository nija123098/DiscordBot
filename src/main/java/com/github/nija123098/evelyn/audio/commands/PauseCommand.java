package com.github.nija123098.evelyn.audio.commands;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.config.Configurable;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.helpers.guildaudiomanager.GuildAudioManager;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;

import static com.github.nija123098.evelyn.command.ModuleLevel.MUSIC;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class PauseCommand extends AbstractCommand {
    public PauseCommand() {
        super("pause", MUSIC, "unpause", "pause_button", "Pauses the currently playing track");
    }

    @Command
    public void command(GuildAudioManager manager, MessageMaker maker) {
        if (manager.isPaused()) {
            manager.pause(false);
            maker.append("Track Unpaused.").mustEmbed();
        } else {
            manager.pause(true);
            maker.append("Track Paused.").mustEmbed();
        }
    }

    @Override
    public boolean useReactions() {
        return true;
    }

    @Override
    public long getCoolDown(Class<? extends Configurable> clazz) {
        if (clazz.equals(Guild.class)) return 5_000;
        if (clazz.equals(User.class)) return 5_000;
        return super.getCoolDown(clazz);
    }
}
