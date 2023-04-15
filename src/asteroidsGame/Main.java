package asteroidsGame;

import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
//import javafx.scene.shape.Polygon;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;
import javafx.scene.input.KeyEvent;
import javafx.stage.Screen;
import javafx.animation.AnimationTimer;
//import javafx.scene.control.ListView;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.control.TextField;
import javafx.application.Platform;
import javafx.scene.control.ToggleButton;
//import java.awt.*;
import java.util.*;
//This is for the points
import java.util.concurrent.atomic.AtomicInteger;

public class Main extends Application {

    //two scene objects being used displayed in primary stage
    Scene gameScene, pauseScene;

    //This gets the stagewidth and height. Change according to screen size
    static double stageWidth, stageHeight;
    static Pane gamePane;
    // Add a list of bullets
    private final List<Bullet> bullets = new ArrayList<>();
    private final List<Asteroid> asteroids = new ArrayList<>();
    //keeps track of when the alien last died
    long lastAlienDeath = System.nanoTime() + (10000L * 1000000);

    //instantiating an Alien called alien that is added to the game scene
    Alien alien;

    // initialising the first level
    public int Level = 1;

    //flag for an alien on the screen
    Boolean alienAdded = false;

//lives
//    private int lives=3;

    private final AtomicInteger points = new AtomicInteger(0);

    @Override
    public void start(Stage primaryStage) {

        primaryStage.setTitle("Group 10 Asteroids Game");
        Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();
        stageWidth = screenSize.getWidth();
        stageHeight = screenSize.getHeight();
        primaryStage.setWidth(stageWidth);
        primaryStage.setHeight(stageHeight);

        //These are listeners. Depend on if its resized of not
        primaryStage.widthProperty().addListener((obs, oldVal, newVal) -> {
            stageWidth = (double) newVal;
            primaryStage.setWidth(stageWidth);
        });

        primaryStage.heightProperty().addListener((obs, oldVal, newVal) -> {
            stageHeight = (double) newVal;
            primaryStage.setHeight(stageHeight);
        });
        //So we are setting it to have a black colour
        gamePane = new Pane();
        gamePane.setStyle(AppConstants.ButtonStyle.BACKGROUND.getStyle());

        int playerX, playerY;
        playerX = (int) (stageWidth / 2);
        playerY = (int) (stageHeight / 2);

        // Instantiating a Player called player that we can manipulate and adding it to the game scene.
        Player player = new Player(playerX, playerY);
        gamePane.getChildren().add(player.getCharacter());

//Game Scene
        ToggleButton pause = new ToggleButton("Pause");
        pause.setStyle(AppConstants.ButtonStyle.BUTTON_BG.getStyle());
        gamePane.setStyle(AppConstants.ButtonStyle.BACKGROUND.getStyle());
       // pause.setSelected(false);
        //pause.setMinSize(Button.USE_PREF_SIZE, Button.USE_PREF_SIZE);
        // Create the pointcard object
        Label pointsLabel = new Label("Points: 0");
        pointsLabel.setFont(AppConstants.AppFont.LABEL_FONT.getFont());
        pointsLabel.setTextFill(AppConstants.AppColor.SHAPE.getColor());

        VBox pointcard = new VBox(pointsLabel);
        pointcard.setAlignment(Pos.CENTER_LEFT);

// Create the Livescard object
        Label livesLabel = new Label("Lives: " + player.getLives());
        livesLabel.setFont(AppConstants.AppFont.LABEL_FONT.getFont());
        livesLabel.setTextFill(AppConstants.AppColor.SHAPE.getColor());

        VBox Livescard = new VBox(livesLabel);
        Livescard.setAlignment(Pos.CENTER_LEFT);

// Create the Levels object
        Label Levels = new Label("Level   " + Level);
        Levels.setFont(AppConstants.AppFont.LABEL_FONT.getFont());
        Levels.setTextFill(AppConstants.AppColor.SHAPE.getColor());

        VBox levelsCard = new VBox(Levels);
        levelsCard.setAlignment(Pos.CENTER_LEFT);

// Stack the objects vertically using a VBox
        VBox vbox = new VBox(pointcard,levelsCard,Livescard);
        vbox.setAlignment(Pos.TOP_LEFT);
        vbox.setSpacing(10);

// Add the VBox to the left-hand side of the screen using a BorderPane
        BorderPane borderPane = new BorderPane();
        borderPane.setLeft(vbox);

// Timer label in VBox
        Label timerLabel = new Label();
        timerLabel.setText("Time: 0s");
        timerLabel.setFont(AppConstants.AppFont.LABEL_FONT.getFont());
        timerLabel.setTextFill(AppConstants.AppColor.SHAPE.getColor());
        timerLabel.setLayoutX(gamePane.getWidth()+600);
        timerLabel.setLayoutY(20);

        gamePane.getChildren().addAll(borderPane,pause,timerLabel);
      primaryStage.setOnShown(event -> {
          pause.toFront();
            pause.setLayoutX(gamePane.getWidth() - 100);
            pause.setLayoutY(20);
        });


        gameScene = new Scene(gamePane, stageWidth, stageHeight);

        // create an instance of Asteroid class
        initAstroids(playerX, playerY, Level);

        // Create the VBox layout container just to center everything
        VBox buttonContainer = new VBox();
        //Pause Scene
        Label pauseSceneTit = new Label("Pause Menu");
        pauseSceneTit.setFont(AppConstants.AppFont.LABEL_FONT.getFont());
        //Will have to make each of these scenes
        Button resume = new Button("Resume");
        Button mainMenu = new Button("Main Menu");
        Button closeGame = new Button("Close Game");
        Button restartName = new Button("Restart");
        Button restartGame = new Button("Restart Game");
        Button controls = new Button("Controls");

        buttonContainer.setSpacing(10); // Set the spacing between buttons
        buttonContainer.setAlignment(Pos.CENTER);
        buttonContainer.getChildren().addAll(pauseSceneTit, mainMenu, resume, closeGame, restartName, controls);

// set the alignment of the VBox and the buttons
        //Center it within the VBbox

        for (Node node : buttonContainer.getChildren()) {
            if (node instanceof Button) {
                ((Button) node).setAlignment(Pos.CENTER);
                ((Button) node).setStyle(AppConstants.ButtonStyle.BUTTON_NODE.getStyle());
            }
        }

        Pane pausePane = new Pane();
        pausePane.setStyle(AppConstants.ButtonStyle.BACKGROUND.getStyle());
        pausePane.getChildren().addAll(buttonContainer);
        pauseSceneTit.setTextFill(AppConstants.AppColor.SHAPE.getColor());
        pauseScene = new Scene(pausePane, stageWidth, stageHeight);

        buttonContainer.layoutBoundsProperty().addListener((obs, oldVal, newVal) -> {
            double x = (pausePane.getWidth() - newVal.getWidth()) / 2;
            double y = (pausePane.getHeight() - newVal.getHeight()) / 2;
            buttonContainer.relocate(x, y);
        });
//This is for it to be resizable
        pausePane.widthProperty().addListener((obs, oldVal, newVal) -> {
            double x = (pausePane.getWidth() - buttonContainer.getLayoutBounds().getWidth()) / 2;
            double y = (pausePane.getHeight() - buttonContainer.getLayoutBounds().getHeight()) / 2;
            buttonContainer.relocate(x, y);
        });
        //This is for resizable to get height
        pausePane.heightProperty().addListener((obs, oldVal, newVal) -> {
            double x = (pausePane.getWidth() - buttonContainer.getLayoutBounds().getWidth()) / 2;
            double y = (pausePane.getHeight() - buttonContainer.getLayoutBounds().getHeight()) / 2;
            buttonContainer.relocate(x, y);
        });

        // Some simple functionality for the buttons
        // resume will return back to the primary scene (gameScene)
        // closeGame will close the application/stage for the game
        // restartGame will restart the application ... not yet built
        // mainMenu will bring you back to the starting screen... not yet built
        VBox InputNames = new VBox(10);
        Label Gameover=new Label("Game Over");
        Font font = Font.font("Lucida Sans Unicode", FontWeight.BOLD, 150);
        Gameover.setFont(font);
        Gameover.setTextFill(AppConstants.AppColor.SHAPE.getColor());


        //Cannot use Scanner as they don't work with JavaFx.So this is the javafx type of scanner object
        TextField name = new TextField();
        name.setText("Players name");
        name.setPrefHeight(25);
        name.setPrefWidth(50);
        name.setEditable(true);
        //This creates a button to submit the name to the leaderboard
        Button submitbutton = new Button("Submit");


        InputNames.getChildren().addAll(Gameover,name, submitbutton, restartGame);
        InputNames.setAlignment(Pos.CENTER); // Center the VBox
        InputNames.setStyle(AppConstants.ButtonStyle.BACKGROUND.getStyle());
        Scene Inputname = new Scene(InputNames, stageWidth, stageHeight);


        primaryStage.setOnCloseRequest(event -> {
            Platform.exit(); // Stop the JavaFX application
            System.exit(0); // Stop the entire application
        });

        closeGame.setOnAction(event -> {
            //This closes the whole game!!! As in like exits the whole javafx
            Platform.exit();
            System.exit(0);
        });
        //This is for restarting with name
        restartName.setOnAction(event -> {
            String playerName = name.getText();
            //scoresList.add(playerName);
            primaryStage.setScene(Inputname);

            //Record and save player scores
            Recorder.addHighScore(playerName, points);
            Recorder.saveHighScores();

        });
        //This will just restart the game
        restartGame.setOnAction(event -> {
            player.resetPosition();
            points.set(0);
            player.setLives(3);
            primaryStage.setScene(gameScene);
            primaryStage.show();
        });
        Pane InputnamePane = new Pane();
        Pane ControlsPane = new Pane();
        InputnamePane.getChildren().add(new Label("Player"));
        ControlsPane.getChildren().add(new Label("Controls"));

        VBox ControlDescription = new VBox();
        ControlDescription.setPadding(new Insets(10, 10, 10, 10));
        ControlDescription.setSpacing(10);
        ControlDescription.setStyle(AppConstants.ButtonStyle.BACKGROUND.getStyle());
        Label header = new Label("Description of Controls");
        header.setTextFill(AppConstants.AppColor.SHAPE.getColor());
        Font myFont = new Font("Arial", 30);
        header.setFont(myFont);
        header.setUnderline(true);
        ControlDescription.getChildren().add(header);
        // Create a label to hold the paragraph
        Label paragraph = new Label("Find the controls below \n" +
                "Press the left arrows on your computer to turn left.\n" +
                "Press the right arrows on your computer to turn right.\n" +
                "Press the up key to allow the ship to accelerate.\n" +
                "Press the down key to allow your ship to decelerate.\n " +
                "Press Z to shoot your bullets \n"+
                "Press shift to use hyperspace. \n");
//       paragraph.setWrapText(true);
        // Add the label to the VBox
        paragraph.setTextFill(AppConstants.AppColor.SHAPE.getColor());
        Button BackGame = new Button("Back to Game");
        ControlDescription.getChildren().addAll(paragraph, BackGame);
        ControlDescription.setAlignment(Pos.CENTER);
        // Create a scene and add the VBox to it
        Scene ControlsScene = new Scene(ControlDescription, 400, 200);

        BackGame.setOnAction(e -> primaryStage.setScene(gameScene));

        controls.setOnAction(e -> primaryStage.setScene(ControlsScene));

        mainMenu.setOnAction(e -> new MainMenu(primaryStage, gameScene));
        long startTime = System.nanoTime();

        new MainMenu(primaryStage, gameScene);
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {

                if(player.getLives() == 0) {
                        String playerName = name.getText();
                        primaryStage.setScene(Inputname);

                        //Record and save player scores
                        Recorder.addHighScore(playerName, points);
                        Recorder.saveHighScores();

                    restartGame.setOnAction(event -> {
                        player.resetPosition();
                        player.setLives(3);
                        points.set(0);
                        primaryStage.setScene(gameScene);
                        primaryStage.show();
                    });

                }
                long elapsedTime = (now - startTime) / 1_000_000_000;

                // update the text of the timerLabel
                timerLabel.setText("Time: " + elapsedTime + "s");

                //creates an alien every 8 seconds
                long currentTime = System.nanoTime();
                if (!alienAdded && ((currentTime - lastAlienDeath) > (10000L * 1000000)) && player.getLives() != 0) {
                    Random random_pos = new Random();
                    int appearWidth = 0;
                    int appearHeight = (int) stageHeight;
                    alien = initAliens(appearWidth, random_pos.nextInt(appearHeight));
                    alienAdded = true;
                }

                player.move();
                //alien follows a random path around the scene
                if (alienAdded && player.getLives() != 0) {
                    alien.move();
                }

                asteroids.forEach(asteroid -> {
                    asteroid.move();
                    if (player.playerCrash(asteroid)) {
                        gamePane.getChildren().removeAll(player.getCharacter());
                        gamePane.getChildren().addAll(player.splitBaseShipPolygon());
                        player.resetPosition();
                        player.setInvincibilityTimer(2);
                        gamePane.getChildren().addAll(player.getCharacter());
                    }

                    //if alien is on screen and it crashes into an asteroid, it's removed
                    if (alienAdded && alien.crash(asteroid)) {
                        lastAlienDeath = System.nanoTime();
                        alienAdded = false;
                        gamePane.getChildren().removeAll(alien.getCharacter());
                        gamePane.getChildren().addAll(alien.splitBaseShipPolygon());
                    }
                });

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
                                Asteroid asteroid1 = new Asteroid((int) newSize, asteroid.increaseSpeedOnDestruction(), asteroid.getCurrentAsteroidX()+30, asteroid.getCurrentAsteroidY()+30);
                                Asteroid asteroid2 = new Asteroid((int) newSize, asteroid.increaseSpeedOnDestruction(), asteroid.getCurrentAsteroidX()-30, asteroid.getCurrentAsteroidY()-30);

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
                livesLabel.setText(player.getHearts());

                if (asteroids.toArray().length == 0){
                    Level += 1;
                    initAstroids(playerX,playerY,Level);
                    Levels.setText("Level" + Level);
                }
            }
        };

        timer.start();

        //Used to store the currently pressed key and is a collection that does not allow duplicate elements
        Set<KeyCode> pressedKeys = new HashSet<>();

        //Adding event filters to game scenes for handling key press events
        gameScene.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            KeyCode code = event.getCode();
            pressedKeys.add(code);

            if (pressedKeys.contains(KeyCode.LEFT)) {
                player.turnLeft();
            }
            if (pressedKeys.contains(KeyCode.RIGHT)) {
                player.turnRight();
            }
            if (pressedKeys.contains(KeyCode.UP)) {
                player.accelerate();
            }
            if (pressedKeys.contains(KeyCode.Z)) {
                Bullet bullet = player.shoot("playerBullet");
                if (bullet != null) {
                    bullets.add(bullet);
                    gamePane.getChildren().add(bullet);
                }
            }
            if (pressedKeys.contains(KeyCode.SHIFT)) {
                player.hyperspace(asteroids, bullets, alien, alienAdded);
            }
        });

        //When a key is released, it is removed from the pressedKeys collection
        gameScene.addEventFilter(KeyEvent.KEY_RELEASED, event -> {
            KeyCode code = event.getCode();
            pressedKeys.remove(code);
        });
        resume.setOnAction(e -> {timer.start();primaryStage.setScene(gameScene);});
        pause.setOnAction(event -> {
            if (pause.isSelected()) {
                primaryStage.setScene(pauseScene);
                timer.stop();
            } else {
                timer.start();
                primaryStage.setScene(gameScene);
            }
        });

        primaryStage.show();
    }

    private void initAstroids(int playerX, int playerY, int level) {
        for (int i = 0; i < level; i++) {
            double size = Math.random() * 20 + 60; // random size between 60 and 80
            double speed = Math.random() * 1; // random speed between 1
            int x = (int) (Math.random() * stageWidth);
            int y = (int) (Math.random() * stageHeight);
            if (x < playerX) {
                x -= 150;
            } else {
                x += 150;
            }
            if (y < playerY) {
                y -= 150;
            } else {
                y += 150;
            }
            Asteroid asteroid = new Asteroid(size, speed, x, y);
            gamePane.getChildren().addAll(asteroid.getAsteroid());
            asteroids.add(asteroid); // add asteroid to the asteroids array
        }
    }

    //factory function for creating aliens
    private Alien initAliens(int alienX, int alienY) {
        Alien alien = new Alien(alienX, alienY);
        gamePane.getChildren().addAll(alien.getCharacter());
        return alien;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
