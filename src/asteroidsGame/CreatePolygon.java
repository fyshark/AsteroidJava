package asteroidsGame;


import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.shape.Polygon;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

public class CreatePolygon {
    public static Polygon big;
    public static Polygon medium;

    public void createAsteroids() {
        // Create the big polygon
        big = new Polygon();
        big.getPoints().addAll(new Double[]{
                200.0, 50.0,
                400.0, 50.0,
                450.0, 150.0,
                400.0, 250.0,
                200.0, 250.0,
                150.0, 150.0,
        });
        big.setFill(Color.DARKCYAN);
        big.setStrokeWidth(9.0);
        big.setStroke(Color.DARKSLATEGREY);

        // Create the medium polygon
        medium = new Polygon();
        medium.getPoints().addAll(new Double[]{
                200.0, 50.0,
                400.0, 50.0,
                450.0, 150.0,
                400.0, 250.0,
                200.0, 250.0,
                150.0, 150.0,
        });
        medium.setFill(Color.CHOCOLATE);
        medium.setStrokeWidth(8.0);
        medium.setStroke(Color.BROWN);
    }
}