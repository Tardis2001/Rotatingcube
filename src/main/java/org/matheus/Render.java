package org.matheus;

import org.lwjgl.opengl.GL;

import static org.lwjgl.opengl.GL11.*;

public class Render {
    public Render() {
        GL.createCapabilities();
    }
    public void cleanup(){

    }
    public void render(){

    }

    public void render(Window window, Scene scene) {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

    }
}
