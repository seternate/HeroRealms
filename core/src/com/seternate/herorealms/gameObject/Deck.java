package com.seternate.herorealms.gameObject;

import java.util.HashMap;

public class Deck {
    HashMap<Integer, Card> deck;


    public Deck() {
        deck = new HashMap<Integer, Card>();
    }

    public void add(Card card) {
        deck.put(card.getId(), card);
    }

}
