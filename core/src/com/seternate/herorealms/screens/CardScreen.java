package com.seternate.herorealms.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.XmlReader;
import com.seternate.herorealms.Main;

public class CardScreen implements Screen {
    final Main game;
    Stage stage;
    ScrollPane scrollPane;

    Skin skin;
    Image background, card_image;
    TextButton back_btn;


    public CardScreen(final Main game){
        this.game = game;
        stage = new Stage();

        skin = new Skin(Gdx.files.internal("skins/plain-james/plain-james-ui.json"));

        background = new Image(game.assetManager.manager.get("background.jpg", Texture.class));
        background.setPosition((Gdx.graphics.getWidth() - background.getWidth())/2, -(background.getHeight() - Gdx.graphics.getHeight()));
        stage.addActor(background);

        back_btn = new TextButton("Back", skin);
        back_btn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(game.screenManager.get(MenuScreen.class));
            }
        });
        back_btn.getLabel().setFontScale(1.8f);


        Table table = new Table();
        for(int i = 0; i < game.xml.getChild(0).getChildrenByName("card").size; i++) {
            card_image = new Image(game.assetManager.manager.get("cards/" + game.xml.getChild(0).getChildrenByName("card").get(i).getChild(0).getChild(0).getText(), Texture.class));
            table.add(card_image).pad(Gdx.graphics.getHeight()/50).width(Gdx.graphics.getWidth()/4).height(Gdx.graphics.getWidth()/4/card_image.getWidth()*card_image.getHeight());
            if((i+1)%3 == 0) {
                table.row();
            }
        }
        table.setSize(table.getColumns()*card_image.getWidth(), table.getRows()*card_image.getHeight());
        //table.pad(Gdx.graphics.getHeight()/50, 0,Gdx.graphics.getHeight()/50, 0);
        scrollPane = new ScrollPane(table);


        Table layout = new Table();
        layout.setFillParent(true);
        layout.add().expandX();
        layout.add(scrollPane).expandY();
        layout.add(back_btn).expandX().right().bottom();
        stage.addActor(layout);
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

    }
}
