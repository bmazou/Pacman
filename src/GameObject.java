enum Direction {
    UP, RIGHT, DOWN, LEFT, NONE;

    private static final Direction[] vals = values();
    public static final Direction[] directionVals = {UP, RIGHT, DOWN, LEFT};


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

    public boolean isPerpendicular(Direction dir) {
        return this == dir.next() || this == dir.prev();
    }

    public boolean isParallel(Direction dir) {
        return this == dir || this == dir.opposite();
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
    public Direction direction = Direction.NONE;
    public Direction requestedDir = Direction.NONE;
    protected App game;     // Tohle bych odebral a dal potřebný věci jako static

    protected boolean collisionOccured(Direction dir) {
        short tile = getTile();
        boolean leftWallColision = dir == Direction.LEFT && (tile & 1) != 0;
        boolean topWallColision = dir == Direction.UP && (tile & 2) != 0;
        boolean rightWallColision = dir == Direction.RIGHT && (tile & 4) != 0;
        boolean bottomWallColision = dir == Direction.DOWN && (tile & 8) != 0;

        return leftWallColision || topWallColision || rightWallColision || bottomWallColision;
    }

    protected boolean atIntersection() {
        return x % App.BLOCK_SIZE == 0 && y % App.BLOCK_SIZE == 0;
    }

    protected int getPosArrY() {    
        return (int) y / App.BLOCK_SIZE;
    }

    protected int getPosArrX() {
        return (int) x / App.BLOCK_SIZE;
    }

    protected short getTile() {
        return game.screenData[getPosArrY()][getPosArrX()];
    }


    protected void changeDirection() {
        if (collisionOccured(direction) ) {
            direction = Direction.NONE;
            return;
        }
        
        if (collisionOccured(requestedDir)) {
            return;
        }
        direction = requestedDir;
    }

    protected void updatePosition() {
        // if (collisionOccured(direction, getTile()))
        //     return;

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