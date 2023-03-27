package asteroidsGame;

import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class MainMenu {

    MainMenu(Stage s) {
        int width = (int) Screen.getPrimary().getBounds().getWidth();
        int height = (int) Screen.getPrimary().getBounds().getHeight();

        // create a stack pane
        Pane r = new Pane();
        Pane playGamePane = new Pane();
        Pane highScoresPane = new Pane();

        playGamePane.getChildren().add(new Label("playGame"));
        highScoresPane.getChildren().add(new Label("highScores"));

        // create a label
        Label gameName = new Label("ASTROIDS");
        gameName.setFont(Font.font("Lucida Sans Unicode", FontWeight.BOLD, 150));
        gameName.setTextFill(Color.WHITE);

        r.getChildren().add(gameName);
        r.setStyle("-fx-background-color: black");

        // create scenes
        Scene mainPageScene = new Scene(r, width, height);
        Scene palyGameScene = new Scene(playGamePane, width, height);
        Scene highScoresScene = new Scene(highScoresPane, width, height);

        // create buttons
        javafx.scene.control.Button[] buttons = generateButtons(mainPageScene);
        javafx.scene.control.Button playGame = buttons[0];
        javafx.scene.control.Button highScores = buttons[1];


        playGame.setOnAction(e -> {
            s.setScene(palyGameScene);
        });

        highScores.setOnAction(e -> {
            s.setScene(highScoresScene);
        });

        r.getChildren().addAll(buttons);

        // set the scene
        s.setScene(mainPageScene);

        playGame.setLayoutX(width / 2d);
        highScores.setLayoutX(width / 2d);
        gameName.setLayoutX(width / 2d);


        playGame.setLayoutY(height / 2f + 100);
        highScores.setLayoutY(height / 2f + 180);
        gameName.setLayoutY(height / 2f - 100);

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

        s.show();
    }

    private javafx.scene.control.Button[] generateButtons(Scene sc) {
        // create a button
        javafx.scene.control.Button playGame = new javafx.scene.control.Button("Play Game");
        javafx.scene.control.Button highScores = new javafx.scene.control.Button("High Scores");

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
                sc.setCursor(Cursor.DEFAULT); //Change cursor to crosshair
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
                sc.setCursor(Cursor.DEFAULT); //Change cursor to crosshair
            }
        });

        return new javafx.scene.control.Button[]{playGame, highScores};
    }
}

