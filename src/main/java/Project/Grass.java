package Project;

public class Grass implements IMapElement {
    private Vector2d position;
    private float energy;

    public Grass(Vector2d position, float energy) {
        this.energy = energy;
        this.position = position;
    }

    @Override
    public String toString() {
        return "*";
    }

    public Vector2d getPosition() {
        return position;
    }

    public float getEnergy() {
        return energy;
    }
}
