package com.github.kaaz.emily.economy;

import com.github.kaaz.emily.config.ConfigHandler;
import com.github.kaaz.emily.config.Configurable;
import com.github.kaaz.emily.config.GuildUser;
import com.github.kaaz.emily.discordobjects.wrappers.DiscordClient;
import com.github.kaaz.emily.discordobjects.wrappers.User;
import com.github.kaaz.emily.economy.configs.CurrentComponentsConfig;
import com.github.kaaz.emily.economy.configs.CurrentMoneyConfig;
import com.github.kaaz.emily.economy.configs.MoneyHistoryConfig;
import com.github.kaaz.emily.exeption.TransactionException;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Made by nija123098 on 5/16/2017.
 */
public class MoneyTransfer {
    private static Map<ItemComponent, Integer> EMPTY = Collections.emptyMap();
    public static void transact(Configurable firstParty, Configurable secondParty, float firstPartyMoney, float secondPartyMoney, String note){
        transact(firstParty, secondParty, EMPTY, EMPTY, firstPartyMoney, secondPartyMoney, note);
    }
    public static void transact(Configurable firstParty, Configurable secondParty, Map<ItemComponent, Integer> firstPartyComponents, Map<ItemComponent, Integer> secondPartyComponents, float firstPartyMoney, float secondPartyMoney, String note){
        if (firstParty instanceof GuildUser && secondParty.equals(DiscordClient.getOurUser())) {
            transact(((GuildUser) firstParty).getUser(), DiscordClient.getOurUser(), firstPartyMoney, secondPartyMoney, note + " copy");
            transact(((GuildUser) firstParty).getGuild(), DiscordClient.getOurUser(), firstPartyMoney, secondPartyMoney, note + " copy");
        }
        MoneyTransfer moneyTransfer = new MoneyTransfer(firstParty, secondParty, firstPartyComponents, secondPartyComponents, firstPartyMoney, secondPartyMoney, note);
        float money = ConfigHandler.getSetting(CurrentMoneyConfig.class, firstParty) + moneyTransfer.firstPartyMoney;
        if (!firstParty.convert(User.class).equals(DiscordClient.getOurUser())) if (money < 0) throw new TransactionException(firstParty, -money);
        float secondMoney = ConfigHandler.getSetting(CurrentMoneyConfig.class, firstParty) + moneyTransfer.secondPartyMoney;
        if (!secondParty.convert(User.class).equals(DiscordClient.getOurUser())) if (secondMoney < 0) throw new TransactionException(secondParty, -secondMoney);
        Map<ItemComponent, Integer> components = ConfigHandler.getSetting(CurrentComponentsConfig.class, firstParty);
        moneyTransfer.firstPartyComponents.forEach((itemComponent, integer) -> components.compute(itemComponent, (c, i) -> {
            if (i == null) i = 0;
            return i - integer;
        }));
        if (!firstParty.convert(User.class).equals(DiscordClient.getOurUser())) components.forEach((itemComponent, integer) -> {
            if (integer < 0) throw new TransactionException(firstParty, itemComponent, -integer);
        });
        Map<ItemComponent, Integer> secondComponents = ConfigHandler.getSetting(CurrentComponentsConfig.class, firstParty);
        moneyTransfer.secondPartyComponents.forEach((itemComponent, integer) -> secondComponents.compute(itemComponent, (c, i) -> {
            if (i == null) i = 0;
            return i - integer;
        }));
        if (!secondParty.convert(User.class).equals(DiscordClient.getOurUser())) secondComponents.forEach((itemComponent, integer) -> {
            if (integer < 0) throw new TransactionException(firstParty, itemComponent, -integer);
        });
        ConfigHandler.setSetting(CurrentMoneyConfig.class, firstParty, money);
        ConfigHandler.setSetting(CurrentMoneyConfig.class, secondParty, secondMoney);
        ConfigHandler.setSetting(CurrentComponentsConfig.class, firstParty, components);
        ConfigHandler.setSetting(CurrentComponentsConfig.class, secondParty, secondComponents);
        ConfigHandler.alterSetting(MoneyHistoryConfig.class, firstParty, moneyMovements -> moneyMovements.add(moneyTransfer));
        ConfigHandler.alterSetting(MoneyHistoryConfig.class, secondParty, moneyMovements -> moneyMovements.add(moneyTransfer.getReverse()));
    }
    private Configurable firstParty, secondParty;
    private Map<ItemComponent, Integer> firstPartyComponents, secondPartyComponents;
    private float firstPartyMoney, secondPartyMoney;// items are the offers, not gains
    private String note;
    protected MoneyTransfer() {}
    private MoneyTransfer(Configurable firstParty, Configurable secondParty, Map<ItemComponent, Integer> firstPartyComponents, Map<ItemComponent, Integer> secondPartyComponents, float firstPartyMoney, float secondPartyMoney, String note) {
        this.firstParty = firstParty;
        this.secondParty = secondParty;
        this.firstPartyComponents = firstPartyComponents;
        this.secondPartyComponents = secondPartyComponents;
        this.note = note;
        Stream.of(ItemComponent.values()).forEach(itemComponent -> {
            int diff = this.firstPartyComponents.getOrDefault(itemComponent, 0) - this.secondPartyComponents.getOrDefault(itemComponent, 0);
            if (diff == 0){
                this.firstPartyComponents.remove(itemComponent);
                this.secondPartyComponents.remove(itemComponent);
            }else{
                this.firstPartyComponents.put(itemComponent, diff);
                this.secondPartyComponents.put(itemComponent, -diff);
            }
        });
        this.firstPartyMoney = firstPartyMoney;
        this.secondPartyMoney = secondPartyMoney;
        float diff = this.firstPartyMoney - this.secondPartyMoney;
        if (diff == 0) {
            this.firstPartyMoney = 0;
            this.secondPartyMoney = 0;
        } else {
            this.firstPartyMoney = diff;
            this.secondPartyMoney = -diff;
        }
    }
    private MoneyTransfer getReverse(){
        return new MoneyTransfer(this.secondParty, this.firstParty, this.secondPartyComponents, this.firstPartyComponents, this.secondPartyMoney, this.firstPartyMoney, this.note);
    }
    public Configurable getParty(boolean first){
        return first ? this.firstParty : this.secondParty;
    }
    public Configurable getFirstParty() {
        return this.firstParty;
    }
    public Configurable getSecondParty() {
        return this.secondParty;
    }
    public Integer getComponent(boolean first, ItemComponent itemComponent){
        return this.getComponents(first).get(itemComponent);
    }
    public Integer getFirstPartyComponet(ItemComponent itemComponent){
        return this.firstPartyComponents.get(itemComponent);
    }
    public Integer getSecondPartyComponet(ItemComponent itemComponent){
        return this.secondPartyComponents.get(itemComponent);
    }
    public Map<ItemComponent, Integer> getComponents(boolean first){
        return first ? this.firstPartyComponents : this.secondPartyComponents;
    }
    public Map<ItemComponent, Integer> getFirstPartyComponents() {
        return this.firstPartyComponents;
    }
    public Map<ItemComponent, Integer> getSecondPartyComponents() {
        return this.secondPartyComponents;
    }
    public float getMoney(boolean first){
        return first ? this.firstPartyMoney : this.secondPartyMoney;
    }
    public float getFirstPartyMoney() {
        return this.firstPartyMoney;
    }
    public float getSecondPartyMoney() {
        return this.secondPartyMoney;
    }
}
