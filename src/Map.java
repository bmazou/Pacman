public class Map {
    public static final short[][] defaultMap1 = {
        {19, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 22},
        {17, 16, 16, 16, 16, 24, 16, 16, 16, 16, 16, 16, 16, 16, 20},
        {25, 24, 24, 24, 28, 0, 17, 16, 16, 16, 16, 16, 16, 16, 20},
        {0, 0, 0, 0, 0, 0, 17, 16, 16, 16, 16, 16, 16, 16, 20},
        {19, 18, 18, 18, 18, 18, 16, 16, 16, 16, 24, 24, 24, 24, 20},
        {17, 16, 16, 16, 16, 16, 16, 16, 16, 20, 0, 0, 0, 0, 21},
        {17, 16, 16, 16, 16, 16, 16, 16, 16, 20, 0, 0, 0, 0, 21},
        {17, 16, 16, 16, 24, 16, 16, 16, 16, 20, 0, 0, 0, 0, 21},
        {17, 16, 16, 20, 0, 17, 16, 16, 16, 16, 18, 18, 18, 18, 20},
        {17, 24, 24, 28, 0, 25, 24, 24, 16, 16, 16, 16, 16, 16, 20},
        {21, 0, 0, 0, 0, 0, 0, 0, 17, 16, 16, 16, 16, 16, 20},
        {17, 18, 18, 22, 0, 19, 18, 18, 16, 16, 16, 16, 16, 16, 20},
        {17, 16, 16, 20, 0, 17, 16, 16, 16, 16, 16, 16, 16, 16, 20},
        {17, 16, 16, 20, 0, 17, 16, 16, 16, 16, 16, 16, 16, 16, 20},
        {25, 24, 24, 24, 26, 24, 24, 24, 24, 24, 24, 24, 24, 24, 28}
    };

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


    public static short[][] curMap; // = convertMap(pacmanMap.clone());

    static {
        convertMap();
    }

    private static void convert1to16(short[][] map) {
        System.out.println("Converting 0's to 16's");
        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[y].length; x++) {
                if (map[y][x] == 1) {
                    map[y][x] = 16;
                }
            }
        }

        System.out.println("After converting 0's to 16's");
        printMap(map);
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

        // curMap = pacmanMap.clone();
        curMap = MapGenerator.getMap().clone();
        printMap(curMap);
        convert1to16(curMap);
        addBorders(curMap);
    }

    public static void printMap(short[][] map) {
        System.out.println("\nCurrent map:");
        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[y].length; x++) {
                System.out.print(map[y][x] + " ");
                if (map[y][x] < 10 && map[y][x] > -1) {
                    System.out.print(" ");
                }
            }
            System.out.println();
        }
        System.out.println();
    };

    public static short[][] getMap() {
        return curMap;
    }

    public static int getMapWidth() {
        return curMap[0].length;
    }

    public static int getMapHeight() {
        return curMap.length;
    }



    // TODO Dva konstruktory, jeden se seedem, jeden bez
}
