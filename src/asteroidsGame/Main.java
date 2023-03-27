package asteroidsGame;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.input.KeyEvent;
import javafx.stage.Screen;
import javafx.animation.AnimationTimer;
import javafx.scene.control.ListView;

public class Main extends Application {

    Scene gameScene, pauseScene, leaderboardScene;
    double stageWidth, stageHeight;

    @Override
    public void start(Stage primaryStage) {

        primaryStage.setTitle("Group 10 Asteroids Game");
        primaryStage.setMaximized(true);
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
        HBox pauseHBox = new HBox();
        pauseHBox.setPadding(new Insets(10, 10, 10, 10));

        final Pane spacer = new Pane();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        spacer.setMinSize(10, 1);
        pause.setMinSize(Button.USE_PREF_SIZE, Button.USE_PREF_SIZE);
        pauseHBox.getChildren().addAll(spacer, pause);
        pause.setOnAction(e -> primaryStage.setScene(pauseScene));
        gameScene= new Scene(pauseHBox, stageWidth, stageHeight);

        // we create int positions X and Y that we will use to create our ship.
        // when we create a class for ship we call in an x and y position,
        // by default these positions are going to be dead in the center.

        int playerX, playerY;
        playerX = (int) (stageWidth/2);
        playerY = (int)(stageHeight/2);

        // Instantiating a Ship called player that we can manipulate and adding it to the game scene.
        Ship player = new Ship (playerX,playerY);
        spacer.getChildren().add(player.getCharacter());

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

        // Button leaderboardButton
        Button leaderboardButton = new Button("Leaderboard");
        leaderboardButton.setOnAction(e -> primaryStage.setScene(leaderboardScene));

        //Potential option for scene
        GridPane gridPauseScene = new GridPane();
        GridPane.setConstraints(pauseSceneTitle, 0, 0);
        GridPane.setConstraints(resume,0, 1);
        GridPane.setConstraints(mainMenu, 0, 2);
        GridPane.setConstraints(closeGame, 0, 3);
        GridPane.setConstraints(leaderboardButton, 0, 4);
        GridPane.setConstraints(restartGame, 0, 5);
        gridPauseScene.getChildren().addAll(pauseSceneTitle, resume, mainMenu, closeGame);
        pauseScene = new Scene(gridPauseScene, stageWidth, stageHeight);

        mainMenu.setOnAction(e -> {
            new MainMenu(primaryStage);
        });

        // Leaderboard Scene
        Label leaderboardTitle = new Label("Leaderboard");
        ListView<String> leaderboardList = new ListView<>();
        ObservableList<String> scoresList = FXCollections.observableArrayList(
                "Player1: 1000",
                "Player2: 900",
                "Player3: 800"
        );
        leaderboardList.setItems(scoresList);

        Button backToPause = new Button("Back to Pause Menu");
        backToPause.setOnAction(e -> primaryStage.setScene(pauseScene));

        VBox leaderboardLayout = new VBox(10);
        leaderboardLayout.getChildren().addAll(leaderboardTitle, leaderboardList, backToPause);
        leaderboardScene = new Scene(leaderboardLayout, stageWidth, stageHeight);

        //Will have to be changed to main menu when implemented
        primaryStage.setScene(gameScene);

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                player.move();
                // update screen to reflect new position
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
            }
        });

        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}