package Sample.Model;

import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;

public class Pacman {

    public static ImagePattern imageRight = new ImagePattern(new Image
            ("/Sample/JPG/Pacman_Dark_Scary_Right.jpg"));
    public static ImagePattern imageLeft = new ImagePattern(new Image
            ("/Sample/JPG/Pacman_Dark_Scary_Left.jpg"));
    public static ImagePattern imageLeftDown = new ImagePattern(new Image
            ("/Sample/JPG/Pacman_Dark_Scary_Left_Down.jpg"));
    public static ImagePattern imageLeftUp = new ImagePattern(new Image
            ("/Sample/JPG/Pacman_Dark_Scary_Left_Up.jpg"));
    public static ImagePattern imageRightUp = new ImagePattern(new Image
            ("/Sample/JPG/Pacman_Dark_Scary_Right_Up.jpg"));
    public static ImagePattern imageRightDown = new ImagePattern(new Image
            ("/Sample/JPG/Pacman_Dark_Scary_Right_Down.jpg"));
    public static int health = 3;
    public int gameHealth;
    public ImagePattern currentImage;
    private int i, j;

    public Pacman(int j, int i) {
        this.i = i;
        this.j = j;
        currentImage = imageRight;
        this.gameHealth = health;
    }

    public static void setMaxLife(int amount) {
        health = amount;
    }

    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
    }

    public void setCurrentImage(ImagePattern currentImage) {
        this.currentImage = currentImage;
    }

    public int getJ() {
        return j;
    }

    public void setJ(int j) {
        this.j = j;
    }

    public void changeI(int amount) {
        i += amount;
    }

    public void changeJ(int amount) {
        j += amount;
    }

    public void reduceLife() {
        this.gameHealth--;
    }

    public void addLife() {
        this.gameHealth++;
    }
}
