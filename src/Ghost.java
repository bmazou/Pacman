import java.util.Arrays;

public class Ghost extends GameObject {
    private Pacman pacman;

    public Ghost(int speed, App game, Pacman pacman) {
        this.game = game;
        this.pacman = pacman;

        randomizeSpeed();
        requestDirection();
    }

    private void randomizeSpeed() {
        int[] speeds = App.viableSpeeds;

        // Get the index of pacman's speed in the array
        int pacmanSpeedIndex = Arrays.binarySearch(speeds, pacman.speed);

        // Choose a random number between 0 and pacmanSpeedIndex
        int randomIndex = (int) (Math.random() * (pacmanSpeedIndex + 1));
        this.speed = speeds[randomIndex];
    }

    @Override
    public void move() {
        if (atIntersection()) {
            requestDirection();
            changeDirection();
        }

        updatePosition();
        checkPacmanCollision();
    }

    private void requestDirection() {
        int startY = getPosArrY();
        int startX = getPosArrX();
        int targetY = pacman.getPosArrY();
        int targetX = pacman.getPosArrX();

        requestedDir = Map.pathDict.get(startY + "," + startX + "," + targetY + "," + targetX);
    }


    private void checkPacmanCollision() {
        int pacX = pacman.x;
        int pacY = pacman.y;
        int halfBlockSize = Math.round(App.BLOCK_SIZE / 2);

        boolean xCollision = (pacX > x - halfBlockSize && pacX < x + halfBlockSize);
        boolean yCollision = (pacY > y - halfBlockSize && pacY < y + halfBlockSize);
        if (xCollision && yCollision && game.inGame) {
            game.dying = true;
        }
    }

    @Override
    public void newGame() {
        randomizeStartingPosition();
    }    

    @Override
    protected void randomizeStartingPosition() {
        short[][] map = MapGenerator.getMap();
        int tempY, tempX;
        int minDistanceFromPacman = (int) Math.sqrt(App.X_BLOCK_COUNT*App.Y_BLOCK_COUNT);
        int pacmanY = pacman.getPosArrY();
        int pacmanX = pacman.getPosArrX();

        do {
            tempY = (int) (Math.random() * App.Y_BLOCK_COUNT);
            tempX = (int) (Math.random() * App.X_BLOCK_COUNT);
        } while (map[tempY][tempX] == 0 || Map.manhD(tempY, tempX, pacmanY, pacmanX) < minDistanceFromPacman);

        x = tempX * App.BLOCK_SIZE;
        y = tempY * App.BLOCK_SIZE;
    }
}