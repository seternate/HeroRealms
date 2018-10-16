package com.seternate.herorealms.networking.messages;

import com.seternate.herorealms.networking.ClientData;

public class ClientConnectMessage extends ClientMessage{
    public static final String NEW_PLAYER = "newplayer";


    public ClientConnectMessage() {}

    public ClientConnectMessage(ClientData data) {
        super(ClientConnectMessage.NEW_PLAYER, data);
    }
}
