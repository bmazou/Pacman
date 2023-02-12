import java.util.Timer;

public class Ghost extends GameObject {
    private Timer timer;
    private final int CHANGE_DIRECTION_INTERVAL = 500;

    // TODO v App model dát static věci ať to sem nemusim dávat
    public Ghost(int x, int y, App model) {
        this.x = x;
        this.y = y;
        this.speed = 3;
        this.model = model;

        randomizeDirection();
        setupTimer();
    }

    private void setupTimer() {
        timer = new Timer();

        // Randomize interval so its +- 1/3 of CHANGE_DIRECTION_INTERVAL
        int curGhostInterval = CHANGE_DIRECTION_INTERVAL + (int) (Math.random() * CHANGE_DIRECTION_INTERVAL / 3)
                - (int) (Math.random() * CHANGE_DIRECTION_INTERVAL / 3);
        timer.schedule(new java.util.TimerTask() {
            @Override
            public void run() {
                randomizeDirection();
            }
        }, 0, curGhostInterval);
    }

    public void move() {
        if (canTurn()) {
            int pos = x / model.BLOCK_SIZE + model.N_BLOCKS * (int) (y / model.BLOCK_SIZE);
            short tile = model.screenData[pos];
            changeDirection(tile);
        }

        updatePosition();
    }

    public void newGame(int newX, int newY) {
        this.x = newX;
        this.y = newY;
        this.direction = Direction.DOWN;
    }

    public void randomizeDirection() {
        // Randomize direction, so it doesn't go in the same direction every time, and
        // it can't go into a wall
        do {
            int random = (int) (Math.random() * 4);
            switch (random) {
                case 0:
                    requestedDir = Direction.LEFT;
                    break;
                case 1:
                    requestedDir = Direction.RIGHT;
                    break;
                case 2:
                    requestedDir = Direction.UP;
                    break;
                case 3:
                    requestedDir = Direction.DOWN;
                    break;
            }

        } while (collisionOccured(requestedDir,
                model.screenData[x / model.BLOCK_SIZE + model.N_BLOCKS * (int) (y / model.BLOCK_SIZE)])
                || areOppositeDirections(requestedDir, direction));
    }

}
