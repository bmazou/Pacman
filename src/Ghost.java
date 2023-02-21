public class Ghost extends GameObject {
    private Pacman pacman;

    public Ghost(int speed, App game, Pacman pacman) {
        this.speed = speed;
        this.game = game;
        this.pacman = pacman;
        System.out.println("Ghost speed: " + this.speed);

        requestDirection();
    }

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

    public void newGame(int newX, int newY) {
        this.x = newX;
        this.y = newY;
        this.direction = Direction.DOWN;
    }    
}