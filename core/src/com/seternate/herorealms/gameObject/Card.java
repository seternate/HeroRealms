package com.seternate.herorealms.gameObject;

import com.badlogic.gdx.utils.XmlReader.Element;

import java.util.ArrayList;

public class Card {
    private int id;
    private String name;
    private String images[];
    //Todo: Create Enum 'Types'
    private ArrayList<String> types;
    //Todo: Create 'Text' class
    private String text;
    private Faction faction;
    private int cost;
    private Defense defense;
    private int level;
    private CardRole cardRole;
    private int setQty;


    public Card() {}

    public Card(Element card) {
        id = Integer.valueOf(card.getAttribute("id"));
        name = card.getAttribute("name");
        images = new String[2];
        images[0] = card.getChildByName("images").getChild(0).getText();
        images[1] = card.getChildByName("images").getChild(1).getText();
        types = new ArrayList<String>();
        for(Element typ : card.getChildByName("types").getChildrenByName("typ")) types.add(typ.getText());
        text = card.getChildByName("text").getText();
        faction = Faction.valueOf(card.getChildByName("faction").getText() == null ? "DEFAULT" : card.getChildByName("faction").getText().toUpperCase());
        cost = Integer.valueOf(card.getChildByName("cost").getText() == null ? "0" : card.getChildByName("cost").getText());
        defense = new Defense(card);
        level = Integer.valueOf(card.getChildByName("level").getText() == null ? "0" : card.getChildByName("level").getText());
        String role = card.getChildByName("role").getText();
        if(role.equals("Market Deck")) cardRole = CardRole.MARKET_DECK;
        else if(role.equals("Starting Deck")) cardRole = CardRole.STARTING_DECK;
        else if(role.equals("Fire Gems")) cardRole = CardRole.FIRE_GEMS;
        else cardRole = CardRole.DEFAULT;
        setQty = Integer.valueOf(card.getChildByName("setqty").getText());
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String[] getImages() {
        return images;
    }

    public ArrayList<String> getTypes() {
        return types;
    }

    public String getText() {
        return text;
    }

    public Faction getFaction() {
        return faction;
    }

    public int getCost() {
        return cost;
    }

    public Defense getDefense() {
        return defense;
    }

    public int getLevel() {
        return level;
    }

    public CardRole getCardRole() {
        return cardRole;
    }

    public int getSetQty() {
        return setQty;
    }
}
