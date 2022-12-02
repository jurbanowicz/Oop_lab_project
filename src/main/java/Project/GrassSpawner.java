package Project;

import java.util.concurrent.ThreadLocalRandom;

public class GrassSpawner {
    private float grassEnergy;

    public GrassSpawner(float grassEnergy) {
        this.grassEnergy = grassEnergy;
    }

    public void growGrass(IWorldMap map) {
        Vector2d available = findAvailableSpace(map);
        Grass grass = new Grass(available, grassEnergy);
        map.placeGrass(grass);
    }
    public Vector2d findAvailableSpace(IWorldMap map) {
        Vector2d mapSize = map.getSize();
        boolean flag = true;
        int tries = 0;
        while (flag) {
            int x = ThreadLocalRandom.current().nextInt(0, mapSize.x);
            int y = ThreadLocalRandom.current().nextInt(0, mapSize.y);
            Vector2d current = new Vector2d(x, y);
            if (!(map.objectAt(current) instanceof Grass)) {
                return current;
            }
            if (tries > 1000) {
                flag = false;
            }
            tries++;
        }
        int x = ThreadLocalRandom.current().nextInt(0, mapSize.x);
        int y = ThreadLocalRandom.current().nextInt(0, mapSize.y);
        return new Vector2d(x, y);
    }
}
