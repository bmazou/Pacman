public class Ghost extends GameObject {
    private Pacman pacman;

    // ? Semirandomize speed?
    public Ghost(int speed, App game, Pacman pacman) {
        this.speed = speed;
        this.game = game;
        this.pacman = pacman;

        randomizeDirection();
    }

    public void move() {
        if (canTurn()) {
            short tile = getTile();
            requestDirection();
            changeDirection(tile);
        }

        updatePosition();
        checkPacmanCollision();
    }

    private void requestDirection() {
        int startY = getPosArrI();
        int startX = getPosArrJ();
        int targetY = pacman.getPosArrI();
        int targetX = pacman.getPosArrJ();

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

    public void newGame(int newX, int newY) {
        this.x = newX;
        this.y = newY;
        this.direction = Direction.DOWN;
    }

    private Direction randomizeDirection() {
        do {
            int random = (int) (Math.random() * 8);
            if (random == 0) {
                return Direction.UP;
                // requestedDir = Direction.UP;
            } else if (random == 1) {
                return Direction.DOWN;
                // requestedDir = Direction.DOWN;
            } else if (random == 2) {
                return Direction.LEFT;
                // requestedDir = Direction.LEFT;
            } else if (random == 3) {
                return Direction.RIGHT;
                // requestedDir = Direction.RIGHT;
            } else { // random == 4 || random == 5
                return getDirectionToPacman();
                // requestedDir = getDirectionToPacman();
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