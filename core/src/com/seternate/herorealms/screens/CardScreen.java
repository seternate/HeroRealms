package com.seternate.herorealms.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.seternate.herorealms.Main;

public class CardScreen implements Screen {
    private static CardScreen cardScreen = null;

    public static CardScreen getCardScreen() {
        return cardScreen;
    }

    public static CardScreen newCardScreen(final Main game) {
        if(cardScreen == null) cardScreen = new CardScreen(game);
        return cardScreen;
    }


    final Main game;
    Stage stage;
    ScrollPane scrollPane;

    Skin skin;
    Image cardImage;
    TextButton backButton;
    Table layoutTable, imageTable;

    Vector2 vImageTableImageSize;
    float fFontScale, fImageTablePad;
    int cardNumber;


    private CardScreen() {
        game = null;
    }

    private CardScreen(final Main game){
        this.game = game;
        stage = new Stage();
        skin = game.assetManager.manager.get("skins/plain-james/plain-james-ui.json", Skin.class);
        backButton = new TextButton("Back", skin);
        cardImage = new Image(game.assetManager.manager.get("cards/" + game.gameDataXML.getChild(0).getChildrenByName("card").get(0).getChild(0).getChild(0).getText(), Texture.class));
        imageTable = new Table();
        layoutTable = new Table();
        scrollPane = new ScrollPane(imageTable);


        vImageTableImageSize = new Vector2(Gdx.graphics.getWidth()*0.2f, cardImage.getHeight()*(Gdx.graphics.getWidth()/cardImage.getWidth())*0.2f);
        fFontScale = Gdx.graphics.getHeight()/ backButton.getHeight()*0.07f;
        fImageTablePad = Gdx.graphics.getHeight()/50;
        //Subtract the 8 'Score Cards'
        cardNumber = game.gameDataXML.getChild(0).getChildrenByName("card").size - 8;


        backButton.getLabel().setFontScale(fFontScale);


        //Create a Table with 3 columns
        for(int i = 0; i < cardNumber; i++) {
            final Element card = game.gameDataXML.getChild(0).getChildrenByName("card").get(i);
            cardImage = new Image(game.assetManager.manager.get("cards/" + card.getChild(0).getChild(0).getText(), Texture.class));
            cardImage.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    game.setScreen(game.screenManager.push(CardDetailScreen.getCardDetailScreen().setCard(card)));
                }
            });
            Cell cell = imageTable.add(cardImage).width(vImageTableImageSize.x).height(vImageTableImageSize.y).expandX();
            if((i+1)%3 == 0) {
                imageTable.row();
            }
            if(i >= 0 && i < 3) {
                cell.padBottom(fImageTablePad);
            } else if(i >= cardNumber - (cardNumber%3) && i < cardNumber) {
                cell.padTop(fImageTablePad);
            } else {
                cell.padBottom(fImageTablePad);
                cell.padTop(fImageTablePad);
            }
        }
        imageTable.padTop(fImageTablePad);
        imageTable.padBottom(fImageTablePad);
        imageTable.setSize(imageTable.getColumns()*cardImage.getWidth(), imageTable.getRows()*cardImage.getHeight());


        layoutTable.setFillParent(true);
        layoutTable.add(scrollPane).expand().fill();
        layoutTable.add(backButton).bottom();


        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(game.screenManager.pop());
            }
        });
    }


    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        stage.addActor(MenuScreen.getMenuScreen().backgroundImage);
        stage.addActor(layoutTable);
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
