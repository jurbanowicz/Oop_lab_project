package Simulation;

import IO.CSVFileWriter;
import Project.*;
import javafx.application.Platform;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

public class SimulationEngine implements Runnable {
    private IWorldMap map;
    private float startingEnergy;
    private int genotypeLength;
    private ArrayList<Animal> animals;
    private ArrayList<Grass> grassOnMap;

    private int simAge;
    private int grassGrowingEachDay;
    private GrassSpawner grassSpawner;
    private AnimalBreeder animalBreeder;
    private boolean isPaused;
    private int dailyEnergyCost;
    private int sleepTime;
    private boolean endSim;
    private float averageDeathAge;
    private int numOfDead;
    private int[] mostPopularGenom;
    private CSVFileWriter csvFileWriter;


    /**
     *
     * @param map simulation map for animals to be placed on
    */
    public SimulationEngine(IWorldMap map, SimulationParameters parameters) {
        this.map = map;
        this.simAge = 0;
        this.startingEnergy = parameters.startingEnergy;
        this.dailyEnergyCost = parameters.dailyEnergyCost;
        this.genotypeLength = parameters.genotypeLength;
        this.animals = generateAnimals(parameters.noAnimals, map);
        this.grassGrowingEachDay = parameters.grassGrowingEachDay;
        this.grassSpawner = new GrassSpawner(parameters.grassEnergy, parameters.grassVariant);
        this.animalBreeder = new AnimalBreeder(map, parameters.breedingCost, parameters.breedingMinEnergy, parameters.genotypeLength, new GenotypeGenerator(this.genotypeLength, parameters.numberOfMutations, parameters.mutationVariant));
        this.map.setAnimalBreeder(this.animalBreeder);
        this.isPaused = false;
        this.grassOnMap = new ArrayList<>();
        this.sleepTime = parameters.sleepTime;
        this.endSim = false;
        this.averageDeathAge = 0;
        this.numOfDead = 0;

        if (parameters.writeToCSV) {
            csvFileWriter = new CSVFileWriter();
        } else {
            csvFileWriter = null;
        }

        for (Animal animal: animals) {
            animal.setMoveVariant(parameters.moveVariant);
            map.place(animal);
        }
        growGrass(parameters.startGrassAmount);
    }

    /**
     * Creates given number of animals with random parameters
     * @param n number of animals to be created
     * @param map game map
     * @return List with all the created animals
     */
    private ArrayList<Animal> generateAnimals(int n, IWorldMap map) {
        Vector2d upperRight = map.getSize();
        ArrayList<Animal> animals = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            int x = ThreadLocalRandom.current().nextInt(0, upperRight.x);
            int y = ThreadLocalRandom.current().nextInt(0, upperRight.y);
            animals.add(new Animal(new Vector2d(x, y), map, genotypeLength, startingEnergy));
        }
        return animals;
    }
    public void pauseSim(boolean value) {
        isPaused = value;
//        System.out.println(value);
    }
    public boolean isSimPaused() {
        return isPaused;
    }
    public void endSimulation() {
        this.endSim = true;
    }

    public void run() {
//        System.out.println(map);
//        printCurrentAnimals();
//        System.out.println("------");

        while (true) {
//            System.out.println("Check");
            if (endSim) {
                break;
            }

            if (!(isSimPaused())) {
                if (animals.size() == 0) {
                    break;
                }

                runSimulationDay();
                map.notifyObserver();

                if (csvFileWriter != null) {
                    csvFileWriter.writeLine(getSimData());
                }
            }

            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                System.out.println(e);
            }
        }
        if (csvFileWriter != null) {
            csvFileWriter.saveFile();
        }
    }
    private void runSimulationDay() {
        removeDeadAnimals();
        moveAnimals();
        consumeGrass();
        breedAnimals();
        increaseAge();
        decreaseEnergy();
        growGrass(grassGrowingEachDay);
    }
    public void removeDeadAnimals() {
        for (Animal animal: animals) {
            if (animal.isAnimalDead()) {
                animal.setDeathDate(simAge);
                map.addDeathPlace(animal.getPosition());
                int age = animal.getAge();
                calculateDeathAvg(age);
            }
        }
        animals.removeIf(Animal::isAnimalDead);
    }
    private void calculateDeathAvg(int age) {
        float current = this.averageDeathAge * this.numOfDead;
        current = current + age;
        numOfDead++;
        averageDeathAge = current/numOfDead;
    }
    public void moveAnimals() {
        for (Animal animal : animals) {
            animal.move();
        }
    }
    public void consumeGrass() {
        grassOnMap.removeAll(map.animalsConsumption());
    }
    public void breedAnimals() {
        ArrayList<Animal> newAnimals = map.breedPossible();
        this.animals.addAll(newAnimals);
    }
    public void increaseAge() {
        for (Animal animal : animals) {
            animal.addAge();
        }
        simAge++;
//        System.out.println(simAge);
    }
    public void growGrass(int amount) {
        for (int i = 0; i < amount; i++) {
            if (getAvailableSpaces() > 0) {
                grassOnMap.add(grassSpawner.growGrass(map));
            } else {
                break;
            }
        }
    }
    public int getAnimalsOnMap() {
        return animals.size();
    }
    public int getGrassOnMap() {
        return map.countGrass();
    }
    public int getSimAge() {
        return this.simAge;
    }
    private void decreaseEnergy() {
        for (Animal animal: animals) {
            animal.subtractEnergy(dailyEnergyCost);
        }
    }
    public ArrayList<Animal> getAnimals() {
        return this.animals;
    }
    public ArrayList<Grass> getGrass() {
        return this.grassOnMap;
    }
    private String getSimData() {
        String data = simAge + "," + getAnimalsOnMap() + ","
                + getGrassOnMap() + "," + getAvailableSpaces() + ","
                + getAverageEnergy() + "," + getAverageDeathAge() + ","
                + Arrays.toString(getMostPopularGenom()).replace(", ", " ")
                + "\n";
        return data;
    }
    public float getAverageEnergy() {
        float total = 0;
        for (Animal animal: animals) {
            total += animal.getEnergy();
        }
        return total/animals.size();
    }
    public int getAvailableSpaces() {
//        return map.countAvailableSpaces();
        return (map.getSize().x*map.getSize().y) -(getGrassOnMap() + getAnimalsOnMap());
    }
    public float getAverageDeathAge() {
        return this.averageDeathAge;
    }
    public int[] getMostPopularGenom() {
        HashMap<int[], Integer> amount = new HashMap<>();
        for (Animal animal: animals) {
            if (amount.containsKey(animal.getGenotype())) {
                int a = amount.remove(animal.getGenotype());
                a++;
                amount.put(animal.getGenotype(), a);
            } else {
                amount.put(animal.getGenotype(), 1);
            }
        }
        int[] result = new int[0];
        int max = 0;
        for (int[] genotype: amount.keySet()) {
            if (amount.get(genotype) > max) {
                max = amount.get(genotype);
                result = genotype;
            }
        }
        this.mostPopularGenom =  result;
        return result;
    }
    public ArrayList<Animal> getAnimalsWithMostPopularGenom() {
        ArrayList<Animal> result = new ArrayList<>();
        for (Animal animal: animals) {
            if (Arrays.equals(animal.getGenotype(), mostPopularGenom)) {
                result.add(animal);
            }
        }
        return result;
    }
}
