package com.seternate.herorealms.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.XmlReader;
import com.seternate.herorealms.Main;

public class SplashScreen implements Screen {
    private static SplashScreen splashScreen = null;

    public static SplashScreen getSplashScreen() {
        return splashScreen;
    }

    public static SplashScreen newSplashScreen(final Main game) {
        if(splashScreen == null) splashScreen = new SplashScreen(game);
        return splashScreen;
    }


    final Main game;
    Stage stage;
    MenuScreen menuScreen;

    Skin skin;
    Label loadLabel;
    Image logoImage;

    Vector2 vLogoImageSize, vLogoImagePosition, vLoadLabelPosition;
    float fLoadLabelScale, time = 0;


    private SplashScreen() {
        game = null;
    }

    private SplashScreen(final Main game) {
        this.game = game;
        stage = new Stage();
        XmlReader xmlReader = new XmlReader();
        game.gameDataXML = xmlReader.parse(Gdx.files.internal("data.xml"));
        game.assetManager.manager.load("HeroRealmsLogo.png", Texture.class);
        game.assetManager.manager.load("background.jpg", Texture.class);
        game.assetManager.manager.load("skins/plain-james/plain-james-ui.json", Skin.class);
        while(!game.assetManager.manager.update());

        skin = game.assetManager.manager.get("skins/plain-james/plain-james-ui.json", Skin.class);
        loadLabel = new Label("Loading...", skin, "white-big");
        logoImage = new Image(game.assetManager.manager.get("HeroRealmsLogo.png", Texture.class));


        vLogoImageSize = new Vector2(logoImage.getWidth(), logoImage.getHeight());
        vLogoImageSize.scl(Gdx.graphics.getHeight()/vLogoImageSize.y*0.5f);
        vLogoImagePosition = new Vector2((Gdx.graphics.getWidth() - vLogoImageSize.x)/2, (Gdx.graphics.getHeight() - vLogoImageSize.y)/2);
        fLoadLabelScale = Gdx.graphics.getHeight()/loadLabel.getHeight()*0.11f;
        vLoadLabelPosition = new Vector2((Gdx.graphics.getWidth() - loadLabel.getWidth()*fLoadLabelScale)/2, vLogoImagePosition.y - loadLabel.getHeight()*fLoadLabelScale - Gdx.graphics.getHeight()/50);


        logoImage.setSize(vLogoImageSize.x, vLogoImageSize.y);
        logoImage.setPosition(vLogoImagePosition.x, vLogoImagePosition.y);
        loadLabel.setFontScale(fLoadLabelScale);
        loadLabel.setPosition(vLoadLabelPosition.x, vLoadLabelPosition.y);


        stage.addActor(loadLabel);
        stage.addActor(logoImage);
    }

    public void update(float delta) {
        time += delta;


        if(game.assetManager.manager.update()) {
            MenuScreen.newMenuScreen(game);
            LobbyScreen.newLobbyScreen(game);
            CardScreen.newCardScreen(game);
            SettingScreen.newSettingScreen(game);
            CardDetailScreen.newCardDetailScreen(game);

            game.setScreen(game.screenManager.push(MenuScreen.getMenuScreen()));
        }

        if(time > 0.5) {
            if(loadLabel.textEquals("Loading...")) loadLabel.setText("Loading");
            else if(loadLabel.textEquals("Loading")) loadLabel.setText("Loading.");
            else if(loadLabel.textEquals("Loading.")) loadLabel.setText("Loading..");
            else loadLabel.setText("Loading...");
            time = 0;
        }
    }

    @Override
    public void show() {
        game.assetManager.loadAssets();
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
    }
}
