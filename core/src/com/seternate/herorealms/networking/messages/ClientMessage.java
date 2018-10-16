package com.seternate.herorealms.networking.messages;

import com.seternate.herorealms.networking.ClientData;

public class ClientMessage implements Message<ClientData> {
    String message;
    ClientData data;


    public ClientMessage() {}

    public ClientMessage(String message, ClientData data) {
        this.message = message;
        this.data = data;
    }

    @Override
    public ClientData getData() {
        return data;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
