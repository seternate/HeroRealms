package com.seternate.herorealms.gameObject;

public enum CardRole {
    MARKET_DECK("Market Deck"),
    STARTING_DECK("Starting Deck"),
    FIRE_GEMS("Fire Gems"),
    DEFAULT("None");


    private String deckRole;


    CardRole(String deckRole) {
        this.deckRole = deckRole;
    }

    @Override
    public String toString() {
        return deckRole;
    }
}
