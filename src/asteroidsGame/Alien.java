package asteroidsGame;

import javafx.geometry.Point2D;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;

import java.util.Random;

public class Alien extends Ship {

    private Player player;
    private Point2D alien;

    private Point2D movement = new Point2D(1, 1);

    public Alien(int x, int y) {
        super(createAlienPolygon(), x, y);
        }

    private static Polygon createAlienPolygon() {
        Polygon polygon = new Polygon(
                5.0d, 70.0d, 15.0d, 55.0d, 20.0d,
                25.0d, 25.0d, 20.0d, 55.0d, 15.0d, 70.0d, 5.0d
        );
        polygon.setRotate(45);
        polygon.setFill(Color.WHITE);
        return polygon;
    }

    public void move(double angle) {
        int ALIEN_SPEED = 1;
        // moves the player's ship based on its current movement vector
        double dx = Math.cos(Math.toRadians(angle)) * ALIEN_SPEED;
        double dy = Math.sin(Math.toRadians(angle)) * ALIEN_SPEED;

        this.ship.setTranslateX(this.ship.getTranslateX() + dx);
        this.ship.setTranslateY(this.ship.getTranslateY() + dy);

        // The conditions below checks that the ship stays on screen.
        if (this.ship.getTranslateX() < 0) {
            this.ship.setTranslateX(this.ship.getTranslateX() + Main.stageWidth);
        }

        if (this.ship.getTranslateX() > Main.stageWidth) {
            this.ship.setTranslateX(this.ship.getTranslateX() % Main.stageWidth);
        }

        if (this.ship.getTranslateY() < 0) {
            this.ship.setTranslateY(this.ship.getTranslateY() + Main.stageHeight);
        }

        if (this.ship.getTranslateY() > Main.stageHeight) {
            this.ship.setTranslateY(this.ship.getTranslateY() % Main.stageHeight);
        }
    }

    public void followPlayer(Player player) {

        this.player = player;
        // calculate the direction vector from the alien to the player
        Point2D direction = player.getPosition().subtract(getPosition()).normalize();
        // set the movement vector to the direction vector
        double angle = Math.toDegrees(Math.atan2(direction.getY(), direction.getX()));
        move(angle);
    }

    public boolean collide(Bullet bullet) {
        Shape collisionArea = Shape.intersect(this.ship, bullet.getHitbox());
        return collisionArea.getBoundsInLocal().getWidth() != -1;
    }
}
