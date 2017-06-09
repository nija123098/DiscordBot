package com.github.kaaz.emily.information;

import com.github.goive.steamapi.SteamApi;
import com.github.goive.steamapi.data.SteamApp;
import com.github.goive.steamapi.exceptions.SteamApiException;
import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.ModuleLevel;
import com.github.kaaz.emily.command.anotations.Command;
import com.github.kaaz.emily.config.ConfigHandler;
import com.github.kaaz.emily.config.configs.user.UserLanguageConfig;
import com.github.kaaz.emily.discordobjects.helpers.MessageMaker;
import com.github.kaaz.emily.discordobjects.wrappers.User;
import com.github.kaaz.emily.util.EmoticonHelper;
import com.github.kaaz.emily.util.LangString;
import com.github.kaaz.emily.util.StringHelper;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Made by nija123098 on 6/6/2017.
 */
public class SteamCommand extends AbstractCommand {
    private static final SteamApi STEAM_API = new SteamApi();
    public SteamCommand() {
        super("steam", ModuleLevel.INFO, null, null, "Shows information on a steam product");
    }
    @Command
    public void command(MessageMaker maker, String name, User user){
        try {
            SteamApp app = STEAM_API.retrieve(name);
            maker.getAuthorName().appendRaw(app.getName());
            maker.withImage(app.getWebsite());
            maker.append(StringHelper.ensureSize(app.getAboutTheGame(), 200));
            maker.getNewFieldPart().withBoth("Price", app.isFreeToPlay() ? "FREE" : app.getPriceCurrency().getSymbol() + app.getPrice());
            maker.getNewFieldPart().withBoth("metacritic", app.getMetacriticScore() + "");
            maker.getNewFieldPart().withBoth("OS", (app.isAvailableForWindows() ? EmoticonHelper.getChars("desktop") : "") + (app.isAvailableForMac() ? EmoticonHelper.getChars("apple") : "") + (app.isAvailableForLinux() ? EmoticonHelper.getChars("penguin") : ""));
            maker.withThumb(app.getHeaderImage());
            if (app.getRequiredAge() != 0) maker.getNewFieldPart().withBoth("age", app.getRequiredAge() + "");
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
            maker.getNewFieldPart().withBoth("Language", supported.get() ? EmoticonHelper.getChars("white_check_mark") : (app.getSupportedLanguages() + ""));
        } catch (SteamApiException e) {
            e.printStackTrace();
        }
    }
}
