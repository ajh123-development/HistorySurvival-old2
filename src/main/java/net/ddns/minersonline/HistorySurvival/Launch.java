package net.ddns.minersonline.HistorySurvival;

import net.ddns.minersonline.HistorySurvival.core.EngineManager;
import net.ddns.minersonline.HistorySurvival.core.WindowManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;

import java.net.URISyntaxException;
import java.util.Objects;

public class Launch {
    private static final Logger LOGGER = LogManager.getLogger(Launch.class);
    private static final WindowManager wm = new WindowManager("History Survival", 0, 0, false);
    private static EngineManager engineManager;

    public Launch(){
        LOGGER.info("History Survival client is ready to start.");
        engineManager = new EngineManager();
        LOGGER.info("Starting!");
        LOGGER.info("Using default game log configuration log4j2.xml (outputs XML)");
        LoggerContext context = (LoggerContext) LogManager.getContext(false);
        try {
            context.setConfigLocation(Objects.requireNonNull(this.getClass().getClassLoader().getResource("log4j2.xml")).toURI());
        } catch (URISyntaxException e) {
            LOGGER.fatal(e);
        }

        LOGGER.info("Loading for game History Survival");
        try {
            engineManager.start();
        } catch(Exception e) {
            LOGGER.fatal(e);
        }

        while (!wm.windowShouldClose()){
            wm.update();
        }

        LOGGER.info("Stopping!");
        wm.cleanUp();
    }

    public static void main(String[] args){
        LOGGER.info("Preparing to launch history survival client");
        Launch launch = new Launch();
    }

    public static WindowManager getWindow() {
        return wm;
    }
}
