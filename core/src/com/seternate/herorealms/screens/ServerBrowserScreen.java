package com.seternate.herorealms.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.seternate.herorealms.Main;
import com.seternate.herorealms.networking.NetworkHelper;
import com.seternate.herorealms.networking.ServerData;

import java.util.ArrayList;

public class ServerBrowserScreen implements Screen {
    final Main game;
    Stage stage;
    NetworkHelper networkHelper;
    ArrayList<ServerData> serverData;

    Skin skin;
    Label serverOwnerNameLabel, serverConnectionsLabel;
    ArrayList<Label[]> serverLabels;
    Table serverTable;


    public ServerBrowserScreen(final Main game) {
        this.game = game;
        stage = new Stage();
        networkHelper = new NetworkHelper();
        serverData = new ArrayList<ServerData>();
        serverLabels = new ArrayList<Label[]>();
        serverTable = new Table();
        skin = game.assetManager.manager.get("skins/plain-james/plain-james-ui.json", Skin.class);
        serverOwnerNameLabel = new Label("Owner", skin, "white-big");
        serverConnectionsLabel = new Label("Connections", skin, "white-big");

        serverTable.setFillParent(true);
        //networkHelper.startServer(game);

        new Thread() {
            @Override
            public void run() {
                while(true) {
                    serverData = networkHelper.getAvailableServers();
                }
            }
        }.start();
    }

    private void buildServerTable() {
        serverTable.clearChildren();
        serverLabels.clear();
        serverTable.add(serverOwnerNameLabel);
        serverTable.add(serverConnectionsLabel);
        serverTable.row();
        if(serverData != null) {
            for (ServerData serverData : serverData) {
                Label[] label = new Label[2];
                label[0] = new Label(serverData.getServerOwner().getName(), skin, "white-big");
                label[1] = new Label(Integer.toString(serverData.getPlayerNumber()) + "/4", skin, "white-big");
                serverLabels.add(label);
            }
            for (Label[] serverLabel : serverLabels) {
                serverTable.add(serverLabel[0]);
                serverTable.add(serverLabel[1]);
                serverTable.row();
            }
        }
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        stage.addActor(serverTable);
    }

    @Override
    public void render(float delta) {
        Gdx.gl20.glClearColor(0, 0 ,0 ,1);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
        buildServerTable();
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
