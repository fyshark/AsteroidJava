package asteroidsGame.flyingobjects;

import asteroidsGame.constants.AppConstants;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import java.util.Random;
import static asteroidsGame.Main.STAGEHEIGHT;
import static asteroidsGame.Main.STAGEWIDTH;

public class Asteroid extends Rectangle{
    public double speed;
    public double rotation;
    private int asteroidX;
    private int asteroidY;
    public double size;
    public Polygon asteroid; // the shape of the asteroid
    private final double INCREASE_SPEED = 0.2;
    private final double INCREASE_ROTATION = 2;

    public Asteroid(double size, double speed, int x, int y) {
        Random rnd = new Random();
        this.speed = speed;
        this.size = size;
        this.rotation = 3;
        this.asteroidX = x;
        this.asteroidY = y;
        this.rotation = rnd.nextDouble() * 360; // assign a random rotation angle between 0 and 360 degrees
        this.asteroid = createAsteroid(size);
        this.asteroid.setFill(AppConstants.AppColor.FILL.getColor());
        this.asteroid.setStroke(AppConstants.AppColor.SHAPE.getColor());
        this.asteroid.setTranslateX(x);
        this.asteroid.setTranslateY(y);
    }

    public Polygon getAsteroid() {
        return asteroid; // returns the shape of the asteroid to the scene we call it on
    }

    public double getSize() {
        return this.size;
    }

    public int getCurrentAsteroidX() {
        return (int) asteroid.getTranslateX();
    }
    public int getCurrentAsteroidY() {
        return (int) asteroid.getTranslateY();
    }
    public double increaseSpeedOnDestruction() {
        this.speed += INCREASE_SPEED;
        return this.speed;
    }

    public double increaseRotationOnDestruction(){
        this.rotation *= INCREASE_ROTATION;
        return this.rotation;
    }

    public void move() {
        // calculate x and y components of movement vector based on current rotation angle
        double dx = Math.cos(Math.toRadians(this.rotation)) * this.speed;
        double dy = Math.sin(Math.toRadians(this.rotation)) * this.speed;

        // update asteroid position based on movement vector
        this.asteroid.setTranslateX(this.asteroid.getTranslateX() + dx);
        this.asteroid.setTranslateY(this.asteroid.getTranslateY() + dy);

        double rotationAngle = this.asteroid.getRotate();
        this.asteroid.setRotate(rotationAngle + rotation / 300);

        // wrap the asteroid around the screen if it goes off the edges
        if (this.asteroid.getTranslateX() < 0) {
            this.asteroid.setTranslateX(STAGEWIDTH);
        }
        if (this.asteroid.getTranslateX() > STAGEWIDTH) {
            this.asteroid.setTranslateX(0);
        }
        if (this.asteroid.getTranslateY() < 0) {
            this.asteroid.setTranslateY(STAGEHEIGHT);
        }
        if (this.asteroid.getTranslateY() > STAGEHEIGHT) {
            this.asteroid.setTranslateY(0);
        }
    }
    public boolean collide(Bullet bullet) {
        Shape collisionArea = Shape.intersect(this.asteroid, bullet.getHitbox());
        return collisionArea.getBoundsInLocal().getWidth() != -1;
    }

    public Polygon createAsteroid(double size) {
        Random rnd = new Random();

        // Generate a random number of sides between 12-20
        int sides = rnd.nextInt(5) + 7;

        // Calculate the angle between each side of the polygon
        double angle = 2 * Math.PI / sides;

        Polygon polygon = new Polygon();
        for (int i = 0; i < sides; i++) {
            // Calculate the x and y coordinates for each point
            double x = size * Math.cos(i * angle);
            double y = size * Math.sin(i * angle);
            polygon.getPoints().addAll(x, y);
        }

        // Randomize the distance of each point from the center of the polygon
        for (int i = 0; i < polygon.getPoints().size(); i++) {
            double change = rnd.nextDouble() * size / 3 * 2 - size / 3;
            polygon.getPoints().set(i, polygon.getPoints().get(i) + change);
        }

        return polygon;
    }
}