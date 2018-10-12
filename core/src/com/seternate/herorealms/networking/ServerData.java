package com.seternate.herorealms.networking;

import com.badlogic.gdx.utils.XmlReader.Element;
import com.seternate.herorealms.gameObject.Card;
import com.seternate.herorealms.gameObject.CardRole;
import com.seternate.herorealms.gameObject.Deck;
import com.seternate.herorealms.gameObject.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServerData {
    private final HashMap<Integer, Player> players;
    private final Deck marketDeck;
    private final Deck startingDeck;
    private final Deck fireGems;
    private final Deck scoreCards;

    private ServerData() {
        marketDeck = new Deck();
        startingDeck = new Deck();
        fireGems = new Deck();
        scoreCards = new Deck();
        players = new HashMap<Integer, Player>();
    }

    public ServerData(Element gameDataXML) {
        this();
        for(Element cardXML : gameDataXML.getChild(0).getChildrenByName("card")) {
            Card card = new Card(cardXML);
            if(card.getCardRole() == CardRole.MARKET_DECK) marketDeck.add(card);
            else if(card.getCardRole() == CardRole.STARTING_DECK) startingDeck.add(card);
            else if(card.getCardRole() == CardRole.FIRE_GEMS) fireGems.add(card);
            else scoreCards.add(card);
        }
    }

    public void addPlayer(int id, Player player) {
        players.put(id, player);
    }

    public int getPlayerNumber() {
        return players.size();
    }

    public List<Player> getPlayer() {
        ArrayList<Player> players = new ArrayList<Player>();
        for(Map.Entry player : this.players.entrySet()) {
            players.add((Player)player.getValue());
        }
        return players;
    }




}
