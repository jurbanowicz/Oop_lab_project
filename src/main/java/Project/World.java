package Project;

import Simulation.SimulationEngine;
import Simulation.SimulationParameters;
import Visualization.App;
import javafx.application.Application;

public class World {
    public static void main(String[] args) {
        SimulationParameters params = new SimulationParameters(1, 10, 1, 5, 2, 4, 2, 5);
        IWorldMap map = new EarthMap(10, 10);
        SimulationEngine engine = new SimulationEngine(map, params);
        engine.run();
        Application.launch(App.class, args);
    }
}
