package Project;

public interface IWorldMap {
    /**
     * get maps size
     * @return vector (map-width, map-height)
     */
    public Vector2d getSize();
    /**
     * return the object at the given map position
     * @param position position to check
     * @return object at the position
     */
    public Object objectAt(Vector2d position);

    /**
     * place given animal on the world map
     * @param animal to be placed
     */
    public void place(Animal animal);

    /**
     * checks if animal can move to goven position
     *
     * @param newPos position to check
     * @param animal animal to be moved
     * @return vector of the animals final position
     */
    public Vector2d canMoveTo(Vector2d newPos, Animal animal);
}
