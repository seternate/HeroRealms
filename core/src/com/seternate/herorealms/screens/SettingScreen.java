package com.seternate.herorealms.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.seternate.herorealms.Main;


public class SettingScreen implements Screen {
    private static SettingScreen settingScreen = null;

    public static SettingScreen getSettingScreen() {
        return settingScreen;
    }

    public static SettingScreen newSettingScreen(final Main game) {
        if(settingScreen == null) settingScreen = new SettingScreen(game);
        return settingScreen;
    }


    final Main game;
    Stage stage;

    Skin skin;
    TextField.TextFieldStyle textFieldStyle;
    TextButton backButton, enterButton;
    TextField nameField;
    Table formTable;

    float fFontScale, fTextFieldPad, fTablePad;


    private SettingScreen() {
        game = null;
    }

    private SettingScreen(final Main game) {
        this.game = game;
        stage = new Stage();
        skin = game.assetManager.manager.get("skins/plain-james/plain-james-ui.json", Skin.class);
        backButton = new TextButton("Back", skin);
        enterButton = new TextButton("Enter", skin);
        formTable = new Table();


        fFontScale = Gdx.graphics.getHeight()/ backButton.getHeight()*0.07f;
        fTextFieldPad = Gdx.graphics.getWidth()/5;
        fTablePad = Gdx.graphics.getHeight()/50;


        textFieldStyle = skin.get(TextField.TextFieldStyle.class);
        textFieldStyle.font.getData().scale(fFontScale);
        nameField = new TextField(game.player.getName(), textFieldStyle);


        backButton.getLabel().setFontScale(fFontScale);
        enterButton.getLabel().setFontScale(fFontScale);
        nameField.setAlignment(Align.center);
        nameField.setMaxLength(12);


        formTable.setFillParent(true);
        formTable.add(nameField).expand().bottom().fillX().padLeft(fTextFieldPad).padRight(fTextFieldPad);
        formTable.row();
        formTable.add(enterButton).expand().top().padTop(fTablePad);
        formTable.row();
        formTable.add(backButton).right().bottom();


        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(game.screenManager.pop());
            }
        });
        enterButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.player.setName(nameField.getText());
                game.player.safe();
                game.setScreen(game.screenManager.pop());
            }
        });
        nameField.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if(keycode == Input.Keys.ENTER) {
                    nameField.getOnscreenKeyboard().show(false);
                    game.player.setName(nameField.getText());
                    game.player.safe();
                    game.setScreen(game.screenManager.pop());
                    return true;
                }
                return false;
            }
        });
    }


    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        nameField.setText(game.player.getName());
        stage.addActor(MenuScreen.getMenuScreen().backgroundImage);
        stage.addActor(formTable);
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
