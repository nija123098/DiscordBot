package com.github.nija123098.evelyn.helping;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.exception.ArgumentException;
import com.google.common.collect.Sets;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import javax.measure.unit.NonSI;
import javax.measure.unit.SI;
import javax.measure.unit.Unit;
import java.util.*;

/**
 * Made by nija123098 on 6/19/2017.
 */
public class ConversionCommand extends AbstractCommand {
    static final Map<String, Un> UN_MAP = new HashMap<>();
    static {
        Sets.union(new HashSet<>(Collections.singletonList(SI.GRAM)), Sets.union(SI.getInstance().getUnits(), NonSI.getInstance().getUnits())).forEach(o -> UN_MAP.put(o.toString().toLowerCase(), new Un(o)));
        Map<Currency, Un> map = new HashMap<>();
        Currency.getAvailableCurrencies().forEach(currency -> map.put(currency, new Un(currency)));
        Currency.getAvailableCurrencies().forEach(currency -> {
            UN_MAP.put(currency.getDisplayName().toLowerCase(), map.get(currency));
            UN_MAP.put(currency.getCurrencyCode().toLowerCase(), map.get(currency));
        });
    }
    public ConversionCommand() {
        super("convert", ModuleLevel.HELPER, "conversion", null, "Converts units and currency amounts");
    }
    @Command
    public void command(String args, MessageMaker maker){
        args = args.toLowerCase();
        String[] split = args.split(" to ");
        if (split.length != 2) throw new ArgumentException("Please format arguments in <amount> <unit> ***TO*** <unit>, it is case sensitive and uses abbreviations");
        int amountIndex = split[0].indexOf(" ");
        if (amountIndex == -1) throw new ArgumentException("Please specify an amount");
        String[] strings = new String[]{split[0].substring(0, amountIndex), split[0].substring(amountIndex + 1, split[0].length()), split[1]};
        Un from = UN_MAP.get(strings[1]), to = UN_MAP.get(strings[2]);
        if (from == null) throw new ArgumentException("Unrecognized unit: " + strings[1]);
        if (to == null) throw new ArgumentException("Unrecognized unit: " + strings[2]);
        if (!from.isCompatible(to)) throw new ArgumentException("The chosen units are not compatible.");
        try {
            maker.appendRaw(from.getConversion(to, Double.parseDouble(strings[0])) + " " + strings[2]);
        } catch (NumberFormatException e){
            throw new ArgumentException("Invalid amount " + strings[0]);
        }
    }
    private static class Un {
        Unit<?> unit;
        Currency currency;
        Un(Unit<?> unit) {
            this.unit = unit;
        }
        Un(Currency currency) {
            this.currency = currency;
        }
        boolean isCompatible(Un un){
             return (this.currency != null && un.currency != null) || (this.unit != null && un.unit != null && this.unit.isCompatible(un.unit));
        }
        double getConversion(Un un, double amount){
            return this.unit != null ? this.unit.getConverterTo(un.unit).convert(amount) : getMonetaryConversionRate(this.currency.getCurrencyCode(), un.currency.getCurrencyCode()) * amount;
        }
    }
    private static Float getMonetaryConversionRate(String from, String to) {
        try {
            HttpClientBuilder builder = HttpClientBuilder.create();
            try (CloseableHttpClient client = builder.build()) {
                return Float.parseFloat(client.execute(new HttpGet("http://quote.yahoo.com/d/quotes.csv?s=" + from + to + "=X&f=l1&e=.csv"), new BasicResponseHandler()));
            }
        } catch (Exception ignored){}
        throw new ArgumentException("Can not convert between " + from + " and " + to);
    }

    @Override
    protected String getLocalUsages() {
        return "#  convert <amount> <unit> to <unit> // convert between two units, case sensitive";
    }
}
