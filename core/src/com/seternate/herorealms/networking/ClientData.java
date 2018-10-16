package com.seternate.herorealms.networking;

import com.seternate.herorealms.gameObject.Player;

public class ClientData {
    private ServerData server;
    public int networkID;
    private Player player;


    public ClientData(){}

    public ClientData(Player player) {
        this.player = player;
    }

    public void setServerData(ServerData server) {
        this.server = server;
    }

    public ServerData getServerData() {
        return server;
    }

    public Player getPlayer() {
        return player;
    }

}
