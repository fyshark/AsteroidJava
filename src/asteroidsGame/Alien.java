package asteroidsGame;

import javafx.scene.shape.Polygon;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;

import java.util.Random;

public class Alien extends BaseShip {

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
}
