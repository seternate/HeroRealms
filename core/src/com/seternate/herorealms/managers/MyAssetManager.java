package com.seternate.herorealms.managers;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;

public class MyAssetManager {
    public final AssetManager manager = new AssetManager();

    public void loadAssets() {
        manager.load("BAS-EN-001-arkus-imperial-dragon.jpg", Texture.class);



    }

    public void dispose() {
        manager.clear();
    }
}
