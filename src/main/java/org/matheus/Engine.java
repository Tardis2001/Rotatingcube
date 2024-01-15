package org.matheus;

public class Engine {
    public static final int TARGET_UPS = 60;
    public static final int TARGET_FPS = 60;
    private final Window window;
    private boolean running;
    private Render render;
    private Scene scene;
    private MainLoop appLogic;
    public Engine(String windowTitle, Window.WindowOptions options,MainLoop appLogic) {
        this.window = new Window(windowTitle, options, () -> {
            resize();
            return null;
        });
        scene = new Scene();
        this.appLogic = appLogic;
        render = new Render();
        running = true;
    }
    private void cleanup(){
        window.cleanup();
        scene.cleanup();
        render.cleanup();

    }
    public void stop(){
        running = false;
    }
    public void destroy(){

    }
    private void run() {
        long initialTime = System.currentTimeMillis();
        float timeU = 1000.0f / TARGET_UPS;
        float timeR = TARGET_FPS > 0 ? 1000.0f / TARGET_FPS : 0;
        float deltaUpdate = 0;
        float deltaFps = 0;

        long updateTime = initialTime;
        while (running && !window.windowShouldClose()) {
            window.pollEvents();
//            window.glfw/**/
            long now = System.currentTimeMillis();
            deltaUpdate += (now - initialTime) / timeU;
            deltaFps += (now - initialTime) / timeR;

            if (TARGET_FPS <= 0 || deltaFps >= 1) {
                appLogic.input(window, scene, now - initialTime);
            }

            if (deltaUpdate >= 1) {
                long diffTimeMillis = now - updateTime;
                  updateTime = now;
                    deltaUpdate--;
                }

                if (TARGET_FPS <= 0 || deltaFps >= 1) {
                    render.render(window, scene);
                deltaFps--;
                window.update();
                }
            initialTime = now;
        }
        cleanup();
    }
    public void start(){
        running = true;
    }
    private void resize() {
    }
}
