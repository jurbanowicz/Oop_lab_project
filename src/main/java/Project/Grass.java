package Project;

public class Grass implements IMapElement {
    private Vector2d position;
    private float energy;

    public Grass(Vector2d position, float energy) {
        this.position = position;
    }

    public Vector2d getPosition() {
        return position;
    }

    public float getEnergy() {
        return energy;
    }
}
