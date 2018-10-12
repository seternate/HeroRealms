package com.seternate.herorealms.networking;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

public class ClientData {
    ServerData connectedServer;
    private ArrayList<ServerData> availableServers;

    public ArrayList<ServerData> getAvailableServers() {
        return availableServers;
    }

    public void setAvailableServers(ArrayList<ServerData> availableServers) {
        this.availableServers = availableServers;
    }

    public ClientData() {
        availableServers = new ArrayList<ServerData>();
    }


    public void setServer(ServerData server) {
        connectedServer = server;
    }


}
