package net.ddns.minersonline.engine.core.managers;

import net.ddns.minersonline.HistorySurvival.Launch;
import net.ddns.minersonline.shared.LogOutputStream;
import net.ddns.minersonline.engine.core.ILogic;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFWErrorCallback;


import java.io.PrintStream;
import static org.lwjgl.glfw.GLFW.*;

public class EngineManager {
    private static final Logger LOGGER = LogManager.getLogger(EngineManager.class);
    private final LogOutputStream errorLog = new LogOutputStream(LOGGER, Level.FATAL);

    public static final long NANOSECOND = 1000000000L;
    public static final float FRAMERATE = 1000;

    private static int fps;
    private static final float frameTime = 1.0f / FRAMERATE;

    private boolean isRunning;
    private WindowManager window;
    private MouseManager mouseManager;

    private GLFWErrorCallback errorCallback;
    private ILogic gameLogic;

    private void init() throws Exception{
        errorCallback = GLFWErrorCallback.createPrint(new PrintStream(errorLog));
        glfwSetErrorCallback(errorCallback);
        window = Launch.getWindow();
        mouseManager = new MouseManager();

        gameLogic = Launch.getGame();
        window.init();
        mouseManager.init();
        gameLogic.init();
    }

    public void start() throws Exception{
        init();
        if(isRunning){
            return;
        }
        run();
    }

    private void run() throws Exception{
        isRunning = true;
        int frames = 0;
        long frameCounter = 0;
        long lastTime = System.nanoTime();
        double unprocessedTime = 0;

        while (isRunning){
            boolean render = false;
            long startTime = System.nanoTime();
            long passedTime = startTime - lastTime;
            lastTime = startTime;

            unprocessedTime += passedTime / (double) NANOSECOND;
            frameCounter += passedTime;

            input();

            while(unprocessedTime > frameTime){
                render = true;
                unprocessedTime -= frameTime;

                if(window.windowShouldClose()){
                    stop();
                    return;
                }
                if(frameCounter >= NANOSECOND){
                    setFps(frames);
                    frames = 0;
                    frameCounter = 0;
                }
            }

            if(render){
                update();
                render();
                frames++;
            }
        }
        cleanUp();
    }

    private void stop(){
        if(!isRunning){
            return;
        }
        isRunning = false;
    }

    private void input() throws Exception{
        mouseManager.getDisPos().set(0, 0);
        mouseManager.input();
        gameLogic.input(mouseManager);
    }

    private void render() throws Exception{
        gameLogic.render();
        window.update();
    }

    private void update() throws Exception{
        gameLogic.update();
    }

    private void cleanUp() throws Exception{
        window.cleanUp();
        gameLogic.cleanUp();
        errorCallback.free();
        glfwTerminate();
    }

    public static int getFps() {
        return fps;
    }

    public static void setFps(int fps) {
        EngineManager.fps = fps;
    }
}
