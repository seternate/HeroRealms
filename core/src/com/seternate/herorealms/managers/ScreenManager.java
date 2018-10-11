package com.seternate.herorealms.managers;

import com.badlogic.gdx.Screen;

import java.util.Stack;


public class ScreenManager<T extends Screen> {
    final Stack<T> screenStack = new Stack<T>();


    public T push(T screen) {
        return screenStack.push(screen);
    }

    public T pop() {
        screenStack.pop();
        return screenStack.peek();
    }

    public T peek() {
        return screenStack.peek();
    }
}
