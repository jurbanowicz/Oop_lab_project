package Project;

public class Animal {
    private Vector2d position;
    private MapDirection direction;
    private int energy;
    private int[] genotype;
    private int genIdx;
    private int age;

    /** Create a new animal
     *
     * @param position starting position off the animal
     * @param genotypeLength length of animals genotype
     */
    public Animal(Vector2d position, int genotypeLength, int startingEnergy) {
        this.position = position;
        this.direction = MapDirection.NORTH;
        this.energy = startingEnergy;
        this.genotype = new GenotypeGenerator().GenerateNew(genotypeLength);
        this.genIdx = 0;
        this.age = 0;
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
        position = newPos;
        increaseGenIdx();
    }
    public void increaseGenIdx() {
        genIdx = (genIdx + 1) % genotype.length;
    }
}
