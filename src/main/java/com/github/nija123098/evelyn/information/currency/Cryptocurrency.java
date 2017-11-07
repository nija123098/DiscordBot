package com.github.nija123098.evelyn.information.currency;

import com.github.nija123098.evelyn.exeption.ArgumentException;
import com.github.nija123098.evelyn.exeption.DevelopmentException;
import com.github.nija123098.evelyn.service.services.MemoryManagementService;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class Cryptocurrency {
    private static final JsonParser JSON_PARSER = new JsonParser();
    private static final Map<String, Cryptocurrency> CRYPTOCURRENCY_MAP = new HashMap<>();
    private static final Map<String, String> NAME_MAP = new HashMap<>();
    static final AtomicReference<JsonObject> COIN_LIST = new AtomicReference<>();
    static {
        JsonParser jsonParser = new JsonParser();
        try{
            COIN_LIST.set(jsonParser.parse(Unirest.get("https://min-api.cryptocompare.com/data/all/coinlist").asString().getBody()).getAsJsonObject().getAsJsonObject("Data"));
            COIN_LIST.get().entrySet().forEach(stringJsonElementEntry -> {
                String key = stringJsonElementEntry.getKey();
                JsonObject jsonObject = stringJsonElementEntry.getValue().getAsJsonObject();
                NAME_MAP.put(key.toLowerCase(), key);
                NAME_MAP.put(jsonObject.get("Name").getAsString().toLowerCase(), key);
                NAME_MAP.put(jsonObject.get("Symbol").getAsString().toLowerCase(), key);
                NAME_MAP.put(jsonObject.get("CoinName").getAsString().toLowerCase(), key);
                NAME_MAP.put(jsonObject.get("FullName").getAsString().toLowerCase(), key);
            });
        } catch (UnirestException e) {
            throw new DevelopmentException("Could not coin list from cryptocompare.com", e);
        }
    }
    public static Cryptocurrency getCryptocurrency(String identification){
        identification = NAME_MAP.get(identification);
        if (identification == null) throw new ArgumentException("Could not find cryptocurrency from identifying string");
        return CRYPTOCURRENCY_MAP.computeIfAbsent(identification, Cryptocurrency::new);
    }
    private int id;
    private String name;
    private final MemoryManagementService.ManagedReference<JsonObject> DATA = new MemoryManagementService.ManagedReference<>(60_000, () -> {
        try {
            return JSON_PARSER.parse(Unirest.get("https://www.cryptocompare.com/api/data/coinsnapshotfullbyid/?id="+this.id).asString().getBody()).getAsJsonObject().getAsJsonObject("Data").getAsJsonObject("General");
        } catch (UnirestException e) {
            throw new DevelopmentException("Could not get up to date information on coin snapshot", e);
        }
    });
    private Cryptocurrency(String name){
        this.name = name;
        this.id = COIN_LIST.get().getAsJsonObject(this.name).get("Id").getAsInt();
    }
    public long getBlockHeight(){
        return DATA.get().get("BlockNumber").getAsLong();
    }
    public double getCoinsMinted(){
        return DATA.get().get("TotalCoinsMined").getAsDouble();
    }
    public long getPossibleCoins(){
        return DATA.get().get("TotalCoinSupply").getAsLong();
    }
    public double getPercentMinted(){
        return this.getCoinsMinted() / (double) this.getPossibleCoins();
    }
    public String getImage(){
        return "https://www.cryptocompare.com" + DATA.get().get("ImageUrl").getAsString();
    }
    public double getConversion(Currency currency){
        return Currency.getConversion(this.name, currency.name());
    }
    public long getHashesPerSecond(){
        return DATA.get().get("NetHashesPerSecond").getAsLong();
    }
    public String getName() {
        return this.name;
    }
    public String getURL(){
        return DATA.get().get("AffiliateUrl").getAsString();
    }
    public BigDecimal getMarketCap(Currency currency){
        return BigDecimal.valueOf(this.getConversion(currency)).multiply(BigDecimal.valueOf(this.getCoinsMinted()));
    }
}
