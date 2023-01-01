package Project;

import com.sun.source.tree.Tree;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.TreeSet;
import java.util.concurrent.ThreadLocalRandom;

public class PortalMap extends AbstractWorldMap {
    public PortalMap(int height, int width) {
        super.height = height;
        super.width = width;
        super.animalList = new HashMap<>();
        super.grassList = new HashMap<>();
        super.deathPlaces = new ArrayList<>();
        initDeathPlaces();
    }

    @Override
    public Vector2d MoveTo(Vector2d newPos) {
        int newX = newPos.x;
        int newY = newPos.y;
        if (newPos.x < 0 || newPos.x >= width || newPos.y < 0 || newPos.y >= height) {
            newX = ThreadLocalRandom.current().nextInt(0, width);
            newY = ThreadLocalRandom.current().nextInt(0, height);
        }
        return new Vector2d(newX, newY);
    }
}
