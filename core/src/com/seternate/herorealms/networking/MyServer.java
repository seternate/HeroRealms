package com.seternate.herorealms.networking;

import com.badlogic.gdx.utils.XmlReader;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.seternate.herorealms.gameObject.Card;
import com.seternate.herorealms.gameObject.CardRole;
import com.seternate.herorealms.gameObject.Deck;
import com.seternate.herorealms.gameObject.Defense;
import com.seternate.herorealms.gameObject.Faction;
import com.seternate.herorealms.gameObject.Player;
import com.seternate.herorealms.networking.messages.ClientConnectMessage;
import com.seternate.herorealms.networking.messages.ServerConnectMessage;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;

public final class MyServer extends Server {
    private ServerData data;
    private boolean running;

    public MyServer(XmlReader.Element gameDataXML, Player player) {
        super(NetworkConstants.WRITE_BUFFER_SIZE, NetworkConstants.OBJECT_BUFFER_SIZE);
        registerClass(this.getKryo());
        try {
            data = new ServerData(gameDataXML, InetAddress.getLocalHost().getHostAddress(), player);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        this.addListener(new Listener() {
            @Override
            public void connected(Connection connection) {
                sendToTCP(connection.getID(), data);
                if(data.getPlayerNumber() >= 4) connection.close();
            }
            @Override
            public void disconnected(Connection connection) {
                data.removePlayer(connection.getID());
                sendToAllTCP(data);
            }
            @Override
            public void received(Connection connection, Object object) {
                if(object instanceof ClientConnectMessage) {
                    ClientData player = ((ClientConnectMessage)object).getData();
                    data.addPlayer(player);
                    System.out.println(data.getPlayerNumber());
                    sendToAllTCP(data);
                }
            }
        });
        running = false;
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

    @Override
    public void start() {
        super.start();
        try {
            this.bind(NetworkConstants.TCP_PORT, NetworkConstants.UDP_PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        running = true;
    }

    @Override
    public void stop() {
        super.stop();
        running = false;
    }

    public boolean isRunning() {
        return running;
    }
}
