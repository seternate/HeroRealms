package com.seternate.herorealms.networking;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.seternate.herorealms.gameObject.Card;
import com.seternate.herorealms.gameObject.CardRole;
import com.seternate.herorealms.gameObject.Deck;
import com.seternate.herorealms.gameObject.Defense;
import com.seternate.herorealms.gameObject.Faction;
import com.seternate.herorealms.gameObject.Player;
import com.seternate.herorealms.networking.messages.ClientConnectMessage;
import com.seternate.herorealms.networking.messages.ServerConnectMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public final class MyClient extends Client {
    private ClientData data;


    public MyClient() {
        super(NetworkConstants.WRITE_BUFFER_SIZE, NetworkConstants.OBJECT_BUFFER_SIZE);
        registerClass(this.getKryo());
    }

    public MyClient(Player player) {
        super(NetworkConstants.WRITE_BUFFER_SIZE, NetworkConstants.OBJECT_BUFFER_SIZE);
        registerClass(this.getKryo());
        data = new ClientData(player);
    }

    private void registerClass(Kryo kryo) {
        kryo.register(ClientConnectMessage.class);
        kryo.register(ServerConnectMessage.class);
        kryo.register(Player.class);
        kryo.register(ServerData.class);
        kryo.register(Deck.class);
        kryo.register(Card.class);
        kryo.register(HashMap.class);
        kryo.register(Defense.class);
        kryo.register(CardRole.class);
        kryo.register(Faction.class);
        kryo.register(String[].class);
        kryo.register(ArrayList.class);
        kryo.register(ClientData.class);
    }

    public void connect(String ipAddress) {
        try {
            super.connect(NetworkConstants.C_TIMEOUT, ipAddress, NetworkConstants.TCP_PORT, NetworkConstants.UDP_PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setID(int id) {
        data.networkID = id;
    }

    public void updateServerData(ServerData serverData) {
        data.setServerData(serverData);
    }

    public ClientData getData() {
        return data;
    }

    public ServerData getServerData() {
        return data.getServerData();
    }

}
