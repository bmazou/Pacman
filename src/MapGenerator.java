import java.util.ArrayList;

public class MapGenerator {
    private static short[][] ghostSpawn = {
        {0, 0, 1, 0, 0},
        {0, 1, 1, 1, 0},
        {0, 0, 0, 0, 0}
    };

    private static final int MAP_WIDTH = 23; 
    private static final int MAP_HEIGHT = 23;
    private static final float BRANCH_CHANCE = 0.2f;    //TODO Tohle bych mohl generavat náhodně (0.3 - 0.7 ?)
    private static final int MAX_BRANCHES = 6;          //TODO Tohle asi klidně taky

    private static ArrayList<Integer> xBranches = new ArrayList<>();
    private static ArrayList<Integer> yBranches = new ArrayList<>();

    private static short[][] curMap = new short[MAP_HEIGHT][MAP_WIDTH];

    static {
        generateMap();
    }

    private static void generateMap() {
        fillEdges();

        // xBranches.add(11);
        // branchRandomlyFromWall(Direction.UP, 11);
        
        int[] spawnPositions = generateSpawnPositions();
        branchRandomlyFromWall(Direction.UP, spawnPositions[0]);
        branchRandomlyFromWall(Direction.DOWN, spawnPositions[1]);
        branchRandomlyFromWall(Direction.LEFT, spawnPositions[2]);
        branchRandomlyFromWall(Direction.RIGHT, spawnPositions[3]);
    }

    private static void fillEdges() {
        for (int i = 0; i < MAP_WIDTH; i++) {
            curMap[0][i] = 1;
            curMap[MAP_HEIGHT - 1][i] = 1;
        }
        yBranches.add(0);
        yBranches.add(MAP_WIDTH - 1);

        for (int i = 0; i < MAP_HEIGHT; i++) {
            curMap[i][0] = 1;
            curMap[i][MAP_WIDTH - 1] = 1;
        }
        xBranches.add(0);
        xBranches.add(MAP_HEIGHT - 1);
    }

    // !
    private static int[] generateSpawnPositions() {
        int[] positions = new int[4];

        // Generate x positions
        int randXPos = (int) (Math.random() * (MAP_WIDTH-4) + 2);   // Generates a random number between 2 and MAP_WIDTH - 2
        positions[0] = randXPos;
        xBranches.add(randXPos);
        do {
            randXPos = (int) (Math.random() * (MAP_WIDTH-4) + 2);
        } while (neigboursXBranches(randXPos));
        positions[1] = randXPos;
        xBranches.add(randXPos);


        // Generate y positions
        int randYPos = (int) (Math.random() * (MAP_HEIGHT-4) + 2);   // Generates a random number between 2 and MAP_HEIGHT - 2
        positions[2] = randYPos;
        yBranches.add(randYPos);
        do {
            randYPos = (int) (Math.random() * (MAP_HEIGHT-4) + 2);
        } while (neigboursYBranches(randYPos));
        positions[3] = randYPos;
        yBranches.add(randYPos);

        return positions;
    }


    private static void branchRandomlyFromWall(Direction dir, int spawnPos) {
        switch (dir) {
            case UP:
                branchOut(1, spawnPos, dir.opposite(), MAX_BRANCHES);
                break;
            case DOWN:
                branchOut(MAP_HEIGHT - 2, spawnPos, dir.opposite(), MAX_BRANCHES);
                break;
            case LEFT:
                branchOut(spawnPos, 1, dir.opposite(), MAX_BRANCHES);
                break;
            case RIGHT:
                branchOut(spawnPos, MAP_WIDTH - 2, dir.opposite(), MAX_BRANCHES);
                break;
            default:
                break;
        }

        System.out.println("Branching from wall at " + spawnPos + ", in direction: " + dir.opposite());
    }


    private static boolean shouldBranch() {
        return Math.random() < BRANCH_CHANCE;
    }


    private static boolean neigboursXBranches(int x) {
        return xBranches.contains(x) || xBranches.contains(x - 1) || xBranches.contains(x + 1);
    }

    private static boolean neigboursYBranches(int y) {
        return yBranches.contains(y) || yBranches.contains(y - 1) || yBranches.contains(y + 1);
    }

    // Can branch, if it doesn't neigbour a column/row that has already been branched
    // ie we don't want to have two branches next to each other 
    private static boolean canBranchHere(int y, int x, Direction dir) {
        if (dir == Direction.LEFT || dir == Direction.RIGHT) {
            return !neigboursXBranches(x);
        } else {
            return !neigboursYBranches(y);
        }
    }

    private static void branchOut(int y, int x, Direction dir, int branchesLeft) {
        if (branchesLeft < 0) return;

        boolean isOutOfBounds = y < 0 || y >= MAP_HEIGHT || x < 0 || x >= MAP_WIDTH;
        if (isOutOfBounds) {
            return;
        }

        // System.out.println("Branching out from " + x + ", " + y + " in direction: " + dir + " with " + branchesLeft + " branches left");

        curMap[y][x] = 1;

        if (canBranchHere(y, x, dir)) {
            if (branchesLeft > 0) {
                if (tryToBranch(y, x, dir.next(), branchesLeft == 1 ? 0 : 2)) branchesLeft--;
            }
            if (branchesLeft > 0) {
                if (tryToBranch(y, x, dir.prev(), branchesLeft == 1 ? 0 : 2)) branchesLeft--;
            }
        }

        branchInDirection(y, x, dir, branchesLeft);
    }

    private static boolean tryToBranch(int y, int x, Direction newDir, int branchesLeft) {
        if (shouldBranch()) {
            addToBranches(y, x, newDir);
            branchInDirection(y, x, newDir, branchesLeft);
            return true;
        }
        return false;
    } 

    private static void branchInDirection(int y, int x, Direction dir, int branchesLeft) {
        int nextX = x + dir.dx();
        int nextY = y + dir.dy();
        branchOut(nextY, nextX, dir, branchesLeft);
    }

    private static void addToBranches(int y, int x, Direction dir) {
        if (dir == Direction.LEFT || dir == Direction.RIGHT) {
            yBranches.add(y);
        } else {
            xBranches.add(x);
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
