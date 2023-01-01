package Project;

import java.util.ArrayList;
import java.util.Collection;

public interface IWorldMap {
    /**
     * get maps size
     * @return vector (map-width, map-height)
     */
    Vector2d getSize();
    /**
     * return the object at the given map position
     * @param position position to check
     * @return object at the position
     */
    Object objectAt(Vector2d position);

    /**
     * place given animal on the world map
     * @param animal to be placed
     */
    void place(Animal animal);

    /**
     * checks if animal can move to given position, if not return a vector that is possible for the animal
     * to move to
     * @param newPos position to check
     * @return vector of the animal's final position
     */
    Vector2d MoveTo(Vector2d newPos);
    boolean isOccupied(Vector2d position);

    /** places grass on the map according to placing variant
     * @param grass to be placed
     */
    void placeGrass(Grass grass);

    /** make animals consume grass at their current positions
     * @return list of grass that was eaten
     */
    ArrayList<Grass> animalsConsumption();

    /** remove an animal that has died from the map
     * @param animal to be removed
     */
    void removeDeadAnimal(Animal animal);
    void setAnimalBreeder(AnimalBreeder animalBreeder);

    /** Breed animals on map that are capable of doing so
     * @return list of newly breeded animals
     */
    ArrayList<Animal> breedPossible();
    void addMapObserver(IMapObserver observer);
    void notifyObserver();
    int countGrass();

    /** adds a death place of an animal at it's position
     * @param position of the animal that died
     */
    void addDeathPlace(Vector2d position);

    /**
     * @return list of positions with the amount of dead animals at that position sorted in ascending order
     */
    ArrayList<Vector2dWithProbability> getDeathPlaceList();
}

