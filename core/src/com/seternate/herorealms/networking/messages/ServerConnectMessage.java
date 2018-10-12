package com.seternate.herorealms.networking.messages;

import com.seternate.herorealms.networking.ServerData;

public class ServerConnectMessage extends ServerMessage {
    public static final String NEW_PLAYER = "newplayer";


    public ServerConnectMessage() {}

    public ServerConnectMessage(ServerData serverData) {
        super.serverData = serverData;
        super.message = ServerConnectMessage.NEW_PLAYER;
    }
}
