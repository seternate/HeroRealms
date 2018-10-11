package com.seternate.herorealms.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.seternate.herorealms.Main;

public class CardDetailScreen implements Screen {
    private static CardDetailScreen cardDetailScreen = null;

    public static CardDetailScreen getCardDetailScreen() {
        return cardDetailScreen;
    }

    public static CardDetailScreen newCardDetailScreen(final Main game) {
        if(cardDetailScreen == null) cardDetailScreen = new CardDetailScreen(game);
        return cardDetailScreen;
    }


    final Main game;
    Stage stage;
    Element card;
    Cell cell;

    Skin skin;
    Image cardImage, background;
    Label detailsLabel[][];
    Table table, details;
    TextButton back_btn;


    private CardDetailScreen() {
        game = null;
    }

    private CardDetailScreen(final Main game) {
        this.game = game;
        stage = new Stage();
        card = game.gameDataXML.getChild(0).getChildrenByName("card").get(0);
        skin = new Skin(Gdx.files.internal("skins/plain-james/plain-james-ui.json"));
        detailsLabel = new Label[2][9];

        background = new Image(game.assetManager.manager.get("background.jpg", Texture.class));
        background.setPosition((Gdx.graphics.getWidth() - background.getWidth())/2, -(background.getHeight() - Gdx.graphics.getHeight()));
        stage.addActor(background);

        back_btn = new TextButton("Back", skin);
        back_btn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(game.screenManager.pop());
            }
        });
        back_btn.getLabel().setFontScale(1.5f);

        detailsLabel[0][0] = new Label("Name:", skin, "white-big");
        detailsLabel[0][1] = new Label("Type:", skin, "white-big");
        detailsLabel[0][2] = new Label("Text:", skin, "white-big");
        detailsLabel[0][3] = new Label("Faction:", skin, "white-big");
        detailsLabel[0][4] = new Label("Cost:", skin, "white-big");
        detailsLabel[0][5] = new Label("Defense:", skin, "white-big");
        detailsLabel[0][6] = new Label("Level:", skin, "white-big");
        detailsLabel[0][7] = new Label("Role:" , skin, "white-big");
        detailsLabel[0][8] = new Label("Quantity:", skin, "white-big");
        detailsLabel[1][0] = new Label(card.getAttribute("name"), skin, "white-big");
        detailsLabel[1][1] = new Label(getType(), skin, "white-big");
        detailsLabel[1][2] = new Label(card.getChildByName("text").getText(), skin, "white-big");
        detailsLabel[1][2].setWrap(true);
        detailsLabel[1][3] = new Label(card.getChildByName("faction").getText(), skin, "white-big");
        detailsLabel[1][4] = new Label(card.getChildByName("cost").getText(), skin, "white-big");
        detailsLabel[1][5] = new Label(card.getChildByName("defense").getAttribute("guard").equals("1") ? "Guard" + card.getChildByName("defense").getText() : card.getChildByName("defense").getText(), skin, "white-big");
        detailsLabel[1][6] = new Label(card.getChildByName("level").getText(), skin, "white-big");
        detailsLabel[1][7] = new Label(card.getChildByName("role").getText(), skin, "white-big");
        detailsLabel[1][8] = new Label(card.getChildByName("setqty").getText(), skin, "white-big");


        details = new Table();
        for(int i = 0; i < detailsLabel[0].length; i++) {
            for(int k = 0; k < detailsLabel.length; k++) {
                if(k == 0) {
                    details.add(detailsLabel[k][i]).expandY().right().padRight(Gdx.graphics.getHeight()/50);
                } else if(k == 1 && i == 8) {
                    Table table = new Table();
                    table.add(detailsLabel[k][i]).expand().left();
                    table.add(back_btn).expand().right().bottom();
                    details.add(table).expand().fill();
                } else if(k == 1 && i == 2) {
                    cell = details.add(detailsLabel[1][2]).expand().left();
                } else {
                    details.add(detailsLabel[k][i]).expand().left();
                }
            }
            details.row();
        }


        table = new Table();
        stage.addActor(table);
        update();
    }


    public String getType() {
        String typ = new String();
        for(int i = 0; i < card.getChild(1).getChildrenByName("type").size; i++) {
            typ += card.getChild(1).getChildrenByName("type").get(i).getText();
            if(i < card.getChild(1).getChildrenByName("type").size - 1) typ += ", ";
        }
        return typ;
    }

    public CardDetailScreen setCard(Element card) {
        this.card = card;
        update();
        return this;
    }

    public void update() {
        cardImage = new Image(game.assetManager.manager.get("cards/" + card.getChild(0).getChild(0).getText(), Texture.class));

        detailsLabel[1][0].setText(card.getAttribute("name"));
        detailsLabel[1][1].setText(getType());
        detailsLabel[1][2].setText(card.getChildByName("text").getText());
        detailsLabel[1][3].setText(card.getChildByName("faction").getText());
        detailsLabel[1][4].setText(card.getChildByName("cost").getText());
        detailsLabel[1][5].setText(card.getChildByName("defense").getAttribute("guard").equals("1") ? "Guard " + card.getChildByName("defense").getText() : card.getChildByName("defense").getText());
        detailsLabel[1][6].setText(card.getChildByName("level").getText());
        detailsLabel[1][7].setText(card.getChildByName("role").getText());
        detailsLabel[1][8].setText(card.getChildByName("setqty").getText());


        table.clear();
        table.setFillParent(true);
        table.add(cardImage).expandY().padLeft(Gdx.graphics.getHeight()/50).padRight(Gdx.graphics.getHeight()/50)
                .height(cardImage.getHeight()*1.5f).width(cardImage.getWidth()*1.5f);
        table.add(details).expand().fill();
        detailsLabel[1][2].setWidth(Gdx.graphics.getWidth() - detailsLabel[0][8].getWidth() - cardImage.getWidth()*1.5f - 3*Gdx.graphics.getHeight()/50);
        cell.width(detailsLabel[1][2].getWidth());
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

    }
}
