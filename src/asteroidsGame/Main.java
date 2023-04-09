package asteroidsGame;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.geometry.Rectangle2D;
import javafx.stage.Stage;
import javafx.scene.input.KeyEvent;
import javafx.stage.Screen;
import javafx.animation.AnimationTimer;
import java.util.ArrayList;
import java.util.List;

public class Main extends Application {

    Scene gameScene, pauseScene;
    static double stageWidth, stageHeight;
    // Add a list of bullets
    private final List<Bullet> bullets = new ArrayList<>();
    private final List<Asteroid> asteroids = new ArrayList<>();
    //flag for an alien on the screen
    Boolean alienAdded = true;

    @Override
    public void start(Stage primaryStage) {

        primaryStage.setTitle("Group 10 Asteroids Game");
        Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();
        stageWidth = screenSize.getWidth();
        stageHeight = screenSize.getHeight();
        primaryStage.setWidth(stageWidth);
        primaryStage.setHeight(stageHeight);

        primaryStage.widthProperty().addListener((obs, oldVal, newVal) -> {
            stageWidth = (double) newVal;
            primaryStage.setWidth(stageWidth);
        });

        primaryStage.heightProperty().addListener((obs, oldVal, newVal) -> {
            stageHeight = (double) newVal;
            primaryStage.setHeight(stageHeight);
        });

        //Game Scene
        Button pause = new Button("Pause");

        Pane gamePane = new Pane();
        gamePane.setStyle("-fx-background-color: black;");
        pause.setMinSize(Button.USE_PREF_SIZE, Button.USE_PREF_SIZE);
        pause.setOnAction(e -> primaryStage.setScene(pauseScene));
        gamePane.getChildren().addAll(pause);
        gameScene= new Scene(gamePane, stageWidth, stageHeight);

        // we create int positions X and Y that we will use to create our ship.
        // when we create a class for ship we call in an x and y position,
        // by default these positions are going to be dead in the center.

        int playerX, playerY;
        playerX = (int)(stageWidth/2);
        playerY = (int)(stageHeight/2);

        // Instantiating a Player called player that we can manipulate and adding it to the game scene.
        Player player = new Player(playerX,playerY);
        gamePane.getChildren().add(player.getCharacter());

        //instantiating an Alien called alien that is added to the game scene
        int alienX = 0;
        int alienY = 0;
        Alien alien = new Alien(alienX, alienY);
        gamePane.getChildren().add(alien.getCharacter());

        // create an instance of Asteroid class
        for (int i = 0; i < 10; i++) {
            double size = Math.random() * 20 + 60; // random size between 60 and 80
            double speed = Math.random() * 1; // random speed between 1
            int x = (int) (Math.random() * stageWidth + playerX/2);
            int y = (int) (Math.random() * stageHeight + playerY/2);
            Asteroid asteroid = new Asteroid(size, speed, x, y);
            gamePane.getChildren().add(asteroid.getAsteroid());
            asteroids.add(asteroid); // add asteroid to the asteroids array
        }

        //Pause Scene
        Label pauseSceneTitle = new Label("Pause Menu");

        //Will have to make each of these scenes
        Button resume = new Button("Resume");
        Button mainMenu = new Button("Main Menu");
        Button closeGame = new Button("Close Game");
        Button restartGame = new Button("Restart Game");

        // Some simple functionality for the buttons
        // resume will return back to the primary scene (gameScene)
        // closeGame will close the application/stage for the game
        // restartGame will restart the application ... not yet built
        // mainMenu will bring you back to the starting screen... not yet built

        resume.setOnAction(e -> primaryStage.setScene(gameScene));
        closeGame.setOnAction(event -> primaryStage.close());
        restartGame.setOnAction(event ->   {
            player.resetPosition();
            primaryStage.setScene(gameScene);
            primaryStage.show();
        });

        //Potential option for scene
        GridPane gridPauseScene = new GridPane();
        GridPane.setConstraints(pauseSceneTitle, 0, 0);
        GridPane.setConstraints(resume,0, 1);
        GridPane.setConstraints(mainMenu, 0, 2);
        GridPane.setConstraints(closeGame, 0, 3);
        GridPane.setConstraints(restartGame, 0, 5);
        gridPauseScene.getChildren().addAll(pauseSceneTitle, resume, mainMenu, closeGame, restartGame);
        pauseScene = new Scene(gridPauseScene, stageWidth, stageHeight);

        mainMenu.setOnAction(e -> new MainMenu(primaryStage,gameScene));

        new MainMenu(primaryStage, gameScene);

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                player.move();
                //alien follows the player around the screen at a slow pace
                alien.followPlayer(player);

                asteroids.forEach(asteroid -> {
                    asteroid.move();
                    if (player.crash(asteroid)) {
                        stop();
                    }

                    //if alien is on screen and it crashes into an asteroid, it's removed
                    if (alienAdded && alien.crash(asteroid)) {
                        gamePane.getChildren().remove(alien.getCharacter());
                        alienAdded = false;
                    }
                });

                // Getting null pointers if we remove the items from the array completely
                // these are temporary arrays used to detect whether a bullet has collided
                // with an asteroid

                List<Asteroid> asteroidsToRemove = new ArrayList<>();
                List<Bullet> bulletsToRemove = new ArrayList<>();

                for (Bullet bullet : bullets) {
                    bullet.move();
                    for (Asteroid asteroid : asteroids) {
                        if (asteroid.collide(bullet)) {
                            gamePane.getChildren().removeAll(asteroid.getAsteroid(), bullet);
                            asteroidsToRemove.add(asteroid);
                            bulletsToRemove.add(bullet);
                        }
                    }

                    //if an alien is on screen and it collides with a bullet it is removed
                    if (alienAdded && alien.collide(bullet)) {
                        gamePane.getChildren().removeAll(alien.getCharacter(), bullet);
                        bulletsToRemove.add(bullet);
                        alienAdded = false;
                    }

                    // WIP if a player collides with a bullet the game is stopped
                    if (player.collide(bullet)) {
                        stop();
                    }

                }

                asteroids.removeAll(asteroidsToRemove);

                bullets.removeIf(bullet -> {
                    if (!bullet.isAlive()) {
                        gamePane.getChildren().remove(bullet);
                        return true;
                    }
                    return false;
                });

                //if there is an alien on screen it will shoot the player
                if (alienAdded) {
                    Bullet bullet = alien.shoot();
                    if (bullet != null) {
                        bullets.add(bullet);
                        gamePane.getChildren().add(bullet);
                    }
                }
            }
        };

        timer.start();

        gameScene.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            switch (event.getCode()) {
                case LEFT:
                    player.turnLeft();
                    break;
                case RIGHT:
                    player.turnRight();
                    break;
                case UP:
                    player.accelerate();
                    break;
                case DOWN:
                    player.decelerate();
                    break;
                case Z: // Update case for z key
                    Bullet bullet = player.shoot("playerBullet");
                    if (bullet != null) {
                        bullets.add(bullet);
                        gamePane.getChildren().add(bullet);
                    }
                    break;
            }
        });

        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}
