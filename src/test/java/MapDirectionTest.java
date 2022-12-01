import org.junit.jupiter.api.Test;

import Project.MapDirection;

import static org.junit.jupiter.api.Assertions.*;

public class MapDirectionTest {
    @Test
    public void test_1() {
        MapDirection direction = MapDirection.NORTH;
        direction = direction.rotate(1);
        assertEquals(MapDirection.NORTH_EAST, direction);
    }
    @Test
    public void test_2() {
        MapDirection direction = MapDirection.NORTH;
        direction = direction.rotate(2);
        assertEquals(MapDirection.EAST, direction);
    }
    @Test
    public void test_3() {
        MapDirection direction = MapDirection.NORTH;
        direction = direction.rotate(3);
        assertEquals(MapDirection.SOUTH_EAST, direction);
    }
    @Test
    public void test_4() {
        MapDirection direction = MapDirection.NORTH;
        direction = direction.rotate(4);
        assertEquals(MapDirection.SOUTH, direction);
    }
    @Test
    public void test_5() {
        MapDirection direction = MapDirection.NORTH;
        direction = direction.rotate(5);
        assertEquals(MapDirection.SOUTH_WEST, direction);
    }
    @Test
    public void test_6() {
        MapDirection direction = MapDirection.NORTH;
        direction = direction.rotate(6);
        assertEquals(MapDirection.WEST, direction);
    }
    @Test
    public void test_7() {
        MapDirection direction = MapDirection.NORTH;
        direction = direction.rotate(7);
        assertEquals(MapDirection.NORTH_WEST, direction);
    }
    @Test
    public void test_8() {
        MapDirection direction = MapDirection.SOUTH;
        direction = direction.rotate(6);
        assertEquals(MapDirection.EAST, direction);
    }
    @Test
    public void test_9() {
        MapDirection direction = MapDirection.SOUTH;
        direction = direction.rotate(4);
        assertEquals(MapDirection.NORTH, direction);
    }
    @Test
    public void test_10() {
        MapDirection direction = MapDirection.SOUTH;
        direction = direction.rotate(5);
        assertEquals(MapDirection.NORTH_EAST, direction);
    }

}
