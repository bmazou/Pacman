enum Direction {
    UP, RIGHT, DOWN, LEFT, NONE;

    private static final Direction[] vals = values();
    public static final Direction[] directionVals = {UP, RIGHT, DOWN, LEFT};

    public Direction next() {
        /**
         * Get the next direction in the enum, ignoring NONE
         */
        return vals[(this.ordinal() + 1) % (vals.length - 1)];
    }

    public Direction prev() {
        /**
         * Get the previous direction in the enum, ignoring NONE
         */
        return vals[(this.ordinal() - 1 + vals.length - 1) % (vals.length - 1)];
    }

    public Direction opposite() {
        /**
         * Get the opposite direction in the enum, ignoring NONE
         */
        return vals[(this.ordinal() + 2) % (vals.length - 1)];
    }

    public int dx() {
        /**
         * Get the x component of the direction
         */
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
        /**
         * Get the y component of the direction
         */
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
    
    protected boolean collisionOccured(Direction dir) {
        /**
         * Check if there is a wall in the direction of the requested move
         * 
         * @param dir The direction of the requested move
         * @return true if there is a wall in the direction of the requested move
         */
        short tile = getTile();
        boolean leftWallColision = dir == Direction.LEFT && (tile & 1) != 0;
        boolean topWallColision = dir == Direction.UP && (tile & 2) != 0;
        boolean rightWallColision = dir == Direction.RIGHT && (tile & 4) != 0;
        boolean bottomWallColision = dir == Direction.DOWN && (tile & 8) != 0;

        return leftWallColision || topWallColision || rightWallColision || bottomWallColision;
    }

    protected boolean atIntersection() {
        /**
         * Check if the object is at an intersection
         * <p>
         * The object is at an intersection if it's position is a multiple of the block size for both x and y
         * 
         * @return true if the object is at an intersection
         */
        return x % Game.BLOCK_SIZE == 0 && y % Game.BLOCK_SIZE == 0;
    }

    protected int getPosArrY() {    
        /**
         * Get the position of the object in the screenData array
         * 
         * @return the Y position of the object in the screenData array
         */
        return (int) y / Game.BLOCK_SIZE;
    }

    protected int getPosArrX() {
        /**
         * Get the position of the object in the screenData array
         * 
         * @return the X position of the object in the screenData array
         */
        return (int) x / Game.BLOCK_SIZE;
    }

    protected short getTile() {
        /**
         * Get the tile at the position of the object
         * 
         * @return the tile content at the position of the object
         */
        return Game.screenData[getPosArrY()][getPosArrX()];
    }


    protected void changeDirection() {
        /**
         * Change the direction of the object
         * <p>
         * If the current direction is a wall, change the direction to NONE so the object doesn't move anymore
         * If there is a wall at the requested direction, ignore the requested direction
         * If no collision occured, change the direction to the requested direction
         */
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
        /**
         * Update the position of the object based on the direction
         */
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

    public abstract void move();
    public abstract void newGame();
    protected abstract void randomizeStartingPosition();
}