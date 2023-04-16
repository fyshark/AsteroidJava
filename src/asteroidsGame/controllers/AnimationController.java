package asteroidsGame.controllers;

import asteroidsGame.flyingobjects.Alien;
import asteroidsGame.flyingobjects.Asteroid;
import asteroidsGame.flyingobjects.Bullet;
import asteroidsGame.flyingobjects.Player;
import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static asteroidsGame.Main.initAliens;
import static asteroidsGame.Main.initAstroids;

public class AnimationController extends AnimationTimer {

    Player player;
    AtomicBoolean gameOver;
    TextField name;
    Stage primaryStage;
    Scene inputName;
    long startTime;
    Label timerLabel;
    javafx.scene.control.Label pointsLabel;
    Label levelLabel;
    Label livesLabel;
    int playerX;
    int playerY;
    public static Boolean alienAdded = false;
    AtomicInteger points;
    public static long lastAlienDeath = System.nanoTime() + (10000L * 1000000);
    double stageHeight;
    public static Alien alien;
    private List<Asteroid> asteroids;
    Pane gamePane;
    private List<Bullet> bullets;
    private AtomicInteger levels;

    public AnimationController(
            Player player,
            AtomicBoolean gameOver,
            TextField name,
            Stage primaryStage,
            Scene inputName,
            Label timerLabel,
            Label pointsLabel,
            Label levelLabel,
            Label livesLabel,
            int playerX,
            int playerY,
            AtomicInteger points,
            double stageHeight,
            List<Asteroid> asteroids,
            Pane gamePane,
            List<Bullet> bullets,
            AtomicInteger levels) {
        this.player = player;
        this.gameOver = gameOver;
        this.name = name;
        this.primaryStage = primaryStage;
        this.inputName = inputName;
        this.timerLabel = timerLabel;
        this.pointsLabel = pointsLabel;
        this.levelLabel = levelLabel;
        this.livesLabel = livesLabel;
        this.playerX = playerX;
        this.playerY = playerY;
        this.points = points;
        this.stageHeight = stageHeight;
        this.asteroids = asteroids;
        this.gamePane = gamePane;
        this.bullets = bullets;
        this.levels = levels;
    }

    @Override
    public void handle(long now) {
        if (player.getLives() == 0 && !gameOver.get()) {
            gameOver.set(true);
            String playerName = name.getText();
            primaryStage.setScene(inputName);

            // Record and save player scores
            Recorder.addHighScore(playerName, points);
            Recorder.saveHighScores();

//                    restartGame.setOnAction(event -> {
//                        player.resetPosition();
//                        player.setLives(3);
//                        points.set(0);
//                        primaryStage.setScene(gameScene);
//                        primaryStage.show();
//                        gameOver.set(false);
//                    });

        }
        long elapsedTime = (now - startTime) / 1_000_000_000;

        // update the text of the timerLabel
        timerLabel.setText("Time: " + elapsedTime + "s");

        //creates an alien every 8 seconds
        long currentTime = System.nanoTime();
        if (!alienAdded && ((currentTime - lastAlienDeath) > (10000L * 1000000)) && player.getLives() != 0) {
            alien = initAliens();
            alienAdded = true;
        }

        player.move();
        //alien follows a random path around the scene
        if (alienAdded && player.getLives() != 0) {
            alien.move();
        }

        List<Asteroid> crashedAsteroidsToRemove = new ArrayList<>();
        List<Asteroid> crashedAsteroidsToAdd = new ArrayList<>();

        asteroids.forEach(asteroid -> {
            asteroid.move();
            if (player.playerCrash(asteroid)) {
                gamePane.getChildren().removeAll(player.getCharacter());
                gamePane.getChildren().addAll(player.splitBaseShipPolygon());
                player.resetPosition();
                player.setInvincibilityTimer(2);
                gamePane.getChildren().addAll(player.getCharacter());

                double newSize = asteroid.getSize() / 2;

                crashedAsteroidsToRemove.add(asteroid);
                gamePane.getChildren().removeAll(asteroid.getAsteroid());

                // split the hit asteroid into two smaller asteroids if it's big or medium
                if (asteroid.getSize() >= 30) {

                    Asteroid asteroid1 = new Asteroid((int) newSize, asteroid.increaseSpeedOnDestruction(), asteroid.getCurrentAsteroidX() + 60, asteroid.getCurrentAsteroidY() + 60);
                    Asteroid asteroid2 = new Asteroid((int) newSize, asteroid.increaseSpeedOnDestruction(), asteroid.getCurrentAsteroidX() - 60, asteroid.getCurrentAsteroidY() - 60);

                    crashedAsteroidsToAdd.add(asteroid1);
                    crashedAsteroidsToAdd.add(asteroid2);

                    // add the new asteroids to the gamePane
                    gamePane.getChildren().addAll(asteroid1.getAsteroid(), asteroid2.getAsteroid());
                }
            }

            //if alien is on screen and it crashes into an asteroid, it's removed
            if (alienAdded && alien.crash(asteroid)) {
                lastAlienDeath = System.nanoTime();
                alienAdded = false;
                gamePane.getChildren().removeAll(alien.getCharacter());
                gamePane.getChildren().addAll(alien.splitBaseShipPolygon());
            }
        });

        gamePane.getChildren().removeAll((crashedAsteroidsToRemove));

        // add the new asteroids created from splitting to the main list
        asteroids.addAll(crashedAsteroidsToAdd);

        // remove the asteroids that were hit by a bullet
        asteroids.removeAll(crashedAsteroidsToRemove);

        // Getting null pointers if we remove the items from the array completely
        // these are temporary arrays used to detect whether a bullet has collided
        // with an asteroid

        List<Asteroid> asteroidsToRemove = new ArrayList<>();
        List<Bullet> bulletsToRemove = new ArrayList<>();
        List<Asteroid> asteroidsToAdd = new ArrayList<>();

        for (Bullet bullet : bullets) {
            bullet.move();
            for (Asteroid asteroid : asteroids) {
                if (asteroid.collide(bullet)) {

                    // increase the player's points
                    bulletsToRemove.add(bullet);
                    gamePane.getChildren().removeAll(bulletsToRemove);

                    // remove the hit asteroid from the asteroids list
                    asteroidsToRemove.add(asteroid);

                    // remove the hit asteroid and bullet from the gamePane
                    gamePane.getChildren().removeAll(asteroid.getAsteroid(), bullet);

                    // create two new asteroids
                    double newSize = asteroid.getSize() / 2;

                    // split the hit asteroid into two smaller asteroids if it's big or medium
                    if (asteroid.getSize() >= 30) {

                        if (asteroid.getSize() >= 60) {
                            points.addAndGet(20);
                        } else if (asteroid.getSize() > 30 && asteroid.getSize() < 60) {
                            points.addAndGet(50);
                        }
                        Asteroid asteroid1 = new Asteroid((int) newSize, asteroid.increaseSpeedOnDestruction(), asteroid.getCurrentAsteroidX() + 30, asteroid.getCurrentAsteroidY() + 30);
                        Asteroid asteroid2 = new Asteroid((int) newSize, asteroid.increaseSpeedOnDestruction(), asteroid.getCurrentAsteroidX() - 30, asteroid.getCurrentAsteroidY() - 30);

                        asteroidsToAdd.add(asteroid1);
                        asteroidsToAdd.add(asteroid2);

                        // add the new asteroids to the gamePane
                        gamePane.getChildren().addAll(asteroid1.getAsteroid(), asteroid2.getAsteroid());
                    } else {
                        points.addAndGet(100);
                        asteroidsToRemove.add(asteroid);
                    }
                    break;
                }
            }

            // add the new asteroids created from splitting to the main list
            asteroids.addAll(asteroidsToAdd);

            // remove the asteroids that were hit by a bullet
            asteroids.removeAll(asteroidsToRemove);

            //if there is an alien on screen & it collides with a player's bullet
            if (alienAdded && alien.collide(bullet) && bullet.shooter == "playerBullet" && player.getLives() != 0) {
                alienAdded = false;
                gamePane.getChildren().removeAll(alien.getCharacter(), bullet);
                gamePane.getChildren().addAll(alien.splitBaseShipPolygon());
                bulletsToRemove.add(bullet);
                points.addAndGet(500);
                lastAlienDeath = System.nanoTime();
            }

            //if the player collides with an alien's bullet
            if (player.playerCollide(bullet) && bullet.shooter == "alienBullet" && player.getLives() != 0) {
                lastAlienDeath = System.nanoTime();
                gamePane.getChildren().removeAll(player.getCharacter());
                gamePane.getChildren().addAll(player.splitBaseShipPolygon());
                player.resetPosition();
                player.setInvincibilityTimer(2);
                gamePane.getChildren().addAll(player.getCharacter());
            }
        }
        if (points.get() > 10000) {
            int playerAddLives = player.getLives();
            playerAddLives += 1;
            player.setLives(playerAddLives);
            if (player.getLives() > 5) {
                player.setLives(5);
            }
        }
        asteroids.removeAll(asteroidsToRemove);

        bullets.removeIf(bullet -> {
            if (!bullet.isAlive()) {
                gamePane.getChildren().removeAll(bullet);
                return true;
            }
            return false;
        });

        //if there is an alien on screen it will shoot the player, alien bullet flag
        if (alienAdded && player.getLives() != 0) {
            Bullet bullet = alien.shoot(player, "alienBullet");
            if (bullet != null) {
                bullets.add(bullet);
                gamePane.getChildren().addAll(bullet);
            }
            if (player.crashAlien(alien)) {
                alienAdded = false;
                gamePane.getChildren().removeAll(player.getCharacter());
                gamePane.getChildren().addAll(player.splitBaseShipPolygon());
                gamePane.getChildren().removeAll(alien.getCharacter());
                gamePane.getChildren().addAll(alien.splitBaseShipPolygon());
                player.resetPosition();
                player.setInvincibilityTimer(2);
                gamePane.getChildren().addAll(player.getCharacter());
                lastAlienDeath = System.nanoTime();
            }
        }
        pointsLabel.setText("Points: " + points.get());
        levelLabel.setText("Levels: " + levels.get());
        livesLabel.setText(player.getHearts());

        if (asteroids.toArray().length == 0) {
            levels.addAndGet(1);
            initAstroids(playerX, playerY, levels.get());
            levelLabel.setText("Level: " + levels.get());
        }
    }

    public void startWithNewTime() {
        startTime = System.nanoTime();
        super.start();
    }
}
