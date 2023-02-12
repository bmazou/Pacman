import java.awt.Image;

public class Pacman extends GameObject {

    public boolean dying = false;
    public Image up;
    public Image down;
    public Image left;
    public Image right;
    // public App model;
    // public Direction requestedDir = Direction.NONE;

    public Pacman(int x, int y, App model) {
        this.x = x;
        this.y = y;
        this.speed = 4;
        this.direction = Direction.NONE;
        this.model = model;
        // loadImages();
    }

    public void move() {
        if (canTurn()) {
            int pos = x / model.BLOCK_SIZE + model.N_BLOCKS * (int) (y / model.BLOCK_SIZE);
            short tile = model.screenData[pos];
            checkFoodColision(pos, tile);
            changeDirection(tile);
        }

        updatePosition();
    }

    // private void changeDirection(short tile) {
    // if (requestedDir == Direction.NONE)
    // return;

    // if (collisionOccured(requestedDir, tile)) {
    // direction = Direction.NONE;
    // return;
    // }

    // direction = requestedDir;
    // }

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
