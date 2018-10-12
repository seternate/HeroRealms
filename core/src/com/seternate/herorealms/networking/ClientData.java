package com.seternate.herorealms.networking;

import java.net.InetAddress;

public class ClientData {
    InetAddress address;

    public boolean setServerAddress(InetAddress address) {
        if(address == null) return false;
        this.address = address;
        return true;
    }

    public InetAddress getServerAddress() {
        return address;
    }
}
