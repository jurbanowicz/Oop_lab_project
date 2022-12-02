package Project;

import Simulation.SimulationEngine;
import Simulation.SimulationParameters;

public class World {
    public static void main(String[] args) {
        IWorldMap map = new EarthMap(10, 10);
        SimulationParameters params = new SimulationParameters(5, 10, 5, 5, 1, 2, 5);
        SimulationEngine engine = new SimulationEngine(map, params);
        engine.run();
    }
}
