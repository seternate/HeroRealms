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
import com.seternate.herorealms.Main;
import com.seternate.herorealms.networking.NetworkHelper;
import com.seternate.herorealms.networking.ServerData;

import java.util.ArrayList;

public class ServerBrowserScreen implements Screen {
    final Main game;
    Stage stage;
    NetworkHelper networkHelper;
    ArrayList<ServerData> serverData;

    Thread searchServers;

    Skin skin;
    Label serverOwnerNameLabel, serverConnectionsLabel;
    ArrayList<Label[]> serverLabels;
    Table layoutTable, serverTable, buttonTable;
    TextButton backButton, serverButton;

    float fFontScale, fFontScaleServerTable, fTablePad;


    public ServerBrowserScreen(final Main game) {
        this.game = game;
        stage = new Stage();
        networkHelper = new NetworkHelper();
        serverLabels = new ArrayList<Label[]>();
        serverTable = new Table();
        buttonTable = new Table();
        layoutTable = new Table();
        skin = game.assetManager.manager.get("skins/plain-james/plain-james-ui.json", Skin.class);
        serverOwnerNameLabel = new Label("Owner", skin, "white-big");
        serverConnectionsLabel = new Label("Players", skin, "white-big");
        backButton = new TextButton("Back", skin);
        serverButton = new TextButton("Create Server", skin);


        fFontScale = CardScreen.getCardScreen().fFontScale;
        fFontScaleServerTable = Gdx.graphics.getHeight()/backButton.getHeight()*0.25f;
        fTablePad = CardScreen.getCardScreen().fImageTablePad;


        serverConnectionsLabel.setFontScale(fFontScaleServerTable *1.2f);
        serverOwnerNameLabel.setFontScale(fFontScaleServerTable *1.2f);
        backButton.getLabel().setFontScale(fFontScale);
        serverButton.getLabel().setFontScale(fFontScale);


        serverTable.left().top();

        buttonTable.add(serverButton).padRight(fTablePad);
        buttonTable.add(backButton);

        layoutTable.setFillParent(true);
        layoutTable.add(serverTable).expand().pad(fTablePad).fill();
        layoutTable.row();
        layoutTable.add(buttonTable).expandX().right();


        serverButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                networkHelper.startServer(game);
            }
        });
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(game.screenManager.pop());
            }
        });


        searchServers = new Thread() {
            @Override
            public void run() {
                while(!Thread.currentThread().isInterrupted()) {
                    networkHelper.searchAvailableServers();
                }
            }
        };
    }

    private void buildServerTable() {
        serverTable.clearChildren();
        serverLabels.clear();
        serverTable.add(serverOwnerNameLabel).left().expandX();
        serverTable.add(serverConnectionsLabel).left();
        serverTable.row();
        if(networkHelper.availableServers != null) {
            for (ServerData serverData : networkHelper.availableServers) {
                Label[] label = new Label[2];
                label[0] = new Label(serverData.getServerOwner().getName(), skin, "white-big");
                label[1] = new Label(Integer.toString(serverData.getPlayerNumber()) + "/4", skin, "white-big");
                label[0].setFontScale(fFontScaleServerTable);
                label[0].addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        System.out.println("Lobbyscreen");
                        game.setScreen(game.screenManager.push(new LobbyScreen(game, networkHelper)));
                        //Todo: server connection
                    }
                });
                label[1].setFontScale(fFontScaleServerTable);
                serverLabels.add(label);
            }
            for (Label[] serverLabel : serverLabels) {
                serverTable.add(serverLabel[0]).left().expandX();
                serverTable.add(serverLabel[1]).left();
                serverTable.row();
            }
        }
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        searchServers.start();
        stage.addActor(MenuScreen.getMenuScreen().backgroundImage);
        stage.addActor(layoutTable);
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
        searchServers.interrupt();
        stage.clear();
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
