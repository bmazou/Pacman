public class Pacman extends GameObject {
    public boolean dying = false;

    public Pacman(int speed, App game) {
        this.speed = speed;
        this.game = game;
    }

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
            game.screenData[posY][posX] = (short) (tile & 15);
            game.score++;
        }
    }

    //? Overridovat to?
    public void newGame(int newX, int newY) {
        x = newX;
        y = newY;
        direction = Direction.NONE;
        requestedDir = Direction.NONE;
        dying = false;
    }

}
