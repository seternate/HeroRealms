package com.seternate.herorealms.gameObject;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.seternate.herorealms.networking.ServerData;

public final class ServerLabel extends Label {
    private ServerData data;


    public ServerLabel(CharSequence text, Skin skin, String styleName) {
        super(text, skin, styleName);
    }

    public ServerLabel(ServerData data, Skin skin, String styleName) {
        super(data.getOwner(), skin, styleName);
        this.data = data;
    }

    public String getIPAddress() {
        return data.getIPAddress();
    }

    public void setText(ServerData data) {
        super.setText(data.getOwner());
        this.data = data;
    }


}
