package com.seternate.herorealms.networking;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.seternate.herorealms.Main;
import com.seternate.herorealms.gameObject.Card;
import com.seternate.herorealms.gameObject.CardRole;
import com.seternate.herorealms.gameObject.Deck;
import com.seternate.herorealms.gameObject.Defense;
import com.seternate.herorealms.gameObject.Faction;
import com.seternate.herorealms.gameObject.Player;
import com.seternate.herorealms.networking.messages.ClientConnectMessage;
import com.seternate.herorealms.networking.messages.ServerConnectMessage;

import java.io.IOException;
import java.lang.reflect.Array;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import sun.nio.ch.Net;

public class NetworkHelper {
    public Client client;
    public ClientData clientData;
    public Server server;
    public ServerData serverData;
    public final ArrayList<ServerData> availableServers = new ArrayList<ServerData>();


    public NetworkHelper() {
        client = new Client(NetworkConstants.WRITE_BUFFER_SIZE, NetworkConstants.OBJECT_BUFFER_SIZE);
        registerClass(client.getKryo());
        clientData = new ClientData();
    }

    public ArrayList<ServerData> searchAvailableServers() {
        ArrayList<InetAddress> availableServers = new ArrayList<InetAddress>();
        List<InetAddress> allServers = client.discoverHosts(NetworkConstants.UDP_PORT, NetworkConstants.DH_TIMEOUT);
        if(allServers.isEmpty()) return null;
        for(InetAddress server : allServers) {
            if(!server.getHostAddress().equals("127.0.0.1")) availableServers.add(server);
        }
        if(availableServers.isEmpty()) return null;

        final ArrayList<ServerData> serverData = new ArrayList<ServerData>();
        ArrayList<Client> clients = new ArrayList<Client>();
        for(InetAddress server : availableServers) {
            final NetworkHelper networkHelper = new NetworkHelper();
            networkHelper.client.start();
            clients.add(networkHelper.client);
            networkHelper.client.addListener(new Listener() {
                @Override
                public void received(Connection connection, Object object) {
                    if(object instanceof ServerData) {
                        serverData.add((ServerData)object);
                        System.out.println("Got ServerData from Connection " + connection.getID());
                        connection.close();
                    }
                }
            });
            try {
                networkHelper.client.connect(NetworkConstants.C_TIMEOUT, server.getHostAddress(), NetworkConstants.TCP_PORT, NetworkConstants.UDP_PORT);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        for(Client client : clients) {
            while(client.isConnected());
        }
        return serverData;
    }

    public boolean startServer(Main game) {
        server = new Server(NetworkConstants.WRITE_BUFFER_SIZE, NetworkConstants.OBJECT_BUFFER_SIZE);
        server.start();
        serverData = new ServerData(game.gameDataXML, game.player);
        try {
            server.bind(NetworkConstants.TCP_PORT, NetworkConstants.UDP_PORT);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        registerClass(server.getKryo());

        server.addListener(new Listener() {
            @Override
            public void connected(Connection connection) {
                //if(serverData.getPlayerNumber() >= 4) connection.close();
                server.sendToTCP(connection.getID(), serverData);
            }

            @Override
            public void received(Connection connection, Object object) {
                if(object instanceof ClientConnectMessage) {
                    Player player = ((ClientConnectMessage)object).getData();
                    serverData.addPlayer(connection.getID(), player);
                    server.sendToAllTCP(new ServerConnectMessage(serverData));
                }
            }
        });
        return true;
    }






    public boolean isServer() {
        List<InetAddress> addresses = client.discoverHosts(NetworkConstants.UDP_PORT, NetworkConstants.DH_TIMEOUT);
        if(addresses.isEmpty()) return false;
        for(InetAddress address : addresses) {
            if(!address.getHostAddress().equals("127.0.0.1")) return true;
        }
        return false;
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
    }

}
