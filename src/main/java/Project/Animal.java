package Project;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.concurrent.ThreadLocalRandom;

public class Animal implements IMapElement{
    private Vector2d position;
    private MapDirection direction;
    private float energy;
    private int[] genotype;
    private int genIdx;
    private int age;
    private IWorldMap map;
    private boolean isDead;
    private ArrayList<IPositionChangeObserver> observers;

    /** Create a new animal
     *
     * @param position starting position of the animal
     * @param genotypeLength length of animals genotype
     * @param startingEnergy amount of energy animal starts the simulation with
     */
    public Animal(Vector2d position, IWorldMap map, int genotypeLength, float startingEnergy) {
        this.position = position;
        this.direction = MapDirection.values()[ThreadLocalRandom.current().nextInt(0, 8)];
        this.energy = startingEnergy;
        this.genotype = new GenotypeGenerator().GenerateNew(genotypeLength);
        this.genIdx = ThreadLocalRandom.current().nextInt(0, genotypeLength);
        this.age = 0;
        this.map = map;
        this.isDead = false;
        this.observers = new ArrayList<>();
        addObserver((IPositionChangeObserver) map);
    }

    @Override
    public String toString() {
        return "A";
    }

    public Vector2d getPosition() {
        return position;
    }

    public MapDirection getDirection() {
        return direction;
    }
    public int[] getGenotype() {
        return genotype;
    }
    public void setGenotype(int[] genotype) {
        this.genotype = genotype;
    }
    public int getAge() {
        return this.age;
    }
    public void addAge() {
        this.age++;
    }
    public float getEnergy() {
        return this.energy;
    }
    public void addEnergy(float amount) {
        this.energy += amount;
    }
    public void subtractEnergy(float amount) {
        this.energy -= amount;
        if (energy <= 0) {
            die();
        }
    }
    public void addObserver(IPositionChangeObserver observer) {
        observers.add(observer);
    }
    public void removeObserver(IPositionChangeObserver observer) {
        observers.remove(observer);
    }
    public void die() {
        isDead = true;
        map.removeDeadAnimal(this);
    }
    public boolean isAnimalDead() {
        return isDead;
    }
    public void move() {
        int currMove = genotype[genIdx];
        Vector2d oldPos = this.position;
        Vector2d newPos = this.position;
        MapDirection newDirection = direction;
        switch (currMove) {
            case 0 -> newPos = oldPos.add(direction.toUnitVector());
            case 4 -> newPos = oldPos.subtract(direction.toUnitVector());
            default -> newDirection = direction.rotate(currMove);
        }
        // default case animal was rotated, so now we need to move it
        if (newPos.equals(oldPos)) {
            direction = newDirection;
            newPos = newPos.add(newDirection.toUnitVector());

        }
        // newPos is already assigned in switch case 0 and 4
        position = map.MoveTo(newPos);
        positionChanged(oldPos, newPos);

        subtractEnergy(1);
        increaseGenIdx();
    }
    public void increaseGenIdx() {
        genIdx = (genIdx + 1) % genotype.length;
    }
    public void consume(Grass grass) {
        addEnergy(grass.getEnergy());
    }
    public static final Comparator<Animal> SortByEnergy = new Comparator<Animal>() {
        @Override
        public int compare(Animal a1, Animal a2) {
            if (a1.getEnergy() > a2.getEnergy()) {
                return 1;
            } else if (a1.getEnergy() == a2.getEnergy()) {
                return 0;
            } else {
                return 0;
            }
        }
    };
    public void positionChanged(Vector2d oldPos, Vector2d newPos) {
        for (IPositionChangeObserver observer: observers) {
            ((IPositionChangeObserver) map).positionChanged(this, oldPos, position);
        }
    }
}
