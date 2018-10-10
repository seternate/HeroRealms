package com.seternate.herorealms;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.seternate.herorealms.managers.MyAssetManager;
import com.seternate.herorealms.managers.ScreenManager;
import com.seternate.herorealms.player.Player;
import com.seternate.herorealms.screens.SplashScreen;

import org.json.JSONException;
import org.json.JSONObject;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class Main extends Game {
	public Element xml;
	public MyAssetManager assetManager = new MyAssetManager(this);
	public ScreenManager screenManager = new ScreenManager();
	public Player player;

	private Socket socket;
	
	@Override
	public void create () {
		player = Player.load();
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
			socket = IO.socket("http://192.168.0.164:8080");
			socket.connect();
		} catch(Exception e) {
			System.out.println(e);
		}
	}

	public void configSocketEvents() {
		socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
			@Override
			public void call(Object... args) {
				Gdx.app.log("SocketIO", "connected");
			}
		}).on("socketID", new Emitter.Listener() {
			@Override
			public void call(Object... args) {
				JSONObject data = (JSONObject) args[0];
				try {
					String id = data.getString("id");
					Gdx.app.log("SocketIO", "My ID: " + id);
				} catch (JSONException e) {
					Gdx.app.log("SocketIO", "Error getting ID");
				}

			}
		});
	}
}
