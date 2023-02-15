import java.util.Set;

public class MapGenerator {
    private static short[][] ghostSpawn = {
        {0, 0, 1, 0, 0},
        {0, 1, 1, 1, 0},
        {0, 0, 0, 0, 0}
    };

    private static final int SPAWN_WIDTH = ghostSpawn[0].length;
    private static final int SPAWN_HEIGHT = ghostSpawn.length;
    private static final int MAP_WIDTH = 19; 
    private static final int MAP_HEIGHT = 19;
    private static final int WIDTH_MIDDLE = MAP_WIDTH / 2;
    private static final int HEIGHT_MIDDLE = MAP_HEIGHT / 2;
    private static final Set<Integer> SPAWN_Y_POS = Set.of(HEIGHT_MIDDLE - 1, HEIGHT_MIDDLE, HEIGHT_MIDDLE + 1);
    private static final Set<Integer> SPAWN_X_POS =  Set.of(WIDTH_MIDDLE - 2, WIDTH_MIDDLE - 1, WIDTH_MIDDLE, WIDTH_MIDDLE + 1, WIDTH_MIDDLE + 2);

    private static short[][] curMap = new short[MAP_HEIGHT][MAP_WIDTH];

    static {
        // Initialize curMap with walls
        // for (int i = 0; i < HEIGHT; i++) {
        //     for (int j = 0; j < WIDTH; j++) {
        //         curMap[i][j] = 1;
        //     }
        // }

        generateMap();
    }

    private static void generateMap() {
        addGhostSpawn();
        branchOut();
    }

    private static void branchOut() {
        branchInDirection(1, 0, Direction.RIGHT);
        // basicBranch();
    }

    private static boolean hitEdge(int y, int x, Direction dir) {
        boolean hitRight = x == MAP_WIDTH - 1 && dir == Direction.RIGHT;
        boolean hitLeft = x == 0 && dir == Direction.LEFT;
        boolean hitUp = y == 0 && dir == Direction.UP;
        boolean hitDown = y == MAP_HEIGHT - 1 && dir == Direction.DOWN;
        return hitRight || hitLeft || hitUp || hitDown;
    }

    // TODO boolean isSubBranch a ten se pak nebude moc branchvovat dal

    private static void branchInDirection(int y, int x, Direction dir) {
        boolean hitEmpty = curMap[y][x] == 1;
        if (hitEmpty) return;
        
        curMap[y][x] = 1;
        
        // if (hitEdge(y, x, dir)) return;
        if (hitEdge(y, x, dir)) {
            Direction nextDir = dir.next();
            int nextX = x + nextDir.x();
            int nextY = y + nextDir.y();
            branchInDirection(nextY, nextX, nextDir);
            return;
        }
        

        switch (dir) {
            case UP:
                branchInDirection(y - 1, x, dir);
                break;
            case DOWN:
                branchInDirection(y + 1, x, dir);
                break;
            case LEFT:
                branchInDirection(y, x - 1, dir);
                break;
            case RIGHT:
                branchInDirection(y, x + 1, dir);
                break;
            default:
                break;
        }


    }
    
    private static void basicBranch() {
        for (int y = 0; y < MAP_HEIGHT; y++) {
            boolean isInSpawnY = SPAWN_Y_POS.contains(y);
            if (isInSpawnY) {
                continue;
            }

            curMap[y][WIDTH_MIDDLE] = 1;
        }

        for (int x = 0; x < MAP_WIDTH; x++) {
            boolean isInSpawnX = SPAWN_X_POS.contains(x);
            if (isInSpawnX) {
                continue;
            }

            curMap[HEIGHT_MIDDLE][x] = 1;
        } 
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
