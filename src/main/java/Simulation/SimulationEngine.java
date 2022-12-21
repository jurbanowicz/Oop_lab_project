package Simulation;

import Project.*;
import javafx.application.Platform;

import java.util.ArrayList;
import java.util.Collection;
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
        this.grassSpawner = new GrassSpawner(parameters.grassEnergy);
        this.animalBreeder = new AnimalBreeder(map, parameters.breedingCost, parameters.breedingMinEnergy, parameters.genotypeLength, new GenotypeGenerator(this.genotypeLength, parameters.numberOfMutations, parameters.mutationVariant));
        this.map.setAnimalBreeder(this.animalBreeder);
        this.isPaused = false;
        this.grassOnMap = new ArrayList<>();

        for (Animal animal: animals) {
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

    public void run() {
//        System.out.println(map);
//        printCurrentAnimals();
//        System.out.println("------");

        while (true) {
//            System.out.println("Check");
            if (!(isSimPaused())) {
                if (animals.size() == 0) {
                    break;
                }
//            System.out.println(map);

//                printCurrentAnimals();
                runSimulationDay();
                map.notifyObserver();
            }

            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                System.out.println(e);
            }
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
        animals.removeIf(Animal::isAnimalDead);
//        for (Animal animal: animals) {
//            if (animal.isAnimalDead()) {
////                animal.setDeathDate(simAge);
//                animals.remove(animal);
//
//            }
//        }
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
            grassOnMap.add(grassSpawner.growGrass(map));
        }
    }
    public void skipAges(int amount) {
        int targetAge = simAge + amount;
        while (simAge < targetAge) {
            runSimulationDay();
        }
        map.notifyObserver();
    }
    public void printCurrentAnimals() {
        System.out.println("Number of animals on map: " + animals.size());
//        for (Animal animal : animals) {
//            System.out.println("Animals current position: " + animal.getPosition().toString() + animal.getDirection().toString() + " Energy: "+ animal.getEnergy());
//        }
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
}
