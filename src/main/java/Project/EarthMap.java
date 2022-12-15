package Project;

import Visualization.MapVisualizer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;

import static java.lang.Math.abs;

public class EarthMap implements IWorldMap, IPositionChangeObserver {
    private int height;
    private int width;

    private HashMap<Vector2d, PriorityQueue<Animal>> animalList;
    private HashMap<Vector2d, Grass> grassList;
    private AnimalBreeder animalBreeder;
    private IMapObserver observer;


    public EarthMap(int height, int width) {
        this.height = height;
        this.width = width;
        animalList = new HashMap<>();
        grassList = new HashMap<>();
    }

    @Override
    public Vector2d getSize() {
        return new Vector2d(width, height);
    }
    @Override
    public void setAnimalBreeder(AnimalBreeder animalBreeder) {
        this.animalBreeder = animalBreeder;
    }

    @Override
    public Object objectAt(Vector2d position) {
        if (animalList.containsKey(position)) {
            return animalList.get(position).peek();
        }
        if (grassList.containsKey(position)) {
            return grassList.get(position);
        }
        return null;
    }

    @Override
    public void place(Animal animal) {
        if (animalList.containsKey(animal.getPosition())) {
            animalList.get(animal.getPosition()).add(animal);
        } else {
            animalList.put(animal.getPosition(), new PriorityQueue<Animal>(Animal.SortByEnergy));
            animalList.get(animal.getPosition()).add(animal);
        }
    }

    @Override
    public Vector2d MoveTo(Vector2d newPos) {
//        int newX = abs(newPos.x) % width;
//        int newY = abs(newPos.y) % height;
        int newX = newPos.x;
        int newY = newPos.y;
        if (newPos.x < 0) {
            newX = width - 1;
        }
        if (newPos.x >= width) {
            newX = 0;
        }
        if (newPos.y < 0) {
            newY = height - 1;
        }
        if (newPos.y >= height) {
            newY = 0;
        }

        return new Vector2d(newX, newY);
    }

    @Override
    public void placeGrass(Grass grass) {
        grassList.put(grass.getPosition(), grass);
    }

    @Override
    public void animalsConsumption() {
        for (PriorityQueue<Animal> current : animalList.values()) {
            Animal animal = current.peek();
            if (grassList.containsKey(animal.getPosition())) {
                animal.consume(grassList.get(animal.getPosition()));
                grassList.remove(animal.getPosition());
            }
        }
    }

    @Override
    public boolean isOccupied(Vector2d position) {
        return animalList.containsKey(position) || grassList.containsKey(position);
    }

    @Override
    public void positionChanged(IMapElement mapElement, Vector2d oldPosition, Vector2d newPosition) {
        if (mapElement instanceof Animal) {
            Animal animal = (Animal) mapElement;
            // Priority Que contains only one animal, so remove the entire Que from the map
            if (animalList.get(oldPosition).size() == 1) {
                animalList.remove(oldPosition);

            } else {
                // remove given animal from priority Que
                animalList.get(oldPosition).remove(animal);
            }
            place(animal);

        }
        else {
            grassList.remove(oldPosition);
        }
    }

    @Override
    public String toString() {
        MapVisualizer visualizer = new MapVisualizer(this);
        return visualizer.draw(new Vector2d(0, 0), new Vector2d(width - 1, height - 1));
    }
    @Override
    public void removeDeadAnimal(Animal animal) {
        if (animalList.get(animal.getPosition()).size() == 1) {
            animalList.remove(animal.getPosition());

        } else {
            // remove given animal from priority Que
            animalList.get(animal.getPosition()).remove(animal);
        }
    }
    @Override
    public ArrayList<Animal> breedPossible() {
        ArrayList<Animal> newAnimals = new ArrayList<>();
        for (PriorityQueue<Animal> animalQue : animalList.values()) {
            if (animalQue.size() > 1) {
                Animal[] possibleBreeders = animalQue.toArray(Animal[]::new);
                for (int i = 0; i < possibleBreeders.length - 1; i++) {
                    if (animalBreeder.isBreedingPossible(possibleBreeders[i], possibleBreeders[i + 1])) {
                        Animal newAnimal = animalBreeder.breedNewAnimal(possibleBreeders[i], possibleBreeders[i + 1]);
                        newAnimals.add(newAnimal);
                        place(newAnimal);
                    }
                    else {
                        break;
                    }
                }
            }
        }
        return newAnimals;
    }
    @Override
    public void addMapObserver(IMapObserver observer) {
        this.observer = observer;
    }

    @Override
    public void notifyObserver() {
        observer.updateMap();
    }
}
