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
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.seternate.herorealms.Main;
import com.seternate.herorealms.networking.ClientData;
import com.seternate.herorealms.networking.MyClient;
import com.seternate.herorealms.networking.MyServer;
import com.seternate.herorealms.networking.ServerData;
import com.seternate.herorealms.networking.messages.ClientConnectMessage;
import com.seternate.herorealms.networking.messages.ClientMessage;

import java.io.IOException;


public class LobbyScreen implements Screen{
    final Main game;
    Stage stage;
    MyClient client;




    boolean ready;


    Skin skin;
    Table layoutTable;
    Label[] playerLabels;
    TextButton backButton, readyButton, startButton;

    float fFontScale, fPadTable;






    public LobbyScreen(final Main game, String ipAddress) throws IOException {
        this.game = game;
        stage = new Stage();
        client = new MyClient(game.player);
        layoutTable = new Table();
        playerLabels = new Label[4];
        skin = game.assetManager.manager.get("skins/plain-james/plain-james-ui.json", Skin.class);
        readyButton = new TextButton("Ready", skin);
        backButton = new TextButton("Back", skin);
        startButton = new TextButton("Start", skin);
        playerLabels[0] = new Label(game.player.getName(), skin, "white-big");
        for(int i = 1; i < playerLabels.length; i++) playerLabels[i] = new Label("", skin, "white-big");

        client.addListener(new Listener() {
            @Override
            public void connected(Connection connection) {
                client.setID(connection.getID());
                client.sendTCP(new ClientConnectMessage(client.getData()));
            }
            @Override
            public void disconnected(Connection connection) {
                game.setScreen(game.screenManager.pop());
            }
            @Override
            public void received(Connection connection, Object object) {
                if(object instanceof ServerData) {
                    ServerData data = (ServerData)object;
                    client.updateServerData(data);
                    updateUI();
                }
            }
        });
        client.start();
        client.connect(ipAddress);


        fFontScale = CardScreen.getCardScreen().fFontScale;
        fPadTable = Gdx.graphics.getHeight()/50;


        for(Label label : playerLabels) {
            label.setFontScale(fFontScale);
        }
        startButton.getLabel().setFontScale(fFontScale);
        backButton.getLabel().setFontScale(fFontScale);
        readyButton.getLabel().setFontScale(fFontScale);
        startButton.setDisabled(false);


        layoutTable.setFillParent(true);
        layoutTable.setDebug(true);
        layoutTable.add(playerLabels[0]).colspan(2).expandY().bottom();
        layoutTable.row();
        for(int i = 1; i < playerLabels.length; i++) {
            layoutTable.add(playerLabels[i]).colspan(2);
            layoutTable.row();
        }
        if(MyServer.server.isRunning()) {
            layoutTable.add(startButton).bottom();
        }
        layoutTable.add(readyButton).expandX().right().padRight(fPadTable).expandY().bottom();
        layoutTable.add(backButton).bottom();


        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                client.close();
                client.stop();
            }
        });
        readyButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(client.getData().isReady()){
                    client.getData().setReady(false);
                    client.sendTCP(new ClientMessage("player_unready", client.getData()));
                    readyButton.setText("Ready");
                }else {
                    client.getData().setReady(true);
                    client.sendTCP(new ClientMessage("player_ready", client.getData()));
                    readyButton.setText("Cancel");
                }
            }
        });
        startButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("starting game");
            }
        });
    }

    private void updateUI() {
        if(client.getServerData() == null) return;
        for(Label playerLabel : playerLabels) {
            playerLabel.setText("");
        }
        playerLabels[0].setText(game.player.getName());
        int i = 1;
        for(ClientData clientData : client.getServerData().getPlayers()) {
            if(!(clientData.networkID == client.getID())) {
                playerLabels[i].setText(clientData.getPlayer().getName());
                i++;
            }
        }
        if(client.getServerData().allReady()) {
            startButton.setDisabled(false);
            startButton.getLabel().setColor(skin.getColor("white"));
        } else {
            startButton.setDisabled(true);
            startButton.getLabel().setColor(skin.getColor("gray"));
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
}
