package asteroidsGame;

import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;
import javafx.util.Duration;

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
        return collisionArea.getBoundsInLocal().getWidth() != -1;
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

}


