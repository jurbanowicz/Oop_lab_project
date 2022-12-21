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
     * checks if animal can move to goven position
     *
     * @param newPos position to check
//     * @param animal animal to be moved
     * @return vector of the animals final position
     */
    Vector2d MoveTo(Vector2d newPos);
    boolean isOccupied(Vector2d position);

    void placeGrass(Grass grass);
    ArrayList<Grass> animalsConsumption();
    void removeDeadAnimal(Animal animal);
    void setAnimalBreeder(AnimalBreeder animalBreeder);
    ArrayList<Animal> breedPossible();
    void addMapObserver(IMapObserver observer);
    void notifyObserver();
    int countGrass();
}

