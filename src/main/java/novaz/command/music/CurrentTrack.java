package novaz.command.music;

import novaz.core.AbstractCommand;
import novaz.db.model.OMusic;
import novaz.db.table.TMusic;
import novaz.handler.MusicPlayerHandler;
import novaz.handler.TextHandler;
import novaz.main.Config;
import novaz.main.NovaBot;
import novaz.util.Misc;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IUser;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * !current
 * retrieves information about the currently playing track
 */
public class CurrentTrack extends AbstractCommand {
	private final String BLOCK_INACTIVE = "▬";
	private final String BLOCK_ACTIVE = ":radio_button:";
	private final String SOUND_CHILL = ":sound:";
	private final String SOUND_LOUD = ":loud_sound:";
	private final float SOUND_TRESHHOLD = 0.4F;

	private final int BLOCK_PARTS = 10;

	public CurrentTrack(NovaBot b) {
		super(b);
	}

	@Override
	public String getDescription() {
		return "retrieves information about the song currently playing";
	}

	@Override
	public String getCommand() {
		return "current";
	}

	@Override
	public String[] getUsage() {
		return new String[]{
				"current               //info about the currently playing song",
				"current title <title> //sets title of current song",
				"current artist        //sets the artist of current song",
				"current correct       //accept the systems suggestion of title/artist",
				"current reversed      //accept the systems suggestion in reverse [title=artist,artist=title]",
		};
	}

	@Override
	public String[] getAliases() {
		return new String[0];
	}

	@Override
	public String execute(String[] args, IChannel channel, IUser author) {
		boolean helpedOut = false;
		OMusic song = bot.getCurrentlyPlayingSong(channel.getGuild());
		if (song.id == 0) {
			return TextHandler.get("command_currentlyplaying_nosong");
		}
		boolean titleIsEmpty = song.title == null || song.title.isEmpty();
		boolean artistIsEmpty = song.artist == null || song.artist.isEmpty();
		String guessTitle = "";
		String guessArtist = "";

		if (song.youtubeTitle.toLowerCase().chars().filter(e -> e == '-').count() >= 1) {
			String[] splitTitle = song.youtubeTitle.split("-");
			guessTitle = splitTitle[splitTitle.length - 1].trim();
			guessArtist = splitTitle[splitTitle.length - 2].trim();
		}

		if (args.length >= 1) {
			String value = "";
			for (int i = 1; i < args.length; i++) {
				value += args[i] + " ";
			}
			value = value.trim();
			if (args.length > 1 && args[0].equalsIgnoreCase("title")) {
				song.title = value;
				TMusic.update(song);
				helpedOut = true;
			} else if (args.length > 1 && args[0].equalsIgnoreCase("artist")) {
				song.artist = value;
				TMusic.update(song);
				helpedOut = true;
			} else if (args[0].equalsIgnoreCase("correct")) {
				song.artist = guessArtist;
				song.title = guessTitle;
				TMusic.update(song);
				helpedOut = true;
			} else if (args[0].equalsIgnoreCase("reversed")) {
				song.artist = guessTitle;
				song.title = guessArtist;
				TMusic.update(song);
				helpedOut = true;
			} else {
				return TextHandler.get("invalid_command_use");
			}
			titleIsEmpty = song.title == null || song.title.isEmpty();
			artistIsEmpty = song.artist == null || song.artist.isEmpty();
		}
		String ret = "Currently playing " + ":notes: ";
		if (titleIsEmpty || artistIsEmpty) {
			ret += song.youtubeTitle;
		} else {
			ret += song.artist + " - " + song.title;
		}
		ret += Config.EOL + Config.EOL;
		MusicPlayerHandler musicHandler = MusicPlayerHandler.getAudioPlayerForGuild(channel.getGuild(), bot);
		ret += getMediaplayerProgressbar(musicHandler.getCurrentSongStartTime(), musicHandler.getCurrentSongLength(), musicHandler.getVolume()) + Config.EOL + Config.EOL;
		List<IUser> userlist = bot.getCurrentlyListening(channel.getGuild());
		if (userlist.size() > 0) {
			ret += ":headphones:  Listeners" + Config.EOL;
			ArrayList<String> displayList = userlist.stream().map(IUser::getName).collect(Collectors.toCollection(ArrayList::new));
			ret += Misc.makeTable(displayList);
		}
		if (titleIsEmpty || artistIsEmpty) {
			ret += "I am missing some information about this song. Could you help me out:question:" + Config.EOL;
			ret += "If you know the title or artist of this song type **current artist <name>** or **current title <name>**" + Config.EOL;
			if (!titleIsEmpty) {
				ret += "Title: " + song.title + Config.EOL;
			}
			if (!artistIsEmpty) {
				ret += "Artist: " + song.artist + Config.EOL;
			}
			if (!helpedOut && !"".equals(guessArtist) && !"".equals(guessTitle)) {
				ret += Config.EOL + "If I can make a guess:" + Config.EOL;
				ret += "artist: **" + guessArtist + "**" + Config.EOL;
				ret += "title: **" + guessTitle + "**" + Config.EOL;
				ret += "If thats correct type **current correct** or if its reversed **current reversed**";

			}
		}
		if (helpedOut) {
			ret += "Thanks for helping out " + author.mention() + "! Have a :cookie:!";
		}
		return ret;
	}

	/**
	 * @param startTime timestamp (in seconds) of the moment the song started playing
	 * @param duration  current song length in seconds
	 * @param volume    volume of the player
	 * @return a formatted mediaplayer
	 */
	private String getMediaplayerProgressbar(long startTime, long duration, float volume) {
		long current = System.currentTimeMillis() / 1000 - startTime;
		String bar = ":pause_button: ";
		int activeBLock = (int) ((float) current / (float) duration * (float) BLOCK_PARTS);
		for (int i = 0; i < BLOCK_PARTS; i++) {
			if (i == activeBLock) {
				bar += BLOCK_ACTIVE;
			} else {
				bar += BLOCK_INACTIVE;
			}
		}
		bar += " [" + Misc.getDurationString(current) + "/" + Misc.getDurationString(duration) + "] ";
		if (volume >= SOUND_TRESHHOLD) {
			bar += SOUND_LOUD;
		} else {
			bar += SOUND_CHILL;
		}
		return bar;
	}
}