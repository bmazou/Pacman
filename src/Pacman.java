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
            short tile = getTile();
            checkFoodCollision2();
            changeDirection(tile);
        }

        updatePosition();

        // System.out.println("X:" + x / game.BLOCK_SIZE + " Y:" + y / game.BLOCK_SIZE);
    }

    private void checkFoodCollision2() {
        int posX = getPosArrI();
        int poxY = getPosArrJ();
        short tile = getTile();
        if ((tile & 16) != 0) {
            game.screenData[posX][poxY] = (short) (tile & 15);
            game.score++;
            System.out.println("Just ate at - X:" + posX + " Y:" + poxY);
            System.out.println("tile is now:" + game.screenData[posX][poxY] + "... because (tile & 15) is:" + ((short) tile & 15));
            System.out.println("tile was " + tile + " and (tile & 15) is:" + ((short) tile & 15));
            System.out.println();
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
