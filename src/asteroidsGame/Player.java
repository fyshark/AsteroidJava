package asteroidsGame;

import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;



public class Player extends BaseShip {

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

}