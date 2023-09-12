package asteroidsGame.controllers;

import asteroidsGame.flyingobjects.Alien;
import asteroidsGame.flyingobjects.Asteroid;
import asteroidsGame.flyingobjects.Bullet;
import asteroidsGame.flyingobjects.Player;
import asteroidsGame.scenes.GameOverScene;
import asteroidsGame.soundeffets.AePlayWave;
import javafx.animation.AnimationTimer;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

import static asteroidsGame.flyingobjects.Alien.initAliens;
import static asteroidsGame.flyingobjects.Asteroid.asteroids;
import static asteroidsGame.flyingobjects.Bullet.bullets;
import static asteroidsGame.scenes.GamePlayScene.*;

public class AnimationController extends AnimationTimer {

    Player player;
    GameOverScene gameOverScene;
    Stage primaryStage;
    long startTime;
    public static Boolean alienAdded = false;
    public static long lastAlienDeath = System.nanoTime() + (10000L * 1000000);
    public static Alien alien;

    // Handles various game logic during gameplay
    // such as updating the timer, creating asteroids, handling collisions between objects
    // and adding points or extra lives
    public AnimationController(
            Player player,
            Stage primaryStage,
            GameOverScene gameOverScene
    ) {
        this.player = player;
        this.primaryStage = primaryStage;
        this.gameOverScene = gameOverScene;
    }

    // The handle method is called by the JavaFX Application thread on each frame
    // and updates the game state accordingly.
    // 60 frames per min
    // The method has to be overridden by class extending.
    // After that, it will be called in all frame during the time when AnimationTimer is active.
    @Override
    public void handle(long now) {
        long elapsedTime = (now - startTime) / 1_000_000_000;
        if (player.getLives() == 0) {
            gameOverScene.getHScore().setText("You gathered this amount of points "+points.get());
            gameOverScene.getTimeTook().setText("This is how long you survived "+ elapsedTime + "s");
            primaryStage.setScene(gameOverScene);
            this.stop();

        } else {

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
                    new AePlayWave("src/boom.wav").start();
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

                        asteroid1.increaseRotationOnDestruction();
                        asteroid2.increaseRotationOnDestruction();

                        crashedAsteroidsToAdd.add(asteroid1);
                        crashedAsteroidsToAdd.add(asteroid2);

                        // add the new asteroids to the gamePane
                        gamePane.getChildren().addAll(asteroid1.getAsteroid(), asteroid2.getAsteroid());
                    }
                }

                //if alien is on screen and it crashes into an asteroid, it's removed
                if (alienAdded && alien.crash(asteroid)) {
                    new AePlayWave("src/boom.wav").start();
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

                        // add a sound when collide
                        new AePlayWave("src/boom.wav").start();

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

                            asteroid1.increaseRotationOnDestruction();
                            asteroid2.increaseRotationOnDestruction();

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
                    new AePlayWave("src/boom.wav").start();
                    alienAdded = false;
                    gamePane.getChildren().removeAll(alien.getCharacter(), bullet);
                    gamePane.getChildren().addAll(alien.splitBaseShipPolygon());
                    bulletsToRemove.add(bullet);
                    points.addAndGet(500);
                    lastAlienDeath = System.nanoTime();
                }

                //if the player collides with an alien's bullet
                if (player.playerCollide(bullet) && bullet.shooter == "alienBullet" && player.getLives() != 0) {
                    new AePlayWave("src/boom.wav").start();
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
                    new AePlayWave("src/boom.wav").start();
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
                Asteroid.initAsteroids(levels.get());
                levelLabel.setText("Level: " + levels.get());
            }
        }

    }

    // initialize the start time of the game timer and starts the animation when restarting
    // startTime used to reset the current time as startTime
    public void startWithNewTime() {
        startTime = System.nanoTime();
        super.start();
    }
}
