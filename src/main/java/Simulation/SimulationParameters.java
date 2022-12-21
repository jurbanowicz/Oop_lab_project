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
    public final int dailyEnergyCost;
    public final int numberOfMutations;
    public final int mutationVariant;

    public SimulationParameters(int noAnimals, float startingEnergy,int dailyEnergyCost, int genotypeLength, int startGrassAmount, int grassGrowingEachDay, int grassEnergy, int breedingCost, int breedingMinEnergy, int numeberOfMutations, int mutationVariant) {
        this.noAnimals = noAnimals;
        this.startingEnergy = startingEnergy;
        this.dailyEnergyCost = dailyEnergyCost;
        this.genotypeLength = genotypeLength;
        this.startGrassAmount = startGrassAmount;
        this.grassGrowingEachDay = grassGrowingEachDay;
        this.grassEnergy = grassEnergy;
        this.breedingCost = breedingCost;
        this.breedingMinEnergy = breedingMinEnergy;
        this.numberOfMutations = numeberOfMutations;
        this.mutationVariant = mutationVariant;
    }
}
