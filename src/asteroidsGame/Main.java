package asteroidsGame;

import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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

//import java.awt.*;
import java.util.ArrayList;
import java.util.List;
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
    //flag for an alien on the screen
    Boolean alienAdded = true;
//lives
    private int lives=3;

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

        //Game Scene
        Button pause = new Button("Pause");
        pause.setStyle("-fx-background-color: white;");
         //So we are setting it to have a black colour
        gamePane = new Pane();
        gamePane.setStyle("-fx-background-color: black;");
        // Should Set the position of the pause button!
//        pause.setTranslateX(stageWidth - pause.getWidth() - 50); // 20 is the margin from the right edge
//        pause.setTranslateY(20); // 20 is the margin from the top edge
        pause.setMinSize(Button.USE_PREF_SIZE, Button.USE_PREF_SIZE); //Creates the minimum size of the button
        //So this method is used to handle the pause button will call the scene change object
       //This is for the points
        Label pointsLabel = new Label("Points: 0");
        pointsLabel.setFont(Font.font("Lucida Sans Unicode", FontWeight.BOLD, 45));
        pointsLabel.setTextFill(Color.WHITE);

        VBox pointcard=new VBox();
        Region region = new Region();
        HBox.setHgrow(region, javafx.scene.layout.Priority.ALWAYS);
        HBox hBox = new HBox(pointsLabel, region);
        pointcard.getChildren().add(hBox);
        pointcard.setAlignment(Pos.CENTER_LEFT);

        VBox Livescard=new VBox();
        Region region1=new Region();
        HBox.setHgrow(region1, javafx.scene.layout.Priority.ALWAYS);

        Label livesLabel = new Label("Lives: " + lives);
        livesLabel.setFont(Font.font("Lucida Sans Unicode", FontWeight.BOLD, 45));
        livesLabel.setTextFill(Color.WHITE);
        livesLabel.setLayoutX(100);
        livesLabel.setLayoutY(20);
        HBox hBox1 = new HBox(livesLabel, region1);
        Livescard.getChildren().add(hBox1);
        Livescard.setAlignment(Pos.CENTER_LEFT);
        BorderPane borderPane = new BorderPane();
        borderPane.setTop(pointcard);
        borderPane.setBottom(Livescard);

        gamePane.getChildren().addAll(pause,borderPane);
        gameScene = new Scene(gamePane, stageWidth, stageHeight);
        pause.setLayoutX(gamePane.getWidth() - 100);
        pause.setLayoutY(20);

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
        initAstroids(playerX, playerY);
        // Create the VBox layout container just to center everything
        VBox buttonContainer = new VBox();
        //Pause Scene
        Label pauseSceneTit = new Label("Pause Menu");
        pauseSceneTit.setFont(Font.font("Lucida Sans Unicode", FontWeight.BOLD, 100));
        //Will have to make each of these scenes
        Button resume = new Button("Resume");
        Button mainMenu = new Button("Main Menu");
        Button closeGame = new Button("Close Game");
        Button restartName=new Button("Restart with Name");
        Button restartGame = new Button("Restart Game");
        Button controls=new Button("Controls");

        buttonContainer.setSpacing(10); // Set the spacing between buttons
        buttonContainer.setAlignment(Pos.CENTER);
        buttonContainer.getChildren().addAll(pauseSceneTit,mainMenu,resume,closeGame,restartName,controls);

// set the alignment of the VBox and the buttons
        //Center it within the VBbox

        for (Node node : buttonContainer.getChildren()) {
            if (node instanceof Button) {
                ((Button) node).setAlignment(Pos.CENTER);
                ((Button) node).setStyle("-fx-font-size: 16pt; -fx-pref-width: 200px;");
            }
        }

        Pane pausePane = new Pane();
        pausePane.setStyle("-fx-background-color: black;");
        pausePane.getChildren().addAll(buttonContainer);
        pauseSceneTit.setTextFill(Color.WHITE);
        pauseScene=new Scene(pausePane, stageWidth, stageHeight);

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


        //Cannot use Scanner as they don't work with JavaFx.So this is the javafx type of scanner object
        TextField name = new TextField();
        name.setText("Players name");
        name.setPrefHeight(25);
        name.setPrefWidth(50);
        name.setEditable(true);
        //This creates a button to submit the name to the leaderboard
        Button submitbutton=new Button("Submit");


        InputNames.getChildren().addAll(name,submitbutton, restartGame);
        InputNames.setAlignment(Pos.CENTER); // Center the VBox
        InputNames.setStyle("-fx-background-color: black");
        Scene Inputname = new Scene(InputNames,stageWidth, stageHeight);

        resume.setOnAction(e -> primaryStage.setScene(gameScene));
        closeGame.setOnAction(event -> primaryStage.close());
        //This is for restarting with name
        restartName.setOnAction(event->{
            String playerName = name.getText();
            //scoresList.add(playerName);
            primaryStage.setScene(Inputname);
    });
        //This will just restart the game
        restartGame.setOnAction(event ->   {
            player.resetPosition();
            primaryStage.setScene(gameScene);
            primaryStage.show();
        });

        Pane InputnamePane=new Pane();
        Pane ControlsPane=new Pane();
        InputnamePane.getChildren().add(new Label("Player"));
        ControlsPane.getChildren().add(new Label("Controls"));

        VBox ControlDescription = new VBox();
        ControlDescription.setPadding(new Insets(10, 10, 10, 10));
        ControlDescription.setSpacing(10);
        ControlDescription.setStyle("-fx-background-color: black");
        Label header=new Label("Description of Controls");
        header.setTextFill(Color.WHITE);
        Font myFont=new Font("Arial",30);
        header.setFont(myFont);
        header.setUnderline(true);
        ControlDescription.getChildren().add(header);
        // Create a label to hold the paragraph
        Label paragraph = new Label("Find the controls below \n" +
                "Press the left arrows on your computer to turn left.\n" +
                " Press the right arrows on your computer to turn right.\n" +
                " Press the up key to allow the ship to accelerate.\n" +
                "Press the down key to allow your ship to decelerate.\n " +
                "Press Z to shoot your bullets \n");
//       paragraph.setWrapText(true);
        // Add the label to the VBox
        paragraph.setTextFill(Color.WHITE);
        Button BackGame = new Button("Back to Game");
        ControlDescription.getChildren().addAll(paragraph,BackGame);
        ControlDescription.setAlignment(Pos.CENTER);
        // Create a scene and add the VBox to it
        Scene ControlsScene = new Scene(ControlDescription, 400, 200);

        BackGame.setOnAction(e -> primaryStage.setScene(gameScene));

        controls.setOnAction(e -> primaryStage.setScene(ControlsScene));
        pause.setOnAction(e -> primaryStage.setScene(pauseScene));

        mainMenu.setOnAction(e -> new MainMenu(primaryStage,gameScene));

        new MainMenu(primaryStage, gameScene);

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                player.move();
                //alien follows the player around the screen at a slow pace
                alien.followPlayer(player);

                if (player.isAlive) {
                    asteroids.forEach(asteroid -> {
                        asteroid.move();
                        if (player.crash(asteroid)) {

                            gamePane.getChildren().remove(player.getCharacter());
                            gamePane.getChildren().addAll(player.splitPlayerPolygon());
                        }

                        //if alien is on screen and it crashes into an asteroid, it's removed
                        if (alienAdded && alien.crash(asteroid)) {
                            gamePane.getChildren().remove(alien.getCharacter());
                            alienAdded = false;
                        }
                    });
                }

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
                            points.addAndGet(100);
                        }
                    }

                    //if an alien is on screen and it collides with a bullet it is removed
                    if (alienAdded && alien.collide(bullet)) {
                        gamePane.getChildren().removeAll(alien.getCharacter(), bullet);
                        bulletsToRemove.add(bullet);
                        points.addAndGet(500);
                        alienAdded = false;
                    }

                    // WIP if a player collides with a bullet the game is stopped
                    //if (player.collide(bullet)) {
                    //    stop();
                    //}

                }
//
//                if (lives<0){
//                    gameScene.Stoping();
//                    gameScene.Restarting();
//                }

                if (points.get()>10000) {
                lives+=1;
                if (lives>5){
                    lives=5;
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
                pointsLabel.setText("Points: " + points.get());
                livesLabel.setText("Lives: " + lives);
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
                    Bullet bullet = player.shoot();
                    if (bullet != null) {
                        bullets.add(bullet);
                        gamePane.getChildren().add(bullet);
                    }
                    break;
            }
        });

        primaryStage.show();

    }

    private void initAstroids(int playerX, int playerY) {
        for (int i = 0; i < 10; i++) {
            double size = Math.random() * 20 + 60; // random size between 60 and 80
            double speed = Math.random() * 1; // random speed between 1
            int x = (int) (Math.random() * stageWidth + playerX /2);
            int y = (int) (Math.random() * stageHeight + playerY /2);
            Asteroid asteroid = new Asteroid(size, speed, x, y);
            gamePane.getChildren().add(asteroid.getAsteroid());
            asteroids.add(asteroid); // add asteroid to the asteroids array
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
