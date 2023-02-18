import java.util.ArrayList;
import java.util.HashMap;

public class Map {
    public static final short[][] pacmanMap = {
		{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, 
		{ 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0 }, 
		{ 0, 1, 0, 0, 1, 0, 0, 0, 1, 0, 1, 0, 0, 0, 1, 0, 0, 1, 0 }, 
		{ 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0 }, 
		{ 0, 1, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 1, 0 }, 
		{ 0, 1, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 1, 0 }, 
		{ 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0 }, 
		{ 1, 1, 1, 0, 1, 0, 1, 1, 1, 1, 1, 1, 1, 0, 1, 0, 1, 1, 1 }, 
		{ 1, 1, 1, 0, 1, 0, 1, 0, 0, 1, 0, 0, 1, 0, 1, 0, 1, 1, 1 }, 
		{ 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1 }, 
		{ 1, 1, 1, 0, 1, 0, 1, 0, 0, 0, 0, 0, 1, 0, 1, 0, 1, 1, 1 }, 
		{ 1, 1, 1, 0, 1, 0, 1, 1, 1, 1, 1, 1, 1, 0, 1, 0, 1, 1, 1 }, 
		{ 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0 }, 
		{ 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0 }, 
		{ 0, 1, 0, 0, 1, 0, 0, 0, 1, 0, 1, 0, 0, 0, 1, 0, 0, 1, 0 }, 
		{ 0, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 0 }, 
		{ 0, 0, 1, 0, 1, 0, 1, 0, 0, 0, 0, 0, 1, 0, 1, 0, 1, 0, 0 }, 
		{ 0, 1, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 1, 0 }, 
		{ 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0 }, 
		{ 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0 }, 
		{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }  
	}; 

    public static short[][] curMap = MapGenerator.getMap(); // = convertMap(pacmanMap.clone());
    public static HashMap<String, Direction> pathDict = new HashMap<>();


    static {
        convertMap();
        fillPathDict();
        // Sort pathDict

        System.out.println("PathDict:" + pathDict.size()); 
        printMap(MapGenerator.getMap(), null);
        // System.out.println("Should be RIGHT: " + pathDict.get("0,0,0,1"));
        // System.out.println("Should be RIGHT: " + pathDict.get("0,0,0,10"));
        // System.out.println("Should be LEFT: " + pathDict.get("0,10,0,3"));
        // System.out.println("Should be DOWN: " + pathDict.get("2,0,10,0"));

    }

    public static short[][] copy(short[][] map) {
        short[][] temp = new short[map.length][map[0].length];
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                temp[i][j] = (short) map[i][j];
            }
        }

        return temp;
    }


    public static void fillPathDict() {
        short[][] map = MapGenerator.getMap();
        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[y].length; x++) {
                for (int targetY = 0; targetY < map.length; targetY++) {
                    for (int targetX = 0; targetX < map[targetY].length; targetX++) {
                        if (map[y][x] == 1 && map[targetY][targetX] == 1) {// && y < 4 && targetY < 4 && x < 4 && targetX < 4) {
                            if (y == targetY && x == targetX) {
                                pathDict.put(y + "," + x + "," + targetY + "," + targetX, Direction.NONE);
                                // pathDict.put("" + y + x + targetY + targetX, Direction.NONE);
                                continue;
                            }
                            ArrayList<Direction> path = findPath(y, x, targetY, targetX, Direction.NONE, new ArrayList<>(), MapGenerator.getMap());
                            

                            if (path != null && path.size() > 0) {
                                pathDict.put(y + "," + x + "," + targetY + "," + targetX, path.get(0));
                                // pathDict.put("" + y + x + targetY + targetX, path.get(0));
                            }else {
                                pathDict.put(y + "," + x + "," + targetY + "," + targetX, Direction.NONE);
                                // pathDict.put("" + y + x + targetY + targetX, Direction.NONE);
                            }
                        }
                    }
                }
            }
        }
    }


    // A function that takes two poisitions in map, and finds the shortest path between them, returning the path as a list of directions, using DFS
    public static ArrayList<Direction> findPath(int y, int x, int targetY, int TargetX, Direction dir, ArrayList<Direction> path, short[][] map) {
        if (dir != Direction.NONE)
            path.add(dir);
        y += dir.dy();
        x += dir.dx();
        if (Map.isOutOfBounds(y, x)) {
            return null;
        };
        if (y == targetY && x == TargetX) {
            return path;
        }

        // 0 is a wall
        if (map[y][x] == 0) {
            return null;
        }

        // 2 is a visited node
        if (map[y][x] == 2) {
            return null;
        }

        map[y][x] = 2;

        int shortestPathLen = Integer.MAX_VALUE;
        ArrayList<Direction> shortestPath = null;
        for (Direction nextDir : Direction.directionVals) {  
            ArrayList<Direction> newPath = findPath(y, x, targetY, TargetX, nextDir, new ArrayList<>(path), copy(map));
            if (newPath != null && newPath.size() < shortestPathLen) {
                shortestPathLen = newPath.size();
                shortestPath = newPath;
            }
        }
        
        return shortestPath;
    }



    public static boolean isOutOfBounds(int y, int x) {
        return y < 0 || y >= getMapHeight() || x < 0 || x >= getMapWidth();
    }

    private static void convert1to16(short[][] map) {
        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[y].length; x++) {
                if (map[y][x] == 1) {
                    map[y][x] = 16;
                }
            }
        }
    }

    private static void addBorders(short[][] map) {
        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[y].length; x++) {
                if (map[y][x] == 16) {
                    boolean isTopEdge = y == 0;
                    boolean bordersTopWall = isTopEdge || map[y - 1][x] == 0;
                    if (bordersTopWall) {
                        map[y][x] += 2;
                    }

                    boolean isBottomEdge = y == map.length - 1;
                    boolean bordersBottomWall = isBottomEdge || map[y + 1][x] == 0;
                    if (bordersBottomWall) {
                        map[y][x] += 8;
                    }
                    
                    boolean isLeftEdge = x == 0;
                    boolean bordersLeftWall = isLeftEdge || map[y][x - 1] == 0;
                    if (bordersLeftWall) {
                        map[y][x] += 1;
                    }

                    boolean isRightEdge = x == map[y].length - 1;
                    boolean bordersRightWall = isRightEdge || map[y][x + 1] == 0;
                    if (bordersRightWall) {
                        map[y][x] += 4;
                    }
                }
            }
        }
    }

    public static void convertMap() {
        // Takes a map with the following form:
        // 1 = empty space
        // 0 = wall
        
        convert1to16(curMap);
        addBorders(curMap);
    }

    public static void printMap(short[][] map, String message) {
        System.out.println(message);
        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[y].length; x++) {
                System.out.print(map[y][x] + " ");
                if (map[y][x] < 10 && map[y][x] > -1) {
                    System.out.print(" ");
                }
            }
            System.out.println();
        }
        System.out.println("\n");
    };

    public static short[][] getMap() {
        short[][] temp = new short[getMapHeight()][getMapWidth()];
        for (int i = 0; i < getMapHeight(); i++) {
            for (int j = 0; j < getMapWidth(); j++) {
                temp[i][j] = (short) curMap[i][j];
            }
        }

        return temp;
    }

    public static int getMapWidth() {
        return curMap[0].length;
    }

    public static int getMapHeight() {
        return curMap.length;
    }



    // TODO Dva konstruktory, jeden se seedem, jeden bez
}
