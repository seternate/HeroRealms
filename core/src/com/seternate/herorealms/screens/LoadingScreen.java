package com.seternate.herorealms.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.XmlReader;
import com.seternate.herorealms.Main;

import java.util.HashMap;
import java.util.Map;

public class LoadingScreen implements Screen {
    final Main game;
    Stage stage;

    Texture logo;
    Skin labelSkin;
    Label loadText;

    float time = 0;


    public LoadingScreen(final Main game) {
        this.game = game;
        stage = new Stage();

        game.assetManager.manager.load("HeroRealmsLogo.png", Texture.class);
        while(!game.assetManager.manager.update());

        logo = game.assetManager.manager.get("HeroRealmsLogo.png");
        Image logo_img = new Image(logo);
        logo_img.setSize(1.5f*logo_img.getWidth()*(Gdx.graphics.getWidth()/Gdx.graphics.getHeight()), 1.5f*logo_img.getHeight()*(Gdx.graphics.getWidth()/Gdx.graphics.getHeight()));
        logo_img.setPosition((Gdx.graphics.getWidth() - logo_img.getWidth())/2, (Gdx.graphics.getHeight() - logo_img.getHeight())/2);
        stage.addActor(logo_img);

        labelSkin = new Skin(Gdx.files.internal("skins/plain-james/plain-james-ui.json"));
        loadText = new Label("Loading...", labelSkin, "white-big");
        loadText.setFontScale(1.8f);
        loadText.setPosition((Gdx.graphics.getWidth() - loadText.getWidth()*loadText.getFontScaleX())/2, logo_img.getY() - loadText.getHeight()*loadText.getFontScaleY() - Gdx.graphics.getHeight()/15);
        stage.addActor(loadText);
    }


    @Override
    public void show() {
        XmlReader xmlReader = new XmlReader();
        game.xml = xmlReader.parse(Gdx.files.internal("data.xml"));

        game.assetManager.loadAssets();
        game.assetManager.manager.load("background.jpg", Texture.class);
    }

    public void update(float delta) {
        if(game.assetManager.manager.update()) {
            game.setScreen(game.screenManager.add(new MenuScreen(game)));
        }
        time += delta;
        if(time > 0.5 && loadText.textEquals("Loading...")) {
            loadText.setText("Loading");
            time = 0;
        }else if(time > 0.5 && loadText.textEquals("Loading")) {
            loadText.setText("Loading.");
            time = 0;
        }else if(time > 0.5 && loadText.textEquals("Loading.")) {
            loadText.setText("Loading..");
            time = 0;
        }else if(time > 0.5 && loadText.textEquals("Loading..")) {
            loadText.setText("Loading...");
            time = 0;
        }
    }

    public void draw() {
        stage.act();
        stage.draw();
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
        stage.dispose();
        labelSkin.dispose();
    }
}
