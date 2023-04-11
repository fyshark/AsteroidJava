package asteroidsGame;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;
import javafx.util.Duration;
import java.util.ArrayList;
import java.util.Random;

public class Player extends BaseShip {

    public Polygon player;

    private static Polygon playerPolygon;
    boolean isAlive;
    Main gamepane;
    public Player(int x, int y) {
        super(createPlayerPolygon(), x, y);
        isAlive = true;
    }


    private static Polygon createPlayerPolygon() {
        // create the polygon shape for the player's ship
        playerPolygon = new Polygon(-10, -10, 20, 0, -10, 10);
        playerPolygon.setFill(Color.BLACK);
        playerPolygon.setRotate(-90);

        // set the border and border color
        playerPolygon.setStroke(Color.WHITE);
        playerPolygon.setStrokeWidth(2);

        return playerPolygon;
    }

    public boolean crash(Asteroid asteroid) {
        if (asteroid.getSize() < 30){
            Shape collisionArea = Shape.intersect(this.ship, asteroid.getAsteroid());
            boolean isCrash = collisionArea.getBoundsInLocal().getWidth() != -1;
            if (isCrash) {
                isAlive = true;
            }
            return isCrash;
        } else {
            Shape collisionArea = Shape.intersect(this.ship, asteroid.getAsteroid());
            boolean isCrash = collisionArea.getBoundsInLocal().getWidth() != -1;
            if (isCrash) {
                isAlive = false;
            }
            return isCrash;
        }
    }

    //defines a collision between a player and a bullet
    public boolean collide(Bullet bullet) {
            Shape collisionArea = Shape.intersect(this.ship, bullet.getHitbox());
            return collisionArea.getBoundsInLocal().getWidth() != -1;
    }

}


