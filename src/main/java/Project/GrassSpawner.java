package Project;

import java.util.concurrent.ThreadLocalRandom;

public class GrassSpawner {
    private float grassEnergy;

    public GrassSpawner(float grassEnergy) {
        this.grassEnergy = grassEnergy;
    }

    public Grass growGrass(IWorldMap map) {
        Vector2d available = findAvailableSpace(map);
        Grass grass = new Grass(available, grassEnergy);
        map.placeGrass(grass);
        return grass;
    }
    private Vector2d findAvailableSpace(IWorldMap map) {
        Vector2d mapSize = map.getSize();
        boolean flag = true;
        int tries = 0;
        while (flag) {
//            int x = ThreadLocalRandom.current().nextInt(0, mapSize.x);
//            int y = ThreadLocalRandom.current().nextInt(0, mapSize.y);
//            Vector2d current = new Vector2d(x, y);
            Vector2d current = findNearEquator(map);
            if (!(map.objectAt(current) instanceof Grass)) {
                return current;
            }
            if (tries > 100) {
                flag = false;
            }
            tries++;
        }
        int x = ThreadLocalRandom.current().nextInt(0, mapSize.x);
        int y = ThreadLocalRandom.current().nextInt(0, mapSize.y);
        return new Vector2d(x, y);
    }
    private Vector2d findNearEquator(IWorldMap map) {
        Vector2d mapSize = map.getSize();
        int equator = mapSize.y/2;
        int equatorHeight = mapSize.y/10;
        if (equatorHeight == 0) {
            equatorHeight = 1;
        }

        int preferred = ThreadLocalRandom.current().nextInt(0, 10);
        if (preferred < 8) {
            int x = ThreadLocalRandom.current().nextInt(0, mapSize.x);
            int y = ThreadLocalRandom.current().nextInt(equator - equatorHeight, equator + equatorHeight);
            return new Vector2d(x, y);
        } else {
            int side = ThreadLocalRandom.current().nextInt(0, 2);
            if (side == 0) {
                int x = ThreadLocalRandom.current().nextInt(0, mapSize.x);
                int y = ThreadLocalRandom.current().nextInt(0, equator - equatorHeight);
                return new Vector2d(x, y);
            } else {
                int x = ThreadLocalRandom.current().nextInt(0, mapSize.x);
                int y = ThreadLocalRandom.current().nextInt(equator + equatorHeight, mapSize.y);
                return new Vector2d(x, y);
            }
        }
    }
}
