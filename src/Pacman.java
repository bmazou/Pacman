public class Pacman extends GameObject {
    public Pacman(int speed) {
        /**
         * Constructor asigning speed
         * 
         * @param speed The speed of the pacman
         */
        this.speed = speed;
    }

    @Override
    public void move() {
        /**
         * Moves the pacman
         * <p>
         * If the pacman is at an intersection, can eat food and change direction
         */
        if (atIntersection()) {
            checkFoodCollision();
            changeDirection();
        }
        updatePosition();
    }
    
    private void checkFoodCollision() {
        /**
         * Check if the pacman is on food
         * <p>
         * If the current tile is food (tile & 16 != 0), eat the food and increase the score
         */
        int posY = getPosArrY();
        int posX = getPosArrX();
        short tile = getTile();
        if ((tile & 16) != 0) {
            Game.screenData[posY][posX] = (short) (tile & 15);
            Game.score++;
        }
    }

    public void requestDirection(Direction dir) {
        /**
         * Request a new direction
         * <p>
         * The requested direction will be asigned when the pacman goes on an intersection
         * 
         * @param dir The requested direction
         */
        requestedDir = dir;
    }

    @Override
    public void newGame() {
        /**
         * Reset pacman's state and randomize starting position
         */
        randomizeStartingPosition();
        direction = Direction.NONE;
        requestedDir = Direction.NONE;
    }

    @Override
    protected void randomizeStartingPosition() {
        /**
         * Randomize the starting position of the pacman
         * <p>
         * The starting position is a random position on the map that is not a wall
         */
        short[][] map = MapGenerator.getMap();
        int tempY, tempX;

        do {
            tempY = (int) (Math.random() * Game.Y_BLOCK_COUNT);
            tempX = (int) (Math.random() * Game.X_BLOCK_COUNT);
        } while (map[tempY][tempX] == 0);

        x = tempX * Game.BLOCK_SIZE;
        y = tempY * Game.BLOCK_SIZE;
    }   

}
