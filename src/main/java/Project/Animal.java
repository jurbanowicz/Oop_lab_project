package Project;

import java.util.Comparator;

public class Animal implements IMapElement{
    private Vector2d position;
    private MapDirection direction;
    private float energy;
    private int[] genotype;
    private int genIdx;
    private int age;
    private IWorldMap map;

    /** Create a new animal
     *
     * @param position starting position of the animal
     * @param genotypeLength length of animals genotype
     * @param startingEnergy amount of energy animal starts the simulation with
     */
    public Animal(Vector2d position, IWorldMap map, int genotypeLength, float startingEnergy) {
        this.position = position;
        this.direction = MapDirection.NORTH;
        this.energy = startingEnergy;
        this.genotype = new GenotypeGenerator().GenerateNew(genotypeLength);
        this.genIdx = 0;
        this.age = 0;
        this.map = map;
    }

    public Vector2d getPosition() {
        return position;
    }

    public MapDirection getDirection() {
        return direction;
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
    public void die() {
        ((IPositionChangeObserver) map).positionChanged(this, position, null);
    }

    /** Move the animal according to its current gene value
     *
     */
    public void move() {
        int currMove = genotype[genIdx];
        Vector2d newPos = this.position;
        switch (currMove) {
            case 0 -> newPos = position.add(direction.toUnitVector());
            case 4 -> newPos = position.subtract(direction.toUnitVector());
            default -> direction = direction.rotate(currMove);
        }
        if (!(position.equals(newPos))) {
            Vector2d oldPos = position;
            position = map.MoveTo(newPos, this);
            ((IPositionChangeObserver) map).positionChanged(this, oldPos, position);
        }
        subtractEnergy(1);
        increaseGenIdx();
    }
    public void increaseGenIdx() {
        genIdx = (genIdx + 1) % genotype.length;
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
}
