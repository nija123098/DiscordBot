package com.github.nija123098.evelyn.information.currency;

import com.github.nija123098.evelyn.exeption.DevelopmentException;
import com.github.nija123098.evelyn.service.services.MemoryManagementService;
import com.google.gson.JsonParser;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

public enum Currency {
    ETH("Ethereum"),
    LTC("Litecoin"),
    DASH("DigitalCash"),
    XMR("Monero"),
    NXT("Nxt"),
    ETC("Ethereum Classic"),
    DOGE("Dogecoin"),
    ZEC("ZCash"),
    BTS("Bitshares"),
    BTC("₿", "Bitcoin"),
    USD("$"),
    EUR("€"),
    GBP("£"),
    RUB("₽", "ruble"),
    CAD("C$", "Can$"),
    SGD("S$"),
    JPY(),
    AUD("A$", "AU$"),
    RON(),
    CNY(),
    CZK("Kč"),
    CHF(),
    BGN(),
    PLN("zł", "zloty", "zlotys"),
    MYR(),
    ZAR(),
    SEK("kr"),
    INR("₹"),
    HKD("HK$"),
    BRL("R$"),
    PKR(),
    MXN("Mex$"),;
    Currency(String...names){
        this((amount) -> amount + (names.length > 1 ? names[1] : " " + names[0]), names);
    }
    Currency(Function<Double, String> display, String...names){
        this.display = display;
        this.names.add(this.name());
        Collections.addAll(this.names, names);
    }
    private Map<Currency, Double> conversion = new MemoryManagementService.ManagedMap<>(10_000);
    private Function<Double, String> display;
    private List<String> names = new ArrayList<>(1);
    private String getDisplay(Double amount){
        return this.display.apply(amount);
    }
    public double getConversion(Currency other) {
        Double value = other.conversion.get(this);
        if (value != null) return Math.pow(value, -1);
        return getConversion(this.name(), other.name());
    }
    private static final Map<String, Currency> NAME_MAP = new HashMap<>();
    static {
        Stream.of(Currency.values()).forEach(unit -> unit.names.forEach(name -> NAME_MAP.put(name, unit)));
    }
    public static Currency getUnitForName(String name){
        return NAME_MAP.get(name);
    }

    private class ReverseDisplay implements Function<Double, String> {
        private Currency currency;
        private ReverseDisplay(Currency currency){
            this.currency = currency;
        }
        @Override
        public String apply(Double aDouble) {
            return aDouble + this.currency.names.get(1);
        }
    }
    public static double getConversion(String from, String to) {
        try {
            return Double.parseDouble(new JsonParser().parse(Unirest.get("https://min-api.cryptocompare.com/data/price?fsym=" + from + "&tsyms=" + to).asString().getBody()).getAsJsonObject().get(to).getAsString());
        } catch (UnirestException e){
            throw new DevelopmentException(e);
        }
    }
}
