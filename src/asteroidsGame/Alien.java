package asteroidsGame;

import javafx.geometry.Point2D;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;


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
//        polygon.setRotate(45);
        polygon.setFill(AppConstants.AppColor.BACKGROUND.getColor());
        polygon.setStroke(AppConstants.AppColor.SHAPE.getColor());
        polygon.setStrokeWidth(2);
        return polygon;
    }

    //alien moves differently than the player, defined here
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

    //defines what a collision is for the alien
    public boolean collide(Bullet bullet) {
        Shape collisionArea = Shape.intersect(this.ship, bullet.getHitbox());
        return collisionArea.getBoundsInLocal().getWidth() != -1;
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

        System.out.println(bulletDirection);
        return new Bullet(bulletX, bulletY, bulletDirection, alienMovement, shooter);
    }

    //defines how a collision between an alien and an asteroid
    public boolean crash(Asteroid asteroid) {
        Shape collisionArea = Shape.intersect(this.ship, asteroid.getAsteroid());
        return collisionArea.getBoundsInLocal().getWidth() != -1;
    }

}
