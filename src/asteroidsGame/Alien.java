package asteroidsGame;

import javafx.geometry.Point2D;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;
import java.util.Random;

public class Alien extends Ship {

    private double playerX;
    private double playerY;
    private Polygon alien;

    private Point2D movement; // the current movement vector of the player's ship

    public Alien(int x, int y) {

            super(createAlienPolygon(), x, y);

            Random rnd = new Random();

            int accelerationAmount = 1 + rnd.nextInt(10);
            for (int i = 0; i < accelerationAmount; i++) {
                accelerate();
            }
        }

    private static Polygon createAlienPolygon() {
        Polygon polygon = new Polygon(
                20.0, 0.0,
                30.0, 10.0,
                40.0, 10.0,
                50.0, 20.0,
                50.0, 30.0,
                30.0, 30.0,
                30.0, 40.0,
                20.0, 40.0,
                20.0, 30.0,
                0.0, 30.0,
                0.0, 20.0,
                10.0, 10.0,
                20.0, 10.0
        );
        polygon.setFill(Color.WHITE);
        return polygon;
    }
/*
    @Override
    public void accelerate(double playerX, double playerY) {
        super.accelerate();
        this.playerX = playerX;
        this.playerY = playerY;
        // accelerates the player's ship in the direction it is facing
        double acceleration = 0.18; // the rate of acceleration
        double maxSpeed = 5.0; // the maximum speed the ship can travel
        double changeX = this.playerX * acceleration;
        double changeY = this.playerY * acceleration;
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
    } */
}
