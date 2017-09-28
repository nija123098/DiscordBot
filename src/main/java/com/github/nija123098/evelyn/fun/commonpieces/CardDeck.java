package com.github.nija123098.evelyn.fun.commonpieces;

import com.github.nija123098.evelyn.util.EmoticonHelper;
import com.github.nija123098.evelyn.util.Rand;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Made by nija123098 on 5/25/2017.
 */
public class CardDeck {
    private final List<Card> inPile = new ArrayList<>(52);
    public CardDeck() {
        for (Suit suit : Suit.values()){
            for (int i = 2; i < 15; ++i) {
                inPile.add(new CardDeck.Card(suit, i));
            }
        }
    }
    public Hand makeHand(int cardCount){
        return new Hand(this, cardCount);
    }
    private Card draw(){
        return Rand.getRand(this.inPile, true);
    }
    public static class Card{
        private Suit suit;
        private int number;
        Card(Suit suit, int number) {
            this.suit = suit;
            this.number = number;
        }
        public Suit getSuit() {
            return this.suit;
        }
        public int getNumber() {
            return this.number;
        }
        public String numberSymbol(){
            return this.number < 11 ? String.valueOf(this.number) : this.numberName().substring(0, 1);
        }
        public String numberName(){
            switch (this.number){
                case 11: return "Jack";
                case 12: return "Queen";
                case 13: return "King";
                case 14: return "Ace";
            }
            return String.valueOf(this.number);
        }
        public String name(){
            return this.numberName() + " of " + this.suit.name();
        }
        @Override
        public String toString(){
            return "[" + this.suit.symbol + this.numberSymbol() + "]";
        }
    }
    public enum Suit{
        SPADES,
        HEARTS,
        DIAMONDS,
        CLUBS,;
        private String symbol;
        Suit() {
            this.symbol = EmoticonHelper.getChars(this.name().toLowerCase(), true);
        }
        public String getSymbol() {
            return this.symbol;
        }
    }
    public class Hand{
        private List<Card> hand;
        private CardDeck deck;
        private Hand(CardDeck deck, int draws){
            this.deck = deck;
            this.hand = new ArrayList<>(draws);
            while (--draws > -1) {
                this.hand.add(deck.draw());
            }
        }
        public Card draw(){
            Card card = Rand.getRand(this.deck.inPile, true);
            this.hand.add(card);
            return card;
        }
        public List<Card> getHand() {
            return this.hand;
        }
        @Override
        public String toString(){
            StringBuilder builder = new StringBuilder();
            this.hand.forEach(card -> builder.append(card.toString()));
            return builder.toString();
        }
    }
    public enum ValueRule{
        BLACK_JACK {
            @Override
            public int value(Hand hand) {
                AtomicInteger total = new AtomicInteger();
                AtomicBoolean eleven = new AtomicBoolean(true);
                while (true){
                    hand.hand.forEach(card -> total.addAndGet(card.number == 14 ? eleven.get() ? 11 : 1 : Math.min(card.number, 10)));
                    if (total.get() < 22 || !eleven.get()) break;
                    eleven.set(false);
                    total.set(0);
                }
                return total.get();
            }
        },;
        public abstract int value(Hand hand);
    }
}
