public class Pacman extends GameObject {

    public boolean dying = false;

    public Pacman(int speed, int x, int y, App game) {
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.direction = Direction.NONE;
        this.game = game;
    }

    public void move() {
        if (canTurn()) {
            int pos = getPos();
            short tile = getTile();
            checkFoodColision(pos, tile);
            changeDirection(tile);
        }

        updatePosition();
    }

    private void checkFoodColision(int pos, short tile) {
        if ((tile & 16) != 0) {
            game.screenData[pos] = (short) (tile & 15);
            game.score++;
        }
    }

    public void newGame(int newX, int newY) {
        x = newX;
        y = newY;
        direction = Direction.NONE;
        dying = false;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

}
