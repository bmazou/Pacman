import java.util.ArrayList;
import java.util.HashMap;

public class Map {
    public static short[][] curMap = MapGenerator.getMap(); // = convertMap(pacmanMap.clone());
    public static HashMap<String, Direction> pathDict = new HashMap<>();


    static {
        convertMap();
        fillPathDict();
        System.out.println("PathDict size:" + pathDict.size()); 
    }


    public static void fillPathDict() {
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
        for (int i = 0; i < path.size(); i++) {
            Direction dir = path.get(i);
            pathDict.put(y + "," + x + "," + targetY + "," + targetX, dir);
            y += dir.dy();
            x += dir.dx();
        }
    }



    //* PATH SEEKING using A* algorithm
    public static int manhD(int y, int x, int targetY, int targetX) {
        return Math.abs(y - targetY) + Math.abs(x - targetX);
    }

    private static Node getLowestCostNode(ArrayList<Node> open) {
        Node lowestCostNode = open.get(0);
        for (Node node : open) {
            if (node.getFCost() < lowestCostNode.getFCost()) {
                lowestCostNode = node;
            }
        }

        return lowestCostNode;
    }

    private static boolean isInList(int y, int x, ArrayList<Node> list) {
        for (Node node : list) {
            if (node.y == y && node.x == x) {
                return true;
            }
        }

        return false;
    }

    private static Node getNode(int y, int x, ArrayList<Node> list) {
        for (Node node : list) {
            if (node.y == y && node.x == x) {
                return node;
            }
        }

        return null;
    }

    public static Node aStarPath(int startY, int startX, int targetY, int targetX) {
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



    //* MAP CONVERSION from 1/0 format to actual format for the game
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
        return MapGenerator.getMapWidth();
    }

    public static int getMapHeight() {
        return MapGenerator.getMapHeight();
    }

    public static int getFoodCount() {
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
        return gCost + hCost;
    }

    private Direction getDirection(Node node, Node parent) {
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
        ArrayList<Direction> path = new ArrayList<>();
        Node node = this;
        while (node.parent != null) {
            path.add(0, getDirection(node, node.parent));
            node = node.parent;
        }

        return path;
    }
}