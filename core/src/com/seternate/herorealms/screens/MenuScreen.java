package com.seternate.herorealms.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.seternate.herorealms.Main;

public class MenuScreen implements Screen {
    final Main game;


    public MenuScreen(final Main game) {
        this.game = game;
        game.assetManager.loadAssets();
    }


    @Override
    public void show() {
        while(!game.assetManager.manager.update());
    }

    @Override
    public void render(float delta) {
        Texture cardBack = game.assetManager.manager.get("BAS-EN-001-arkus-imperial-dragon.jpg");
        Gdx.gl.glClearColor(1 ,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.begin();
        //game.batch.draw(new Texture(Gdx.files.internal("hero_realms_back.jpg")), 0, 0);
        game.batch.draw(game.assetManager.manager.get("BAS-EN-001-arkus-imperial-dragon.jpg", Texture.class), 0, 0);
        game.batch.end();
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
