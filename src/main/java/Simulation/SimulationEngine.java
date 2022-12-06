package Simulation;

import Project.*;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class SimulationEngine {
    IWorldMap map;
    float startingEnergy;
    int genotypeLength;
    ArrayList<Animal> animals;

    int simAge;
    int grassGrowingEachDay;
    GrassSpawner grassSpawner;
    AnimalBreeder animalBreeder;


    /**
     *
     * @param map simulation map for animals to be placed on
    */
    public SimulationEngine(IWorldMap map, SimulationParameters parameters) {
        this.map = map;
        this.simAge = 0;
        this.startingEnergy = parameters.startingEnergy;
        this.genotypeLength = parameters.genotypeLength;
        this.animals = generateAnimals(parameters.noAnimals, map);
        this.grassGrowingEachDay = parameters.grassGrowingEachDay;
        this.grassSpawner = new GrassSpawner(parameters.grassEnergy);
        this.animalBreeder = new AnimalBreeder(map, parameters.breedingCost, parameters.breedingMinEnergy, parameters.genotypeLength, new GenotypeGenerator());
        this.map.setAnimalBreeder(this.animalBreeder);

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

    public void run() {
//        System.out.println(map);
//        printCurrentAnimals();
//        System.out.println("------");

        while (simAge < 30) {

            if (animals.size() == 0) {
                break;
            }
            System.out.println(map);
            printCurrentAnimals();

            removeDeadAnimals();
            moveAnimals();
            consumeGrass();
            breedAnimals();
            increaseAge();
            growGrass(grassGrowingEachDay);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println(e);
            }
        }
    }
    public void removeDeadAnimals() {
        animals.removeIf(Animal::isAnimalDead);
//        for (Animal animal: animals) {
//            if (animal.isAnimalDead()) {
//                map.removeDeadAnimal(animal);
//            }
//        }
    }
    /**
     *  Move each of the animals on the map according to their current gene
     */
    public void moveAnimals() {
        for (Animal animal : animals) {
            animal.move();
        }
    }
    public void consumeGrass() {
        map.animalsConsumption();
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
        System.out.println(simAge);
    }
    public void growGrass(int amount) {
        for (int i = 0; i < amount; i++) {
            grassSpawner.growGrass(map);
        }
    }
    public void printCurrentAnimals() {
        System.out.println("Number of animals on map: " + animals.size());
//        for (Animal animal : animals) {
//            System.out.println("Animals current position: " + animal.getPosition().toString() + animal.getDirection().toString() + " Energy: "+ animal.getEnergy());
//        }
    }
}
