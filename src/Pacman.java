public class Pacman extends GameObject {
    public boolean dying = false;

    public Pacman(int speed, Game game) {
        this.speed = speed;
        this.game = game;
    }

    @Override
    public void move() {
        if (atIntersection()) {
            checkFoodCollision();
            changeDirection();
        }
        updatePosition();
    }
    
    private void checkFoodCollision() {
        int posY = getPosArrY();
        int posX = getPosArrX();
        short tile = getTile();
        if ((tile & 16) != 0) {
            Game.screenData[posY][posX] = (short) (tile & 15);
            Game.score++;
        }
    }

    public void requestDirection(Direction dir) {
        requestedDir = dir;
    }

    @Override
    public void newGame() {
        randomizeStartingPosition();
        direction = Direction.NONE;
        requestedDir = Direction.NONE;
        dying = false;
    }

    @Override
    protected void randomizeStartingPosition() {
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
