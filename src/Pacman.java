public class Pacman extends GameObject {

    public boolean dying = false;

    public Pacman(int speed, App game) {
        this.speed = speed;
        this.direction = Direction.NONE;
        this.game = game;
    }

    public void move() {
        if (canTurn()) {
            short tile = getTile();
            checkFoodCollision2();
            changeDirection(tile);
        }

        updatePosition();
    }

    private void checkFoodCollision2() {
        int posX = getPosArrI();
        int poxY = getPosArrJ();
        short tile = getTile();
        if ((tile & 16) != 0) {
            game.screenData[posX][poxY] = (short) (tile & 15);
            game.score++;
        }
    }

    public void newGame(int newX, int newY) {
        x = newX;
        y = newY;
        direction = Direction.NONE;
        requestedDir = Direction.NONE;
        dying = false;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

}
