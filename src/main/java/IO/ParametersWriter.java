package IO;

import Simulation.SimulationParameters;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class ParametersWriter {

    public void writeParameters(SimulationParameters parameters, String settingsName) {

        String text = parameters.toString();

        try {

            FileWriter fWriter = new FileWriter(
                    "/Users/jacekurbanowicz/Desktop/WIET/Obiektowe/oop_project_1/src/main/resources/SimSpecs/" + settingsName + ".txt");

            fWriter.write(text);
            System.out.println(text);
            fWriter.close();
        }
        catch (IOException e) {
            System.out.print(e.getMessage());
        }
    }
    public void write(SimulationParameters parameters, String settingsName) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("/Users/jacekurbanowicz/Desktop/WIET/Obiektowe/oop_project_1/src/main/resources/SimSpecs/" + settingsName + ".txt"));
            writer.write(parameters.noAnimals + "\n");
            writer.write(parameters.startingEnergy + "\n");
            writer.write(parameters.dailyEnergyCost + "\n");
            writer.write(parameters.genotypeLength + "\n");
            writer.write(parameters.startGrassAmount + "\n");
            writer.write(parameters.grassGrowingEachDay + "\n");
            writer.write(parameters.grassEnergy + "\n");
            writer.write(parameters.breedingCost + "\n");
            writer.write(parameters.breedingMinEnergy + "\n");
            writer.write(parameters.numberOfMutations + "\n");
            writer.write(parameters.mapHeight + "\n");
            writer.write(parameters.mapWidth + "\n");
            writer.write(parameters.sleepTime + "\n");
            writer.write(parameters.mapVariant + "\n");
            writer.write(parameters.mutationVariant + "\n");
            writer.write(parameters.moveVariant + "\n");
            writer.close();
        } catch (IOException e) {
            System.out.print(e.getMessage());
        }

    }
}
