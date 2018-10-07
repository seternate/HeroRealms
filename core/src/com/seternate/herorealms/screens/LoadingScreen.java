package com.seternate.herorealms.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.seternate.herorealms.Main;

import java.util.HashMap;
import java.util.Map;

public class LoadingScreen implements Screen {
    final Main game;
    Texture logo;
    Label loadText;


    public LoadingScreen(final Main game) {
        this.game = game;
        game.assetManager.manager.load("HeroRealmsLogo.png", Texture.class);
        while(!game.assetManager.manager.update());
        logo = game.assetManager.manager.get("HeroRealmsLogo.png");

        game.assetManager.loadAssets();
    }


    @Override
    public void show() {

    }

    public void update(float delta) {
        if(game.assetManager.manager.update()) game.setScreen(new MenuScreen(game));

    }

    public void draw() {
        game.batch.begin();
        game.batch.draw(logo, (Gdx.graphics.getWidth() - logo.getWidth())/2, (Gdx.graphics.getHeight() - logo.getHeight())/2);
        game.batch.end();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        update(delta);
        draw();
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

    }

    @Override
    public void dispose() {

    }
}
