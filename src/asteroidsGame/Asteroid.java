package asteroidsGame;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
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
        this.asteroid.setFill(Color.WHITE);
        this.asteroid.setTranslateX(x);
        this.asteroid.setTranslateY(y);
    }

    public Polygon getAsteroid() {

        return asteroid; // returns the shape of the asteroid to the scene we call it on
    }

    public double getSize() {

        return this.size;
    }
    public void split() {
        if (this.size > 10) { // only split if the asteroid is large enough
            Random rnd = new Random();
            double newSize = this.size / 3;
            double newX1 = this.asteroid.getTranslateX() + rnd.nextDouble() * newSize;
            double newY1 = this.asteroid.getTranslateY() + rnd.nextDouble() * newSize;
            double newX2 = this.asteroid.getTranslateX() - rnd.nextDouble() * newSize;
            double newY2 = this.asteroid.getTranslateY() - rnd.nextDouble() * newSize;

            Asteroid asteroid1 = new Asteroid(newSize, this.speed, (int) newX1, (int) newY1);
            Asteroid asteroid2 = new Asteroid(newSize, this.speed, (int) newX2, (int) newY2);

            asteroid1.getAsteroid().setRotate(rnd.nextDouble() * 360);
            asteroid2.getAsteroid().setRotate(rnd.nextDouble() * 360);

            // add the new asteroids to the game's list of asteroids
        }
    }

    public double increaseSpeedOnDestruction() {
        this.speed += 0.25;
        return this.speed;
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