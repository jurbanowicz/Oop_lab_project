package Project;

import Visualization.MapVisualizer;

import java.util.*;

public abstract class AbstractWorldMap implements IWorldMap, IPositionChangeObserver{
    protected int height;
    protected int width;

    protected HashMap<Vector2d, PriorityQueue<Animal>> animalList;
    protected HashMap<Vector2d, Grass> grassList;
    protected AnimalBreeder animalBreeder;
    protected IMapObserver observer;
    protected ArrayList<Vector2dWithProbability> deathPlaces;

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
    public abstract Vector2d MoveTo(Vector2d newPos);

    @Override
    public void placeGrass(Grass grass) {
        grassList.put(grass.getPosition(), grass);
    }

    @Override
    public ArrayList<Grass> animalsConsumption() {
        ArrayList<Grass> grassConsumed = new ArrayList<>();
        for (PriorityQueue<Animal> current : animalList.values()) {
            Animal animal = current.peek();
            if (grassList.containsKey(animal.getPosition())) {
                animal.consume(grassList.get(animal.getPosition()));
                grassConsumed.add(grassList.get(animal.getPosition()));
                grassList.remove(animal.getPosition());
            }
        }
        return grassConsumed;
    }

    @Override
    public boolean isOccupied(Vector2d position) {
        return animalList.containsKey(position) || grassList.containsKey(position);
    }

    @Override
    public void positionChanged(IMapElement mapElement, Vector2d oldPosition, Vector2d newPosition) {
//        System.out.println("Memory allocated to agent DrawMap at line 120 ...");
//        System.out.println("Memory allocated to this agent..." + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory())/(1024*1024));
//        if (mapElement instanceof Animal) {
            Animal animal = (Animal) mapElement;
            // Priority Que contains only one animal, so remove the entire Que from the map
            if (animalList.get(oldPosition).size() == 1) {
                animalList.remove(oldPosition);

            } else {
                // remove given animal from priority Que
                animalList.get(oldPosition).remove(animal);
            }
            place(animal);
//
//        }
//        else {
//            grassList.remove(oldPosition);
//        }
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
    @Override
    public int countGrass() {
        return this.grassList.size();
    }

    @Override
    public void addDeathPlace(Vector2d position) {
        for (Vector2dWithProbability vector: deathPlaces) {
            if (vector.toVector2d().x == position.x && vector.toVector2d().y == position.y) {
                vector.increaseProbability();
                break;
            }
        }
    }
    protected void initDeathPlaces() {
        for (int i=0; i < height; i++) {
            for (int j=0; j < width; j++) {
                deathPlaces.add(new Vector2dWithProbability(j, i, 0));
            }
        }
        Collections.shuffle(deathPlaces);
    }
    @Override
    public ArrayList<Vector2dWithProbability> getDeathPlaceList() {
        Collections.sort(deathPlaces, Comparator.comparingInt(Vector2dWithProbability::getProb));
        return deathPlaces;
    }

}

