package com.seternate.herorealms.managers;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.XmlReader;
import com.seternate.herorealms.Main;

public class MyAssetManager {
    public final AssetManager manager = new AssetManager();
    private Main game;

    public MyAssetManager(Main game) {
        this.game = game;
    }

    public void loadAssets() {
        for(XmlReader.Element cards : game.xml.getChild(0).getChildrenByName("card")) {
            for (XmlReader.Element image : cards.getChild(0).getChildrenByName("image")) {
                manager.load(image.getText(), Texture.class);
            }
        }
    }

    public void dispose() {
        manager.clear();
    }
}
