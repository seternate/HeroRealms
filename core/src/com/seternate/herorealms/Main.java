package com.seternate.herorealms;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.seternate.herorealms.managers.MyAssetManager;
import com.seternate.herorealms.managers.ScreenManager;
import com.seternate.herorealms.player.Player;
import com.seternate.herorealms.screens.SplashScreen;

public class Main extends Game {
	public Element gameDataXML;
	public MyAssetManager assetManager = new MyAssetManager(this);
	public ScreenManager screenManager = new ScreenManager();
	public Player player;

	
	@Override
	public void create () {
		player = Player.load();

		screenManager.push(SplashScreen.newSplashScreen(this));
		this.setScreen(SplashScreen.getSplashScreen());
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
