package com.github.nija123098.evelyn.favor.configs.balencing;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.perms.BotRole;
import com.github.nija123098.evelyn.util.FormatHelper;

public class FavorRankEquationConfig extends AbstractConfig<String, Guild> {
    public FavorRankEquationConfig() {
        super("favor_rank_equation", BotRole.GUILD_TRUSTEE, null, "The equation to decided what rank a user is where `x` is favor and y is the rank the receive");
    }
    @Override
    protected String validateInput(Guild configurable, String s) {
        return FormatHelper.filtering(s, c -> !c.equals(' ')).replace("Y", "y").replace("=y", "").replace("y=", "").replace("X", "x");
    }
}
