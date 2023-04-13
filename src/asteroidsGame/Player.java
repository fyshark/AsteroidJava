package asteroidsGame;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
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

    public Polygon player;

    private static Polygon playerPolygon;

    private double hyperspaceCoolDown = 250 * 1000000;
    private long lastHyperspaceTime = System.nanoTime();
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

    public void hyperspace(List<Asteroid> asteroids, List<Bullet> bullets) {

        double tempClosest;
        Map<Integer, Integer> entityPos = new HashMap<>();
        for (Asteroid asteroid : asteroids) {
            entityPos.put(asteroid.getCurrentAsteroidX(), asteroid.getCurrentAsteroidY());
        }

        for (Bullet bullet : bullets) {
            entityPos.put((int)bullet.getX(), (int)bullet.getY());
        }

        int closestX = (int)(Math.random() * stageWidth);
        int closestY = (int)(Math.random() * stageHeight);
        System.out.println(entityPos);
        System.out.println(closestX);
        while (entityPos.get(closestX) != null) {
            System.out.println("test");
            for (Integer entityKey : entityPos.keySet()) {
                double ac = Math.abs(entityKey - closestX);
                double cb = Math.abs(entityPos.get(entityKey) - closestY);
                tempClosest = Math.hypot(ac, cb);
                if (tempClosest < 150) {
                    closestX = (int)(Math.random() * stageWidth);
                    closestY = (int)(Math.random() * stageHeight);
                }
            }
        }

        this.ship.setTranslateX(closestX);
        this.ship.setTranslateY(closestY);
    }
}


