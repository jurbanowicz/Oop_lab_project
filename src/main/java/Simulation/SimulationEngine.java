package Simulation;

import Project.Animal;
import Project.IWorldMap;
import Project.Vector2d;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class SimulationEngine {
    IWorldMap map;
    float startingEnergy;
    int genotypeLength;
    ArrayList<Animal> animals;

    int simAge;


    /**
     *
     * @param map simulation map for animals to be placed on
     * @param noAnimals number of animals in game
     * @param startingEnergy amount of energy that animals start the simulation with
     * @param genotypeLength length of genotype created for the animals
     */
    public SimulationEngine(IWorldMap map, int noAnimals, float startingEnergy, int genotypeLength) {
        this.map = map;
        this.simAge = 0;
        this.startingEnergy = startingEnergy;
        this.genotypeLength = genotypeLength;
        this.animals = generateAnimals(noAnimals, map);

        for (Animal animal: animals) {
            map.place(animal);
        }
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

        while (simAge < 20) {
            moveAnimals();


            increaseAge();
        }
    }
    /**
     *  Move each of the animals on the map according to their current gene
     */
    public void moveAnimals() {
        for (Animal animal : animals) {
            animal.move();
            System.out.println(animal.getPosition().toString() + animal.getDirection().toString());
        }
    }
    public void increaseAge() {
        for (Animal animal : animals) {
            animal.addAge();
        }
        simAge++;
        System.out.println(simAge);
    }
}
