package Project;

public interface IPositionChangeObserver {
    /**
     * Inform the observer about the change of map element form old position to new positon
     * @param element element that has moved
     * @param oldPosition elements old position
     * @param newPosition elements new position
     */
    public void positionChanged(IMapElement element, Vector2d oldPosition, Vector2d newPosition);
}
