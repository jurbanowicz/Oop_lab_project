package Project;

public class Vector2dWithProbability{
    private int x;
    private int y;
    private int prob;
    public Vector2dWithProbability(int x, int y, int prob) {
        this.x = x;
        this.y = y;
        this.prob = prob;
    }
    public Vector2d toVector2d() {
        return new Vector2d(this.x, this.y);
    }
    public int getProb() {
        return this.prob;
    }
    public void increaseProbability() {
        prob++;
    }
}
