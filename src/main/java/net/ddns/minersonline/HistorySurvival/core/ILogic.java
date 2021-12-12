package net.ddns.minersonline.HistorySurvival.core;

public interface ILogic {
    void init() throws Exception;
    void input();
    void update();
    void render();
    void cleanUp();
}