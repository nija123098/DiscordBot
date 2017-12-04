package com.github.nija123098.evelyn.information;

import com.github.goive.steamapi.SteamApi;
import com.github.goive.steamapi.data.SteamApp;
import com.github.goive.steamapi.exceptions.SteamApiException;
import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.config.configs.user.UserLanguageConfig;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.exception.ArgumentException;
import com.github.nija123098.evelyn.exception.DevelopmentException;
import com.github.nija123098.evelyn.util.EmoticonHelper;
import com.github.nija123098.evelyn.util.FormatHelper;
import com.github.nija123098.evelyn.util.LangString;
import com.github.nija123098.evelyn.util.StringHelper;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class SteamCommand extends AbstractCommand {
    private static final SteamApi STEAM_API = new SteamApi();
    public SteamCommand() {
        super("steam", ModuleLevel.INFO, "steam game", null, "Shows information on a steam product");
    }
    @Command
    public void command(MessageMaker maker, @Argument String name, User user){
        if (name.isEmpty()) throw new ArgumentException("Please give me the name of a game");
        try {
            SteamApp app = STEAM_API.retrieve(name);
            maker.withUrl("https://" + app.getWebsite());
            maker.getTitle().appendRaw(app.getName());
            maker.append(StringHelper.ensureSize(app.getAboutTheGame(), 200));
            maker.getNewFieldPart().withBoth("Price", app.isFreeToPlay() ? "FREE" : app.getPriceCurrency().getSymbol() + app.getPrice());
            maker.getNewFieldPart().withBoth("metacritic", app.getMetacriticScore() == null ? "none" : app.getMetacriticScore() + "");
            maker.getNewFieldPart().withBoth("OS", (app.isAvailableForWindows() ? EmoticonHelper.getChars("desktop", true) : "") + (app.isAvailableForMac() ? EmoticonHelper.getChars("apple", true) : "") + (app.isAvailableForLinux() ? EmoticonHelper.getChars("penguin", true) : ""));
            maker.withThumb(app.getHeaderImage());
            String code = ConfigHandler.getSetting(UserLanguageConfig.class, user);
            if (code == null) code = "en";
            String language = LangString.getLangName(code);
            AtomicBoolean supported = new AtomicBoolean();
            app.getSupportedLanguages().forEach(s -> {
                if (supported.get()) return;
                s = s.toLowerCase();
                if (s.contains("chinese")){
                    String[] nameFrags = s.split(" ");
                    s = nameFrags[1] + " " + nameFrags[0];
                }
                if (language.equals(s)) supported.set(true);
            });
            maker.getNewFieldPart().withBoth("Language", supported.get() ? EmoticonHelper.getChars("white_check_mark", false) : (FormatHelper.cleanOfXML(app.getSupportedLanguages()) + ""));
        } catch (SteamApiException e) {
            throw new DevelopmentException("Steam just hates it's api.", e);
        }
    }
}
