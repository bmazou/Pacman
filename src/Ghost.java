import java.util.Arrays;

public class Ghost extends GameObject {
    private Pacman pacman;

    public Ghost(int speed, Pacman pacman, int ghostNumber) {
        /**
         * Constructor asigning speed and pacman
         * <p>
         * The first has the same speed as pacman, the others have random speed slower than pacman
         */
        this.pacman = pacman;

        if (ghostNumber == 0) {
            this.speed = pacman.speed;
        } else {
            randomizeSpeed();
        }
        requestDirection();
    }

    private void randomizeSpeed() {
        /**
         * Randomize the speed of the ghost, but make sure it's slower than pacman
         */
        int[] speeds = Game.viableSpeeds;

        // Get the index of pacman's speed in the array
        int pacmanSpeedIndex = Arrays.binarySearch(speeds, pacman.speed);

        // Choose a random number between 0 and pacmanSpeedIndex - 1
        int randomIndex = (int) (Math.random() * pacmanSpeedIndex);
        this.speed = speeds[randomIndex];
    }

    @Override
    public void move() {
        /**
         * Moves the ghost
         * <p>
         * If the ghost is at an intersection, it request a new direction to pacman and goes that way.
         * If the ghost is at the same position as pacman, pacman dies.
         */
        if (atIntersection()) {
            requestDirection();
            changeDirection();
        }

        updatePosition();
        checkPacmanCollision();
    }

    private void requestDirection() {
        /**
         * Request a new direction from the pathDict
         * <p>
         * The requested direction will be asigned when the ghost goes on an intersection
         * The key is a composition of the ghost's and pacman's position
         */
        int startY = getPosArrY();
        int startX = getPosArrX();
        int targetY = pacman.getPosArrY();
        int targetX = pacman.getPosArrX();

        requestedDir = Map.pathDict.get(startY + "," + startX + "," + targetY + "," + targetX);
    }


    private void checkPacmanCollision() {
        /**
         * Check if the ghost is on pacman
         * <p>
         * If the ghost is on pacman, pacman dies
         */
        int pacX = pacman.x;
        int pacY = pacman.y;
        int halfBlockSize = Math.round(Game.BLOCK_SIZE / 2);

        boolean xCollision = (pacX > x - halfBlockSize && pacX < x + halfBlockSize);
        boolean yCollision = (pacY > y - halfBlockSize && pacY < y + halfBlockSize);
        if (xCollision && yCollision && Game.inGame) {
            Game.dying = true;
        }
    }

    @Override
    public void newGame() {
        /**
         * Randomizes ghost's starting position
         */
        randomizeStartingPosition();
    }    

    @Override
    protected void randomizeStartingPosition() {
        /**
         * Randomizes ghost's starting position
         * <p>
         * The ghost's starting position is not on a wall and at least 1/2 of the map away from pacman
         * The distance is calculated using the Manhattan distance
         */
        short[][] map = MapGenerator.getMap();
        int tempY, tempX;
        int minDistanceFromPacman = (int) Math.sqrt(Game.X_BLOCK_COUNT*Game.Y_BLOCK_COUNT) / 2;
        int pacmanY = pacman.getPosArrY();
        int pacmanX = pacman.getPosArrX();

        do {
            tempY = (int) (Math.random() * Game.Y_BLOCK_COUNT);
            tempX = (int) (Math.random() * Game.X_BLOCK_COUNT);
        } while (map[tempY][tempX] == 0 || Map.manhD(tempY, tempX, pacmanY, pacmanX) < minDistanceFromPacman);

        x = tempX * Game.BLOCK_SIZE;
        y = tempY * Game.BLOCK_SIZE;
    }
}