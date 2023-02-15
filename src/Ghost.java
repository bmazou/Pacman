import java.util.Timer;

public class Ghost extends GameObject {
    private Timer timer;
    private Pacman pacman;
    private final int CHANGE_DIRECTION_INTERVAL = 400;

    // ? Semirandomize speed?
    public Ghost(int speed, int x, int y, App game, Pacman pacman) {
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.game = game;
        this.pacman = pacman;

        randomizeDirection();
        setupTimer();
    }

    private void setupTimer() {
        timer = new Timer();

        // Get random number between 2/3 and 4/3
        double random = (Math.random() * 1) + 0.5;

        int curGhostInterval = (int) (CHANGE_DIRECTION_INTERVAL * random);

        timer.schedule(new java.util.TimerTask() {
            @Override
            public void run() {
                randomizeDirection();
            }
        }, 0, curGhostInterval);
    }

    public void move() {
        if (collisionOccured(direction, getTile())) {
            randomizeDirection();
        }

        if (canTurn()) {
            short tile = getTile();
            changeDirection(tile);
        }

        updatePosition();
        checkPacmanCollision();
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

    public void newGame(int newX, int newY) {
        this.x = newX;
        this.y = newY;
        this.direction = Direction.DOWN;
    }

    // TODO Možná ať to povolí jít opačně když narazí do zdi
    //? Ten pacman seeking dát jen když je v blízkosti?
    // Go to the 4 directions randomly, or directly to the pacman with extra 2/6 chance
    // Meaning that overall the ghost has 50% chance to go directly to the pacman each turn (unless it's blocked by wall)
    private void randomizeDirection() {
        do {
            int random = (int) (Math.random() * 8);
            if (random == 0) {
                requestedDir = Direction.UP;
            } else if (random == 1) {
                requestedDir = Direction.DOWN;
            } else if (random == 2) {
                requestedDir = Direction.LEFT;
            } else if (random == 3) {
                requestedDir = Direction.RIGHT;
            } else { // random == 4 || random == 5
                requestedDir = getDirectionToPacman();
            }

        } while (collisionOccured(requestedDir, getTile()));
        // } while (collisionOccured(requestedDir, getTile()) || areOppositeDirections(requestedDir, direction));
    }

    // Function that returns the direction to the pacman
    private Direction getDirectionToPacman() {
        int pacX = pacman.x;
        int pacY = pacman.y;

        int xDiff = pacX - x;
        int yDiff = pacY - y;

        if (Math.abs(xDiff) > Math.abs(yDiff)) {
            if (xDiff > 0) {
                return Direction.RIGHT;
            } else {
                return Direction.LEFT;
            }
        } else {
            if (yDiff > 0) {
                return Direction.DOWN;
            } else {
                return Direction.UP;
            }
        }
    }

}
