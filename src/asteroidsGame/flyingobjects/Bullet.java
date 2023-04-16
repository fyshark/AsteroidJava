package asteroidsGame.flyingobjects;

import asteroidsGame.constants.AppConstants;
import asteroidsGame.Main;
import javafx.geometry.Point2D;
import javafx.scene.shape.Rectangle;

public class Bullet extends Rectangle {
    private Point2D velocity; // Bullet velocity
    private boolean isAlive = true; // Are the bullets still alive
    public static int BULLET_WIDTH = 3; // Declare bullet width
    private static final int DISAPPEAR_DISTANCE = (int) Main.stageHeight / 2;
    private double initX, initY;
    private Double crossX, crossY;
    private Float remindingDistance;
    public String shooter;
/*
    public Bullet(double x, double y, double direction, String shooter) {
        setWidth(BULLET_WIDTH); // Bullet width
        setHeight(5); // Bullet height
        setTranslateX(x); // x-coordinate of the bullet
        setTranslateY(y); // y-coordinate of the bullet
        setFill(AppConstants.AppColor.SHAPE.getColor());
        //flag for shooter, alien/player can't shoot themselves
        this.shooter = shooter;

        initX = x;
        initY = y;

        double speed = 9; // Bullet speed
        double changeX = Math.cos(Math.toRadians(direction)) * speed;
        double changeY = Math.sin(Math.toRadians(direction)) * speed;
        velocity = new Point2D(changeX, changeY);
    }
*/
    public Bullet(double x, double y, double direction, Point2D baseSpeed, String shooter) {
        setWidth(BULLET_WIDTH); // Bullet width
        setHeight(5); // Bullet height
        setTranslateX(x); // x-coordinate of the bullet
        setTranslateY(y); // y-coordinate of the bullet
        setFill(AppConstants.AppColor.SHAPE.getColor());
        //flag for shooter, alien/player can't shoot themselves
        this.shooter = shooter;

        initX = x;
        initY = y;

        double speedFactor = 2;
        // When the bullet does not move along the x or y axis direction
        if (baseSpeed.getX() != 0 || baseSpeed.getY() != 0) {
            double speed = baseSpeed.magnitude();
            speedFactor = Math.max(speedFactor, speed);
        }

        double radians = Math.toRadians(direction);
        double changeX = Math.cos(radians);
        double changeY = Math.sin(radians);
        velocity = new Point2D(changeX, changeY).multiply(speedFactor * 3);
    }

    public void move() {
        if (isAlive) {
            // Update bullet position
            setTranslateX(getTranslateX() + velocity.getX());
            setTranslateY(getTranslateY() + velocity.getY());

            // The conditions below checks that the bullets stays on stage.
            // Condition when bullets go beyond the left side of the x-coordinate system boundary
            if (getTranslateX() < 0) {
                remindingDistance = DISAPPEAR_DISTANCE - calculateDistanceBetweenPoints(getTranslateX(), getTranslateY() + velocity.getY(), initX, initY);
                crossX = getTranslateX() + Main.stageWidth;
                setTranslateX(crossX);

                crossY = getTranslateY();
            }

            // Condition when bullets go beyond the right side of the x-coordinate system boundary
            if (getTranslateX() > Main.stageWidth) {
                remindingDistance = DISAPPEAR_DISTANCE - calculateDistanceBetweenPoints(getTranslateX(), getTranslateY() + velocity.getY(), initX, initY);
                crossX = getTranslateX() % Main.stageWidth;
                setTranslateX(crossX);

                crossY = getTranslateY();
            }

            // Condition when bullets go below the bottom of the y-coordinate system boundary
            if (getTranslateY() < 0) {
                remindingDistance = DISAPPEAR_DISTANCE - calculateDistanceBetweenPoints(getTranslateX() + velocity.getX(), getTranslateY(), initX, initY);
                crossY = getTranslateY() + Main.stageHeight;
                setTranslateY(crossY);
                crossX = getTranslateX();
            }

            // Condition when bullets go beyond the top of the y-coordinate system boundary
            if (getTranslateY() > Main.stageHeight) {
                remindingDistance = DISAPPEAR_DISTANCE - calculateDistanceBetweenPoints(getTranslateX() + velocity.getX(), getTranslateY(), initX, initY);
                crossY = getTranslateY() % Main.stageHeight;
                setTranslateY(crossY);

                crossX = getTranslateX();
            }

            // Get the distance before and after the flight of the same bullet, regardless of whether it leaps out of the stage
            float distance = calculateDistanceBetweenPoints(getTranslateX(), getTranslateY(), crossX != null ? crossX : initX, crossY != null ? crossY : initY);

            // Determining if a bullet is out of bounds
            if (remindingDistance != null && remindingDistance - distance < 0) {
                isAlive = false;
            }
            if (remindingDistance == null && distance > DISAPPEAR_DISTANCE) {
                isAlive = false;
            }
        }
    }

    private float calculateDistanceBetweenPoints(double x1, double y1, double x2, double y2) {

        return (float) Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    }

    public boolean isAlive() {
        return isAlive;
    }

    public Rectangle getHitbox() {
        return new Rectangle(getTranslateX(), getTranslateY(), getWidth(), getHeight());
    }

    public int getCurrentBulletX() {
        return (int) velocity.getX();
    }
    public int getCurrentBulletY() {
        return (int) velocity.getY();
    }

}