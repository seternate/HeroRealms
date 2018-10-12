package com.seternate.herorealms.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.seternate.herorealms.Main;
import com.seternate.herorealms.gameObject.Card;
import com.seternate.herorealms.gameObject.CardRole;
import com.seternate.herorealms.gameObject.Deck;
import com.seternate.herorealms.gameObject.Defense;
import com.seternate.herorealms.gameObject.Faction;
import com.seternate.herorealms.gameObject.Player;
import com.seternate.herorealms.networking.ClientData;
import com.seternate.herorealms.networking.NetworkConstants;
import com.seternate.herorealms.networking.ServerData;
import com.seternate.herorealms.networking.messages.ClientConnectMessage;
import com.seternate.herorealms.networking.messages.ServerConnectMessage;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class LobbyScreen implements Screen {
    final Main game;
    Stage stage;
    Server server;
    ServerData serverData;
    Client client;
    ClientData clientData;

    Skin skin;
    Label player1Label, player2Label, player3Label, player4Label;
    Table layoutTable;


    public LobbyScreen(final Main game){
        this.game = game;
        stage = new Stage();
        //setting up networking
        openClientAndOrServer();
        skin = game.assetManager.manager.get("skins/plain-james/plain-james-ui.json", Skin.class);
        player1Label = new Label(game.player.getName(), skin, "white-big");
        player2Label = new Label("", skin, "white-big");
        player3Label = new Label("", skin, "white-big");
        player4Label = new Label("", skin, "white-big");
        layoutTable = new Table();

        layoutTable.setDebug(true);
        layoutTable.setFillParent(true);
        layoutTable.add(player1Label);
        layoutTable.row();
        layoutTable.add(player2Label);
        layoutTable.row();
        layoutTable.add(player3Label);
        layoutTable.row();
        layoutTable.add(player4Label);
    }

    public InetAddress searchForOpenServers() {
        List<InetAddress> addresses = client.discoverHosts(NetworkConstants.UDP_PORT, 1000);
        if(addresses.isEmpty()) return null;
        for(InetAddress address : addresses) {
            if(!address.getHostAddress().equals("127.0.0.1")) return address;
        }
        return null;
    }

    public void openServer() {
        server = new Server(20000, 20000);
        server.start();
        serverData = new ServerData(game.gameDataXML);
        try {
            server.bind(NetworkConstants.TCP_PORT, NetworkConstants.UDP_PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Register classes for networking
        server.getKryo().register(ClientConnectMessage.class);
        server.getKryo().register(ServerMessage.class);
        server.getKryo().register(Player.class);
        server.getKryo().register(ServerData.class);
        server.getKryo().register(Deck.class);
        server.getKryo().register(Card.class);
        server.getKryo().register(HashMap.class);
        server.getKryo().register(Defense.class);
        server.getKryo().register(CardRole.class);
        server.getKryo().register(Faction.class);
        server.getKryo().register(String[].class);
        server.getKryo().register(ArrayList.class);
        addServerListener();
    }

    public void addServerListener() {
        server.addListener(new Listener() {
            @Override
            public void connected(Connection connection) {
                if(serverData.getPlayerNumber() >= 4) connection.close();
            }

            @Override
            public void received(Connection connection, Object object) {
                if(object instanceof ClientConnectMessage) {
                    Player player = ((ClientConnectMessage)object).getData();
                    serverData.addPlayer(connection.getID(), player);
                    server.sendToAllExceptTCP(connection.getID(), new ServerConnectMessage(serverData));
                }
            }
        });
    }

    public void openClientAndOrServer() {
        new Thread() {
            @Override
            public void run() {
                client = new Client(20000, 20000);
                client.start();
                //Register classes for networking
                client.getKryo().register(ClientConnectMessage.class);
                client.getKryo().register(ServerMessage.class);
                client.getKryo().register(Player.class);
                client.getKryo().register(ServerData.class);
                client.getKryo().register(Deck.class);
                client.getKryo().register(Card.class);
                client.getKryo().register(HashMap.class);
                client.getKryo().register(Defense.class);
                client.getKryo().register(CardRole.class);
                client.getKryo().register(Faction.class);
                client.getKryo().register(String[].class);
                client.getKryo().register(ArrayList.class);
                //Add Listener
                addClientListener();
                clientData = new ClientData();
                if(!clientData.setServerAddress(searchForOpenServers())) {
                    openServer();
                    clientData.setServerAddress(searchForOpenServers());
                }
                //Todo: if connection fails show user something
                try {
                    client.connect(NetworkConstants.C_TIMEOUT, clientData.getServerAddress(), NetworkConstants.TCP_PORT, NetworkConstants.UDP_PORT);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public void addClientListener() {
        client.addListener(new Listener() {
            @Override
            public void connected(Connection connection) {
                game.player.setNetworkID(connection.getID());
                client.sendTCP(new ClientConnectMessage(game.player));
            }

            @Override
            public void received(Connection connection, Object object) {
                if(object instanceof ServerConnectMessage) {
                    ServerData serverData = ((ServerConnectMessage)object).getData();
                    for(int i = 0; i < serverData.getPlayerNumber(); i++) {
                        switch(i) {

                        }
                    }




                        int i = 0;
                        System.out.println(serverData.getPlayer().size());
                        for(Player player : serverData.getPlayer()) {
                            if(game.player.getNetworkID() == player.getNetworkID()) continue;
                            if(i == 0) player2Label.setText(player.getName());
                            if(i == 1) player3Label.setText(player.getName());
                            if(i == 2) player4Label.setText(player.getName());
                            i++;
                        }
                    }
                }
            }
        });
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        stage.addActor(MenuScreen.getMenuScreen().backgroundImage);
        stage.addActor(layoutTable);
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
        server.stop();
        client.stop();
    }

    @Override
    public void dispose() {
        stage.dispose();
        try {
            server.dispose();
            client.dispose();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
