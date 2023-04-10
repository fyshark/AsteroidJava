package asteroidsGame;

import javafx.geometry.Point2D;
import javafx.scene.shape.Polygon;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import static java.lang.System.currentTimeMillis;


public class Alien extends BaseShip {

    private Player player;

    private long lastBulletTime; // Add a field to store the last bullet time for alien
    private static final long SHOOT_CD = 2500L * 1000000; // 2500 ms

    private long lastRotate = System.nanoTime();


    public Alien(int x, int y) {
        super(createAlienPolygon(), x, y);
        }

        //design of the alien
    private static Polygon createAlienPolygon() {
        Polygon polygon = new Polygon(
                5.0d, 70.0d, 15.0d, 55.0d, 20.0d,
                25.0d, 25.0d, 20.0d, 55.0d, 15.0d, 70.0d, 5.0d
        );
        polygon.setRotate(45);
        polygon.setFill(Color.WHITE);
        return polygon;
    }

    //alien moves differently than the player, defined here
    public void move() {

        int ALIEN_SPEED = 1;

        long currentTime = System.nanoTime();

        if(currentTime - lastRotate > 3000L * 1000000) {
            this.ship.setRotate(Math.random() * 360);
            lastRotate = System.nanoTime();
        }

        // moves the alien based on its current movement vector
        double dx = Math.cos(Math.toRadians(this.ship.getRotate())) * ALIEN_SPEED;
        double dy = Math.sin(Math.toRadians(this.ship.getRotate())) * ALIEN_SPEED;

        this.ship.setTranslateX(this.ship.getTranslateX() + dx * -1);
        this.ship.setTranslateY(this.ship.getTranslateY() + dy * 0.5);

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
    public Bullet shoot(Player player) {
        this.player = player;

        long currentTime = System.nanoTime();
        if (currentTime - lastBulletTime < SHOOT_CD) {
            return null; // If the CD is not enough, don't create a bullet
        }

        lastBulletTime = currentTime;

        //    Get the position and direction of the bullet
        //    Adjusting the position of bullets fired from the tip of the ship
        double bulletX = this.ship.getTranslateX() + 50d;
        double bulletY = this.ship.getTranslateY() + 50d;

        // calculate the direction vector from the alien to the player
        Point2D direction = player.getPosition().subtract(getPosition()).normalize();
        // set the movement vector to the direction vector
        double bulletDirection = Math.toDegrees(Math.atan2(direction.getY(), direction.getX()));

        return new Bullet(bulletX, bulletY, bulletDirection, "alienBullet");
    }

    //defines how a collision between an alien and an asteroid
    public boolean crash(Asteroid asteroid) {
        Shape collisionArea = Shape.intersect(this.ship, asteroid.getAsteroid());
        return collisionArea.getBoundsInLocal().getWidth() != -1;
    }

}
