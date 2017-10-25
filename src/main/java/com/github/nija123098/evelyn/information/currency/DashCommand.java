package com.github.nija123098.evelyn.information.currency;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.exeption.DevelopmentException;
import com.github.nija123098.evelyn.service.services.ScheduleService;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.awt.Color;
import java.util.concurrent.atomic.AtomicReference;

import static com.github.nija123098.evelyn.information.currency.CryptocurrencyCommand.API_RETURN;

public class DashCommand extends AbstractCommand {
    private static final Color COLOR = new Color(0x0074B6);
    private static final JsonParser JSON_PARSER = new JsonParser();
    private static final AtomicReference<JsonObject> GENERAL_OBJECT = new AtomicReference<>();
    public DashCommand() {
        super(CryptocurrencyCommand.class, "dash", "dash", null, null, "Shows information on the crypocurrency Dash");
    }
    @Command
    public void command(MessageMaker maker, User user){
        maker.withUrl("https://www.dash.org/").getTitle().appendRaw("Dash");
        maker.append("Dash, it's Digital Cash").withColor(COLOR);
        maker.withThumb(API_RETURN.get().get("BaseImageUrl").getAsString() + API_RETURN.get().get("Data").getAsJsonObject().get("DASH").getAsJsonObject().get("ImageUrl").getAsString());
        if (GENERAL_OBJECT.get() == null){
            try {
                GENERAL_OBJECT.set(JSON_PARSER.parse(Unirest.get("https://www.dashcentral.org/api/v1/public").asString().getBody()).getAsJsonObject().getAsJsonObject("general"));
                ScheduleService.schedule(60_000, () -> GENERAL_OBJECT.set(null));
            } catch (UnirestException e) {
                throw new DevelopmentException("Could not get up to date information on Dash.", e);
            }
        }
        maker.getNewFieldPart().withBoth("Master Node Count", GENERAL_OBJECT.get().getAsJsonObject().get("registered_masternodes").getAsString());
        Currency currency = ConfigHandler.getSetting(PreferredCurrencyConfig.class, user);
        maker.getNewFieldPart().withBoth("Value " + currency.name(), String.valueOf(Currency.DASH.getConversion(currency)));
        maker.getNewFieldPart().withBoth("Blocks", GENERAL_OBJECT.get().get("consensus_blockheight").getAsString());
    }
}
