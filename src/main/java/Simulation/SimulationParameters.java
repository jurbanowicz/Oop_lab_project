package Simulation;
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
    public final int grassVariant;
    public final int mapVariant;
    public final int moveVariant;
    public final int mapHeight;
    public final int mapWidth;
    public final int sleepTime;
    public final boolean writeToCSV;

    public SimulationParameters(int noAnimals, float startingEnergy,int dailyEnergyCost, int genotypeLength, int startGrassAmount, int grassGrowingEachDay, int grassEnergy, int breedingCost, int breedingMinEnergy, int numeberOfMutations, int mutationVariant, int mapHeight, int mapWidth, int mapVariant, int moveVariant, int sleepTime, int grassVariant, boolean writeToCSV) {
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
        this.mapHeight = mapHeight;
        this.mapWidth = mapWidth;
        this.mapVariant = mapVariant;
        this.sleepTime = sleepTime;
        this.moveVariant = moveVariant;
        this.grassVariant = grassVariant;
        this.writeToCSV = writeToCSV;
    }

    @Override
    public String toString() {
        String result = noAnimals + '\n' +
                String.valueOf(startingEnergy) + '\n' + dailyEnergyCost + '\n' +
                genotypeLength+ '\n' + startGrassAmount+ '\n' +
                grassGrowingEachDay+ '\n' + grassEnergy+ '\n'
                + breedingCost+ '\n' + breedingMinEnergy+ '\n' +
                numberOfMutations+ '\n' + mapHeight+ '\n' +
                mapWidth+ '\n' + sleepTime+ '\n' +
                mapVariant+ '\n' + mutationVariant+ '\n' + moveVariant + '\n';
        return result;
    }
}
