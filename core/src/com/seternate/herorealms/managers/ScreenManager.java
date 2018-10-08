package com.seternate.herorealms.managers;

import com.badlogic.gdx.Screen;

import java.util.ArrayList;


public class ScreenManager<T extends Screen> {
    final ArrayList <T> screens = new ArrayList<T>();

    public T add(T screen) {
        for(T s : screens) {
            if(screen.getClass() == s.getClass()) return s;
        }
        screens.add(screen);
        return screen;
    }

    public T get(Class<T> screenClass) {
        for(T s : screens) {
            if(s.getClass() == screenClass) return s;
        }
        return null;
    }
}
