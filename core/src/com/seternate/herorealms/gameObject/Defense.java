package com.seternate.herorealms.gameObject;

import com.badlogic.gdx.utils.XmlReader.Element;

public class Defense {
    private boolean guard;
    private int defense;


    public Defense() {}

    public Defense(Element card) {
        this.guard = card.getChildByName("defense").getAttribute("guard").equals("0") ? false : true;
        this.defense = Integer.valueOf(card.getChildByName("defense").getText().equals("None") ? "0" : card.getChildByName("defense").getText());
    }

    public boolean isGuard() {
        return guard;
    }

    public int getDefense() {
        return defense;
    }
}
