package Sample.Model;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.Random;

public class Map {
    public static final int MAP_WIDTH = 25;
    public static final int MAP_LENGTH = 31;
    final static int width = 12;
    final static int length = 15;
    public static Map map1;
    public static Map map2;
    public static Map map3;
    private static char[][] maze;

    static {
        map1 = new Map();
        map2 = new Map();
        map3 = new Map();
        map1.createMaze();
        map2.createMaze();
        map3.createMaze();
    }

    public int[][] mazeData = new int[2 * width + 1][2 * length + 1];
    public int[][] ghostsMazeData = new int[MAP_WIDTH][MAP_LENGTH];
    public int[][] redGhostMazeData = new int[MAP_WIDTH][MAP_LENGTH];
    public int[][] cyanGhostMazeData = new int[MAP_WIDTH][MAP_LENGTH];
    public int[][] pinkGhostMazeData = new int[MAP_WIDTH][MAP_LENGTH];
    public int[][] orangeGhostMazeData = new int[MAP_WIDTH][MAP_LENGTH];
    public boolean isSaved = false;

    public transient Rectangle[][] gameCells = new Rectangle[2 * width + 1][2 * length + 1];

    private static boolean isSafe(int[][] mat, int[][] visited, int x, int y) {
        return !(mat[x][y] == 1 || visited[x][y] != 0);
    }

    private static boolean isValid(int x, int y) {
        return (x < MAP_WIDTH && y < MAP_LENGTH && x >= 0 && y >= 0);
    }

    public void createMaze() {
        Random randomNumber = new Random();
        mazeInit();
        int y = randomNumber.nextInt(width);
        int x = randomNumber.nextInt(length);
        generate(2 * y + 1, 2 * x + 1);
        changeMaze();
        removeRandomWalls();
    }

    public Rectangle[][] getGameCells() {
        return gameCells;
    }

    public int[][] getMazeData() {
        return mazeData;
    }

    public Pane Init(boolean isForGame) {
        Pane pane = new Pane();
        int size;
        if (isForGame) size = 20;
        else {
            size = 8;
        }
        for (int i = 0; i < MAP_WIDTH * size; i += size) {
            for (int j = 0; j < MAP_LENGTH * size; j += size) {
                Rectangle rec = new Rectangle(j, i, size, size);
                if (mazeData[i / size][j / size] == 0) {
                    rec.setFill(Color.BLACK);

                } else if (mazeData[i / size][j / size] == 1) {
                    rec.setFill(Color.CYAN);
                }
                gameCells[i / size][j / size] = rec;
                pane.getChildren().add(rec);
            }
        }
        if (isForGame) {
            pane.setMaxHeight(500);
            pane.setMaxWidth(620);
        }
        return pane;
    }

    public void mazeInit() {
        maze = new char[2 * width + 1][2 * length + 1];
        for (int j = 0; j < 2 * width + 1; j++) {
            for (int i = 0; i < 2 * length + 1; i++) {
                if (j == 0 && i == 1 || j == 2 * width
                        && i == 2 * length - 1) {
                    maze[j][i] = 'e';
                } else if (j == 0 || j == 2 * width
                        || i == 0 || i == 2 * length) {
                    maze[j][i] = '1';
                } else if (i % 2 == 1 && j % 2 == 1) {
                    maze[j][i] = 's';
                } else {
                    maze[j][i] = '1';
                }
            }
        }
    }

    private void changeMaze() {
        for (int j = 0; j < 2 * width + 1; j++) {
            for (int i = 0; i < 2 * length + 1; i++) {
                if (maze[j][i] == '*') {
                    mazeData[j][i] = 0;
                    ghostsMazeData[j][i] = 0;
                } else if (maze[j][i] == 'e') {
                    mazeData[j][i] = 1;
                    ghostsMazeData[j][i] = 1;
                } else {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(maze[j][i]);
                    mazeData[j][i] = Integer.parseInt(stringBuilder.toString());
                    ghostsMazeData[j][i] = Integer.parseInt(stringBuilder.toString());
                }
            }
        }
    }

    private void printMaze() {
        for (int j = 0; j < 2 * width + 1; j++) {
            for (int i = 0; i < 2 * length + 1; i++) {
                System.out.print(ghostsMazeData[j][i] + " ");
            }
            System.out.println();
        }
    }

    private void generate(int y, int x) {
        maze[y][x] = '*';
        while (isInBound(y, x + 2) && maze[y][x + 2] == 's' || isInBound(y + 2, x) && maze[y + 2][x] == 's'
                || isInBound(y, x - 2) && maze[y][x - 2] == 's' || isInBound(y - 2, x) && maze[y - 2][x] == 's') {
            ArrayList<Integer> moves = new ArrayList<>();
            moves.add(0);
            moves.add(1);
            moves.add(2);
            moves.add(3);
            while (true) {
                int randomNumber = getRandomNumber(moves);
                if (isInBound(y, x + 2) && randomNumber == 0 && maze[y][x + 2] == 's') {
                    generate(y, x + 2);
                    maze[y][x + 1] = '0';
                    break;
                } else if (isInBound(y + 2, x) && randomNumber == 1 && maze[y + 2][x] == 's') {
                    generate(y + 2, x);
                    maze[y + 1][x] = '0';
                    break;
                } else if (isInBound(y, x - 2) && randomNumber == 2 && maze[y][x - 2] == 's') {
                    generate(y, x - 2);
                    maze[y][x - 1] = '0';
                    break;
                } else if (isInBound(y - 2, x) && randomNumber == 3 && maze[y - 2][x] == 's') {
                    generate(y - 2, x);
                    maze[y - 1][x] = '0';
                    break;
                }
            }
        }
    }

    private boolean isInBound(int y, int x) {
        return y < 2 * width + 1 && y >= 0 && x < 2 * length + 1 && x >= 0;
    }

    private int getRandomNumber(ArrayList<Integer> moves) {
        int length = moves.size();
        Random randomNumber = new Random();
        int selectNumber = Math.abs(randomNumber.nextInt(length));
        int sendNumber = moves.get(selectNumber);
        moves.remove(selectNumber);
        return sendNumber;
    }

    private void removeRandomWalls() {
        int countRemoved = 0;
        mazeData[12][15] = 0;
        redGhostMazeData = copyArraysValues(ghostsMazeData, redGhostMazeData);
        pinkGhostMazeData = copyArraysValues(ghostsMazeData, pinkGhostMazeData);
        orangeGhostMazeData = copyArraysValues(ghostsMazeData, orangeGhostMazeData);
        cyanGhostMazeData = copyArraysValues(ghostsMazeData, cyanGhostMazeData);
        while (countRemoved < 90) {
            Random randomNumber = new Random();
            int randomX = randomNumber.nextInt(2 * length + 1);
            int randomY = randomNumber.nextInt(2 * width + 1);
            if (mazeData[randomY][randomX] == 1 && randomY != 0 && randomY != 2 * width && randomX != 0 &&
                    randomX != 2 * length && isNotAHole(randomY, randomX)) {
                mazeData[randomY][randomX] = 0;
                if (countRemoved <= 14) {
                    redGhostMazeData[randomY][randomX] = 0;
                } else if (countRemoved > 14 && countRemoved <= 29) {
                    pinkGhostMazeData[randomY][randomX] = 0;
                } else if (countRemoved > 29 && countRemoved <= 44) {
                    orangeGhostMazeData[randomY][randomX] = 0;
                } else if (countRemoved > 44 && countRemoved <= 59) {
                    cyanGhostMazeData[randomY][randomX] = 0;
                }
                countRemoved++;
            }
        }
    }

    private boolean isNotAHole(int j, int i) {
        return mazeData[j + 1][i] != 1 || mazeData[j][i + 1] != 1 || mazeData[j][i - 1] != 1 || mazeData[j - 1][i] != 1;
    }

    public int[][] getGhostMazeData(GhostColor color) {
        if (color == GhostColor.RED) {
            return redGhostMazeData;
        } else if (color == GhostColor.PINK) {
            return pinkGhostMazeData;
        } else if (color == GhostColor.CYAN) {
            return cyanGhostMazeData;
        } else if (color == GhostColor.ORANGE) {
            return orangeGhostMazeData;
        } else {
            return null;
        }
    }

    public ArrayList<Integer> findShortestPath(int[][] visited, int i, int j, int x, int y, ArrayList<Integer> min_dist,
                                               ArrayList<Integer> move, int[][] ghostMazeData) {
        int[][] mat = ghostMazeData;
        if (i == x && j == y) {
            if (min_dist.get(0) == -1 || move.size() < min_dist.size()) return move;
            else return min_dist;
        }
        visited[i][j] = 1;
        if (isValid(i + 1, j) && isSafe(mat, visited, i + 1, j)) {
            ArrayList<Integer> newMove = (ArrayList<Integer>) move.clone();
            newMove.add(0);
            min_dist = findShortestPath(visited, i + 1, j, x, y, min_dist, newMove, ghostMazeData);
        }
        if (isValid(i, j + 1) && isSafe(mat, visited, i, j + 1)) {
            ArrayList<Integer> newMove = (ArrayList<Integer>) move.clone();
            newMove.add(3);
            min_dist = findShortestPath(visited, i, j + 1, x, y, min_dist, newMove, ghostMazeData);
        }
        if (isValid(i - 1, j) && isSafe(mat, visited, i - 1, j)) {
            ArrayList<Integer> newMove = (ArrayList<Integer>) move.clone();
            newMove.add(2);
            min_dist = findShortestPath(visited, i - 1, j, x, y, min_dist, newMove, ghostMazeData);
        }
        if (isValid(i, j - 1) && isSafe(mat, visited, i, j - 1)) {
            ArrayList<Integer> newMove = (ArrayList<Integer>) move.clone();
            newMove.add(1);
            min_dist = findShortestPath(visited, i, j - 1, x, y, min_dist, newMove, ghostMazeData);
        }
        visited[i][j] = 0;
        return min_dist;
    }

    private int[][] copyArraysValues(int[][] original, int[][] copied) {
        for (int j = 0; j < MAP_WIDTH; j++) {
            for (int i = 0; i < MAP_LENGTH; i++) {
                copied[j][i] = original[j][i];
            }
        }
        return copied;
    }
}
