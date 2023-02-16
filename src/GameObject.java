
enum Direction {
    UP, RIGHT, DOWN, LEFT, NONE;

    private static final Direction[] vals = values();

    // Ignores NONE
    public Direction next() {
        return vals[(this.ordinal() + 1) % (vals.length - 1)];
    }

    // Ignores NONE
    public Direction prev() {
        return vals[(this.ordinal() - 1 + vals.length - 1) % (vals.length - 1)];
    }

    public Direction opposite() {
        return vals[(this.ordinal() + 2) % (vals.length - 1)];
    }

    public Direction randomNextPrev() {
        return Math.random() < 0.5 ? next() : prev();
    }

    public int dx() {
        switch (this) {
            case LEFT:
                return -1;
            case RIGHT:
                return 1;
            default:
                return 0;
        }
    }

    public int dy() {
        switch (this) {
            case UP:
                return -1;
            case DOWN:
                return 1;
            default:
                return 0;
        }
    }
}


public abstract class GameObject {
    public int x;
    public int y;
    public int speed;
    public Direction direction;
    public Direction requestedDir = Direction.NONE;
    protected App game;

    protected boolean collisionOccured(Direction dir, short tile) {
        boolean leftWallColision = dir == Direction.LEFT && (tile & 1) != 0;
        boolean topWallColision = dir == Direction.UP && (tile & 2) != 0;
        boolean rightWallColision = dir == Direction.RIGHT && (tile & 4) != 0;
        boolean bottomWallColision = dir == Direction.DOWN && (tile & 8) != 0;

        return leftWallColision || topWallColision || rightWallColision || bottomWallColision;
    }

    protected boolean canTurn() {
        return x % App.BLOCK_SIZE == 0 && y % App.BLOCK_SIZE == 0;
    }

    protected int getPosArrI() {    
        return y / App.BLOCK_SIZE;
    }

    protected int getPosArrJ() {
        return x / App.BLOCK_SIZE;
    }

    protected short getTile() {
        return game.screenData[getPosArrI()][getPosArrJ()];
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
        return (dir1 == Direction.UP && dir2 == Direction.DOWN)
                || (dir1 == Direction.DOWN && dir2 == Direction.UP)
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