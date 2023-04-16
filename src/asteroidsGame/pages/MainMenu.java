package asteroidsGame.pages;


import asteroidsGame.controllers.Recorder;
import asteroidsGame.constants.AppConstants;
import asteroidsGame.controllers.AnimationController;
import asteroidsGame.soundeffets.AePlayWave;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.control.Button;

import java.util.List;
import java.util.stream.Collectors;


public class MainMenu {
   Scene mainPageScene;


   public MainMenu(Stage primaryStage, Scene gameScene, AnimationController timer) {
       int width = (int) Screen.getPrimary().getBounds().getWidth();
       int height = (int) Screen.getPrimary().getBounds().getHeight();


       // create a stack pane
       Pane r = new Pane();
       Pane playGamePane = new Pane();
       Pane highScoresPane = new Pane();
       Button backToPause = new Button("Back to Main Menu");
       backToPause.setOnAction(e -> primaryStage.setScene(mainPageScene));

       playGamePane.getChildren().addAll(new Label("playGame"), backToPause);
//       highScoresPane.getChildren().add(new Label("highScores"));

       // create scenes
       mainPageScene = new Scene(r, width, height);
       Scene palyGameScene = new Scene(playGamePane, width, height);
       Scene highScoresScene = new Scene(highScoresPane, width, height);

       VBox OpeningPage = new VBox();
       OpeningPage.setSpacing(10);
       // OpeningPage.setStyle("-fx-background-color: black");
       // create a label
       Label gameName = new Label("ASTROIDS");
       gameName.setFont(Font.font("Lucida Sans Unicode", FontWeight.BOLD, 150));
       gameName.setTextFill(AppConstants.AppColor.SHAPE.getColor());

        r.getChildren().add(gameName);
        r.setStyle(AppConstants.ButtonStyle.BACKGROUND.getStyle());

        // create buttons
        Button[] buttons = generateButtons(mainPageScene);
        Button playGame = buttons[0];
        Button highScores = buttons[1];

       // Load high scores
       Recorder.loadHighScores();

        // Leaderboard Scene
        Label leaderboardTitle = new Label("Leaderboard");
        ListView<String> leaderboardList = new ListView<>();
        //Convert high scores to strings and add them to the ObservableList
       List<String> highScoreStrings = Recorder.getHighScores().stream()
               .map(entry -> String.format("%1$-" + (400 - entry.getName().length()) / 2 + "s%2$s%3$" + (200 - (200 - entry.getName().length()) / 2 - entry.getName().length()) + "s",
                       "", entry.getName(), entry.getScore()))
               .collect(Collectors.toList());

       ObservableList<String> scoresList = FXCollections.observableArrayList(highScoreStrings);

       leaderboardTitle.setFont(Font.font("Lucida Sans Unicode", FontWeight.BOLD, 45));
       leaderboardTitle.setTextFill(AppConstants.AppColor.SHAPE.getColor());
        leaderboardList.setItems(scoresList);
        backToPause = new Button("Back to Main Menu");
        backToPause.setOnAction(e -> primaryStage.setScene(mainPageScene));

       VBox leaderboardLayout = new VBox(10);
       leaderboardLayout.getChildren().addAll(leaderboardTitle, leaderboardList, backToPause);
       leaderboardLayout.setAlignment(Pos.CENTER);
       leaderboardLayout.setStyle(AppConstants.ButtonStyle.BACKGROUND.getStyle());
       Scene leaderboardScene = new Scene(leaderboardLayout, width, height);


       r.getChildren().addAll(buttons);

       playGame.setOnAction(e -> {
            primaryStage.setScene(gameScene);
            timer.startWithNewTime();
            //The game starts with BGM
            //new AePlayWave("src/BGM.wav").start();
            new AePlayWave("src/start.wav").start();
        });

       highScores.setOnAction(e -> {
           primaryStage.setScene(leaderboardScene);
       });

// set the scene
        primaryStage.setScene(mainPageScene);

        playGame.setLayoutY(height / 3f + 100);
        highScores.setLayoutY(height / 3f + 180);
        gameName.setLayoutY(height / 4f - 100);

        centerElements(width, gameName, playGame, highScores);


        primaryStage.show();
    }




    private static void centerElements(int width, Label gameName, Button playGame, Button highScores) {
        gameName.widthProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue o, Object oldVal,
                                Object newVal) {
                gameName.setLayoutX(width / 2d - (double) newVal / 2d);
            }
        });

        playGame.widthProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue o, Object oldVal,
                                Object newVal) {
                playGame.setLayoutX(width / 2d - (double) newVal / 2d);
            }
        });

        highScores.widthProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue o, Object oldVal,
                                Object newVal) {
                highScores.setLayoutX(width / 2d - (double) newVal / 2d);
            }
        });
    }

    private Button[] generateButtons(Scene sc) {
        // create a button
        Button playGame = new Button("Play Game");
        Button highScores = new Button("High Scores");

       playGame.setTextFill(AppConstants.AppColor.SHAPE.getColor());
       highScores.setTextFill(AppConstants.AppColor.SHAPE.getColor());

       playGame.setStyle(AppConstants.ButtonStyle.BUTTON_STYLE.getStyle());
       highScores.setStyle(AppConstants.ButtonStyle.BUTTON_STYLE.getStyle());


       playGame.setOnMouseEntered(new EventHandler() {
           @Override
           public void handle(Event event) {
               sc.setCursor(Cursor.HAND); //Change cursor to hand
           }
       });

       playGame.setOnMouseExited(new EventHandler() {
           public void handle(Event event) {
               sc.setCursor(Cursor.DEFAULT); //Change cursor to default
           }
       });


       highScores.setOnMouseEntered(new EventHandler() {
           @Override
           public void handle(Event event) {
               sc.setCursor(Cursor.HAND); //Change cursor to hand
           }
       });


       highScores.setOnMouseExited(new EventHandler() {
           public void handle(Event event) {
               sc.setCursor(Cursor.DEFAULT); //Change cursor to default
           }
       });


       return new Button[]{playGame, highScores};
   }
}


