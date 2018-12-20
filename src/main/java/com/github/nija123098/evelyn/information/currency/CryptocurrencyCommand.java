package com.github.nija123098.evelyn.information.currency;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ContextType;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.util.FormatHelper;
import com.github.nija123098.evelyn.util.ThreadHelper;
import com.google.gson.JsonObject;
import org.apache.commons.lang3.tuple.MutablePair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class CryptocurrencyCommand extends AbstractCommand {
    private static final List<MutablePair<String, String>> ORDER = new ArrayList<>();
    private static final String STATEMENT = "Trading should not be done using Evelyn and no responsibility is taken by the bot's developers for accuracy of information nor for trades done.";
    private static final ScheduledExecutorService EXECUTOR_SERVICE = Executors.newSingleThreadScheduledExecutor(r -> ThreadHelper.getDemonThreadSingle(r, "Crypto-Command-Thread"));
    static {
        for (int i = 0; i < 10; i++) ORDER.add(new MutablePair<>());
        EXECUTOR_SERVICE.scheduleAtFixedRate(() -> Cryptocurrency.COIN_LIST.get().getAsJsonObject().entrySet().forEach(entry -> {
            JsonObject object = entry.getValue().getAsJsonObject();
            int order = object.get("SortOrder").getAsInt();
            if (order > 10) return;
            MutablePair<String, String> value = ORDER.get(order - 1);
            value.setLeft(entry.getKey());
            value.setRight(object.get("CoinName").getAsString());
        }), 0, 5, TimeUnit.MINUTES);
    }
    public CryptocurrencyCommand() {
        super("cryptocurrency", ModuleLevel.INFO, "crypto", null, "Displays basic information on several cryptocurrencies");
    }
    @Command
    public static void command(MessageMaker maker, User user, @Argument(optional = true, replacement = ContextType.NONE) Cryptocurrency cryptocurrency) {
        Currency currency = ConfigHandler.getSetting(PreferredCurrencyConfig.class, user);
        if (cryptocurrency == null) {
            maker.getTitle().append("Cryptocurrency value");
            List<List<String>> table = new ArrayList<>();
            ORDER.forEach(triple -> table.add(Arrays.asList(triple.getLeft(), String.valueOf(Currency.getConversion(triple.getLeft(), currency.name())), triple.getRight())));
            maker.appendRaw(FormatHelper.makeAsciiTable(Arrays.asList("Symbol", "Price " + currency.name(), "Name"), table, null));
            maker.getFooter().appendRaw(STATEMENT);
        }else{
            maker.getTitle().append(cryptocurrency.getName());
            maker.withUrl(cryptocurrency.getURL());
            maker.withColor(cryptocurrency.getImage());
            maker.withThumb(cryptocurrency.getImage());
            maker.getNewFieldPart().withBoth("Blocks", FormatHelper.addComas(cryptocurrency.getBlockHeight()));
            maker.getNewFieldPart().withBoth("Coins Minted", FormatHelper.addComas(String.valueOf(cryptocurrency.getCoinsMinted())) + " " + String.valueOf(cryptocurrency.getPercentMinted() * 100).substring(0, 5) + "%");
            if (cryptocurrency.getHashesPerSecond() != 0) maker.getNewFieldPart().withBoth("Hashes per Second", FormatHelper.addComas(cryptocurrency.getHashesPerSecond()));
            maker.getNewFieldPart().withBoth("Value " + currency.name(), currency.getDisplay(cryptocurrency.getConversion(currency)));
            maker.getNewFieldPart().withBoth("Market Cap " + currency.name(), FormatHelper.addComas(cryptocurrency.getMarketCap(currency).toString()));
            maker.getNote().append(STATEMENT);
        }
    }
}
