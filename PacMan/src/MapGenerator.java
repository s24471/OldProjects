import java.util.Arrays;
import java.util.Random;

public class MapGenerator {
    private static final int WALL = TileType.WALL.getValue();
    private static final int EMPTY = TileType.EMPTY.getValue();
    private static final int PLAYER_START = TileType.PLAYER_START.getValue();
    private static final int GHOST_SPAWN = TileType.GHOST_SPAWN.getValue();
    private static final int NUMBER_OF_GHOSTS = 3;

    public static void main(String[] args) {

        Random random = new Random();
        int size = random.nextInt(100 - 10 + 1) + 10;
        int[][] map = generateMap(size);
        printMap(map);
    }

    public static int[][] generateMap(int size) {
        Random random = new Random();
        int[][] map = new int[size][size];
        for (int i = 0; i < size; i++) {
            Arrays.fill(map[i], WALL);
        }

        generateMaze(map, 0, size - 1, 0, size - 1, random);


        for (int i = 0; i < NUMBER_OF_GHOSTS+1; i++) {
            int r1, r2;
            do {
                r1 = random.nextInt(size-5)+3;
                r2 = random.nextInt(size-5)+3;
            } while (map[r1][r2] != EMPTY);
            if(i == NUMBER_OF_GHOSTS)map[r1][r2] = PLAYER_START;
            else map[r1][r2] = GHOST_SPAWN;
        }


        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (i == 1 || i == size - 2 || j == 1 || j == size - 2) {
                    map[i][j] = EMPTY;
                }
                if (i == 0 || i == size - 1 || j == 0 || j == size - 1) {
                    map[i][j] = WALL;
                }
            }
        }
        return map;
    }

    public static void generateMaze(int[][] map, int startX, int endX, int startY, int endY, Random random) {
        if (startX > endX || startY > endY) {
            return;
        }

        int width = endX - startX + 1;
        int height = endY - startY + 1;

        if (width < 3 || height < 3) {
            return;
        }

        int split;
        if (width > height) {
            split = startX + random.nextInt(width - 2) + 1;
            for (int i = startY; i <= endY; i++) {
                map[split][i] = EMPTY;
            }
            generateMaze(map, startX, split - 1, startY, endY, random);
            generateMaze(map, split + 1, endX, startY, endY, random);
        } else {
            split = startY + random.nextInt(height - 2) + 1;
            for (int i = startX; i <= endX; i++) {
                map[i][split] = EMPTY;
            }
            generateMaze(map, startX, endX, startY, split - 1, random);
            generateMaze(map, startX, endX, split + 1, endY, random);
        }
    }

    public static void printMap(int[][] map) {
        for (int[] row : map) {
            for (int cell : row) {
                System.out.print(cell + " ");
            }
            System.out.println();
        }
    }
}

