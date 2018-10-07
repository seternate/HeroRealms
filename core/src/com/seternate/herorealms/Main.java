package com.seternate.herorealms;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.seternate.herorealms.data.GameResource;
import com.seternate.herorealms.managers.MyAssetManager;
import com.seternate.herorealms.screens.MenuScreen;

public class Main extends Game {
	public SpriteBatch batch;
	public GameResource resources = new GameResource();
	public MyAssetManager assetManager = new MyAssetManager();
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		this.setScreen(new MenuScreen(this));
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		assetManager.dispose();
		super.dispose();
	}

}
