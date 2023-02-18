import java.util.ArrayList;
import java.util.Timer;

public class Ghost extends GameObject {
    private Timer timer;
    private Pacman pacman;
    private final int CHANGE_DIRECTION_INTERVAL = 4;
    private short[][] map = MapGenerator.getMap();  // 0 = wall, 1 = path

    // ? Semirandomize speed?
    public Ghost(int speed, App game, Pacman pacman) {
        this.speed = speed;
        this.game = game;
        this.pacman = pacman;

        randomizeDirection();
        setupTimer();

        System.out.println("Seting up");
        Map.printMap(map, "1/0 map from ghost");  //!!!!
    }


    private void setupTimer() {
        timer = new Timer();

        // Get random number between 2/3 and 4/3
        double random = (Math.random() * 1) + 0.5;

        int curGhostInterval = (int) (CHANGE_DIRECTION_INTERVAL * random);

        timer.schedule(new java.util.TimerTask() {
            @Override
            public void run() {
                setNextDirection();
            }
        }, 0, curGhostInterval);
    }

    public void move() {
        if (collisionOccured(direction, getTile())) {
            randomizeDirection();
        }

        if (canTurn()) {
            short tile = getTile();
            changeDirection(tile);
        }

        updatePosition();
        checkPacmanCollision();
    }

    private void setNextDirection() {
        int mapY = getPosArrI();
        int mapX = getPosArrJ();
        int targetY = pacman.getPosArrI();
        int targetX = pacman.getPosArrJ();


        requestedDir = Map.pathDict.get(mapY + "," + mapX + "," + targetY + "," + targetX);

        System.out.println("Pac y: " + targetY + " Pac x: " + targetX + "... Requested dir: " + requestedDir);
        System.out.println("Coordinates: " + mapY + ", " + mapX + " Target: " + targetY + ", " + targetX);
        System.out.println();

        // ArrayList<Direction> path = findPath(mapY, mapX, 15, 15, Direction.NONE, new ArrayList<>(), MapGenerator.getMap());
        // if (path != null) {
            // System.out.println("Found a direction: " + path.get(0));
        // }else {
            // System.out.println("Oops can't find it");
        // }
        // Set the first element in path as requestedDir
        // requestedDir = path != null ? path.get(0) : randomizeDirection();
        // System.out.println("Requested dir: " + requestedDir);
    }

    public static ArrayList<Direction> findPath(int y, int x, int targetY, int TargetX, Direction dir, ArrayList<Direction> path, short[][] curMap) {
        if (dir != Direction.NONE)
            path.add(dir);
        y += dir.dy();
        x += dir.dx();
        if (Map.isOutOfBounds(y, x)) {
            // System.out.println("Went out of bounds");
            return null;
        };
        // System.out.println("\nwent " + dir + " to " + y + ", " + x);
        if (y == targetY && x == TargetX) {
            // System.out.println("Found the target!");
            return path;
        }

        // 0 is a wall
        if (curMap[y][x] == 0) {
            // System.out.println("Hit a wall!");
            return null;
        }

        // 2 is a visited node
        if (curMap[y][x] == 2) {
            // System.out.println("Already visited this node!");
            return null;
        }

        curMap[y][x] = 2;

        int shortestPathLen = Integer.MAX_VALUE;
        ArrayList<Direction> shortestPath = null;
        for (Direction nextDir : Direction.directionVals) {   
            ArrayList<Direction> newPath = findPath(y, x, targetY, TargetX, nextDir, new ArrayList<>(path), curMap.clone());
            if (newPath != null && newPath.size() < shortestPathLen) {
                shortestPathLen = newPath.size();
                shortestPath = newPath;
            }
        }
        
        return shortestPath;
    }


    private void checkPacmanCollision() {
        int pacX = pacman.x;
        int pacY = pacman.y;
        int halfBlockSize = Math.round(App.BLOCK_SIZE / 2);

        boolean xCollision = (pacX > x - halfBlockSize && pacX < x + halfBlockSize);
        boolean yCollision = (pacY > y - halfBlockSize && pacY < y + halfBlockSize);
        if (xCollision && yCollision && game.inGame) {
            game.dying = true;
        }
    }

    public void newGame(int newX, int newY) {
        this.x = newX;
        this.y = newY;
        this.direction = Direction.DOWN;
    }

    // TODO Možná ať to povolí jít opačně když narazí do zdi
    //? Ten pacman seeking dát jen když je v blízkosti?
    // Go to the 4 directions randomly, or directly to the pacman with extra 2/6 chance
    // Meaning that overall the ghost has 50% chance to go directly to the pacman each turn (unless it's blocked by wall)
    private Direction randomizeDirection() {
        do {
            int random = (int) (Math.random() * 8);
            if (random == 0) {
                return Direction.UP;
                // requestedDir = Direction.UP;
            } else if (random == 1) {
                return Direction.DOWN;
                // requestedDir = Direction.DOWN;
            } else if (random == 2) {
                return Direction.LEFT;
                // requestedDir = Direction.LEFT;
            } else if (random == 3) {
                return Direction.RIGHT;
                // requestedDir = Direction.RIGHT;
            } else { // random == 4 || random == 5
                return getDirectionToPacman();
                // requestedDir = getDirectionToPacman();
            }

        } while (collisionOccured(requestedDir, getTile()));
        // } while (collisionOccured(requestedDir, getTile()) || areOppositeDirections(requestedDir, direction));
    }


    // Function that returns the direction to the pacman
    private Direction getDirectionToPacman() {
        int pacX = pacman.x;
        int pacY = pacman.y;

        int xDiff = pacX - x;
        int yDiff = pacY - y;

        if (Math.abs(xDiff) > Math.abs(yDiff)) {
            if (xDiff > 0) {
                return Direction.RIGHT;
            } else {
                return Direction.LEFT;
            }
        } else {
            if (yDiff > 0) {
                return Direction.DOWN;
            } else {
                return Direction.UP;
            }
        }
    }
    
}   
