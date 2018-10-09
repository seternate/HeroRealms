package com.seternate.herorealms.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.seternate.herorealms.Main;

public class MenuScreen implements Screen {
    final Main game;
    Stage stage;
    CardScreen cardScreen;

    Skin skin;
    Image background, logo;

    TextButton quickplay_btn, cards_btn, settings_btn, exit_btn;


    public MenuScreen(final Main game) {
        stage = new Stage();
        this.game = game;
        cardScreen = new CardScreen(game);

        skin = new Skin(Gdx.files.internal("skins/plain-james/plain-james-ui.json"));

        background = new Image(game.assetManager.manager.get("background.jpg", Texture.class));
        background.setPosition((Gdx.graphics.getWidth() - background.getWidth())/2, -(background.getHeight() - Gdx.graphics.getHeight()));
        stage.addActor(background);
        logo = new Image(game.assetManager.manager.get("HeroRealmsLogo.png", Texture.class));
        logo.setPosition(Gdx.graphics.getWidth()-logo.getWidth() - Gdx.graphics.getHeight()/50, Gdx.graphics.getHeight()/50);
        stage.addActor(logo);


        quickplay_btn = new TextButton("Quickplay", skin);
        quickplay_btn.getLabel().setFontScale(1.8f);

        cards_btn = new TextButton("Cards", skin);
        cards_btn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(game.screenManager.add(cardScreen));
            }
        });
        cards_btn.getLabel().setFontScale(1.8f);

        settings_btn = new TextButton("Settings", skin);
        settings_btn.getLabel().setFontScale(1.8f);

        exit_btn = new TextButton("Exit", skin);
        exit_btn.getLabel().setFontScale(1.8f);


        Table table = new Table();
        table.setFillParent(true);
        table.pad(Gdx.graphics.getHeight()/10, Gdx.graphics.getHeight()/50, Gdx.graphics.getHeight()/10, 0).left();

        table.add(quickplay_btn).expandY().fillX();
        table.row();
        table.add(cards_btn).expandY().fillX();
        table.row();
        table.add(settings_btn).expandY().fillX();
        table.row();
        table.add(exit_btn).expandY().fillX();
        stage.addActor(table);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    public void update(float delta) {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        update(delta);
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

    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }
}
