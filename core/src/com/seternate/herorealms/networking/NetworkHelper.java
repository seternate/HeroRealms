package com.seternate.herorealms.networking;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

public class NetworkHelper {
    private static boolean searchServer = false;
    private static volatile ArrayList<ServerData> availableServers = new ArrayList<ServerData>();

    private static Thread searchServerThread = new Thread() {
        @Override
        public void run() {
            while(true) {
                if(searchServer) {
                    Client client = new MyClient();
                    ArrayList<InetAddress> servers = new ArrayList<InetAddress>();
                    final ArrayList<ServerData> data = new ArrayList<ServerData>();
                    ArrayList<Client> helperClients = new ArrayList<Client>();
                    List<InetAddress> allServers = client.discoverHosts(NetworkConstants.UDP_PORT, NetworkConstants.DH_TIMEOUT);

                    if(allServers.isEmpty()) continue;

                    for(InetAddress server : allServers) {
                        if(!server.getHostAddress().equals("127.0.0.1")) servers.add(server);
                    }

                    if(servers.isEmpty()) continue;

                    for(InetAddress server : servers) {
                        final Client helperClient = new MyClient();
                        helperClients.add(helperClient);
                        helperClient.addListener(new Listener() {
                            @Override
                            public void received(Connection connection, Object object) {
                                if(object instanceof ServerData) {
                                    data.add((ServerData)object);
                                    connection.close();
                                    helperClient.close();
                                }
                            }
                        });
                        helperClient.start();
                        try {
                            helperClient.connect(NetworkConstants.C_TIMEOUT, server.getHostAddress(), NetworkConstants.TCP_PORT, NetworkConstants.UDP_PORT);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    for(Client helperClient : helperClients) while(helperClient.isConnected());
                    availableServers.clear();
                    for(ServerData d : data) availableServers.add(d);
                }
            }

        }
    };

    public static void searchServers(boolean running) {
        System.out.println(searchServer );
        if(!searchServerThread.isAlive() && !searchServer) {
            searchServerThread.start();
        }
        searchServer = running;
        System.out.println(searchServer + " " + searchServerThread.isAlive());
    }

    public static ArrayList<ServerData> getAvailableServers() {
        return availableServers;
    }







    /*final Main game;
    public Client client;
    public ClientData clientData;
    public Server server;
    public volatile ServerData serverData;
    public volatile ArrayList<ServerData> availableServers = new ArrayList<ServerData>();
    private boolean serverRunning;


    public NetworkHelper(final Main game) {
        this.game = game;
        client = new Client(NetworkConstants.WRITE_BUFFER_SIZE, NetworkConstants.OBJECT_BUFFER_SIZE);
        registerClass(client.getKryo());
        clientData = new ClientData();
        serverRunning = false;
        client.start();
        client.addListener(new Listener() {
            @Override
            public void received(Connection connection, Object object) {
                if(object instanceof ServerData) {
                    clientData.connectedServer = (ServerData)object;
                }
            }

            @Override
            public void connected(Connection connection) {
                game.player.setNetworkID(connection.getID());
                client.sendTCP(new ClientConnectMessage(game.player));
            }
        });
    }

    public void searchAvailableServers() {
        final ArrayList<InetAddress> availableServers = new ArrayList<InetAddress>();
        List<InetAddress> allServers = client.discoverHosts(NetworkConstants.UDP_PORT, NetworkConstants.DH_TIMEOUT);
        final ArrayList<ServerData> serverData = new ArrayList<ServerData>();
        ArrayList<Client> clients = new ArrayList<Client>();

        if(allServers.isEmpty()) return;

        for(InetAddress server : allServers) {
            if(!server.getHostAddress().equals("127.0.0.1")) availableServers.add(server);
        }

        if(availableServers.isEmpty()) return;

        for(InetAddress server : availableServers) {
            final NetworkHelper networkHelper = new NetworkHelper(game);
            clients.add(networkHelper.client);
            networkHelper.client.addListener(new Listener() {
                @Override
                public void received(Connection connection, Object object) {
                    if(object instanceof ServerData) {
                        serverData.add((ServerData)object);
                        connection.close();
                        networkHelper.client.stop();
                        networkHelper.client.close();
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

        this.availableServers.clear();
        for(ServerData data : serverData) this.availableServers.add(data);
    }

    public boolean connect(String owner) {
        ArrayList<ServerData> servers = availableServers;
        String ipAddress = null;
        for(ServerData server : servers){
            if(server.getServerOwner().getName().equals(owner)) ipAddress = server.getIPAddress();
        }
        if(ipAddress == null) return false;
        try {
            client.connect(NetworkConstants.C_TIMEOUT, ipAddress, NetworkConstants.TCP_PORT, NetworkConstants.UDP_PORT);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean startServer() {
        if(isServerRunning()) return false;
        server = new Server(NetworkConstants.WRITE_BUFFER_SIZE, NetworkConstants.OBJECT_BUFFER_SIZE);
        server.start();
        serverData = new ServerData(game.gameDataXML, game.player);
        try {
            serverData.serverIP = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return false;
        }
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
                server.sendToTCP(connection.getID(), serverData);
                if(serverData.getPlayerNumber() >= 4) connection.close();
                System.out.println(serverData.getPlayerNumber());
            }

            @Override
            public void received(Connection connection, Object object) {
                if(object instanceof ClientConnectMessage) {
                    Player player = ((ClientConnectMessage)object).getData();
                    serverData.addPlayer(connection.getID(), player);
                    server.sendToAllTCP(new ServerConnectMessage(serverData));
                }
            }

            @Override
            public void disconnected(Connection connection) {
                serverData.removePlayerById(connection.getID());
                server.sendToAllTCP(new ServerConnectMessage(serverData));
            }
        });
        serverRunning = true;
        return true;
    }

    public void stopServer() {
        server.close();
        serverRunning = false;
    }

    public void close() {
        client.stop();
        client.close();
        stopServer();
    }

    public boolean isServerRunning() {
        return serverRunning;
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
    }*/

}
