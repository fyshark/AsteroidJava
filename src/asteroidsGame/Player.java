package asteroidsGame;

import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;
import javafx.util.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Map;
import java.util.HashMap;

import static asteroidsGame.Main.stageHeight;
import static asteroidsGame.Main.stageWidth;

public class Player extends BaseShip {

    private static Polygon playerPolygon;
    private boolean isInvincible = false;
    boolean isAlive;
    Main gamepane;

    public int getLives() {
        return lives;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    private int lives = 3;

    public Player(int x, int y) {
        super(createPlayerPolygon(), x, y);
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

    public boolean crash(Asteroid asteroid) {
        if (asteroid.getSize() < 30) {
            Shape collisionArea = Shape.intersect(this.ship, asteroid.getAsteroid());
            boolean isCrash = collisionArea.getBoundsInLocal().getWidth() != -1;
            if (isCrash) {
                isAlive = true;
            }
            return isCrash;
        } else {
            Shape collisionArea = Shape.intersect(this.ship, asteroid.getAsteroid());
            boolean isCrash = collisionArea.getBoundsInLocal().getWidth() != -1;
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
    }

    //defines a collision between a player and a bullet
    public boolean collide(Bullet bullet) {
        Shape collisionArea = Shape.intersect(this.ship, bullet.getHitbox());
        boolean isCollide = collisionArea.getBoundsInLocal().getWidth() != -1;

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

    public void setInvincibilityTimer(double duration) {
        isInvincible = true;

        // Create a Timeline for the flashing effect
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

        int closestX = (int) (Math.random() * stageWidth);
        int closestY = (int) (Math.random() * stageHeight);
        while (entityPos.get(closestX) != null) {
            for (Integer entityKey : entityPos.keySet()) {
                double ac = Math.abs(entityKey - closestX);
                double cb = Math.abs(entityPos.get(entityKey) - closestY);
                distance = Math.hypot(ac, cb);
                if (distance < 150) {
                    closestX = (int) (Math.random() * stageWidth);
                    closestY = (int) (Math.random() * stageHeight);
                }
            }
        }

        setInvincibilityTimer(1);
        this.ship.setTranslateX(closestX);
        this.ship.setTranslateY(closestY);
    }

    //defines a collision between a player and a bullet
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


