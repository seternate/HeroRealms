package com.seternate.herorealms;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.seternate.herorealms.managers.MyAssetManager;
import com.seternate.herorealms.managers.ScreenManager;
import com.seternate.herorealms.screens.SplashScreen;

import io.socket.client.IO;
import io.socket.client.Socket;

public class Main extends Game {
	public SpriteBatch batch;
	public Element xml;
	public MyAssetManager assetManager = new MyAssetManager(this);
	public ScreenManager screenManager = new ScreenManager();

	private Socket socket;
	
	@Override
	public void create () {
        connectSocket();
		this.setScreen(screenManager.add(new SplashScreen(this)));
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

	public void connectSocket() {
		try {
			socket = IO.socket("http://127.0.0.1:8080");
			socket.connect();
		} catch(Exception e) {
			System.out.println(e);
		}
	}
}
