package asteroidsGame.flyingobjects;

import asteroidsGame.constants.AppConstants;
import javafx.geometry.Point2D;
import javafx.scene.shape.Polygon;

public class Alien extends BaseShip {

    private Player player;

    private long lastBulletTime = System.nanoTime() + 1000L * 1000000; // Add a field to store the last bullet time for alien
    private static final long SHOOT_CD = 2500L * 1000000; // 2500 ms

    //tracks last rotation of the alien
    private long lastRotate = System.nanoTime();
    private Point2D alienMovement;

    public Alien(int x, int y) {
        super(createAlienPolygon(), x, y);
        }

        //design of the alien
    private static Polygon createAlienPolygon() {
        Polygon polygon = new Polygon();
        polygon.getPoints().addAll(
                30.0, 10.0,
                40.0, 10.0,
                45.0, 20.0,
                65.0, 30.0,
                45.0, 40.0,
                25.0, 40.0,
                5.0, 30.0,
                25.0, 20.0,
                30.0, 10.0,
                40.0, 10.0,
                45.0, 20.0,
                30.0, 20.0,
                5.0, 30.0,
                65.0, 30.0
        );

        polygon.setFill(AppConstants.AppColor.BACKGROUND.getColor());
        polygon.setStroke(AppConstants.AppColor.SHAPE.getColor());
        polygon.setStrokeWidth(2);
        return polygon;
    }

    //alien moves function
    public void move() {

        int ALIEN_SPEED = 1;

        long currentTime = System.nanoTime();
        //alien changes direction every 8 seconds
        if(currentTime - lastRotate > 8000L * 1000000) {
            this.ship.setRotate(Math.random() * 360);
            lastRotate = System.nanoTime();
        }

        // moves the alien based on its current movement vector
        double dx = Math.cos(Math.toRadians(this.ship.getRotate())) * ALIEN_SPEED;
        double dy = Math.sin(Math.toRadians(this.ship.getRotate())) * ALIEN_SPEED;

        this.alienMovement = new Point2D(dx, dy);

        this.ship.setTranslateX(this.ship.getTranslateX() + dx);
        this.ship.setTranslateY(this.ship.getTranslateY() + dy);
        screenBounds();
    }

    //how the alien shoots the player
    public Bullet shoot(Player player, String shooter) {
        this.player = player;

        long currentTime = System.nanoTime();
        if (currentTime - lastBulletTime < SHOOT_CD) {
            return null; // If the CD is not enough, don't create a bullet
        }

        lastBulletTime = currentTime;

        //    Get the position and direction of the bullet
        //    Adjusting the position of bullets fired from the tip of the ship
        double bulletX = this.ship.getTranslateX() + 20d;
        double bulletY = this.ship.getTranslateY() + 20d;

        // calculate the direction vector from the alien to the player
        Point2D direction = player.getPosition().subtract(getPosition()).normalize();
        // set the movement vector to the direction vector
        double bulletDirection = Math.toDegrees(Math.atan2(direction.getY(), direction.getX()));

        return new Bullet(bulletX, bulletY, bulletDirection, alienMovement, shooter);
    }



}
