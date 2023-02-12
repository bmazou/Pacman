
enum Direction {
    UP, DOWN, LEFT, RIGHT, NONE
}

public abstract class GameObject {
    public int x;
    public int y;
    public int speed;
    public Direction direction;
    public Direction requestedDir = Direction.NONE;
    protected App model;

    protected boolean collisionOccured(Direction dir, short tile) {
        boolean leftWallColision = dir == Direction.LEFT && (tile & 1) != 0;
        boolean topWallColision = dir == Direction.UP && (tile & 2) != 0;
        boolean rightWallColision = dir == Direction.RIGHT && (tile & 4) != 0;
        boolean bottomWallColision = dir == Direction.DOWN && (tile & 8) != 0;

        return leftWallColision || topWallColision || rightWallColision || bottomWallColision;
    }

    protected boolean canTurn() {
        return x % model.BLOCK_SIZE == 0 && y % model.BLOCK_SIZE == 0;
    }

    protected void changeDirection(short tile) {
        if (requestedDir == Direction.NONE)
            return;

        if (collisionOccured(requestedDir, tile)) {
            direction = Direction.NONE;
            return;
        }

        direction = requestedDir;
    }

    protected boolean areOppositeDirections(Direction dir1, Direction dir2) {
        return (dir1 == Direction.UP && dir2 == Direction.DOWN) || (dir1 == Direction.DOWN && dir2 == Direction.UP)
                || (dir1 == Direction.LEFT && dir2 == Direction.RIGHT)
                || (dir1 == Direction.RIGHT && dir2 == Direction.LEFT);
    }

    protected void updatePosition() {
        switch (direction) {
            case UP:
                y -= speed;
                break;
            case DOWN:
                y += speed;
                break;
            case LEFT:
                x -= speed;
                break;
            case RIGHT:
                x += speed;
                break;
            case NONE:
                break;
        }
    }

}