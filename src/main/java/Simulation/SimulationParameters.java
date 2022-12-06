package Simulation;

import Project.IWorldMap;

public class SimulationParameters {
    public final int noAnimals;
    public final float startingEnergy;
    public final int genotypeLength;
    public final int startGrassAmount;
    public final int grassGrowingEachDay;
    public final int grassEnergy;
    public final int breedingCost;
    public final int breedingMinEnergy;

    public SimulationParameters(int noAnimals, float startingEnergy, int genotypeLength, int startGrassAmount, int grassGrowingEachDay, int grassEnergy, int breedingCost, int breedingMinEnergy) {
        this.noAnimals = noAnimals;
        this.startingEnergy = startingEnergy;
        this.genotypeLength = genotypeLength;
        this.startGrassAmount = startGrassAmount;
        this.grassGrowingEachDay = grassGrowingEachDay;
        this.grassEnergy = grassEnergy;
        this.breedingCost = breedingCost;
        this.breedingMinEnergy = breedingMinEnergy;
    }
}
