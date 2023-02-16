import java.util.Set;

public class MapGenerator {
    private static short[][] ghostSpawn = {
        {0, 0, 1, 0, 0},
        {0, 1, 1, 1, 0},
        {0, 0, 0, 0, 0}
    };

    private static final int SPAWN_WIDTH = ghostSpawn[0].length;
    private static final int SPAWN_HEIGHT = ghostSpawn.length;
    private static final int MAP_WIDTH = 23; 
    private static final int MAP_HEIGHT = 23;
    private static final int WIDTH_MIDDLE = MAP_WIDTH / 2;
    private static final int HEIGHT_MIDDLE = MAP_HEIGHT / 2;
    private static final Set<Integer> SPAWN_Y_POS = Set.of(HEIGHT_MIDDLE - 1, HEIGHT_MIDDLE, HEIGHT_MIDDLE + 1);
    private static final Set<Integer> SPAWN_X_POS =  Set.of(WIDTH_MIDDLE - 2, WIDTH_MIDDLE - 1, WIDTH_MIDDLE, WIDTH_MIDDLE + 1, WIDTH_MIDDLE + 2);
    private static final float BRANCH_CHANCE = 0.1f;
    private static final int MAX_BRANCHES = 2;

    private static short[][] curMap = new short[MAP_HEIGHT][MAP_WIDTH];

    static {
        generateMap();
    }

    private static void generateMap() {
        branchOut();
    }

    private static void branchRandomlyFromWall(Direction dir) {
        switch (dir) {
            case UP:
                branchOut(1, (int) (Math.random() * (MAP_WIDTH-4) + 2), dir.opposite(), MAX_BRANCHES);
                break;
            case DOWN:
                branchOut(MAP_HEIGHT - 2, (int) (Math.random() * (MAP_WIDTH-4) + 2), dir.opposite(), MAX_BRANCHES);
                break;
            case LEFT:
                branchOut((int) (Math.random() * (MAP_HEIGHT-4) + 2), 1, dir.opposite(), MAX_BRANCHES);
                break;
            case RIGHT:
                branchOut((int) (Math.random() * (MAP_HEIGHT-4) + 2), MAP_WIDTH - 2, dir.opposite(), MAX_BRANCHES);
                break;
            default:
                break;
        }
    }

    private static void branchOut() {
        fillEdges();

        branchRandomlyFromWall(Direction.UP);
        branchRandomlyFromWall(Direction.DOWN);
        branchRandomlyFromWall(Direction.LEFT);
        branchRandomlyFromWall(Direction.RIGHT);


        // * From walls
        // branchOut(HEIGHT_MIDDLE, 1, Direction.RIGHT, MAX_BRANCHES);
        // branchOut(MAP_HEIGHT - 2, WIDTH_MIDDLE, Direction.UP, MAX_BRANCHES);
        // branchOut(1, WIDTH_MIDDLE, Direction.DOWN, MAX_BRANCHES);
        // branchOut(HEIGHT_MIDDLE, MAP_WIDTH - 2, Direction.LEFT, MAX_BRANCHES);

        // * From spawn
        // branchOut(HEIGHT_MIDDLE - 2, WIDTH_MIDDLE, Direction.UP, MAX_BRANCHES);
        // branchOut(HEIGHT_MIDDLE + 1, WIDTH_MIDDLE, Direction.DOWN, MAX_BRANCHES);
        // branchOut(HEIGHT_MIDDLE, WIDTH_MIDDLE - 2, Direction.LEFT, MAX_BRANCHES);
        // branchOut(HEIGHT_MIDDLE, WIDTH_MIDDLE + 2, Direction.RIGHT, MAX_BRANCHES);
    }

    private static boolean hitEdge(int y, int x, Direction dir) {
        boolean hitRight = x == MAP_WIDTH - 1 && dir == Direction.RIGHT;
        boolean hitLeft = x == 0 && dir == Direction.LEFT;
        boolean hitUp = y == 0 && dir == Direction.UP;
        boolean hitDown = y == MAP_HEIGHT - 1 && dir == Direction.DOWN;
        return hitRight || hitLeft || hitUp || hitDown;
    }

    private static boolean shouldBranch() {
        return Math.random() < BRANCH_CHANCE;
    }

    private static void fillEdges() {
        for (int i = 0; i < MAP_WIDTH; i++) {
            curMap[0][i] = 1;
            curMap[MAP_HEIGHT - 1][i] = 1;
        }
        for (int i = 0; i < MAP_HEIGHT; i++) {
            curMap[i][0] = 1;
            curMap[i][MAP_WIDTH - 1] = 1;
        }
    }

    private static void branchOut(int y, int x, Direction dir, int branchesLeft) {
        if (branchesLeft < 0) return;

        boolean isOutOfBounds = y < 0 || y >= MAP_HEIGHT || x < 0 || x >= MAP_WIDTH;
        // boolean isOutOfBounds = y < 1 || y >= MAP_HEIGHT - 1 || x < 1 || x >= MAP_WIDTH - 1;
        if (isOutOfBounds) {
            return;
        }


        // boolean hitEmpty = curMap[y][x] == 1;
        // if (hitEmpty) return;
        
        curMap[y][x] = 1;

        
        // if (hitEdge(y, x, dir)) {
        //     branchInDirection(y, x, dir.next(), branchesLeft);
        //     return;
        // }

        if (branchesLeft > 0) {
            if (tryToBranch(y, x, dir.next(), branchesLeft == 1 ? 0 : 1)) branchesLeft--;
        }
        if (branchesLeft > 0) {
            if (tryToBranch(y, x, dir.prev(), branchesLeft == 1 ? 0 : 1)) branchesLeft--;
        }

        branchInDirection(y, x, dir, branchesLeft);
    }

    private static boolean tryToBranch(int y, int x, Direction newDir, int branchesLeft) {
        if (shouldBranch()) {
            branchInDirection(y, x, newDir, branchesLeft);
            return true;
        }
        return false;
    } 

    private static void branchInDirection(int y, int x, Direction dir, int branchesLeft) {
        int nextX = x + dir.dx();
        int nextY = y + dir.dy();
        System.out.println("nextY: " + nextY + " nextX: " + nextX);
        branchOut(nextY, nextX, dir, branchesLeft);
    }


    private static void addGhostSpawn() {
        // Adds ghost spawn to the middle of curMap
        int x = (MAP_WIDTH - ghostSpawn[0].length) / 2;
        int y = (MAP_HEIGHT - ghostSpawn.length) / 2;

        for (int i = 0; i < ghostSpawn.length; i++) {
            for (int j = 0; j < ghostSpawn[0].length; j++) {
                curMap[y + i][x + j] = ghostSpawn[i][j];
            }
        }
    }



    public static short[][] getMap() {
        return curMap;
    }


}
