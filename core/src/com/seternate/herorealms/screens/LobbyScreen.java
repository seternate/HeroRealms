package com.seternate.herorealms.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.seternate.herorealms.Main;

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


    private LobbyScreen() {
        game = null;
    }

    private LobbyScreen(final Main game) {
        this.game = game;
        stage = new Stage();
    }


    @Override
    public void show() {

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
