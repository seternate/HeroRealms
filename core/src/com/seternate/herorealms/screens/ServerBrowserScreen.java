package com.seternate.herorealms.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.seternate.herorealms.Main;
import com.seternate.herorealms.networking.NetworkHelper;
import com.seternate.herorealms.networking.ServerData;

import java.util.ArrayList;

public class ServerBrowserScreen implements Screen {
    final Main game;
    Stage stage;
    NetworkHelper networkHelper;

    Thread searchServers;

    Skin skin;
    Label serverOwnerNameLabel, serverConnectionsLabel;
    Label[][] serverLabels;
    Table layoutTable, serverTable, buttonTable;
    TextButton backButton, serverButton;

    float fFontScale, fFontScaleServerTable, fTablePad;


    public ServerBrowserScreen(final Main game) {
        this.game = game;
        stage = new Stage();
        networkHelper = new NetworkHelper();
        serverTable = new Table();
        buttonTable = new Table();
        layoutTable = new Table();
        serverLabels = new Label[5][2];
        skin = game.assetManager.manager.get("skins/plain-james/plain-james-ui.json", Skin.class);
        serverOwnerNameLabel = new Label("Owner", skin, "white-big");
        serverConnectionsLabel = new Label("Players", skin, "white-big");
        backButton = new TextButton("Back", skin);
        String serverButtonText = networkHelper.isServerRunning() ? "Close Server" : "Create Server";
        serverButton = new TextButton(serverButtonText, skin);


        fFontScale = CardScreen.getCardScreen().fFontScale;
        fFontScaleServerTable = Gdx.graphics.getHeight()/serverOwnerNameLabel.getHeight()*0.125f;
        fTablePad = CardScreen.getCardScreen().fImageTablePad;


        serverConnectionsLabel.setFontScale(fFontScaleServerTable *1.2f);
        serverOwnerNameLabel.setFontScale(fFontScaleServerTable *1.2f);
        backButton.getLabel().setFontScale(fFontScale);
        serverButton.getLabel().setFontScale(fFontScale);
        for(Label[] label : serverLabels) {
            label[0] = new Label("", skin, "white-big");
            label[1] = new Label("", skin, "white-big");
            label[0].setFontScale(fFontScaleServerTable);
            label[1].setFontScale(fFontScaleServerTable);
            label[0].addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    //Todo: inform user about failed connection with popup, searchThread wont stop()
                    if(!networkHelper.connect(((Label)event.getListenerActor()).getText().toString())) {System.out.println("Failed Server connection");return;}
                    game.setScreen(game.screenManager.push(new LobbyScreen(game, networkHelper)));
                }
            });
        }


        buttonTable.add(serverButton).padRight(fTablePad);
        buttonTable.add(backButton);

        serverTable.left().top();
        serverTable.add(serverOwnerNameLabel).left().expandX();
        serverTable.add(serverConnectionsLabel).left();
        serverTable.row();
        for(Label[] label : serverLabels) {
            serverTable.add(label[0]).left();
            serverTable.add(label[1]).left();
            serverTable.row();
        }

        layoutTable.setFillParent(true);
        layoutTable.add(serverTable).expand().pad(fTablePad).fill();
        layoutTable.row();
        layoutTable.add(buttonTable).expandX().right();


        serverButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(networkHelper.isServerRunning()) {
                    networkHelper.stopServer();
                    serverButton.setText("Create Server");
                } else {
                    networkHelper.startServer(game);
                    serverButton.setText("Close Server");
                }

            }
        });
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                networkHelper.close();
                game.setScreen(game.screenManager.pop());
            }
        });


        searchServers = new Thread() {
            @Override
            public void run() {
                while(!this.isInterrupted()) {
                    networkHelper.searchAvailableServers();
                }
            }
        };
    }

    private void updateServerTable() {
        for(Label[] label : serverLabels) {
            label[0].setText("");
            label[1].setText("");
        }
        if(networkHelper.availableServers != null) {
            ArrayList<ServerData> data = networkHelper.availableServers;
            for(int i = 0; i < serverLabels.length && i < data.size(); i++) {
                serverLabels[i][0].setText(data.get(i).getServerOwner().getName());
                serverLabels[i][1].setText(Integer.toString(data.get(i).getPlayerNumber()) + "/4");
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
        updateServerTable();
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
        System.out.println("interrupt search");
        searchServers.interrupt();
        stage.clear();
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
