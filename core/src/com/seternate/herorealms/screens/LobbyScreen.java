package com.seternate.herorealms.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.esotericsoftware.kryonet.Listener;
import com.seternate.herorealms.Main;
import com.seternate.herorealms.gameObject.Player;
import com.seternate.herorealms.networking.NetworkHelper;

import java.util.ArrayList;


public class LobbyScreen implements Screen{
    final Main game;
    Stage stage;
    NetworkHelper networkHelper;
    boolean ready;

    Skin skin;
    Table layoutTable;
    Label[] playerLabels;
    TextButton backButton, readyButton;

    float fFontScale, fPadTable;


    public LobbyScreen(final Main game, final NetworkHelper networkHelper) {
        this.game = game;
        stage = new Stage();
        this.networkHelper = networkHelper;
        layoutTable = new Table();
        playerLabels = new Label[4];
        skin = game.assetManager.manager.get("skins/plain-james/plain-james-ui.json", Skin.class);
        playerLabels[0] = new Label(game.player.getName(), skin, "white-big");
        for(int i = 1; i < playerLabels.length; i++) playerLabels[i] = new Label("", skin, "white-big");
        readyButton = new TextButton("Ready", skin);
        backButton = new TextButton("Back", skin);


        fFontScale = CardScreen.getCardScreen().fFontScale;
        fPadTable = Gdx.graphics.getHeight()/50;


        for(Label label : playerLabels) {
            label.setFontScale(fFontScale);
        }
        backButton.getLabel().setFontScale(fFontScale);
        readyButton.getLabel().setFontScale(fFontScale);


        layoutTable.setFillParent(true);
        layoutTable.setDebug(true);
        layoutTable.add(playerLabels[0]).colspan(2).expandY().bottom();
        layoutTable.row();
        for(int i = 1; i < playerLabels.length; i++) {
            layoutTable.add(playerLabels[i]).colspan(2);
            layoutTable.row();
        }
        layoutTable.add(readyButton).expandX().right().padRight(fPadTable).expandY().bottom();
        layoutTable.add(backButton).bottom();


        playerLabels[0].addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(ready) {
                    ready = false;
                    playerLabels[0].setColor(skin.getColor("white"));
                } else {
                    ready = true;
                    playerLabels[0].setColor(skin.getColor("gray"));
                }
            }
        });
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                networkHelper.client.close();
                game.setScreen(game.screenManager.pop());
            }
        });
        readyButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(ready) {
                    ready = false;
                    playerLabels[0].setColor(skin.getColor("white"));
                } else {
                    ready = true;
                    playerLabels[0].setColor(skin.getColor("gray"));
                }
            }
        });

    }

    public void updatePlayer() {
        int i = 1;
        for(int k = 1; k < playerLabels.length; k++) {
            playerLabels[k].setText("");
        }
        for(Player player : networkHelper.serverData.getPlayer()) {
            if(player.equals(game.player)) continue;
            playerLabels[i].setText(player.getName());
            i++;
        }

    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        stage.addActor(MenuScreen.getMenuScreen().backgroundImage);
        stage.addActor(layoutTable);
    }

    @Override
    public void render(float delta) {
        Gdx.gl20.glClearColor(0, 0 ,0 ,1);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
        updatePlayer();
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
        stage.dispose();
    }




    /*

    public void openServer() {
        server = new Server(NetworkConstants.WRITE_BUFFER_SIZE, NetworkConstants.OBJECT_BUFFER_SIZE);
        server.start();
        serverData = new ServerData(game.gameDataXML);
        try {
            server.bind(NetworkConstants.TCP_PORT, NetworkConstants.UDP_PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Register classes for networking
        register(server.getKryo());
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
                    server.sendToAllTCP(new ServerConnectMessage(serverData));
                }
            }
        });
    }

    public void openClientAndOrServer() {
        new Thread() {
            @Override
            public void run() {
                //Creating & starting client
                client = new Client(NetworkConstants.WRITE_BUFFER_SIZE, NetworkConstants.OBJECT_BUFFER_SIZE);
                client.start();
                //Register for client the classes for networking
                register(client.getKryo());
                //Add Listener to client
                addClientListener();
                *//*
                Search for open servers over UDP. If no server was found, create a server and get
                its address.
                 *//*
                clientData = new ClientData();
                if(!isServer()) openServer();
                clientData.setServerAddress(getServer());
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
                    int i = 0;
                    for(Player player : serverData.getPlayer()) {
                        if(player.getNetworkID() == connection.getID()) continue;
                        if(i == 0) player2Label.setText(player.getName());
                        else if(i == 1) player3Label.setText(player.getName());
                        else if(i == 2) player4Label.setText(player.getName());
                        i++;
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
    }*/
}
