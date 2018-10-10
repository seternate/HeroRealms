package com.seternate.herorealms.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

import java.io.File;

public class Player {
    public static final String STATS = "player";


    public static Player load() {
        Preferences pref = Gdx.app.getPreferences(Player.STATS);
        return new Player(pref.getString("name"));
    }


    private String name;


    public Player(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void safe() {
        Preferences pref = Gdx.app.getPreferences(Player.STATS);
        pref.putString("name", name);
        pref.flush();
    }

}
