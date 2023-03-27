package asteroidsGame;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.layout.*;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;

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

//Pause Scene
        Label pauseSceneTitle = new Label("Pause Menu");

        //Will have to make each of these scenes
        Button resume = new Button("Resume");
        Button mainMenu = new Button("Main Menu");
        Button closeGame = new Button("Close Game");

        resume.setOnAction(e -> primaryStage.setScene(gameScene));



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

        gridPauseScene.getChildren().addAll(pauseSceneTitle, resume, mainMenu, closeGame, leaderboardButton);
        pauseScene = new Scene(gridPauseScene, stageWidth, stageHeight);

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
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}