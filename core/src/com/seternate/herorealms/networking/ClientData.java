package com.seternate.herorealms.networking;

import com.seternate.herorealms.gameObject.Player;

public class ClientData {
    private ServerData server;
    public int networkID;
    private Player player;
    private boolean ready;


    public ClientData(){ready = false;}

    public ClientData(Player player) {
        this();
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

    public boolean isReady() {
        return ready;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }

}
