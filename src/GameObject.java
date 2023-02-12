
enum Direction {
    UP, DOWN, LEFT, RIGHT, NONE
}

public abstract class GameObject {
    public int x;
    public int y;
    public int speed;
    public Direction direction;

    // public void move() {
    // switch (direction) {
    // case UP:
    // y -= speed;
    // break;
    // case DOWN:
    // y += speed;
    // break;
    // case LEFT:
    // x -= speed;
    // break;
    // case RIGHT:
    // x += speed;
    // break;
    // }
    // }

}