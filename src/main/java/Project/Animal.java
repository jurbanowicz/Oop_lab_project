package Project;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.concurrent.ThreadLocalRandom;

public class Animal implements IMapElement{
    private Vector2d position;
    private MapDirection direction;
    private float energy;
    private int[] genotype;
    private int genotypeLenght;
    private int genIdx;
    private int age;
    private IWorldMap map;
    private boolean isDead;
    private int deathDate;
    private ArrayList<IPositionChangeObserver> observers;
    private int moveVariant;
    private int grassConsumed;
    private int children;

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
        this.genotypeLenght = genotypeLength;
        this.genotype = new GenotypeGenerator(genotypeLength,0, 0).GenerateNew();
        this.genIdx = ThreadLocalRandom.current().nextInt(0, genotypeLength);
        this.age = 0;
        this.map = map;
        this.isDead = false;
        this.observers = new ArrayList<>();
        addObserver((IPositionChangeObserver) map);
        this.grassConsumed = 0;
        this.deathDate = -1;
        this.children = 0;
    }

    @Override
    public String toString() {
        return "A";
    }


    public void setMoveVariant(int moveVariant) {
        this.moveVariant = moveVariant;
    }
    public int getMoveVariant() {
        return moveVariant;
    }

    public void addObserver(IPositionChangeObserver observer) {
        observers.add(observer);
    }
    public void removeAllObservers() {
        this.observers = null;
    }
    public void die() {
        isDead = true;
        map.removeDeadAnimal(this);
        removeAllObservers();
    }
    public boolean isAnimalDead() {
        return isDead;
    }
    public void move() {
        int currMove = genotype[genIdx];

        if (moveVariant == 1) {
            int randomMove = ThreadLocalRandom.current().nextInt(0, 10);
            if (randomMove < 2) {
                currMove = ThreadLocalRandom.current().nextInt(0, genotypeLenght);
            }
        }
        Vector2d oldPos = this.position;
        MapDirection newDirection = direction.rotate(currMove);
        Vector2d newPos = oldPos.add(newDirection.toUnitVector());
        position = map.MoveTo(newPos);
        positionChanged(oldPos, newPos);

        increaseGenIdx();
    }
    public void increaseGenIdx() {
        genIdx = (genIdx + 1) % genotype.length;
    }
    public void consume(Grass grass) {
        addEnergy(grass.getEnergy());
        grassConsumed++;
    }
    public static final Comparator<Animal> SortByEnergy = new Comparator<Animal>() {
        @Override
        public int compare(Animal a1, Animal a2) {
            if (a1.getEnergy() > a2.getEnergy()) {
                return 1;
            } else if (a1.getEnergy() == a2.getEnergy()) {
                return a1.compareByEnergy(a1, a2);
            } else {
                return -1;
            }
        }
    };
    public int compareByEnergy(Animal a1, Animal a2) {
        if (a1.getAge() > a2.getAge()) {
            return 1;
        } else if (a1.getAge() == a2.getAge()) {
            return 0;
        } else {
            return -1;
        }
    }
    public void positionChanged(Vector2d oldPos, Vector2d newPos) {
        for (IPositionChangeObserver observer: observers) {
            observer.positionChanged(this, oldPos, position);
        }
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
    public int getGrassConsumed() {
        return grassConsumed;
    }
    public void setDeathDate(int deathDate) {
        this.deathDate = deathDate;
    }
    public int getDeathDate() {
        return deathDate;
    }
    public int getChildren() {
        return this.children;
    }
    public void addChildren() {
        this.children++;
    }
}
