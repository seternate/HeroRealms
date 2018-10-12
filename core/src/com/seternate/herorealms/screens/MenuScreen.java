package com.seternate.herorealms.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.seternate.herorealms.Main;

public class MenuScreen implements Screen {
    private static MenuScreen menuScreen = null;

    public static MenuScreen getMenuScreen() {
        return menuScreen;
    }

    public static MenuScreen newMenuScreen(final Main game) {
        if(menuScreen == null) menuScreen = new MenuScreen(game);
        return menuScreen;
    }


    final Main game;
    Stage stage;

    Skin skin;
    Image backgroundImage, logoImage;
    TextButton quickplayButton, cardsButton, settingsButton;
    Table buttonTable;

    Vector2 vBackgroundImageSize, vBackgroundImagePosition, vLogoImageSize, vLogoImagePosition;
    float fFontScale;


    private MenuScreen() {
        game = null;
    }

    private MenuScreen(final Main game) {
        this.game = game;
        stage = new Stage();
        skin = game.assetManager.manager.get("skins/plain-james/plain-james-ui.json", Skin.class);
        backgroundImage = new Image(game.assetManager.manager.get("background.jpg", Texture.class));
        logoImage = new Image(game.assetManager.manager.get("HeroRealmsLogo.png", Texture.class));
        quickplayButton = new TextButton("Quickplay", skin);
        cardsButton = new TextButton("Cards", skin);
        settingsButton = new TextButton("Settings", skin);
        buttonTable = new Table();


        vBackgroundImageSize = new Vector2(backgroundImage.getWidth(), backgroundImage.getHeight());
        vBackgroundImageSize.scl(Gdx.graphics.getHeight()/vBackgroundImageSize.y);
        vBackgroundImagePosition = new Vector2((Gdx.graphics.getWidth() - vBackgroundImageSize.x)/2, (Gdx.graphics.getHeight() - vBackgroundImageSize.y)/2);
        vLogoImageSize = new Vector2(logoImage.getWidth(), logoImage.getHeight());
        vLogoImageSize.scl(Gdx.graphics.getHeight()/vLogoImageSize.y*0.25f);
        vLogoImagePosition = new Vector2(Gdx.graphics.getWidth() - vLogoImageSize.x - Gdx.graphics.getHeight()/50, Gdx.graphics.getHeight()/50);
        fFontScale = Gdx.graphics.getHeight()/ quickplayButton.getHeight()*0.11f;


        backgroundImage.setSize(vBackgroundImageSize.x, vBackgroundImageSize.y);
        backgroundImage.setPosition(vBackgroundImagePosition.x, vBackgroundImagePosition.y);
        logoImage.setSize(vLogoImageSize.x, vLogoImageSize.y);
        logoImage.setPosition(vLogoImagePosition.x, vLogoImagePosition.y);
        quickplayButton.getLabel().setFontScale(fFontScale);
        cardsButton.getLabel().setFontScale(fFontScale);
        settingsButton.getLabel().setFontScale(fFontScale);


        buttonTable.setFillParent(true);
        buttonTable.padLeft(Gdx.graphics.getHeight()/50).left();
        buttonTable.add().expandY();
        buttonTable.row();
        buttonTable.add(quickplayButton).expandY().fillX();
        buttonTable.row();
        buttonTable.add(cardsButton).expandY().fillX();
        buttonTable.row();
        buttonTable.add(settingsButton).expandY().fillX();
        buttonTable.row();
        buttonTable.add().expandY();


        quickplayButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(game.screenManager.push(new LobbyScreen(game)));
            }
        });
        cardsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(game.screenManager.push(CardScreen.getCardScreen()));
            }
        });
        settingsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(game.screenManager.push(SettingScreen.getSettingScreen()));
            }
        });
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        stage.addActor(backgroundImage);
        stage.addActor(logoImage);
        stage.addActor(buttonTable);
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
        stage.dispose();
    }
}
