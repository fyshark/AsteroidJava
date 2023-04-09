package asteroidsGame;


import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.Node;
import javafx.scene.layout.Region;


public class MainMenu {
   Scene mainPageScene;


   MainMenu(Stage primaryStage, Scene gameScene) {
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
       gameName.setTextFill(Color.WHITE);

        r.getChildren().add(gameName);
        r.setStyle("-fx-background-color: black");

        // create buttons
        Button[] buttons = generateButtons(mainPageScene);
        Button playGame = buttons[0];
        Button highScores = buttons[1];

        // Leaderboard Scene
        Label leaderboardTitle = new Label("Leaderboard");
        ListView<String> leaderboardList = new ListView<>();
        ObservableList<String> scoresList = FXCollections.observableArrayList(
                "Player1: 1000",
                "Player2: 900",
                "Player3: 800"
        );
        leaderboardList.setItems(scoresList);
        backToPause = new Button("Back to Main Menu");
        backToPause.setOnAction(e -> primaryStage.setScene(mainPageScene));

       VBox leaderboardLayout = new VBox(10);
       leaderboardLayout.getChildren().addAll(leaderboardTitle, leaderboardList, backToPause);
       Scene leaderboardScene = new Scene(leaderboardLayout, width, height);

       r.getChildren().addAll(buttons);

       playGame.setOnAction(e -> {
            primaryStage.setScene(gameScene);

            //The game starts with BGM
            new AePlayWave("src/BGM.wav").start();
        });

       highScores.setOnAction(e -> {
           primaryStage.setScene(leaderboardScene);
       });

// set the scene
        primaryStage.setScene(mainPageScene);

        playGame.setLayoutY(height / 2f + 100);
        highScores.setLayoutY(height / 2f + 180);
        gameName.setLayoutY(height / 2f - 100);

        centerElements(width, gameName, playGame, highScores);


        mainPageScene.widthProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            playGame.setLayoutX(newValue.doubleValue() / 2 - (playGame.widthProperty().getValue() / 2));
            highScores.setLayoutX(newValue.doubleValue() / 2 - (highScores.widthProperty().getValue() / 2));
            gameName.setLayoutX(newValue.doubleValue() / 2 - (gameName.widthProperty().getValue() / 2));
        });
        mainPageScene.heightProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            playGame.setLayoutY(newValue.doubleValue() / 2 - (playGame.heightProperty().getValue() / 2 - 100));
            highScores.setLayoutY(newValue.doubleValue() / 2 - (highScores.heightProperty().getValue() / 2 - 180));
            gameName.setLayoutY(newValue.doubleValue() / 2 - (gameName.heightProperty().getValue() / 2 + 100));
        });

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

       playGame.setTextFill(Color.WHITE);
       highScores.setTextFill(Color.WHITE);

       playGame.setStyle("-fx-focus-color: transparent; -fx-background-color: #000000; -fx-font-size:40");
       highScores.setStyle("-fx-focus-color: transparent; -fx-background-color: #000000; -fx-font-size:40");


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


