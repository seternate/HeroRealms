package com.seternate.herorealms.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.seternate.herorealms.Main;

import org.json.JSONObject;

import java.io.IOException;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.List;


public class LobbyScreen implements Screen {
    private static LobbyScreen lobbyScreen = null;

    public static LobbyScreen getLobbyScreen() {
        return lobbyScreen;
    }

    public static LobbyScreen newLobbyScreen(final Main game) {
        if(lobbyScreen == null) lobbyScreen = new LobbyScreen(game);
        return lobbyScreen;
    }


    final Main game;
    Stage stage;
    Server server;
    Client client;

    TextButton sendMessage;
    Skin skin;



    public void createCommunication() {
        server = new Server();
        server.start();
        try {
            server.bind(9021, 9021);
        } catch (IOException e) {
            e.printStackTrace();
        }
        server.addListener(new Listener() {
            @Override
            public void connected(Connection connection) {
                System.out.println("Connected: " + connection.getID());
            }

            @Override
            public void received(Connection connection, Object object) {
                if(object instanceof JSONObject) {
                    JSONObject json = (JSONObject)object;
                    System.out.println(json.get("name"));
                }
            }
        });
        server.getKryo().register(HashMap.class);
        server.getKryo().register(JSONObject.class);

        client = new Client();
        client.start();
        List<InetAddress> adress = client.discoverHosts(9021, 1000);
        for(InetAddress a : adress) {
            System.out.println(a.getHostAddress());

        }
        /*try {
            client.connect(5000, "192.168.0.164", 9021);
        } catch (IOException e) {
            e.printStackTrace();
        }
        client.getKryo().register(java.util.HashMap.class);
        client.getKryo().register(JSONObject.class);


        JSONObject json = new JSONObject();
        json.put("name", "Levin");
        client.sendTCP(json);*/



    }





    private LobbyScreen() {
        game = null;
    }

    private LobbyScreen(final Main game) {
        this.game = game;
        stage = new Stage();

        createCommunication();

        skin = game.assetManager.manager.get("skins/plain-james/plain-james-ui.json", Skin.class);
        sendMessage = new TextButton("Send Message", skin);
        sendMessage.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                JSONObject json = new JSONObject();
                json.put("name", "Message button");
                client.sendTCP(json);
            }
        });
    }


    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        stage.addActor(sendMessage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        stage.clear();
    }

    @Override
    public void dispose() {

    }
}
