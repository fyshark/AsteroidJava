package asteroidsGame;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Point2D;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Random;

public abstract class BaseShip {
    int defaultShipX; // the default x-coordinate of the player's ship
    int defaultShipY; // the default y-coordinate of the player's ship

    Point2D defaultShipSpeed; // the default speed of the player's ship

    public Polygon ship; // make ship public so that Player class can access the shape -> required for detecting collision.
    private Point2D movement; // the current movement vector of the player's ship

    private long lastBulletTime; // Add a field to store the last bullet time
    private static final long SHOOT_CD = 250 * 1000000; // 250 ms

    public BaseShip(Polygon polygon, int x, int y) {

        // constructor to create the player's ship
        // takes x and y coordinates as parameters which determines where the ship is loaded into our scene.
        this.ship = polygon;
        this.defaultShipX = x; // we remember the default value for X and Y so that we can reuse these values when restarting the game.
        this.defaultShipY = y;

        this.ship.setTranslateX(defaultShipX);
        this.ship.setTranslateY(defaultShipY);

        // likewise we do the same for our speed and set it to (0,0).
        // This means that when we restart the game we do not restart the ship with the same velocity as last recorded in our previous game.
        this.defaultShipSpeed = new Point2D(0, 0);

        this.movement = new Point2D(0, 0); // initial movement vector is set to (0,0). Our ship is not going anywhere just yet.
        //Initial bullet firing time
        this.lastBulletTime = 0;
    }

    public Polygon getCharacter() {
        return this.ship; // returns the shape of the player's ship to the scene we call it on
    }

    public void resetPosition() {
        // resets the player's ship to its default position and speed
        this.ship.setTranslateX(defaultShipX);
        this.ship.setTranslateY(defaultShipY);
        this.ship.setRotate(-90);
        this.movement = this.defaultShipSpeed;
    }

    public void turnLeft() {
        // turns the player's ship to the left by 30 degrees
        this.ship.setRotate(this.ship.getRotate() - 30);
    }

    public void turnRight() {
        // turns the player's ship to the right by 30 degrees
        this.ship.setRotate(this.ship.getRotate() + 30);
    }

    public void accelerate() {
        // accelerates the player's ship in the direction it is facing
        double acceleration = 0.18; // the rate of acceleration
        double maxSpeed = 8.0; // the maximum speed the ship can travel
        double changeX = Math.cos(Math.toRadians(ship.getRotate())) * acceleration;
        double changeY = Math.sin(Math.toRadians(ship.getRotate())) * acceleration;
        movement = movement.add(changeX, changeY); // add the acceleration vector to the movement vector
        double speed = movement.magnitude(); // calculate the speed of the ship
        if (speed > maxSpeed) {
            // normalize() returns a vector with the same direction as movement, but with a magnitude (or length) of 1.0.
            // This is useful when you want to maintain the direction of a vector but adjust its length.
            // multiply() is a method on Point2D that multiplies the x and y components of the vector by a value.
            // In this case, we're multiplying the normalized vector (with a magnitude of 1.0) by maxSpeed, which gives us a vector with the same direction as movement but a magnitude of maxSpeed.
            // We're then updating the movement vector to be the new vector we just calculated in which has a magnitude of at most maxSpeed.
            movement = movement.normalize().multiply(maxSpeed);
        }
    }

    public void decelerate() {
        // decelerates the player's ship by a certain amount
        double deceleration = 0.12; // the rate of deceleration
        // subtract the deceleration vector from the movement vector to get the ship to 'slow down'
        movement = movement.subtract(movement.multiply(deceleration));
    }

    public void move() {
        // moves the player's ship based on its current movement vector
        this.ship.setTranslateX(this.ship.getTranslateX() + this.movement.getX());
        this.ship.setTranslateY(this.ship.getTranslateY() + this.movement.getY());

        // The conditions below checks that the ship stays on screen.
        if (this.ship.getTranslateX() < 0) {
            this.ship.setTranslateX(this.ship.getTranslateX() + Main.stageWidth);
        }

        if (this.ship.getTranslateX() > Main.stageWidth) {
            this.ship.setTranslateX(this.ship.getTranslateX() % Main.stageWidth);
        }

        if (this.ship.getTranslateY() < 0) {
            this.ship.setTranslateY(this.ship.getTranslateY() + Main.stageHeight);
        }

        if (this.ship.getTranslateY() > Main.stageHeight) {
            this.ship.setTranslateY(this.ship.getTranslateY() % Main.stageHeight);
        }
    }

    public Bullet shoot(String shooter) {
        long currentTime = System.nanoTime();
        if (currentTime - lastBulletTime < SHOOT_CD) {
            return null; // If the CD is not enough, don't create a bullet
        }

        lastBulletTime = currentTime;

        //    Get the position and direction of the bullet
        //    Adjusting the position of bullets fired from the tip of the ship
        double bulletX = this.ship.getTranslateX() + Bullet.BULLET_WIDTH / 2d;
        double bulletY = this.ship.getTranslateY();

        double bulletDirection = ship.getRotate();

        Bullet bullet = new Bullet(bulletX, bulletY, bulletDirection, this.movement, shooter);
        return bullet;
    }

    public Point2D getPosition() {
        return new Point2D(this.ship.getTranslateX(), this.ship.getTranslateY());
    }

    public ArrayList<Line> splitBaseShipPolygon() {
        // Get the coordinates of the vertices of the polygon
        double[] points = this.ship.getPoints().stream().mapToDouble(Double::doubleValue).toArray();
        double x1 = points[0] + this.ship.getTranslateX(), y1 = points[1] + this.ship.getTranslateY();
        double x2 = points[2] + this.ship.getTranslateX(), y2 = points[3] + this.ship.getTranslateY();
        double x3 = points[4] + this.ship.getTranslateX(), y3 = points[5] + this.ship.getTranslateY();

        // Create three lines that correspond to the sides of the triangle
        Line line1 = new Line(x1, y1, x2, y2);
        Line line2 = new Line(x2, y2, x3, y3);
        Line line3 = new Line(x3, y3, x1, y1);

        // Set the color and stroke width of the lines
        line1.setStroke(AppConstants.AppColor.SHAPE.getColor());
        line2.setStroke(AppConstants.AppColor.SHAPE.getColor());
        line3.setStroke(AppConstants.AppColor.SHAPE.getColor());
        line1.setStrokeWidth(2);
        line2.setStrokeWidth(2);
        line3.setStrokeWidth(2);

        ArrayList lineslist = new ArrayList() {
            {
                add(line1);
                add(line2);
                add(line3);
            }
        };
        decomposeTriangle(lineslist);
        return lineslist;
    }

    private void decomposeTriangle(ArrayList<Line> lineslist) {
        Line line1 = lineslist.get(0);
        Line line2 = lineslist.get(1);
        Line line3 = lineslist.get(2);

        // Create a Timeline to animate the movement of the lines
        Random random = new Random();
        double durationInSeconds = 0.1;
        Timeline timeline = new Timeline();

        for (int i = 0; i < 30; i++) {
            double offsetX1 = random.nextDouble() * 20 - 10;
            double offsetY1 = random.nextDouble() * 20 - 10;
            double offsetX2 = random.nextDouble() * 20 - 10;
            double offsetY2 = random.nextDouble() * 20 - 10;
            double offsetX3 = random.nextDouble() * 20 - 10;
            double offsetY3 = random.nextDouble() * 20 - 10;

            Duration keyFrameDuration = Duration.seconds(durationInSeconds / 30.0);
            KeyFrame kf1 = new KeyFrame(keyFrameDuration.multiply(i), event -> {
                line1.setStartX(line1.getStartX() + offsetX1);
                line1.setStartY(line1.getStartY() + offsetY1);
                line1.setEndX(line1.getEndX() + offsetX1);
                line1.setEndY(line1.getEndY() + offsetY1);

                line2.setStartX(line2.getStartX() + offsetX2);
                line2.setStartY(line2.getStartY() + offsetY2);
                line2.setEndX(line2.getEndX() + offsetX2);
                line2.setEndY(line2.getEndY() + offsetY2);

                line3.setStartX(line3.getStartX() + offsetX3);
                line3.setStartY(line3.getStartY() + offsetY3);
                line3.setEndX(line3.getEndX() + offsetX3);
                line3.setEndY(line3.getEndY() + offsetY3);
            });
            timeline.getKeyFrames().add(kf1);
        }

        timeline.play();
        timeline.setOnFinished(event -> {
            Main.gamePane.getChildren().removeAll(lineslist);
        });

    }

}