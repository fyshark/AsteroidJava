package asteroidsGame;

import javafx.geometry.Point2D;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;

public class Bullet extends Rectangle {
    private Point2D velocity; // Bullet velocity
    private boolean isAlive = true; // Are the bullets still alive
    public static int BULLET_WIDTH = 5; // Declare bullet width

    public Bullet(double x, double y, double direction) {
        setWidth(BULLET_WIDTH); // Bullet width
        setHeight(5); // Bullet height
        setTranslateX(x); // x-coordinate of the bullet
        setTranslateY(y); // y-coordinate of the bullet
        setFill(Color.WHITE);


        double speed = 2; // Bullet speed
        double changeX = Math.cos(Math.toRadians(direction)) * speed;
        double changeY = Math.sin(Math.toRadians(direction)) * speed;
        velocity = new Point2D(changeX, changeY);
    }

    public void move() {
        if (isAlive) {
            // Update bullet position
            setTranslateX(getTranslateX() + velocity.getX());
            setTranslateY(getTranslateY() + velocity.getY());

            // Determining if a bullet is out of bounds
            if (getTranslateX() < 0 || getTranslateX() > Main.stageWidth || getTranslateY() < 0 || getTranslateY() > Main.stageHeight) {
                isAlive = false;
            }
        }
    }

    public boolean isAlive() {
        return isAlive;
    }

    public Rectangle getHitbox() {

        return new Rectangle(getTranslateX(), getTranslateY(), getWidth(), getHeight());
    }

}


