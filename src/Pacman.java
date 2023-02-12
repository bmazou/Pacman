import java.awt.Image;

public class Pacman extends GameObject {
    public boolean dying = false;
    public Image up;
    public Image down;
    public Image left;
    public Image right;
    public App model;
    public Direction requestedDir = Direction.NONE;

    public Pacman(int x, int y, App model) {
        this.x = x;
        this.y = y;
        this.speed = 4;
        this.direction = Direction.NONE;
        this.model = model;
        // loadImages();
    }

    public void move() {
        if (x % model.BLOCK_SIZE == 0 && y % model.BLOCK_SIZE == 0) {
            int pos = x / model.BLOCK_SIZE + model.N_BLOCKS * (int) (y / model.BLOCK_SIZE);
            short tile = model.screenData[pos];
            checkFoodColision(pos, tile);
            changeDirection(tile);
        }

        // TODO Do funkce?
        switch (direction) {
            case UP:
                y -= speed;
                break;
            case DOWN:
                y += speed;
                break;
            case LEFT:
                x -= speed;
                break;
            case RIGHT:
                x += speed;
                break;
            case NONE:
                break;
        }
    }

    private void changeDirection(short tile) {
        if (requestedDir == Direction.NONE)
            return;

        boolean leftWallColision = requestedDir == Direction.LEFT && (tile & 1) != 0;
        boolean topWallColision = requestedDir == Direction.UP && (tile & 2) != 0;
        boolean rightWallColision = requestedDir == Direction.RIGHT && (tile & 4) != 0;
        boolean bottomWallColision = requestedDir == Direction.DOWN && (tile & 8) != 0;

        if (leftWallColision || topWallColision || rightWallColision || bottomWallColision) {
            direction = Direction.NONE;
            return;
        }

        direction = requestedDir;
    }

    private void checkFoodColision(int pos, short tile) {
        if ((tile & 16) != 0) {
            model.screenData[pos] = (short) (tile & 15);
            model.score++;
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
