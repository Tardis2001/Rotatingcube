package org.matheus;

import org.matheus.*;

public class MainLoop{

    public static void main(String[] args) {
        MainLoop main = new MainLoop();
        Engine gameEng = new Engine("Tutorial", new Window.WindowOptions(800,600,false),main);
        gameEng.start();
    }

    public void cleanup() {
        // Nothing to be done yet
    }


    public void init(Window window, Scene scene, Render render) {
        // Nothing to be done yet
    }


    public void input(Window window, Scene scene, long diffTimeMillis) {
        // Nothing to be done yet
    }


    public void update(Window window, Scene scene, long diffTimeMillis) {
        // Nothing to be done yet
    }
}
