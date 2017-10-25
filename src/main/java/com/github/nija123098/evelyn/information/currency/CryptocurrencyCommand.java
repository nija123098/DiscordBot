package com.github.nija123098.evelyn.information.currency;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.exeption.DevelopmentException;
import com.github.nija123098.evelyn.service.services.ScheduleService;
import com.github.nija123098.evelyn.util.FormatHelper;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.apache.commons.lang3.tuple.MutablePair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class CryptocurrencyCommand extends AbstractCommand {
    static final AtomicReference<JsonObject> API_RETURN = new AtomicReference<>();
    private static final List<MutablePair<String, String>> ORDER = new ArrayList<>();
    static {
        JsonParser jsonParser = new JsonParser();
        for (int i = 0; i < 10; i++) ORDER.add(new MutablePair<>());
        try{API_RETURN.set(jsonParser.parse(Unirest.get("https://www.cryptocompare.com/api/data/coinlist/").asString().getBody()).getAsJsonObject());
        } catch (UnirestException e) {throw new DevelopmentException("Could not load data from cryptocompare.com", e);}
        ScheduleService.scheduleRepeat(0, 300_000, () -> API_RETURN.get().get("Data").getAsJsonObject().entrySet().forEach(entry -> {
            JsonObject object = entry.getValue().getAsJsonObject();
            int order = object.get("SortOrder").getAsInt();
            if (order > 10) return;
            MutablePair<String, String> value = ORDER.get(order - 1);
            value.setLeft(entry.getKey());
            value.setRight(object.get("CoinName").getAsString());
        }));
    }
    public CryptocurrencyCommand() {
        super("cryptocurrency", ModuleLevel.INFO, "crypto", null, "Displays basic information on several cryptocurrencies");
    }
    @Command
    public void command(MessageMaker maker, User user){
        maker.getTitle().append("Cryptocurrency value");
        List<List<String>> table = new ArrayList<>();
        Currency currency = ConfigHandler.getSetting(PreferredCurrencyConfig.class, user);
        ORDER.forEach(triple -> table.add(Arrays.asList(triple.getLeft(), String.valueOf(Currency.getConversion(triple.getLeft(), currency.name())), triple.getRight())));
        maker.appendRaw(FormatHelper.makeAsciiTable(Arrays.asList("Symbol", "Price " + currency.name(), "Name"), table, null));
        maker.getNote().append("Trading should not be done using Evelyn");
    }
}
