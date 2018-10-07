package com.seternate.herorealms;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.seternate.herorealms.managers.MyAssetManager;
import com.seternate.herorealms.screens.LoadingScreen;

public class Main extends Game {
	public SpriteBatch batch;
	public Element xml;
	public MyAssetManager assetManager = new MyAssetManager(this);
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		loadGameData();
		this.setScreen(new LoadingScreen(this));
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

	public void loadGameData() {
		XmlReader xmlReader = new XmlReader();
		xml = xmlReader.parse(Gdx.files.internal("data.xml"));
	}

}
