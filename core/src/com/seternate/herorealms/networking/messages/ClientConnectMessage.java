package com.seternate.herorealms.networking.messages;

import com.seternate.herorealms.gameObject.Player;

public class ClientConnectMessage extends ClientMessage{
    public static final String NEW_PLAYER = "newplayer";


    public ClientConnectMessage() {}

    public ClientConnectMessage(Player player) {
        super.player = player;
        super.message = ClientConnectMessage.NEW_PLAYER;
    }
}
