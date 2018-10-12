package com.seternate.herorealms.networking.messages;

import com.seternate.herorealms.networking.ServerData;

public class ServerMessage implements Message<ServerData> {
    String message;
    ServerData serverData;


    public ServerMessage() {}

    public ServerMessage(String message, ServerData serverData) {
        this.message = message;
        this.serverData = serverData;
    }

    @Override
    public ServerData getData() {
        return serverData;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
