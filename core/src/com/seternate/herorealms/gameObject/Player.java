package com.seternate.herorealms.gameObject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

import java.util.Random;

public class Player {
    public static final String STATS = "player";


    public static Player load() {
        Preferences pref = Gdx.app.getPreferences(Player.STATS);
        return new Player(pref.getString("name"));
    }


    private String name;
    private int id;


    public Player() {}

    public Player(String name) {
        id = new Random().nextInt();
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

    public int getID() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof Player && ((Player) o).id == this.id){
            return true;
        }
        return false;
    }
}
