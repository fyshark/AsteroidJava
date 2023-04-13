package asteroidsGame;

import javafx.geometry.Point2D;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

import java.util.Random;

import static asteroidsGame.Main.stageHeight;
import static asteroidsGame.Main.stageWidth;

public class Asteroid extends Rectangle{
    public double speed;
    private Point2D movement; // the current movement vector of the player's ship
    public double rotation;
    public int asteroidX;
    public int asteroidY;
    public double size;
    public Polygon asteroid; // the shape of the asteroid

    public Asteroid(double size, double speed, int x, int y) {
        Random rnd = new Random();
        this.speed = speed;
        this.size = size;
        this.rotation = 3;
        this.asteroidX = x;
        this.asteroidY = y;
        this.rotation = rnd.nextDouble() * 360; // assign a random rotation angle between 0 and 360 degrees
        RandomAsteroidGenerator generator = new RandomAsteroidGenerator();
        this.asteroid = generator.createAsteroid(size);
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
        this.speed += 0.15;
        return this.speed;
    }

    public double increaseRotationOnDestruction(){
        this.rotation *= 3;
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
            this.asteroid.setTranslateX(stageWidth);
        }
        if (this.asteroid.getTranslateX() > stageWidth) {
            this.asteroid.setTranslateX(0);
        }
        if (this.asteroid.getTranslateY() < 0) {
            this.asteroid.setTranslateY(stageHeight);
        }
        if (this.asteroid.getTranslateY() > stageHeight) {
            this.asteroid.setTranslateY(0);
        }
    }
    public boolean collide(Bullet bullet) {
        Shape collisionArea = Shape.intersect(this.asteroid, bullet.getHitbox());
        return collisionArea.getBoundsInLocal().getWidth() != -1;
    }
}