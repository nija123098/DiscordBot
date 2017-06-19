package com.github.kaaz.emily.fun.blackjack;

import com.github.kaaz.emily.config.Configurable;
import com.github.kaaz.emily.fun.commonpieces.CardDeck;
import com.github.kaaz.emily.util.LanguageHelper;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Made by nija123098 on 5/25/2017.
 */
public class BlackJackGame {
    private static final Map<Configurable, BlackJackGame> GAME_MAP = new ConcurrentHashMap<>();
    public static BlackJackGame getGame(Configurable configurable){
        return GAME_MAP.get(configurable);
    }
    public static void setGame(Configurable configurable, BlackJackGame game){
        GAME_MAP.put(configurable, game);
    }
    private Configurable player;
    private CardDeck.Hand dealerHand, playerHand;
    public BlackJackGame(Configurable player) {
        this.player = player;
        CardDeck deck = new CardDeck();
        this.dealerHand = deck.makeHand(1);
        this.playerHand = deck.makeHand(2);
    }
    public int playerHit(){
        this.playerHand.draw();
        return value(this.playerHand);
    }
    public int dealerHit(){
        this.dealerHand.draw();
        return value(this.dealerHand);
    }
    public int playerValue(){
        return value(this.playerHand);
    }
    @Override
    public String toString(){
        return "Dealer's hand (" + value(this.dealerHand) + ")\n" + this.dealerHand.toString() + "\n\n" + LanguageHelper.makePleural(this.player.getName()) + " hand (" + value(this.playerHand) + ")\n" + this.playerHand.toString();
    }
    public boolean playerBlackJack(){
        return blackJack(this.playerHand);
    }
    public boolean dealerBlackJack(){
        return blackJack(this.dealerHand);
    }
    public static int value(CardDeck.Hand hand){
        return CardDeck.ValueRule.BLACK_JACK.value(hand);
    }
    public static boolean blackJack(CardDeck.Hand hand){
        return hand.getHand().size() != 2 && (isJack(hand.getHand().get(0)) && isAce(hand.getHand().get(1)) || isJack(hand.getHand().get(1)) && isAce(hand.getHand().get(0)));
    }
    private static boolean isJack(CardDeck.Card card){
        return card.getNumber() == 11 && (card.getSuit().equals(CardDeck.Suit.CLUBS) || card.getSuit().equals(CardDeck.Suit.SPADES));
    }
    private static boolean isAce(CardDeck.Card card){
        return card.getNumber() == 14;
    }
}
