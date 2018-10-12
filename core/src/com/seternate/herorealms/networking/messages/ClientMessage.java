package com.seternate.herorealms.networking.messages;

import com.seternate.herorealms.gameObject.Player;

public class ClientMessage implements Message<Player> {
    String message;
    Player player;


    public ClientMessage() {}

    public ClientMessage(String message, Player player) {
        this.message = message;
        this.player = player;
    }

    @Override
    public Player getData() {
        return player;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
