package asteroidsGame.utils;

import javafx.scene.shape.Polygon;
import java.util.Random;

public class RandomAsteroidGenerator {

    public Polygon createAsteroid(double size) {
        Random rnd = new Random();

        // Generate a random number of sides between 12-20
        int sides = rnd.nextInt(5) + 7;

        // Calculate the angle between each side of the polygon
        double angle = 2 * Math.PI / sides;

        Polygon polygon = new Polygon();
        for (int i = 0; i < sides; i++) {
            // Calculate the x and y coordinates for each point
            double x = size * Math.cos(i * angle);
            double y = size * Math.sin(i * angle);
            polygon.getPoints().addAll(x, y);
        }

        // Randomize the distance of each point from the center of the polygon
        for (int i = 0; i < polygon.getPoints().size(); i++) {
            double change = rnd.nextDouble() * size / 3 * 2 - size / 3;
            polygon.getPoints().set(i, polygon.getPoints().get(i) + change);
        }

        return polygon;
    }
}
