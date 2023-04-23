package asteroidsGame.flyingobjects;

import asteroidsGame.constants.AppConstants;
import asteroidsGame.controllers.AnimationController;
import javafx.geometry.Point2D;
import javafx.scene.shape.Polygon;

import java.util.Random;

import static asteroidsGame.constants.AppConstants.STAGE_HEIGHT;
import static asteroidsGame.scenes.GamePlayScene.gamePane;

public class Alien extends BaseShip {

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
        Polygon polygon = new Polygon(
                23.4, 0, 32.4, 32.4,
                0, 23.4, 32.4, 41.1, 0, 41.1,
                32.4, 48.8, 0, 56.5, 32.4, 56.5,
                23.4, 81, 41.1, 56.5, 48.8, 56.5,
                41.1, 48.8, 48.8, 41.1, 41.1, 41.1,
                48.8, 23.4, 41.1, 32.4
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

    // removes alien from pane
    public static void removeAliens() {
        if (AnimationController.alien != null) {
            gamePane.getChildren().remove(AnimationController.alien.getCharacter());
            AnimationController.alien = null;
            AnimationController.alienAdded = false;
        }
    }
    //factory function for creating aliens
    public static Alien initAliens() {
        Random random_pos = new Random();
        int alienX = 0;
        int appearHeight = (int) STAGE_HEIGHT;
        int alienY = random_pos.nextInt(appearHeight);
        Alien alien = new Alien(alienX, alienY);
        gamePane.getChildren().addAll(alien.getCharacter());
        return alien;
    }
}
