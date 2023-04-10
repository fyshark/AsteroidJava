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
        Shape collisionArea = Shape.intersect(this.ship, asteroid.getAsteroid());
        boolean isCrash = collisionArea.getBoundsInLocal().getWidth() != -1;
        if (isCrash) {
            isAlive = false;
        }
        return isCrash;
    }

    //defines a collision between a player and a bullet
    public boolean collide(Bullet bullet) {
        Shape collisionArea = Shape.intersect(this.ship, bullet.getHitbox());
        return collisionArea.getBoundsInLocal().getWidth() != -1;
    }

    public ArrayList<Line> splitPlayerPolygon() {
        // Get the coordinates of the vertices of the polygon
        double[] points = playerPolygon.getPoints().stream().mapToDouble(Double::doubleValue).toArray();
        double x1 = points[0] + playerPolygon.getTranslateX(), y1 = points[1] + playerPolygon.getTranslateY();
        double x2 = points[2] + playerPolygon.getTranslateX(), y2 = points[3] + playerPolygon.getTranslateY();
        double x3 = points[4] + playerPolygon.getTranslateX(), y3 = points[5] + playerPolygon.getTranslateY();

        // Create three lines that correspond to the sides of the triangle
        Line line1 = new Line(x1, y1, x2, y2);
        Line line2 = new Line(x2, y2, x3, y3);
        Line line3 = new Line(x3, y3, x1, y1);

        // Set the color and stroke width of the lines
        line1.setStroke(Color.WHITE);
        line2.setStroke(Color.WHITE);
        line3.setStroke(Color.WHITE);
        line1.setStrokeWidth(2);
        line2.setStrokeWidth(2);
        line3.setStrokeWidth(2);

        ArrayList lineslist = new ArrayList() {
            {
                add(line1);
                add(line2);
                add(line3);
            }
        };
        decomposeTriangle(lineslist);
        return lineslist;
    }

    private void decomposeTriangle(ArrayList<Line> lineslist) {
        Line line1 = lineslist.get(0);
        Line line2 = lineslist.get(1);
        Line line3 = lineslist.get(2);

        // Create a Timeline to animate the movement of the lines
        Random random = new Random();
        double durationInSeconds = 0.1;
        Timeline timeline = new Timeline();

        for (int i = 0; i < 30; i++) {
            double offsetX1 = random.nextDouble() * 20 - 10;
            double offsetY1 = random.nextDouble() * 20 - 10;
            double offsetX2 = random.nextDouble() * 20 - 10;
            double offsetY2 = random.nextDouble() * 20 - 10;
            double offsetX3 = random.nextDouble() * 20 - 10;
            double offsetY3 = random.nextDouble() * 20 - 10;

            Duration keyFrameDuration = Duration.seconds(durationInSeconds / 30.0);
            KeyFrame kf1 = new KeyFrame(keyFrameDuration.multiply(i), event -> {
                line1.setStartX(line1.getStartX() + offsetX1);
                line1.setStartY(line1.getStartY() + offsetY1);
                line1.setEndX(line1.getEndX() + offsetX1);
                line1.setEndY(line1.getEndY() + offsetY1);

                line2.setStartX(line2.getStartX() + offsetX2);
                line2.setStartY(line2.getStartY() + offsetY2);
                line2.setEndX(line2.getEndX() + offsetX2);
                line2.setEndY(line2.getEndY() + offsetY2);

                line3.setStartX(line3.getStartX() + offsetX3);
                line3.setStartY(line3.getStartY() + offsetY3);
                line3.setEndX(line3.getEndX() + offsetX3);
                line3.setEndY(line3.getEndY() + offsetY3);
            });
            timeline.getKeyFrames().add(kf1);
        }

        timeline.play();
        timeline.setOnFinished(event -> {
            Main.gamePane.getChildren().removeAll(lineslist);
        });

    }

}


