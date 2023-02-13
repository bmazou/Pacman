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
    
    public static final short[][] testMap = {
        {-1, -1, 0, -1, -1},
        {-1, 0, 0, 0, -1}, 
        {-1, 0, 0, 0, -1}, 
        {-1, 0, 0, 0, -1},
        {-1, -1, -1, -1, -1}
    };

    public static final short[][] pacmanMap = {
		{ -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 }, 
		{ -1, 0, 0, 0, 0, 0, 0, 0, 0, -1, 0, 0, 0, 0, 0, 0, 0, 0, -1 }, 
		{ -1, 0, -1, -1, 0, -1, -1, -1, 0, -1, 0, -1, -1, -1, 0, -1, -1, 0, -1 }, 
		{ -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1 }, 
		{ -1, 0, -1, -1, 0, -1, 0, -1, -1, -1, -1, -1, 0, -1, 0, -1, -1, 0, -1 }, 
		{ -1, 0, 0, 0, 0, -1, 0, 0, 0, -1, 0, 0, 0, -1, 0, 0, 0, 0, -1 }, 
		{ -1, -1, -1, -1, 0, -1, -1, -1, 0, -1, 0, -1, -1, -1, 0, -1, -1, -1, -1 }, 
		{ 0, 0, 0, -1, 0, -1, 0, 0, 0, 0, 0, 0, 0, -1, 0, -1, 0, 0, 0 }, 
		{ 0, 0, 0, -1, 0, -1, 0, -1, -1, 4, -1, -1, 0, -1, 0, -1, 0, 0, 0 }, 
		{ 0, 0, 0, -1, 0, 0, 0, -1, 0, 0, 0, -1, 0, 0, 0, -1, 0, 0, 0 }, 
		{ 0, 0, 0, -1, 0, -1, 0, -1, -1, -1, -1, -1, 0, -1, 0, -1, 0, 0, 0 }, 
		{ 0, 0, 0, -1, 0, -1, 0, 0, 0, 0, 0, 0, 0, -1, 0, -1, 0, 0, 0 }, 
		{ -1, -1, -1, -1, 0, -1, 0, -1, -1, -1, -1, -1, 0, -1, 0, -1, -1, -1, -1 }, 
		{ -1, 0, 0, 0, 0, 0, 0, 0, 0, -1, 0, 0, 0, 0, 0, 0, 0, 0, -1 }, 
		{ -1, 0, -1, -1, 0, -1, -1, -1, 0, -1, 0, -1, -1, -1, 0, -1, -1, 0, -1 }, 
		{ -1, 0, 0, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1, 0, 0, -1 }, 
		{ -1, -1, 0, -1, 0, -1, 0, -1, -1, -1, -1, -1, 0, -1, 0, -1, 0, -1, -1 }, 
		{ -1, 0, 0, 0, 0, -1, 0, 0, 0, -1, 0, 0, 0, -1, 0, 0, 0, 0, -1 }, 
		{ -1, 0, -1, -1, -1, -1, -1, -1, 0, -1, 0, -1, -1, -1, -1, -1, -1, 0, -1 }, 
		// { -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1 }, 
		// { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 }  
	}; 

    public static short[][] curMap = convertMap(pacmanMap);
    

    private static short[][] convertNeg1to0(short[][] map) {
        System.out.println("Converting -1's to 0's");
        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[y].length; x++) {
                if (map[y][x] == -1) {
                    map[y][x] = 0;
                }
            }
        }

        return map;
    }

    public static short[][] convertMap(short[][] map) {
        // Takes a map with the following form:
        // -1 = wall
        // 0 = empty

        // Converts the map the following way:
        // Add 16 to empty
        // Add 1 if empty borders a wall on left
        // Add 2 if empty borders a wall on top
        // Add 4 if empty borders a wall on right
        // Add 8 if empty borders a wall on bottom
        // And at the end, convert -1's to 0's

        System.out.println("Converting map");
        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[y].length; x++) {
                if (map[y][x] == 0) {
                    map[y][x] += 16;

                    boolean isTopEdge = y == 0;
                    boolean bordersTopWall = isTopEdge || map[y - 1][x] == -1;
                    if (bordersTopWall) {
                        map[y][x] += 2;
                    }

                    boolean isBottomEdge = y == map.length - 1;
                    boolean bordersBottomWall = isBottomEdge || map[y + 1][x] == -1;
                    if (bordersBottomWall) {
                        map[y][x] += 8;
                    }
                    
                    boolean isLeftEdge = x == 0;
                    boolean bordersLeftWall = isLeftEdge || map[y][x - 1] == -1;
                    if (bordersLeftWall) {
                        map[y][x] += 1;
                    }

                    boolean isRightEdge = x == map[y].length - 1;
                    boolean bordersRightWall = isRightEdge || map[y][x + 1] == -1;
                    if (bordersRightWall) {
                        map[y][x] += 4;
                    }
                }
            }
        }
        // return map;
        return convertNeg1to0(map.clone());
    }

    public static void printMap(short[][] map) {
        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[y].length; x++) {
                System.out.print(map[y][x] + " ");
            }
            System.out.println();
        }
    };
    
    public static void test() {
        System.out.println("Test map:");
        short[][] myMap = convertMap(testMap);
        printMap(myMap);
    }

    public static short[][] getMap() {
        return curMap;
    }

    public static int getMapWidth() {
        return curMap[0].length;
    }

    public static void printMapDimensions() {
        System.out.println("Map dimensions - x: " + getMapWidth() + ", y: " + curMap.length);
    }



    // TODO Dva konstruktory, jeden se seedem, jeden bez
}
