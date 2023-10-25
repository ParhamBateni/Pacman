package Sample.Model;

import Sample.View.GamePage;
import javafx.scene.image.Image;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

import static java.lang.Thread.sleep;

public class Ghost implements Runnable {
    private final GhostColor color;
    private final GamePage gamePage;
    public ImagePattern imageRight;
    public ImagePattern imageLeft;
    public ImagePattern imageUp;
    public ImagePattern imageDown;
    public ImagePattern currentImage;
    public long delay;
    private String imageFolder;
    private int i, j;
    private Rectangle previousDottedRectangle;
    private Rectangle previousBombedRectangle;

    public Ghost(GhostColor color, GamePage gamePage, int j, int i) {
        this.color = color;
        this.gamePage = gamePage;
        this.i = i;
        this.j = j;
        if (color == GhostColor.RED) {
            imageFolder = "Red";
            currentImage = imageLeft;
        } else if (color == GhostColor.CYAN) {
            imageFolder = "Cyan";
            currentImage = imageRight;
        } else if (color == GhostColor.PINK) {
            imageFolder = "Pink";
            currentImage = imageRight;
        } else if (color == GhostColor.ORANGE) {
            imageFolder = "Orange";
            currentImage = imageLeft;
        }

        imageRight = new ImagePattern(new Image("/Sample/JPG/ghosts/" + imageFolder + "/Right.png"));
        imageLeft = new ImagePattern(new Image("/Sample/JPG/ghosts/" + imageFolder + "/Left.png"));
        imageUp = new ImagePattern(new Image("/Sample/JPG/ghosts/" + imageFolder + "/Up.png"));
        imageDown = new ImagePattern(new Image("/Sample/JPG/ghosts/" + imageFolder + "/Down.png"));

    }


    @Override
    public void run() {
        if (!gamePage.isGamePaused) {
            try {
                sleep(2000 + delay);
                if (delay != 0) delay = 0;
            } catch (Exception e) {
            }
        }
        while (true) {
            if (gamePage.pacman.getI() == i && gamePage.pacman.getJ() == j) continue;
            int counter = 0;
            ArrayList<Integer> movePattern = findPath();
            int randomNumber;
            if (gamePage.isBombAte && GamePage.isHardModeOn) {
                randomNumber = runAwayPattern();
            } else {
                randomNumber = movePattern.get(counter);
                counter++;
            }
            if (randomNumber == 0) {
                if (moveDown() == false) break;
            } else if (randomNumber == 1) {
                if (moveLeft() == false) break;
            } else if (randomNumber == 2) {
                if (moveUp() == false) break;
            } else if (randomNumber == 3) {
                if (moveRight() == false) break;
            }
            try {
                sleep(100);
            } catch (Exception e) {
            }
        }
        if (!gamePage.isBombAte) {
            if (!gamePage.isAudioMuted) new MediaPlayer(gamePage.deathSound).play();
            gamePage.isGameEnded = true;
            gamePage.deActivateBomb();
            gamePage.endRound(color);
        }
    }

    private ArrayList<Integer> findPath() {
        ArrayList<Integer> moves = new ArrayList<>();
        ArrayList<Integer> movePattern;
        ArrayList<Integer> min_moves = new ArrayList<>();
        min_moves.add(-1);
        int[][] ghostMazeData = gamePage.map.getGhostMazeData(color);
        int pacmanJ = gamePage.pacman.getJ();
        int pacmanI = gamePage.pacman.getI();
        if (ghostMazeData[pacmanJ][pacmanI] == 1) {
            ghostMazeData[pacmanJ][pacmanI] = 0;
            movePattern = gamePage.map.findShortestPath(new int[25][31], j, i, pacmanJ, pacmanI, min_moves, moves,
                    ghostMazeData);
            ghostMazeData[pacmanJ][pacmanI] = 1;
        } else {
            movePattern = gamePage.map.findShortestPath(new int[25][31], j, i, pacmanJ, pacmanI, min_moves, moves,
                    ghostMazeData);
        }
        return movePattern;
    }

    private boolean moveRight() {
        Rectangle[][] cells = gamePage.map.getGameCells();
        cells[j][i].setFill(null);
        if (previousDottedRectangle != null) previousDottedRectangle.setFill(gamePage.dots);
        else if (previousBombedRectangle != null) previousBombedRectangle.setFill(gamePage.bomb);
        i++;
        currentImage = imageRight;
        if (isPacmanInCell(j, i)) {
            if (!gamePage.isBombAte) cells[j][i].setFill(imageRight);
            else {
                i--;
                cells[j][i].setFill(gamePage.deadGhost);
            }
            return false;
        }
        if (cells[j][i].getFill() == gamePage.dots)
            previousDottedRectangle = cells[j][i];
        else {
            previousDottedRectangle = null;
        }
        if (cells[j][i].getFill() == gamePage.bomb) {
            previousBombedRectangle = cells[j][i];
        } else {
            previousBombedRectangle = null;
        }
        if (gamePage.isBombAte) cells[j][i].setFill(gamePage.deadGhost);
        else cells[j][i].setFill(imageRight);
        return true;
    }

    private boolean moveLeft() {
        Rectangle[][] cells = gamePage.map.getGameCells();
        cells[j][i].setFill(null);
        if (previousDottedRectangle != null) previousDottedRectangle.setFill(gamePage.dots);
        else if (previousBombedRectangle != null) previousBombedRectangle.setFill(gamePage.bomb);
        i--;
        currentImage = imageLeft;
        if (isPacmanInCell(j, i)) {
            if (!gamePage.isBombAte) cells[j][i].setFill(imageLeft);
            else {
                i++;
                cells[j][i].setFill(gamePage.deadGhost);
            }
            return false;
        }
        if (cells[j][i].getFill() == gamePage.dots)
            previousDottedRectangle = cells[j][i];
        else {
            previousDottedRectangle = null;
        }
        if (cells[j][i].getFill() == gamePage.bomb) {
            previousBombedRectangle = cells[j][i];
        } else {
            previousBombedRectangle = null;
        }
        if (gamePage.isBombAte) cells[j][i].setFill(gamePage.deadGhost);
        else cells[j][i].setFill(imageLeft);
        return true;
    }

    private boolean moveUp() {
        Rectangle[][] cells = gamePage.map.getGameCells();
        cells[j][i].setFill(null);
        if (previousDottedRectangle != null) previousDottedRectangle.setFill(gamePage.dots);
        else if (previousBombedRectangle != null) previousBombedRectangle.setFill(gamePage.bomb);
        j--;
        currentImage = imageUp;
        if (isPacmanInCell(j, i)) {
            if (!gamePage.isBombAte) cells[j][i].setFill(imageUp);
            else {
                j++;
                cells[j][i].setFill(gamePage.deadGhost);
            }
            return false;
        }
        if (cells[j][i].getFill() == gamePage.dots)
            previousDottedRectangle = cells[j][i];
        else {
            previousDottedRectangle = null;
        }
        if (cells[j][i].getFill() == gamePage.bomb) {
            previousBombedRectangle = cells[j][i];
        } else {
            previousBombedRectangle = null;
        }
        if (gamePage.isBombAte) cells[j][i].setFill(gamePage.deadGhost);
        else cells[j][i].setFill(imageUp);
        return true;
    }

    private boolean moveDown() {
        Rectangle[][] cells = gamePage.map.getGameCells();
        cells[j][i].setFill(null);
        if (previousDottedRectangle != null) previousDottedRectangle.setFill(gamePage.dots);
        else if (previousBombedRectangle != null) previousBombedRectangle.setFill(gamePage.bomb);
        j++;
        currentImage = imageDown;
        if (isPacmanInCell(j, i)) {
            if (!gamePage.isBombAte) cells[j][i].setFill(imageDown);
            else {
                j--;
                cells[j][i].setFill(gamePage.deadGhost);
            }
            return false;
        }
        if (cells[j][i].getFill() == gamePage.dots)
            previousDottedRectangle = cells[j][i];
        else {
            previousDottedRectangle = null;
        }
        if (cells[j][i].getFill() == gamePage.bomb) {
            previousBombedRectangle = cells[j][i];
        } else {
            previousBombedRectangle = null;
        }
        if (gamePage.isBombAte) cells[j][i].setFill(gamePage.deadGhost);
        else cells[j][i].setFill(imageDown);
        return true;
    }

    private boolean isPacmanInCell(int j, int i) {
        Paint cellImage = gamePage.map.getGameCells()[j][i].getFill();
        return cellImage == Pacman.imageLeft || cellImage == Pacman.imageLeftDown || cellImage == Pacman.imageLeftUp ||
                cellImage == Pacman.imageRightUp || cellImage == Pacman.imageRightDown || cellImage == Pacman.imageRight;
    }

    public int getJ() {
        return j;
    }

    public int getI() {
        return i;
    }

    public void restartGhost() {
        delay = 3000;
        previousBombedRectangle = null;
        previousDottedRectangle = null;
        currentImage = gamePage.deadGhost;
        if (color == GhostColor.RED) {
            j = 23;
            i = 29;
            gamePage.map.getGameCells()[j][i].setFill(gamePage.deadGhost);
            gamePage.redGhostThread.stop();
            gamePage.redGhostThread = new Thread(this);
            gamePage.redGhostThread.start();
        } else if (color == GhostColor.CYAN) {
            j = 23;
            i = 1;
            gamePage.map.getGameCells()[j][i].setFill(gamePage.deadGhost);
            gamePage.cyanGhostThread.stop();
            gamePage.cyanGhostThread = new Thread(this);
            gamePage.cyanGhostThread.start();
        } else if (color == GhostColor.ORANGE) {
            j = 1;
            i = 29;
            gamePage.map.getGameCells()[j][i].setFill(gamePage.deadGhost);
            gamePage.orangeGhostThread.stop();
            gamePage.orangeGhostThread = new Thread(this);
            gamePage.orangeGhostThread.start();
        } else if (color == GhostColor.PINK) {
            j = 1;
            i = 1;
            gamePage.map.getGameCells()[j][i].setFill(gamePage.deadGhost);
            gamePage.pinkGhostThread.stop();
            gamePage.pinkGhostThread = new Thread(this);
            gamePage.pinkGhostThread.start();
        }
    }

    private double getDistance(int j, int i) {
        return Math.sqrt(Math.pow(i - gamePage.pacman.getI(), 2) + Math.pow(j - gamePage.pacman.getJ(), 2));
    }

    private int runAwayPattern() {
        double distanceNow = getDistance(j, i);
        if (getDistance(j + 1, i) > distanceNow && gamePage.map.getMazeData()[j + 1][i] == 0) {
            return 0;
        } else if (getDistance(j, i - 1) > distanceNow && gamePage.map.getMazeData()[j][i - 1] == 0) {
            return 1;
        } else if (getDistance(j - 1, i) > distanceNow && gamePage.map.getMazeData()[j - 1][i] == 0) {
            return 2;
        } else if (gamePage.map.getMazeData()[j][i + 1] == 0) {
            return 3;
        } else {
            return 4;
        }

    }
}
