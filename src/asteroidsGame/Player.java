package asteroidsGame;

import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;


public class Player extends BaseShip {

    public Polygon player;

    public Player(int x, int y) {
        super(createPlayerPolygon(), x, y);
    }


    private static Polygon createPlayerPolygon() {
        // create the polygon shape for the player's ship
        Polygon polygon = new Polygon(-10, -10, 20, 0, -10, 10);
        polygon.setFill(Color.WHITE);
        polygon.setRotate(-90);
        return polygon;
    }

    public boolean crash(Asteroid asteroid) {
        Shape collisionArea = Shape.intersect(this.ship, asteroid.getAsteroid());
        return collisionArea.getBoundsInLocal().getWidth() != -1;
    }

    //defines a collision between a player and a bullet
    public boolean collide(Bullet bullet) {
        Shape collisionArea = Shape.intersect(this.ship, bullet.getHitbox());
        return collisionArea.getBoundsInLocal().getWidth() != -1;
    }
}
