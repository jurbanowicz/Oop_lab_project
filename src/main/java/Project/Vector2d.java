package Project;

import java.util.Objects;

public class Vector2d {
    public final int x;
    public final int y;

    public Vector2d(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public String toString() {
        return String.format("(%d, %d)", this.x, this.y);
    }
    @Override
    public int hashCode() {
        return Objects.hash(this.x, this.y);
    }

    boolean precedes(Vector2d other) {
        return this.x <= other.x && this.y <= other.y;
    }

    boolean follows(Vector2d other) {
        return this.x >= other.x && this.y >= other.y;
    }

    Vector2d upperRight(Vector2d other) {
        int newX;
        int newY;
        if (this.x >= other.x) {
            newX = this.x;
        } else {
            newX = other.x;
        }
        if (this.y >= other.y) {
            newY = this.y;
        } else {
            newY = other.y;
        }
        return new Vector2d(newX, newY);
    }

    Vector2d lowerLeft(Vector2d other) {
        int newX;
        int newY;
        if (this.x <= other.x) {
            newX = this.x;
        } else {
            newX = other.x;
        }
        if (this.y <= other.y) {
            newY = this.y;
        } else {
            newY = other.y;
        }
        return new Vector2d(newX, newY);
    }

    Vector2d add(Vector2d other) {
        int newX;
        int newY;
        newX = this.x + other.x;
        newY = this.y + other.y;
        return new Vector2d(newX, newY);
    }

    Vector2d subtract(Vector2d other) {
        int newX;
        int newY;
        newX = this.x - other.x;
        newY = this.y - other.y;
        return new Vector2d(newX, newY);
    }

    public boolean equals(Object other) {
        if (!(other instanceof Vector2d))
            return false;
        Vector2d that = (Vector2d) other;
        if (this.x == that.x && this.y == that.y) {
            return true;
        }
        return false;
    }

    Vector2d opposite() {
        int newX;
        int newY;
        newX = this.x * (-1);
        newY = this.y * (-1);
        return new Vector2d(newX, newY);
    }
}
