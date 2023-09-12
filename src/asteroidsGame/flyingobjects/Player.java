package asteroidsGame.flyingobjects;

import asteroidsGame.constants.AppConstants;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.geometry.Point2D;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;
import javafx.util.Duration;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import static asteroidsGame.constants.AppConstants.STAGE_HEIGHT;
import static asteroidsGame.constants.AppConstants.STAGE_WIDTH;


public class Player extends BaseShip {

    private static Polygon playerPolygon;
    private boolean isInvincible = false;
    boolean isAlive;
    private int lives = 3;

    public Player() {
        super(createPlayerPolygon(), (int) (STAGE_WIDTH / 2), (int) (STAGE_HEIGHT / 2));
        isAlive = true;
    }

    private static Polygon createPlayerPolygon() {
        // create the polygon shape for the player's ship
        playerPolygon = new Polygon(-10, -10, 20, 0, -10, 10);
        playerPolygon.setFill(AppConstants.AppColor.BACKGROUND.getColor());
        playerPolygon.setRotate(-90);

        // set the border and border color
        playerPolygon.setStroke(AppConstants.AppColor.SHAPE.getColor());
        playerPolygon.setStrokeWidth(2);

        return playerPolygon;
    }

    public int getLives() {
        return lives;
    }

    public String getHearts() {
        String hearts = "\u2764"; // Unicode value for the heart emoji
        String livesHearts = hearts.repeat(lives); // Repeat the heart emoji for the number of lives
        return livesHearts; // Return the String value of hearts
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public void accelerate() {
        // accelerates the player's ship in the direction it is facing
        double acceleration = 0.5; // the rate of acceleration
        double maxSpeed = 10.0; // the maximum speed the ship can travel
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

    public void move() {
        // moves the player's ship based on its current movement vector
        this.ship.setTranslateX(this.ship.getTranslateX() + this.movement.getX());
        this.ship.setTranslateY(this.ship.getTranslateY() + this.movement.getY());
        screenBounds();
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

    // Defined as responsible for handling crashes between the player's ship and asteroids
    // If the ship crashes with an asteroid while not invincible
    // the player's lives are decremented and invincibility is set for a few seconds to prevent further crashes
    public boolean playerCrash(Asteroid asteroid) {
        boolean isCrash = crash(asteroid);
        if (isCrash && !isInvincible) {
            isAlive = false;
            lives -= 1;
            setInvincibilityTimer(2); // Make the player invincible and flash for 3 seconds
            if (lives < 0) {
                lives = 0;
            }
        }
        return isCrash;
    }

    //defines a collision between a player and a bullet
    public boolean playerCollide(Bullet bullet) {
        boolean isCollide = collide(bullet);

        if (isCollide && !isInvincible && bullet.shooter == "alienBullet") {
            isAlive = false;
            lives -= 1;
            setInvincibilityTimer(2); // Make the player invincible and flash for 3 seconds
            if (lives < 0) {
                lives = 0;
            }
        }
        return isCollide;
    }

    // Set a duration for the invincibility timer of a player's ship.
    // When the timer is active, the ship becomes invincible and flashes for a set duration
    // after which it becomes vulnerable again.
    public void setInvincibilityTimer(double duration) {
        isInvincible = true;

        // Create a Timeline for the flashing effect
        // The flashing effect is achieved using a Timeline object with alternative visible and invisible states defined by KeyFrames
        final int flashCount = 3;
        Timeline flashTimeline = new Timeline();
        flashTimeline.setCycleCount(flashCount * 2);

        // Create alternating keyframes for visible and invisible states
        for (int i = 0; i < flashCount; i++) {
            flashTimeline.getKeyFrames().addAll(
                    new KeyFrame(Duration.seconds(i * duration / flashCount), event -> ship.setOpacity(0.3)),
                    new KeyFrame(Duration.seconds((i + 0.5) * duration / flashCount), event -> ship.setOpacity(1))
            );
        }

        // Start the flashing effect
        flashTimeline.play();

        // Create the invincibility timer
        PauseTransition invincibilityTimer = new PauseTransition(Duration.seconds(duration));
        invincibilityTimer.setOnFinished(event -> {
            isInvincible = false;
            ship.setOpacity(1); // Ensure player opacity is reset to 1 when invincibility ends
            flashTimeline.stop(); // Stop the flashing effect
        });

        invincibilityTimer.play();
    }

    public void hyperspace(List<Asteroid> asteroids, List<Bullet> bullets, Alien alien, Boolean alienAdded) {

        double distance;
        Map<Integer, Integer> entityPos = new HashMap<>();
        for (Asteroid asteroid : asteroids) {
            entityPos.put(asteroid.getCurrentAsteroidX(), asteroid.getCurrentAsteroidY());
        }

        for (Bullet bullet : bullets) {
            entityPos.put((int) bullet.getX(), (int) bullet.getY());
        }

        if (alienAdded) {
            entityPos.put((int) alien.getPosition().getX(), (int) alien.getPosition().getY());
        }

        int closestX = (int) (Math.random() * STAGE_WIDTH);
        int closestY = (int) (Math.random() * STAGE_HEIGHT);
        while (entityPos.get(closestX) != null) {
            for (Integer entityKey : entityPos.keySet()) {
                double ac = Math.abs(entityKey - closestX);
                double cb = Math.abs(entityPos.get(entityKey) - closestY);
                distance = Math.hypot(ac, cb);
                if (distance < 150) {
                    closestX = (int) (Math.random() * STAGE_WIDTH);
                    closestY = (int) (Math.random() * STAGE_HEIGHT);
                }
            }
        }

        setInvincibilityTimer(2);
        this.movement = new Point2D(0, 0);
        this.ship.setTranslateX(closestX);
        this.ship.setTranslateY(closestY);
    }

    //defines a collision between a player and an alien
    public boolean crashAlien(Alien other) {
        Shape collisionArea = Shape.intersect(this.ship, other.ship);
        boolean isCollide = collisionArea.getBoundsInLocal().getWidth() != -1;

        if (isCollide && !isInvincible) {
            isAlive = false;
            lives -= 1;
            setInvincibilityTimer(2); // Make the player invincible and flash for 3 seconds
            if (lives < 0) {
                lives = 0;
            }
        }
        return isCollide;
    }

}


