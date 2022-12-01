package Project;

import java.util.HashMap;
import java.util.PriorityQueue;

import static java.lang.Math.abs;

public class EarthMap implements IWorldMap, IPositionChangeObserver {
    private int height;
    private int width;

    private HashMap<Vector2d, PriorityQueue<Animal>> animalList;
    private HashMap<Vector2d, Grass> grassList;

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
    public Object objectAt(Vector2d position) {
        if (animalList.containsKey(position)) {
            return animalList.get(position);
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
        }
    }

    @Override
    public Vector2d MoveTo(Vector2d newPos, Animal animal) {
        int newX = abs(newPos.x) % width;
        int newY = abs(newPos.y) % height;

        return new Vector2d(newX, newY);
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
            if (newPosition != null) {
                place(animal);
            }
        }
        else {
            grassList.remove(oldPosition);
        }
    }
}
