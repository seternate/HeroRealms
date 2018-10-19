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
            while (true) {
                if (searchServer) {
                    Client client = new MyClient();
                    ArrayList<InetAddress> servers = new ArrayList<InetAddress>();
                    final ArrayList<ServerData> data = new ArrayList<ServerData>();
                    ArrayList<Client> helperClients = new ArrayList<Client>();
                    List<InetAddress> allServers = client.discoverHosts(NetworkConstants.UDP_PORT, NetworkConstants.DH_TIMEOUT);

                    availableServers.clear();
                    if (allServers.isEmpty()) continue;

                    for (InetAddress server : allServers) {
                        if (!server.getHostAddress().equals("127.0.0.1")) servers.add(server);
                    }

                    if (servers.isEmpty()) continue;

                    for (InetAddress server : servers) {
                        final Client helperClient = new MyClient();
                        helperClients.add(helperClient);
                        helperClient.addListener(new Listener() {
                            @Override
                            public void received(Connection connection, Object object) {
                                if (object instanceof ServerData) {
                                    data.add((ServerData) object);
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

                    for (Client helperClient : helperClients) while (helperClient.isConnected()) ;
                    for (ServerData d : data) availableServers.add(d);
                }else {
                    try {
                        this.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    };

    public static void searchServers(boolean running) {
        if (!searchServerThread.isAlive() && !searchServer) {
            searchServerThread.start();
        }
        searchServer = running;
    }

    public static ArrayList<ServerData> getAvailableServers() {
        return availableServers;
    }
}