package com.seternate.herorealms.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
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

    Table layout;


    public CardScreen(final Main game){
        this.game = game;
        game.screenManager.add(new CardDetailScreen(game));
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
        back_btn.getLabel().setFontScale(1.5f);


        Table table = new Table();
        table.padTop(Gdx.graphics.getHeight()/50);
        table.padBottom(Gdx.graphics.getHeight()/50);
        Cell cell;
        int cardNumber = game.xml.getChild(0).getChildrenByName("card").size - 8;
        for(int i = 0; i < cardNumber; i++) {
            card_image = new Image(game.assetManager.manager.get("cards/" + game.xml.getChild(0).getChildrenByName("card").get(i).getChild(0).getChild(0).getText(), Texture.class));
            final XmlReader.Element card = game.xml.getChild(0).getChildrenByName("card").get(i);
            card_image.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    game.setScreen(((CardDetailScreen)game.screenManager.get(CardDetailScreen.class)).setCard(card));
                }
            });
            cell = table.add(card_image).width(Gdx.graphics.getWidth()/5f).height(Gdx.graphics.getWidth()/5f/card_image.getWidth()*card_image.getHeight()).expandX();
            if((i+1)%3 == 0) {
                table.row();
            }
            if(i >= 0 && i < 3) {
                cell.padBottom(Gdx.graphics.getHeight()/50);
            } else if(i >= cardNumber - (cardNumber%3) && i < cardNumber) {
                cell.padTop(Gdx.graphics.getHeight()/50);
            } else {
                cell.padBottom(Gdx.graphics.getHeight()/50);
                cell.padTop(Gdx.graphics.getHeight()/50);
            }
        }
        table.setSize(table.getColumns()*card_image.getWidth(), table.getRows()*card_image.getHeight());
        scrollPane = new ScrollPane(table);


        layout = new Table();
        layout.setFillParent(true);
        layout.add(scrollPane).expand().fill();
        layout.add(back_btn).bottom();
        stage.addActor(layout);
    }


    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
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

    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }
}
