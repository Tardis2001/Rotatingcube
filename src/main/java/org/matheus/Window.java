package org.matheus;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryUtil;
import java.util.concurrent.Callable;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {

    private long windowHandle;
    private Callable<Void> resizeFunc;
    private int width;

    private int height;
    public Window(String title,WindowOptions opts,Callable<Void> resizeFunc){
        this.resizeFunc = resizeFunc;
        if(!glfwInit()){
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_RESIZABLE,GL_FALSE);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);

        if(opts.compatibleProfile){
            glfwWindowHint(GLFW_OPENGL_PROFILE,GLFW_OPENGL_COMPAT_PROFILE);
        }else {
            glfwWindowHint(GLFW_OPENGL_PROFILE,GLFW_OPENGL_CORE_PROFILE);
            glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT,GL_TRUE);
        }

        if(opts.width > 0 && opts.height > 0) {
            this.width = opts.width;
            this.height = opts.height;
        }else{
            glfwWindowHint(GLFW_MAXIMIZED,GLFW_FALSE);
            GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
            width = vidMode.width();
            height = vidMode.height();

        }

        windowHandle = glfwCreateWindow(width,height,title,NULL,NULL);
        if(windowHandle == NULL){
            throw new IllegalStateException("Failed to create the GLFW window");
        }
        glfwMakeContextCurrent(windowHandle);
        GL.createCapabilities();
        glViewport(0,0,width,height);
        glfwSetFramebufferSizeCallback(windowHandle,(windowHandle,width,height)->{
            if(resizeFunc!= null){
                try {
                    resizeFunc.call();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });

        if(opts.fps > 0){
            glfwSwapInterval(0);
        }else{
            glfwSwapInterval(1);
        }

        glfwShowWindow(windowHandle);

        glfwSetKeyCallback(windowHandle,(windowHandle,key,scancode,action,mods)->{
            if(key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE){
                glfwSetWindowShouldClose(windowHandle,true);
            }
        });

        int[] arrWidth = new int[1];
        int[] arrHeight = new int[1];
        glfwGetFramebufferSize(windowHandle,arrWidth,arrHeight);
        width = arrWidth[0];
        height = arrHeight[0];
    }
    public void cleanup(){
        glfwFreeCallbacks(windowHandle);
        glfwDestroyWindow(windowHandle);
        glfwTerminate();
//        GLFWErrorCallback callback = new GLFWErrorCallback(GLFW_ERROR);
    }
    public int getHeight(){
        return height;
    }
    public int getWidth(){
        return width;
    }
    public long getWindowHandle(){
        return windowHandle;
    }
    public void pollEvents(){
        glfwPollEvents();
    }
    public void update(){
        glfwSwapBuffers(windowHandle);
    }
    public void setTitle(String title){
        glfwSetWindowTitle(windowHandle,title);
    }
    public boolean windowShouldClose(){
        return glfwWindowShouldClose(windowHandle);
    }
    protected void resized(int width,int height){
        this.width = width;
        this.height = height;
        try{
            resizeFunc.call();
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
    public static class WindowOptions {
        public WindowOptions(int width,int height,boolean compatibleProfile){
            this.width = width;
            this.height = height;
            this.compatibleProfile = compatibleProfile;
            this.fps = 60;
        }
        public boolean compatibleProfile;
        public int fps;
//        public int ups = Engine.TARGET_UPS;
        public int width;
        public int height;
    }
}