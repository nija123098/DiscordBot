package com.github.kaaz.emily.economy;

import com.github.kaaz.emily.config.ConfigHandler;
import com.github.kaaz.emily.config.Configurable;
import com.github.kaaz.emily.discordobjects.wrappers.User;
import com.github.kaaz.emily.economy.configs.MoneyHistoryConfig;
import com.github.kaaz.emily.economy.configs.TransferTaxConfig;

/**
 * Made by nija123098 on 5/1/2017.
 */
public class MoneyTransfer {
    public static void transfer(int toFirstParty, Configurable firstParty, Configurable secondParty, String message){
        transfer(toFirstParty, firstParty, secondParty, message, message);
    }
    public static void transfer(int toFirstParty, Configurable firstParty, Configurable secondParty, String messageToFirst, String messageToSecond){
        if (toFirstParty == 0) return;
        boolean firstEarner = toFirstParty > 0;
        if (!firstEarner){
            Configurable switc = firstParty;
            firstParty = secondParty;
            secondParty = switc;
        }
        Configurable gov = (firstEarner ? firstParty : secondParty).getGoverningObject();
        float tax = ConfigHandler.getSetting(TransferTaxConfig.class, gov) * Math.abs(toFirstParty);
        straightTransfer(tax, gov, secondParty, "Tax");
        straightTransfer(toFirstParty - tax, firstParty, secondParty, messageToFirst);
        straightTransfer(-toFirstParty, secondParty, firstParty, messageToSecond);
    }
    public static void straightTransfer(float toFirstParty, Configurable firstParty, Configurable secondParty, String message){
        ConfigHandler.changeSetting(MoneyHistoryConfig.class, firstParty, moneyTransfers -> {
            moneyTransfers.add(new MoneyTransfer(toFirstParty, secondParty, message));
            return moneyTransfers;
        });
    }
    private float amount;
    private String userID;
    private String message;
    private MoneyTransfer(float amount, Configurable otherEnd, String message) {
        this.amount = amount;
        this.userID = otherEnd.getID();
        this.message = message;
    }
    public MoneyTransfer(float amount, String userID, String message) {
        this.amount = amount;
        this.userID = userID;
        this.message = message;
    }
    public float getAmount() {
        return amount;
    }
    public User getUser() {
        return User.getUser(this.userID);
    }
    public String getMessage() {
        return message;
    }

    public String getUserID() {
        return userID;
    }
}
