package com.github.nija123098.evelyn.economy.currencytransfer;

import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.config.Configurable;
import com.github.nija123098.evelyn.config.GuildUser;
import com.github.nija123098.evelyn.discordobjects.wrappers.DiscordClient;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.economy.configs.CurrencyHistoryConfig;
import com.github.nija123098.evelyn.economy.configs.CurrentComponentsConfig;
import com.github.nija123098.evelyn.economy.configs.CurrentCurrencyConfig;
import com.github.nija123098.evelyn.exception.ArgumentException;
import com.github.nija123098.evelyn.exception.TransactionException;
import com.github.nija123098.evelyn.perms.BotRole;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Stream;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class CurrencyTransfer {
    public static void transact(Configurable firstParty, Configurable secondParty, int firstPartyMoney, int secondPartyMoney, String note){
        transact(firstParty, secondParty, null, null, firstPartyMoney, secondPartyMoney, note);
    }
    public static synchronized void transact(Configurable firstParty, Configurable secondParty, Map<ItemComponent, Integer> firstPartyComponents, Map<ItemComponent, Integer> secondPartyComponents, int firstPartyMoney, int secondPartyMoney, String note){
        if (firstParty.equals(secondParty)) throw new ArgumentException("You can't send yourself money");
        if (!DiscordClient.getOurUser().equals(secondParty) && !firstParty.getGoverningObject().equals(secondParty.getGoverningObject())) throw new ArgumentException("You can't send global currency to guild currency and vice versa");
        if (firstPartyComponents == null) firstPartyComponents = Collections.emptyMap();
        if (secondPartyComponents == null) secondPartyComponents = Collections.emptyMap();
        CurrencyTransfer moneyTransfer = new CurrencyTransfer(firstParty, secondParty, firstPartyComponents, secondPartyComponents, firstPartyMoney, secondPartyMoney, note);
        int money = ConfigHandler.getSetting(CurrentCurrencyConfig.class, firstParty) + moneyTransfer.firstPartyMoney;
        if ((firstParty instanceof User || firstParty instanceof GuildUser) && !firstParty.convert(User.class).equals(DiscordClient.getOurUser()) && money < 0) throw new TransactionException(firstParty, -money);
        int secondMoney = ConfigHandler.getSetting(CurrentCurrencyConfig.class, firstParty) + moneyTransfer.secondPartyMoney;
        if ((secondParty instanceof User || secondParty instanceof GuildUser) && !secondParty.convert(User.class).equals(DiscordClient.getOurUser()) && secondMoney < 0) throw new TransactionException(secondParty, -secondMoney);
        Map<ItemComponent, Integer> components = ConfigHandler.getSetting(CurrentComponentsConfig.class, User.getUser(firstParty.getID()));
        moneyTransfer.firstPartyComponents.forEach((itemComponent, integer) -> components.compute(itemComponent, (c, i) -> {
            if (i == null) i = 0;
            return i - integer;
        }));
        if ((firstParty instanceof User || firstParty instanceof GuildUser) && !firstParty.convert(User.class).equals(DiscordClient.getOurUser())) components.forEach((itemComponent, integer) -> {
            if (integer < 0) throw new TransactionException(firstParty, itemComponent, -integer);
        });
        Map<ItemComponent, Integer> secondComponents = ConfigHandler.getSetting(CurrentComponentsConfig.class, User.getUser(firstParty.getID()));
        moneyTransfer.secondPartyComponents.forEach((itemComponent, integer) -> secondComponents.compute(itemComponent, (c, i) -> {
            if (i == null) i = 0;
            return i - integer;
        }));
        if ((secondParty instanceof User || secondParty instanceof GuildUser) && !secondParty.convert(User.class).equals(DiscordClient.getOurUser())) secondComponents.forEach((itemComponent, integer) -> {
            if (integer < 0) throw new TransactionException(firstParty, itemComponent, -integer);
        });
        ConfigHandler.setSetting(CurrentCurrencyConfig.class, firstParty, money);
        ConfigHandler.setSetting(CurrentCurrencyConfig.class, secondParty, secondMoney);
        ConfigHandler.setSetting(CurrentComponentsConfig.class, User.getUser(firstParty.getID()), components);
        ConfigHandler.setSetting(CurrentComponentsConfig.class, User.getUser(secondParty.getID()), secondComponents);
        ConfigHandler.alterSetting(CurrencyHistoryConfig.class, firstParty, moneyMovements -> {
            moneyMovements.add(moneyTransfer);
            if (moneyMovements.size() > 15) moneyMovements.remove(0);
        });
        ConfigHandler.alterSetting(CurrencyHistoryConfig.class, secondParty, moneyMovements -> {
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
        transact(user, DiscordClient.getOurUser(), firstPartyComponents, null, firstPartyMoney * ((grant.get() && BotRole.SUPPORTER.hasRequiredRole(user, null)) ? 2 : 1), 0, note);
    }
    private Configurable firstParty, secondParty;
    private Map<ItemComponent, Integer> firstPartyComponents, secondPartyComponents;
    private int firstPartyMoney, secondPartyMoney;// items are the offers, not gains
    private String note;
    protected CurrencyTransfer() {}
    private CurrencyTransfer(Configurable firstParty, Configurable secondParty, Map<ItemComponent, Integer> firstPartyComponents, Map<ItemComponent, Integer> secondPartyComponents, int firstPartyMoney, int secondPartyMoney, String note) {
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
    private CurrencyTransfer getReverse(){
        return new CurrencyTransfer(this.secondParty, this.firstParty, this.secondPartyComponents, this.firstPartyComponents, this.secondPartyMoney, this.firstPartyMoney, this.note);
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
