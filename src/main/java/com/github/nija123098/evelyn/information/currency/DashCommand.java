package com.github.nija123098.evelyn.information.currency;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.exception.DevelopmentException;
import com.github.nija123098.evelyn.service.services.MemoryManagementService;
import com.github.nija123098.evelyn.util.FormatHelper;
import com.google.common.util.concurrent.AtomicDouble;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.awt.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class DashCommand extends AbstractCommand {
    private static final Color COLOR = new Color(0x0074B6);
    private static final JsonParser JSON_PARSER = new JsonParser();
    private static final MemoryManagementService.ManagedReference<JsonArray> MASTERNODE_ARRAYS = new MemoryManagementService.ManagedReference<>(1_200_000, () -> {
        try {
            return JSON_PARSER.parse(Unirest.get("https://www.dashninja.pl/api/masternodes?testnet=0&balance=1").asString().getBody()).getAsJsonObject().getAsJsonArray("data");
        } catch (UnirestException e) {
            throw new DevelopmentException("Could not get up to date information on Dash.", e);
        }
    });
    private static final MemoryManagementService.ManagedReference<Double> DASH_IN_MASTER_NODES = new MemoryManagementService.ManagedReference<>(1_200_000, () -> {
        AtomicDouble dashInNodes = new AtomicDouble();
        MASTERNODE_ARRAYS.get().forEach(jsonElement -> {
            JsonElement jsonObject = jsonElement.getAsJsonObject().get("Balance");// sometimes this returns a primitive of "false"
            if (jsonObject instanceof JsonObject) dashInNodes.addAndGet(((JsonObject) jsonObject).get("Value").getAsDouble());
        });
        return dashInNodes.get();
    });
    private static final MemoryManagementService.ManagedReference<Integer> ONLINE_NODES = new MemoryManagementService.ManagedReference<>(20_000, () -> {
        AtomicInteger integer = new AtomicInteger();
        MASTERNODE_ARRAYS.get().forEach(jsonElement -> {
            if (jsonElement.getAsJsonObject().get("ActiveCount").getAsInt() > jsonElement.getAsJsonObject().get("InactiveCount").getAsInt()) integer.incrementAndGet();
        });
        return integer.get();
    });
    private static final MemoryManagementService.ManagedReference<JsonArray> BLOCK_DATA = new MemoryManagementService.ManagedReference<>(600_000, () -> {
        try {
            return JSON_PARSER.parse(Unirest.get("https://www.dashninja.pl/api/blocks?testnet=0").asString().getBody()).getAsJsonObject().getAsJsonObject("data").getAsJsonArray("blocks");
        } catch (UnirestException e) {
            throw new DevelopmentException("Could not get up to date information on Dash.", e);
        }
    });
    private static final MemoryManagementService.ManagedReference<Long> DIFFICULTY = new MemoryManagementService.ManagedReference<>(600_000, () -> {
        AtomicLong value = new AtomicLong();
        double factor = 1D / BLOCK_DATA.get().size();
        BLOCK_DATA.get().forEach(jsonElement -> value.addAndGet((long) (jsonElement.getAsJsonObject().get("BlockTime").getAsLong() * factor)));
        return value.get();
    });
    private static final MemoryManagementService.ManagedReference<Double> PAYOUT_AVERAGE = new MemoryManagementService.ManagedReference<>(600_000, () -> {
        AtomicDouble value = new AtomicDouble();
        BLOCK_DATA.get().forEach(jsonElement -> value.addAndGet(jsonElement.getAsJsonObject().get("BlockMNValue").getAsDouble()));
        return value.get();
    });
    public DashCommand() {//BlockMNValue
        super(CryptocurrencyCommand.class, "dash", "dash", null, null, "Shows information on the crypocurrency Dash");
    }
    @Command
    public void command(MessageMaker maker, User user){
        Cryptocurrency dash = Cryptocurrency.getCryptocurrency("dash");
        maker.getNewFieldPart().withBoth("Masternodes", ONLINE_NODES.get() + " online of " + MASTERNODE_ARRAYS.get().size() + " " + String.valueOf(((double) ONLINE_NODES.get() / MASTERNODE_ARRAYS.get().size() * 100)).substring(0, 5) + "%");
        maker.getNewFieldPart().withBoth("24H Average Difficulty", FormatHelper.addComas(DIFFICULTY.get()));
        maker.getNewFieldPart().withBoth("Dash in Masternodes", FormatHelper.addComas(String.valueOf(Math.floor(DASH_IN_MASTER_NODES.get()))) + " " + String.valueOf(DASH_IN_MASTER_NODES.get() / dash.getCoinsMinted() * 100).substring(0, 5) + "%");
        maker.getNewFieldPart().withBoth("Masternode Monthly Earnings", String.valueOf(PAYOUT_AVERAGE.get() / ONLINE_NODES.get() * 30).substring(0, 4) + " Dash");
        CryptocurrencyCommand.command(maker, user, dash);
        maker.append("Dash, it's Digital Cash").withColor(COLOR);
    }
}
