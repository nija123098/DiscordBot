package com.github.kaaz.emily.economy;

import com.github.kaaz.emily.config.ConfigHandler;
import com.github.kaaz.emily.config.Configurable;
import com.github.kaaz.emily.config.GuildUser;
import com.github.kaaz.emily.discordobjects.wrappers.DiscordClient;
import com.github.kaaz.emily.discordobjects.wrappers.Guild;
import com.github.kaaz.emily.discordobjects.wrappers.User;
import com.github.kaaz.emily.economy.configs.CurrentComponentsConfig;
import com.github.kaaz.emily.economy.configs.CurrentMoneyConfig;
import com.github.kaaz.emily.economy.configs.MoneyHistoryConfig;
import com.github.kaaz.emily.exeption.ArgumentException;
import com.github.kaaz.emily.exeption.TransactionException;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Stream;

/**
 * Made by nija123098 on 5/16/2017.
 */
public class MoneyTransfer {
    public static void transact(Configurable firstParty, Configurable secondParty, int firstPartyMoney, int secondPartyMoney, String note){
        transact(firstParty, secondParty, null, null, firstPartyMoney, secondPartyMoney, note);
    }
    public static synchronized void transact(Configurable firstParty, Configurable secondParty, Map<ItemComponent, Integer> firstPartyComponents, Map<ItemComponent, Integer> secondPartyComponents, int firstPartyMoney, int secondPartyMoney, String note){
        if (firstParty.equals(secondParty)) throw new ArgumentException("You can't send yourself money");
        if (!DiscordClient.getOurUser().equals(secondParty) && !firstParty.getGoverningObject().equals(secondParty.getGoverningObject())) throw new ArgumentException("You can't send global currency to guild currency and vice versa");
        if (firstPartyComponents == null) firstPartyComponents = Collections.emptyMap();
        if (secondPartyComponents == null) secondPartyComponents = Collections.emptyMap();
        MoneyTransfer moneyTransfer = new MoneyTransfer(firstParty, secondParty, firstPartyComponents, secondPartyComponents, firstPartyMoney, secondPartyMoney, note);
        int money = ConfigHandler.getSetting(CurrentMoneyConfig.class, firstParty) + moneyTransfer.firstPartyMoney;
        if ((firstParty instanceof User || firstParty instanceof GuildUser) && !firstParty.convert(User.class).equals(DiscordClient.getOurUser()) && money < 0) throw new TransactionException(firstParty, -money);
        int secondMoney = ConfigHandler.getSetting(CurrentMoneyConfig.class, firstParty) + moneyTransfer.secondPartyMoney;
        if ((secondParty instanceof User || secondParty instanceof GuildUser) && !secondParty.convert(User.class).equals(DiscordClient.getOurUser()) && secondMoney < 0) throw new TransactionException(secondParty, -secondMoney);
        Map<ItemComponent, Integer> components = ConfigHandler.getSetting(CurrentComponentsConfig.class, firstParty);
        moneyTransfer.firstPartyComponents.forEach((itemComponent, integer) -> components.compute(itemComponent, (c, i) -> {
            if (i == null) i = 0;
            return i - integer;
        }));
        if ((firstParty instanceof User || firstParty instanceof GuildUser) && !firstParty.convert(User.class).equals(DiscordClient.getOurUser())) components.forEach((itemComponent, integer) -> {
            if (integer < 0) throw new TransactionException(firstParty, itemComponent, -integer);
        });
        Map<ItemComponent, Integer> secondComponents = ConfigHandler.getSetting(CurrentComponentsConfig.class, firstParty);
        moneyTransfer.secondPartyComponents.forEach((itemComponent, integer) -> secondComponents.compute(itemComponent, (c, i) -> {
            if (i == null) i = 0;
            return i - integer;
        }));
        if ((secondParty instanceof User || secondParty instanceof GuildUser) && !secondParty.convert(User.class).equals(DiscordClient.getOurUser())) secondComponents.forEach((itemComponent, integer) -> {
            if (integer < 0) throw new TransactionException(firstParty, itemComponent, -integer);
        });
        ConfigHandler.setSetting(CurrentMoneyConfig.class, firstParty, money);
        ConfigHandler.setSetting(CurrentMoneyConfig.class, secondParty, secondMoney);
        ConfigHandler.setSetting(CurrentComponentsConfig.class, firstParty, components);
        ConfigHandler.setSetting(CurrentComponentsConfig.class, secondParty, secondComponents);
        ConfigHandler.alterSetting(MoneyHistoryConfig.class, firstParty, moneyMovements -> {
            moneyMovements.add(moneyTransfer);
            if (moneyMovements.size() > 15) moneyMovements.remove(0);
        });
        ConfigHandler.alterSetting(MoneyHistoryConfig.class, secondParty, moneyMovements -> {
            moneyMovements.add(moneyTransfer.getReverse());
            if (moneyMovements.size() > 15) moneyMovements.remove(0);
        });
    }
    public static void transact(Guild guild, User user, Map<ItemComponent, Integer> firstPartyComponents, int firstPartyMoney, String note){
        AtomicBoolean grant = new AtomicBoolean(firstPartyMoney > 0);
        if (grant.get() && firstPartyComponents != null) firstPartyComponents.forEach((itemComponent, integer) -> {
            if (grant.get() && integer != null) grant.set(integer > 0);
        });
        if (guild != null && grant.get()){
            transact(guild, DiscordClient.getOurUser(), firstPartyComponents, null, firstPartyMoney, 0, note + " tax");
            transact(GuildUser.getGuildUser(guild, user), DiscordClient.getOurUser(), firstPartyComponents, null, firstPartyMoney, 0, note + " in this server");
        }
        transact(user, DiscordClient.getOurUser(), firstPartyComponents, null, firstPartyMoney, 0, note);
    }
    private Configurable firstParty, secondParty;
    private Map<ItemComponent, Integer> firstPartyComponents, secondPartyComponents;
    private int firstPartyMoney, secondPartyMoney;// items are the offers, not gains
    private String note;
    protected MoneyTransfer() {}
    private MoneyTransfer(Configurable firstParty, Configurable secondParty, Map<ItemComponent, Integer> firstPartyComponents, Map<ItemComponent, Integer> secondPartyComponents, int firstPartyMoney, int secondPartyMoney, String note) {
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
        int diff = this.firstPartyMoney - this.secondPartyMoney;
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
