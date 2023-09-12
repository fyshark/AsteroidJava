package asteroidsGame.flyingobjects;

import asteroidsGame.constants.AppConstants;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Point2D;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Random;

import static asteroidsGame.constants.AppConstants.STAGE_HEIGHT;
import static asteroidsGame.constants.AppConstants.STAGE_WIDTH;
import static asteroidsGame.scenes.GamePlayScene.gamePane;

public abstract class BaseShip {
    int defaultShipX; // the default x-coordinate of the BaseShip
    int defaultShipY; // the default y-coordinate of the BaseShip

    Point2D defaultShipSpeed; // the default speed of the BaseShip

    public Polygon ship; // make ship public so that subclass can access the shape -> required for detecting collision.
    protected Point2D movement; // the current movement vector of the BaseShip

    private long lastBulletTime; // Add a field to store the last bullet time
    private static final long SHOOT_CD = 250 * 1000000; // 250 ms

    public BaseShip(Polygon polygon, int x, int y) {

        // constructor to create the BaseShip
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
        return this.ship;
    } // returns the shape of the BaseShip to the scene we call it on

    // Reset the position of the ship so that it enters the screen again from the other side of the edge.
    public void screenBounds() {
        // The conditions below checks that the ship stays on screen.
        if (this.ship.getTranslateX() < 0) {
            this.ship.setTranslateX(this.ship.getTranslateX() + STAGE_WIDTH);
        }

        if (this.ship.getTranslateX() > STAGE_WIDTH) {
            this.ship.setTranslateX(this.ship.getTranslateX() % STAGE_WIDTH);
        }

        if (this.ship.getTranslateY() < 0) {
            this.ship.setTranslateY(this.ship.getTranslateY() + STAGE_HEIGHT);
        }

        if (this.ship.getTranslateY() > STAGE_HEIGHT) {
            this.ship.setTranslateY(this.ship.getTranslateY() % STAGE_HEIGHT);
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

    //defines how a collision between a BaseShip and an asteroid
    public boolean crash(Asteroid asteroid) {
        Shape collisionArea = Shape.intersect(this.ship, asteroid.getAsteroid());
        return collisionArea.getBoundsInLocal().getWidth() != -1;
    }

    //defines what a collision is for a BaseShip with a bullet
    public boolean collide(Bullet bullet) {
        Shape collisionArea = Shape.intersect(this.ship, bullet.getHitbox());
        return collisionArea.getBoundsInLocal().getWidth() != -1;
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

    // animates the movement of three lines representing a triangle, then removes them from a game pane
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
            gamePane.getChildren().removeAll(lineslist);
        });

    }

}