package Project;

public class EarthMap implements IWorldMap{
    private int height;
    private int width;

    public EarthMap(int height, int width) {
        this.height = height;
        this.width = width;
    }

    @Override
    public Vector2d getSize() {
        return new Vector2d(width, height);
    }

    @Override
    public Object objectAt(Vector2d position) {
        return null;
    }

    @Override
    public void place(Animal animal) {

    }

    @Override
    public Vector2d canMoveTo(Vector2d newPos, Animal animal) {
        return null;
    }
}
