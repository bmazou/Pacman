import java.util.ArrayList;
import java.util.HashMap;

public class Map {
    public static short[][] curMap;
    public static HashMap<String, Direction> pathDict;

    
    public static void generateMap() {
        /**
         * Generates a map 
         * <p>
         * It's generated using the MapGenerator class. It then converts the map to the logical format used by the game. Lastly it fills the pathDict
         */
        MapGenerator.generateMap();
        curMap = MapGenerator.getMap();
        convertMap();

        pathDict = new HashMap<>();
        fillPathDict();
    }   

    //* ----------------- MAP CONVERSION ----------------- *//
    public static boolean isOutOfBounds(int y, int x) {
        /**
         * Checks if the given coordinates are out of bounds of the map
         * 
         * @param y The y coordinate
         * @param x The x coordinate
         * 
         * @return True if the coordinates are out of bounds, false otherwise
         */
        return y < 0 || y >= getMapHeight() || x < 0 || x >= getMapWidth();
    }

    private static void convert1to16(short[][] map) {
        /**
         * Converts all 1's in the map to 16's
         * 
         * @param map The map to convert (its reference)
         */

        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[y].length; x++) {
                if (map[y][x] == 1) {
                    map[y][x] = 16;
                }
            }
        }
    }

    private static void addBorders(short[][] map) {
        /**
         * Adds borders to the map
         * <p>
         * 16 is currently a tile with food. This method adds 1/2/4/8 representing the borders of the tile. The logic is more closely explained in the pdf documentation
         * 
         * @param map The map to add borders to (its reference)
         */
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
        /**
         * Converts a generated map to the logical format used by the game
         */
        convert1to16(curMap);
        addBorders(curMap);
    }

    //* ----------------- GETTERS ----------------- *//
    public static short[][] getMap() {
        /**
         * Returns a copy the logical map
         * 
         * @return A copy of current logical map
         */
        short[][] temp = new short[getMapHeight()][getMapWidth()];
        for (int i = 0; i < getMapHeight(); i++) {
            for (int j = 0; j < getMapWidth(); j++) {
                temp[i][j] = (short) curMap[i][j];
            }
        }

        return temp;
    }

    public static int getMapWidth() {
        /**
         * Returns the width of the map
         * 
         * @return The width of the map
         */
        return MapGenerator.getMapWidth();
    }

    public static int getMapHeight() {
        /**
         * Returns the height of the map
         * 
         * @return The height of the map
         */
        return MapGenerator.getMapHeight();
    }

    public static int getFoodCount() {
        /**
         * Returns the amount of food on the map
         * 
         * @return The amount of food on the map
         */
        int count = 0;
        for (int y = 0; y < getMapHeight(); y++) {
            for (int x = 0; x < getMapWidth(); x++) {
                if ((curMap[y][x]& 16) != 0) {
                    count++;
                }
            }
        }

        return count;
    }

    //* ----------------- Direction precalculation ----------------- *//
    public static void fillPathDict() {
        /**
         * Fills the pathDict with all possible paths
         * <p>
         * This method is called once at the start of the game. It calculates all possible paths between all tiles on the map. It then stores the paths in the pathDict
         * It uses A* algorithm to calculate the shortest path between two tiles and stores the best direction to take at the current tile
         */
        short[][] map = MapGenerator.getMap();
        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[y].length; x++) {
                for (int targetY = map.length - 1; targetY >= 0; targetY--) {
                    for (int targetX = map[targetY].length - 1; targetX >= 0; targetX--) {
                        boolean bothEmptySpace = map[y][x] == 1 && map[targetY][targetX] == 1;
                        if (!bothEmptySpace) continue;

                        boolean alreadyInDict = pathDict.containsKey(y + "," + x + "," + targetY + "," + targetX);
                        if (alreadyInDict) continue;

                        if (y == targetY && x == targetX) {
                            pathDict.put(y + "," + x + "," + targetY + "," + targetX, Direction.NONE);
                            continue;
                        }
                        
                        Node node = aStarPath(y, x, targetY, targetX);
                        ArrayList<Direction> path = node.getPath();
                        insertPath(y, x, targetY, targetX, path);
                    }
                }
            }
        }
    }

    private static void insertPath(int y, int x, int targetY, int targetX, ArrayList<Direction> path) {
        /**
         * For each tile in the path, stores the direction to take at that tile
         */
        for (int i = 0; i < path.size(); i++) {
            Direction dir = path.get(i);
            pathDict.put(y + "," + x + "," + targetY + "," + targetX, dir);
            y += dir.dy();
            x += dir.dx();
        }
    }



    //*  ----------------- PATHFINDING -----------------  *//
    public static int manhD(int y, int x, int targetY, int targetX) {
        /**
         * Calculates the manhattan distance between two tiles
         * 
         * @param y The y coordinate of the first tile
         * @param x The x coordinate of the first tile
         * @param targetY The y coordinate of the second tile
         * @param targetX The x coordinate of the second tile
         * @return The manhattan distance between the two tiles
         */
        return Math.abs(y - targetY) + Math.abs(x - targetX);
    }

    private static Node getLowestCostNode(ArrayList<Node> open) {
        /**
         * Returns the node with the lowest fCost from a list of nodes
         * 
         * @param open The list of nodes to search through
         * @return The node with the lowest fCost
         */
        Node lowestCostNode = open.get(0);
        for (Node node : open) {
            if (node.getFCost() < lowestCostNode.getFCost()) {
                lowestCostNode = node;
            }
        }

        return lowestCostNode;
    }

    private static boolean isInList(int y, int x, ArrayList<Node> list) {
        /**
         * Checks if a node is in a list of nodes
         * 
         * @param y The y coordinate of the node
         * @param x The x coordinate of the node
         * @param list The list of nodes to search through
         * @return True if the node is in the list, false otherwise
         */
        for (Node node : list) {
            if (node.y == y && node.x == x) {
                return true;
            }
        }

        return false;
    }

    private static Node getNode(int y, int x, ArrayList<Node> list) {
        /**
         * Returns the node with the given coordinates from a list of nodes, or null if it is not in the list
         * 
         * @param y The y coordinate of the node
         * @param x The x coordinate of the node
         * @param list The list of nodes to search through
         * @return The node with the given coordinates
         */
        for (Node node : list) {
            if (node.y == y && node.x == x) {
                return node;
            }
        }

        return null;
    }

    public static Node aStarPath(int startY, int startX, int targetY, int targetX) {
        /**
         * Calculates the shortest path between two tiles using the A* algorithm
         * 
         * @param startY The y coordinate of the starting tile
         * @param startX The x coordinate of the starting tile
         * @param targetY The y coordinate of the target tile
         * @param targetX The x coordinate of the target tile
         * @return The node at the target tile, which contains the path to take
         */
        short[][] map = Map.getMap();
        ArrayList<Node> open = new ArrayList<>();
        ArrayList<Node> closed = new ArrayList<>();
        Node startNode = new Node(startY, startX, 0, manhD(startY, startX, targetY, targetX), null);
        open.add(startNode);

        while (true) {
            if (open.size() == 0) {
                System.out.println("No path found");
                return null;
            }

            Node curNode = getLowestCostNode(open);
            open.remove(curNode);
            closed.add(curNode);

            if (curNode.y == targetY && curNode.x == targetX) {
                return curNode;
            }

            for (Direction direction : Direction.directionVals) {
                int newY = curNode.y + direction.dy();
                int newX = curNode.x + direction.dx();

                if (Map.isOutOfBounds(newY, newX)) continue;
                if (map[newY][newX] == 0) continue;
                if (isInList(newY, newX, closed)) continue;

                int gCost = curNode.gCost + 1;
                // If new path to neigbour is shorter, set neigbour's parent to current node
                if (isInList(newY, newX, open)) {
                    Node neigbour = getNode(newY, newX, open);
                    if (gCost < neigbour.gCost) {
                        neigbour.parent = curNode;
                        neigbour.gCost = gCost;
                    }
                }else {
                    Node neigbour = new Node(newY, newX, gCost, manhD(newY, newX, targetY, targetX), curNode);
                    open.add(neigbour);
                }
            }
        }
    }
}



class Node {
    public int y;
    public int x;
    public int gCost;
    public int hCost;
    public Node parent;

    public Node(int y, int x, int gCost, int hCost, Node parent) {
        this.y = y;
        this.x = x;
        this.gCost = gCost;
        this.hCost = hCost;
        this.parent = parent;
    }

    public int getFCost() {
        /**
         * Returns the fCost of the node (sum of gCost and hCost)
         * 
         * @return The fCost of the node
         */
        return gCost + hCost;
    }

    private Direction getDirection(Node node, Node parent) {
        /**
         * Returns the direction from the parent node to the node
         * 
         * @param node The node to get the direction to
         * @param parent The parent node
         * @return The direction from the parent node to the node
         */
        if (node.y == parent.y && node.x == parent.x) {
            return Direction.NONE;
        }

        if (node.y == parent.y) {
            if (node.x > parent.x) {
                return Direction.RIGHT;
            } else {
                return Direction.LEFT;
            }
        } else {
            if (node.y > parent.y) {
                return Direction.DOWN;
            } else {
                return Direction.UP;
            }
        }
    }

    public ArrayList<Direction> getPath() {
        /**
         * Returns the path to take to get to the node from the starting node
         * 
         * @return Path from the starting node to the node
         */
        ArrayList<Direction> path = new ArrayList<>();
        Node node = this;
        while (node.parent != null) {
            path.add(0, getDirection(node, node.parent));
            node = node.parent;
        }

        return path;
    }
}