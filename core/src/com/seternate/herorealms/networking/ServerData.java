package com.seternate.herorealms.networking;

import com.badlogic.gdx.utils.XmlReader.Element;
import com.seternate.herorealms.gameObject.Card;
import com.seternate.herorealms.gameObject.CardRole;
import com.seternate.herorealms.gameObject.Deck;
import com.seternate.herorealms.gameObject.Player;

import java.util.ArrayList;
import java.util.HashMap;

public class ServerData {
    private String serverIP;
    private Player owner;
    private final HashMap<Integer, ClientData> players;
    private final Deck marketDeck;
    private final Deck startingDeck;
    private final Deck fireGems;
    private final Deck scoreCards;

    private ServerData() {
        marketDeck = new Deck();
        startingDeck = new Deck();
        fireGems = new Deck();
        scoreCards = new Deck();
        players = new HashMap<Integer, ClientData>();
    }

    public ServerData(Element gameDataXML, String serverIP, Player owner) {
        this();
        this.serverIP = serverIP;
        this.owner = owner;
        for(Element cardXML : gameDataXML.getChild(0).getChildrenByName("card")) {
            Card card = new Card(cardXML);
            if(card.getCardRole() == CardRole.MARKET_DECK) marketDeck.add(card);
            else if(card.getCardRole() == CardRole.STARTING_DECK) startingDeck.add(card);
            else if(card.getCardRole() == CardRole.FIRE_GEMS) fireGems.add(card);
            else scoreCards.add(card);
        }
    }

    public int getPlayerNumber() {
        return players.size();
    }

    public String getIPAddress() {
        return serverIP;
    }

    public String getOwner() {
        return owner.getName();
    }

    public boolean isOwner(Player player) {
        return owner.equals(player);
    }

    public void addPlayer(ClientData player) {
        players.put(player.networkID, player);
    }

    public ArrayList<ClientData> getPlayers() {
        ArrayList<ClientData> data = new ArrayList<ClientData>();
        for(ClientData player : players.values()) {
            data.add(player);
        }
        return data;
    }

    public ClientData removePlayer(int connectionID) {
        return players.remove(connectionID);
    }

    public boolean allReady() {
        for(ClientData player : players.values()) {
            if(!player.isReady()) return false;
        }
        return true;
    }

    public void updatePlayer(ClientData player) {
        players.put(player.networkID, player);
    }
}
