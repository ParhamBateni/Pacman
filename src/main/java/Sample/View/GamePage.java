package Sample.View;

import Sample.Controller.DataBaseController;
import Sample.Model.*;
import javafx.application.Application;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class GamePage extends Application implements EventHandler<KeyEvent> {
    public static boolean isHardModeOn;
    public final ImagePattern deadGhost = new ImagePattern(new Image("/Sample/JPG/ghosts/Vulnerable-ghost.png"));
    private final Media dotSound = new Media(new File("src\\main\\resources\\Sample\\Audio\\pacman_eatfruit.wav")
            .toURI().toString());
    private final Media startSound = new Media(new File("src\\main\\resources\\Sample\\Audio\\pacman_beginning.wav")
            .toURI().toString());
    private final Media endSound = new Media(new File("src\\main\\resources\\Sample\\Audio\\fail-buzzer-03.mp3")
            .toURI().toString());
    private final Media countDownSound = new Media(new File("src\\main\\resources\\Sample\\Audio\\countDown.wav")
            .toURI().toString());
    private final Media eatGhostSound = new Media(new File("src\\main\\resources\\Sample\\Audio\\pacman_eatghost.wav")
            .toURI().toString());
    private final ImagePattern lifeImage = new ImagePattern(new Image("/Sample/JPG/life.png"));
    public Scene scene;
    public Map map;
    public Pacman pacman;
    public boolean isAudioMuted = false;
    public boolean isGamePaused;
    public boolean isGameEnded;
    public Thread redGhostThread;
    public Thread pinkGhostThread;
    public Thread orangeGhostThread;
    public Thread cyanGhostThread;
    public boolean isBombAte = false;
    public Media deathSound = new Media(new File("src\\main\\resources\\Sample\\Audio\\pacman_death.wav")
            .toURI().toString());
    public ImagePattern dots = new ImagePattern(new Image("/Sample/JPG/dots.png"));
    public ImagePattern bomb = new ImagePattern(new Image("/Sample/JPG/bomb.png"));
    private boolean isSoundMuted;
    private Stage stage;
    private Stage pauseStage;
    private Ghost redGhost;
    private Ghost cyanGhost;
    private Ghost pinkGhost;
    private Ghost orangeGhost;
    private boolean gameHasBeenStarted;
    private final AnchorPane anchorPane = new AnchorPane();
    private final HBox lifeBox = new HBox(10);
    private final HBox scoreBox = new HBox();
    private int score = 0;
    private int countGhostsAte = 0;
    private AudioClip audioPlayer = null;
    private AudioClip bombAudioPlayer;
    private TimerTask bombTimerTask;
    private Timer bombTimer;
    private final EventHandler exitButtonHandler = new EventHandler() {
        @Override
        public void handle(Event event) {
            pauseStage.close();
            exitGame();
        }
    };
    private final EventHandler continueButtonHandler = new EventHandler() {
        @Override
        public void handle(Event event) {
            resumeGame();
        }
    };
    private final EventHandler newGameButtonHandler = new EventHandler() {
        @Override
        public void handle(Event event) {
            try {
                score = 0;
                start(stage);
                pauseStage.close();
            } catch (Exception e) {
            }
        }
    };

    public void playNewGame(Stage stage) {
        if (User.loggedInUser == null && User.notLoggedInUser.getSelectedMap() != null) {
            map = User.notLoggedInUser.getSelectedMap();
        } else if (User.loggedInUser != null && User.loggedInUser.getSelectedMap() != null) {
            map = User.loggedInUser.getSelectedMap();
        } else {
            map = new Map();
            map.createMaze();
        }
        try {
            start(stage);
        } catch (Exception e) {
        }
    }

    @Override
    public void start(Stage stage) throws Exception {
        WelcomePage.mediaPlayer.setVolume(0.3);
        new MediaPlayer(startSound).play();
        this.stage = stage;
        stage.setX(330.0);
        stage.setY(70.0);
        Pane gamePane = map.Init(true);
        gamePane.setLayoutX(130);
        gamePane.setLayoutY(50);
        Pane infoPane = new Pane();
        infoPane.getChildren().add(lifeBox);
        Text scoreText = new Text("Score: " + score);
        scoreText.setFill(Color.CHARTREUSE);
        scoreText.setFont(Font.font(20.0));
        scoreBox.getChildren().add(scoreText);
        scoreBox.setLayoutY(40.0);
        infoPane.getChildren().add(scoreBox);
        anchorPane.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        anchorPane.getChildren().add(infoPane);
        anchorPane.getChildren().add(gamePane);
        scene = new Scene(anchorPane, 850, 650);
        scene.setOnKeyPressed(this::handle);
        addElements(false);
        for (int i = 0; i < pacman.gameHealth; i++) {
            Rectangle lifeRectangle = new Rectangle(30, 30);
            lifeRectangle.setFill(lifeImage);
            lifeBox.getChildren().add(lifeRectangle);
        }
        stage.setScene(scene);
        stage.show();
    }

    private void addElements(boolean isResumeGame) {
        if (isResumeGame) {
            pacman.setJ(12);
            pacman.setI(15);
            for (int j = 0; j < 25; j++) {
                for (int i = 0; i < 31; i++) {
                    if (isGhostInCell(j, i)) map.getGameCells()[j][i].setFill(Color.BLACK);
                }
            }
        } else pacman = new Pacman(12, 15);
        if (!isResumeGame) {
            for (int j = 0; j < 25; j++) {
                for (int i = 0; i < 31; i++) {
                    if (map.getMazeData()[j][i] == 0) map.getGameCells()[j][i].setFill(dots);
                }
            }
            for (int i = 0; i < 3; i++) {
                while (true) {
                    Random random = new Random();
                    int bombY = random.nextInt(25);
                    int bombX = random.nextInt(31);
                    if (map.getMazeData()[bombY][bombX] == 0) {
                        map.getGameCells()[bombY][bombX].setFill(bomb);
                        break;
                    }
                }
            }
        }
        threadsInit();
    }

    private void threadsInit() {
        redGhost = new Ghost(GhostColor.RED, this, 23, 29);
        cyanGhost = new Ghost(GhostColor.CYAN, this, 23, 1);
        pinkGhost = new Ghost(GhostColor.PINK, this, 1, 1);
        orangeGhost = new Ghost(GhostColor.ORANGE, this, 1, 29);
        map.getGameCells()[12][15].setFill(Pacman.imageRight);
        map.getGameCells()[23][29].setFill(redGhost.imageLeft);
        map.getGameCells()[23][1].setFill(cyanGhost.imageRight);
        map.getGameCells()[1][1].setFill(pinkGhost.imageRight);
        map.getGameCells()[1][29].setFill(orangeGhost.imageLeft);

        redGhostThread = new Thread(redGhost);
        cyanGhostThread = new Thread(cyanGhost);
        orangeGhostThread = new Thread(orangeGhost);
        pinkGhostThread = new Thread(pinkGhost);
    }


    @Override
    public void handle(KeyEvent keyEvent) {
        if (isGameEnded) {
            isGameEnded = false;
            gameHasBeenStarted = false;
            newRound();
        }
        if (canPacmanMove(keyEvent)) {
            if (!gameHasBeenStarted) {
                try {
                    setGhostThreads(true, false);
                } catch (Exception e) {
                }
                gameHasBeenStarted = true;
            }
            if (keyEvent.getCode() == KeyCode.DOWN) pacmanMoveDown();
            else if (keyEvent.getCode() == KeyCode.RIGHT) pacmanMoveRight();
            else if (keyEvent.getCode() == KeyCode.UP) pacmanMoveUp();
            else if (keyEvent.getCode() == KeyCode.LEFT) pacmanMoveLeft();

            if (isAllDotsAte()) resetRound();
        }
        if (keyEvent.getCode() == KeyCode.ESCAPE) {
            try {
                setGhostThreads(false, false);
                openPauseMenu();
            } catch (Exception e) {
            }
        } else if (keyEvent.getCode() == KeyCode.M) isAudioMuted = isAudioMuted == false;
        if (audioPlayer != null && !isAudioMuted) {
            audioPlayer.play();
            audioPlayer = null;
        }
    }

    private void pacmanMoveRight() {
        map.getGameCells()[pacman.getJ()][pacman.getI()].setFill(null);
        ImagePattern changedImage = Pacman.imageRight;
        pacman.changeI(1);
        if (map.getGameCells()[pacman.getJ()][pacman.getI()].getFill() == dots) {
            updateScoreBox(5);
        } else if (map.getGameCells()[pacman.getJ()][pacman.getI()].getFill() == bomb) {
            activateBomb();
        } else if (map.getGameCells()[pacman.getJ()][pacman.getI()].getFill() == deadGhost) {
            getGhostInCell(pacman.getJ(), pacman.getI()).restartGhost();
            audioPlayer = new AudioClip(eatGhostSound.getSource());
            updateScoreBox((++countGhostsAte) * 200);
        }
        map.getGameCells()[pacman.getJ()][pacman.getI()].setFill(changedImage);
        pacman.setCurrentImage(changedImage);
    }

    private void pacmanMoveDown() {
        map.getGameCells()[pacman.getJ()][pacman.getI()].setFill(null);
        pacman.changeJ(1);
        ImagePattern changedImage;
        if (pacman.currentImage == Pacman.imageRight || pacman.currentImage == Pacman.imageRightUp) {
            changedImage = Pacman.imageRightDown;
        } else if (pacman.currentImage == Pacman.imageLeft || pacman.currentImage == Pacman.imageLeftUp) {
            changedImage = Pacman.imageLeftDown;
        } else {
            changedImage = pacman.currentImage;
        }
        if (map.getGameCells()[pacman.getJ()][pacman.getI()].getFill() == dots) {
            updateScoreBox(5);
        } else if (map.getGameCells()[pacman.getJ()][pacman.getI()].getFill() == bomb) {
            activateBomb();
        } else if (map.getGameCells()[pacman.getJ()][pacman.getI()].getFill() == deadGhost) {
            getGhostInCell(pacman.getJ(), pacman.getI()).restartGhost();
            audioPlayer = new AudioClip(eatGhostSound.getSource());
            updateScoreBox((++countGhostsAte) * 200);
        }
        map.getGameCells()[pacman.getJ()][pacman.getI()].setFill(changedImage);
        pacman.setCurrentImage(changedImage);
    }

    private void pacmanMoveUp() {
        map.getGameCells()[pacman.getJ()][pacman.getI()].setFill(null);
        pacman.changeJ(-1);
        ImagePattern changedImage;
        if (pacman.currentImage == Pacman.imageRight || pacman.currentImage == Pacman.imageRightDown) {
            changedImage = Pacman.imageRightUp;
        } else if (pacman.currentImage == Pacman.imageLeft || pacman.currentImage == Pacman.imageLeftDown) {
            changedImage = Pacman.imageLeftUp;
        } else {
            changedImage = pacman.currentImage;
        }
        if (map.getGameCells()[pacman.getJ()][pacman.getI()].getFill() == dots) {
            updateScoreBox(5);

        } else if (map.getGameCells()[pacman.getJ()][pacman.getI()].getFill() == bomb) {
            activateBomb();
        } else if (map.getGameCells()[pacman.getJ()][pacman.getI()].getFill() == deadGhost) {
            getGhostInCell(pacman.getJ(), pacman.getI()).restartGhost();
            audioPlayer = new AudioClip(eatGhostSound.getSource());
            updateScoreBox((++countGhostsAte) * 200);
        }
        map.getGameCells()[pacman.getJ()][pacman.getI()].setFill(changedImage);
        pacman.setCurrentImage(changedImage);
    }

    private void pacmanMoveLeft() {
        ImagePattern changedImage = Pacman.imageLeft;
        map.getGameCells()[pacman.getJ()][pacman.getI()].setFill(null);
        pacman.changeI(-1);
        if (map.getGameCells()[pacman.getJ()][pacman.getI()].getFill() == dots) {
            updateScoreBox(5);
        } else if (map.getGameCells()[pacman.getJ()][pacman.getI()].getFill() == bomb) {
            activateBomb();
        } else if (map.getGameCells()[pacman.getJ()][pacman.getI()].getFill() == deadGhost) {
            getGhostInCell(pacman.getJ(), pacman.getI()).restartGhost();
            audioPlayer = new AudioClip(eatGhostSound.getSource());
            updateScoreBox((++countGhostsAte) * 200);
        }
        pacman.setCurrentImage(changedImage);
        map.getGameCells()[pacman.getJ()][pacman.getI()].setFill(changedImage);
    }

    private void resetRound() {
        gameHasBeenStarted = false;
        Rectangle lifeRectangle = new Rectangle(30, 30);
        lifeRectangle.setFill(lifeImage);
        lifeBox.getChildren().add(lifeRectangle);
        endRound(GhostColor.ORANGE);
        pacman.addLife();
        addElements(false);
        scene.setOnKeyPressed(this::handle);
        stage.setScene(scene);
        stage.show();
    }

    private void updateScoreBox(int amount) {
        score += amount;
        scoreBox.getChildren().clear();
        Text scoreText = new Text("Score: " + score);
        scoreText.setFill(Color.CHARTREUSE);
        scoreText.setFont(Font.font(20.0));
        scoreBox.getChildren().add(scoreText);
    }

    private void openPauseMenu() {
        isGamePaused = true;
        Button continueButton = new Button("continue");
        Button exitButton = new Button("save and exit");
        continueButton.setOnAction(continueButtonHandler);
        exitButton.setOnAction(exitButtonHandler);
        VBox buttonPlaces = new VBox(20);
        buttonPlaces.setAlignment(Pos.CENTER);
        BackgroundFill background_fill = new BackgroundFill(Color.BLACK,
                CornerRadii.EMPTY, Insets.EMPTY);
        Background background = new Background(background_fill);
        buttonPlaces.setBackground(background);
        buttonPlaces.getChildren().add(continueButton);
        buttonPlaces.getChildren().add(exitButton);
        Scene scene = new Scene(buttonPlaces, 300.0, 300.0);
        pauseStage = new Stage();
        pauseStage.setX(620.0);
        pauseStage.setY(300.0);
        pauseStage.initStyle(StageStyle.UNDECORATED);
        pauseStage.initModality(Modality.APPLICATION_MODAL);
        scene.getStylesheets().add("/Sample/CSS/MenuCss.css");
        pauseStage.initOwner(stage);
        pauseStage.setScene(scene);
        pauseStage.show();
    }

    private boolean canPacmanMove(KeyEvent keyEvent) {
        int i = pacman.getI();
        int j = pacman.getJ();
        if (keyEvent.getCode() == KeyCode.DOWN) {
            return j < Map.MAP_WIDTH && map.getMazeData()[j + 1][i] != 1 &&
                    (!isGhostInCell(j + 1, i) || isBombAte);
        } else if (keyEvent.getCode() == KeyCode.UP) {
            return j >= 1 && map.getMazeData()[j - 1][i] != 1 &&
                    (!isGhostInCell(j - 1, i) || isBombAte);
        } else if (keyEvent.getCode() == KeyCode.RIGHT) {
            return i < Map.MAP_LENGTH && map.getMazeData()[j][i + 1] != 1 &&
                    (!isGhostInCell(j, i + 1) || isBombAte);
        } else if (keyEvent.getCode() == KeyCode.LEFT) {
            return i >= 1 && map.getMazeData()[j][i - 1] != 1 &&
                    (!isGhostInCell(j, i - 1) || isBombAte);
        }
        return false;
    }

    private void setGhostThreads(boolean start, boolean resume) {
        if (start) {
            redGhostThread.start();
            cyanGhostThread.start();
            orangeGhostThread.start();
            pinkGhostThread.start();
        } else if (resume) {
            redGhostThread = new Thread(redGhost);
            cyanGhostThread = new Thread(cyanGhost);
            orangeGhostThread = new Thread(orangeGhost);
            pinkGhostThread = new Thread(pinkGhost);
            redGhostThread.start();
            cyanGhostThread.start();
            orangeGhostThread.start();
            pinkGhostThread.start();
        } else {
            redGhostThread.suspend();
            cyanGhostThread.suspend();
            orangeGhostThread.suspend();
            pinkGhostThread.suspend();
        }
    }

    public void resumeGame() {
        pauseStage.close();
        setGhostThreads(false, true);
    }

    public void endRound(GhostColor color) {
        try {
            if (color == GhostColor.CYAN) {
                redGhostThread.stop();
                pinkGhostThread.stop();
                orangeGhostThread.stop();
                cyanGhostThread.stop();
            } else if (color == GhostColor.PINK) {
                redGhostThread.stop();
                orangeGhostThread.stop();
                cyanGhostThread.stop();
                pinkGhostThread.stop();
            } else if (color == GhostColor.ORANGE) {
                redGhostThread.stop();
                cyanGhostThread.stop();
                pinkGhostThread.stop();
                orangeGhostThread.stop();
            } else {
                cyanGhostThread.stop();
                pinkGhostThread.stop();
                orangeGhostThread.stop();
                redGhostThread.stop();
            }
            redGhostThread.stop();
            pinkGhostThread.stop();
            orangeGhostThread.stop();
            cyanGhostThread.stop();
        } catch (Exception e) {
        }
    }

    public void newRound() {
        pacman.reduceLife();
        lifeBox.getChildren().remove(lifeBox.getChildren().get(lifeBox.getChildren().size() - 1));
        if (pacman.gameHealth == 0) {
            if (!isAudioMuted) {
                new MediaPlayer(endSound).play();
            }
            if (User.loggedInUser != null) {
                User.loggedInUser.setMaxScore(score);
            }
            endGame();
        } else {
            addElements(true);
            scene.setOnKeyPressed(this::handle);
            stage.setScene(scene);
            stage.show();
        }
    }

    private void endGame() {
        Text text = new Text("You lost!");
        text.setFill(Color.RED);
        text.setFont(Font.font("", FontWeight.BOLD, 20.0));

        Button newGameButton = new Button("new game");
        Button exitButton = new Button("exit");

        VBox buttonPlaces = new VBox(20);
        buttonPlaces.setAlignment(Pos.CENTER);
        BackgroundFill background_fill = new BackgroundFill(Color.BLACK,
                CornerRadii.EMPTY, Insets.EMPTY);
        Background background = new Background(background_fill);
        buttonPlaces.setBackground(background);
        buttonPlaces.getChildren().add(text);
        buttonPlaces.getChildren().add(newGameButton);
        buttonPlaces.getChildren().add(exitButton);

        Scene scene = new Scene(buttonPlaces, 300.0, 300.0);
        pauseStage = new Stage();
        pauseStage.setX(620.0);
        pauseStage.setY(300.0);
        pauseStage.initStyle(StageStyle.UNDECORATED);
        newGameButton.setOnAction(newGameButtonHandler);
        exitButton.setOnAction(exitButtonHandler);
        pauseStage.initModality(Modality.APPLICATION_MODAL);
        scene.getStylesheets().add("/Sample/CSS/MenuCss.css");
        pauseStage.initOwner(stage);
        pauseStage.setScene(scene);
        pauseStage.show();
    }

    private void exitGame() {
        pauseStage.close();
        WelcomePage.mediaPlayer.setVolume(1.0);
        try {
            if (User.loggedInUser != null) {
                try {
                    DataBaseController.saveUsersData();
                } catch (Exception e) {
                    System.err.println("can not save data!");
                }
                new MainPage().start(stage);
            } else {
                WelcomePage.welcomePage.start(stage);
            }
        } catch (Exception e) {
        }
    }

    private boolean isGhostInCell(int j, int i) {
        Paint gameCellFill = map.getGameCells()[j][i].getFill();
        if (gameCellFill == null) {
            return false;
        }
        return gameCellFill == redGhost.currentImage ||
                gameCellFill == pinkGhost.currentImage ||
                gameCellFill == cyanGhost.currentImage ||
                gameCellFill == orangeGhost.currentImage;
    }

    private boolean isAllDotsAte() {
        for (int j = 0; j < 25; j++) {
            for (int i = 0; i < 31; i++) {
                if (map.getGameCells()[j][i].getFill() == dots) {
                    return false;
                }
            }
        }
        return true;
    }

    private void activateBomb() {
        if (!isBombAte) {
            bombTimerTask = new DeadGhostTimer(this);
            bombTimer = new Timer();
            isBombAte = true;
            redGhost.currentImage = deadGhost;
            orangeGhost.currentImage = deadGhost;
            cyanGhost.currentImage = deadGhost;
            pinkGhost.currentImage = deadGhost;
            if (!isAudioMuted) {
                bombAudioPlayer = new AudioClip(countDownSound.getSource());
                bombAudioPlayer.play();
            }
            bombTimer.schedule(bombTimerTask, (long) 10000.0);
            map.getGameCells()[redGhost.getJ()][redGhost.getI()].setFill(deadGhost);
            map.getGameCells()[pinkGhost.getJ()][pinkGhost.getI()].setFill(deadGhost);
            map.getGameCells()[cyanGhost.getJ()][cyanGhost.getI()].setFill(deadGhost);
            map.getGameCells()[orangeGhost.getJ()][orangeGhost.getI()].setFill(deadGhost);
        } else {
            bombAudioPlayer.stop();
            bombAudioPlayer = new AudioClip(countDownSound.getSource());
            bombAudioPlayer.play();
            bombTimerTask.cancel();
            bombTimerTask = new DeadGhostTimer(this);
            bombTimer.schedule(bombTimerTask, (long) 10000.0);
        }
    }

    public void deActivateBomb() {
        isBombAte = false;
        countGhostsAte = 0;
        for (int j = 0; j < 25; j++) {
            for (int i = 0; i < 31; i++) {
                if (isGhostInCell(j, i)) {
                    Ghost ghost = getGhostInCell(j, i);
                    ghost.currentImage = ghost.imageDown;
                    map.getGameCells()[j][i].setFill(ghost.currentImage);
                }
            }
        }
    }

    public Ghost getGhostInCell(int j, int i) {
        if (redGhost.getJ() == j && redGhost.getI() == i) {
            return redGhost;
        } else if (pinkGhost.getI() == i && pinkGhost.getJ() == j) {
            return pinkGhost;
        } else if (cyanGhost.getJ() == j && cyanGhost.getI() == i) {
            return cyanGhost;
        } else if (orangeGhost.getI() == i && orangeGhost.getJ() == j) {
            return orangeGhost;
        }
        return null;
    }
}
