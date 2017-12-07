package com.github.nija123098.evelyn.information;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.exception.ArgumentException;
import com.github.nija123098.evelyn.util.FormatHelper;

/**
 * @author Soarnir
 * @since 1.0.0
 */
public class LMGTFYCommand extends AbstractCommand {

	//constructor
	public LMGTFYCommand() {
		super("lmgtfy", ModuleLevel.INFO, null, null, "help another user use google");
	}

	@Command
	public void command(@Argument String arg, MessageMaker maker) {

		if (arg.length() > 0) {
			maker.mustEmbed().appendRaw(FormatHelper.embedLink("let me google that for you", ("http://lmgtfy.com/?q=" + arg.replace(' ', '+'))));
		} else {
			throw new ArgumentException("You'll need to provide a search term");
		}

	}
}