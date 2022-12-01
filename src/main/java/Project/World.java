package Project;

import Simulation.SimulationEngine;

public class World {
    public static void main(String[] args) {
        IWorldMap map = new EarthMap(10, 10);
        SimulationEngine engine = new SimulationEngine(map, 1, 10, 1);
        engine.run();
    }
}
