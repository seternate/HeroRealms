package com.seternate.herorealms.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.seternate.herorealms.Main;
import com.seternate.herorealms.gameObject.ServerLabel;
import com.seternate.herorealms.networking.MyServer;
import com.seternate.herorealms.networking.NetworkHelper;
import com.seternate.herorealms.networking.ServerData;

import java.io.IOException;
import java.util.ArrayList;

public class ServerBrowserScreen implements Screen {
    final Main game;
    Stage stage;
    Dialog dialog;
    MyServer server;


    Skin skin;
    Label serverNameLabel, serverPlayerLabel, dialogLabel;
    ServerLabel[][] serverLabels;
    Table layoutTable, serverTable, buttonTable;
    TextButton backButton, serverButton, okButton;

    float fFontScale, fFontScaleServerTable, fTablePad;


    public ServerBrowserScreen(final Main game) {
        this.game = game;
        stage = new Stage();
        server = new MyServer(game.gameDataXML, game.player);
        skin = game.assetManager.manager.get("skins/plain-james/plain-james-ui.json", Skin.class);
        serverNameLabel = new Label("Owner", skin, "white-big");
        serverPlayerLabel = new Label("Players", skin, "white-big");
        serverLabels = new ServerLabel[5][2];
        backButton = new TextButton("Back", skin);
        serverButton = new TextButton("Create Server", skin);
        serverTable = new Table();
        buttonTable = new Table();
        layoutTable = new Table();

        dialog = new Dialog("Failed connection", skin);
        dialogLabel = new Label("Connecting to server failed!", skin, "white-big");
        okButton = new TextButton("Ok", skin);


        fFontScale = CardScreen.getCardScreen().fFontScale;
        fFontScaleServerTable = Gdx.graphics.getHeight()/ serverNameLabel.getHeight()*0.125f;
        fTablePad = CardScreen.getCardScreen().fImageTablePad;


        serverPlayerLabel.setFontScale(fFontScaleServerTable *1.2f);
        serverNameLabel.setFontScale(fFontScaleServerTable *1.2f);
        dialogLabel.setFontScale(fFontScale);
        backButton.getLabel().setFontScale(fFontScale);
        serverButton.getLabel().setFontScale(fFontScale);
        okButton.getLabel().setFontScale(fFontScale);
        for(Label[] label : serverLabels) {
            label[0] = new ServerLabel("", skin, "white-big");
            label[1] = new ServerLabel("", skin, "white-big");
            label[0].setFontScale(fFontScaleServerTable);
            label[1].setFontScale(fFontScaleServerTable);
            label[0].addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    try{
                        game.setScreen(game.screenManager.push(new LobbyScreen(game, ((ServerLabel)event.getListenerActor()).getIPAddress())));
                    }catch(IOException e) {
                        dialog.show(stage);
                    }
                }
            });
        }


        dialog.getContentTable().add(dialogLabel).space(fTablePad);
        dialog.getButtonTable().add(okButton).space(fTablePad);

        buttonTable.add(serverButton).padRight(fTablePad);
        buttonTable.add(backButton);

        serverTable.left().top();
        serverTable.add(serverNameLabel).left().expandX();
        serverTable.add(serverPlayerLabel).left();
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
                if(server.isRunning()) {
                    server.stop();
                    serverButton.setText("Create Server");
                    return;
                }
                server.start();
                serverButton.setText("Close Server");
            }
        });
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                server.stop();
                game.setScreen(game.screenManager.pop());
            }
        });
        okButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dialog.hide();
            }
        });
    }

    private void updateServerTable() {
        for(Label[] label : serverLabels) {
            label[0].setText("");
            label[1].setText("");
        }
        ArrayList<ServerData> data = NetworkHelper.getAvailableServers();
        for(int i = 0; i < serverLabels.length && i < data.size(); i++) {
            serverLabels[i][0].setText(data.get(i));
            serverLabels[i][1].setText(Integer.toString(data.get(i).getPlayerNumber()) + "/4");
        }
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        for(Label[] label : serverLabels) {
            label[0].setText("");
            label[1].setText("");
        }
        NetworkHelper.searchServers(true);
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
        stage.clear();
        NetworkHelper.searchServers(false);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
