package Project;

import Visualization.MapVisualizer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;

import static java.lang.Math.abs;

public class EarthMap extends AbstractWorldMap{
//    private int height;
//    private int width;
//
//    private HashMap<Vector2d, PriorityQueue<Animal>> animalList;
//    private HashMap<Vector2d, Grass> grassList;
//    private AnimalBreeder animalBreeder;
//    private IMapObserver observer;


    public EarthMap(int height, int width) {
        super.height = height;
        super.width = width;
        super.animalList = new HashMap<>();
        super.grassList = new HashMap<>();
    }

    @Override
    public Vector2d MoveTo(Vector2d newPos) {
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
}