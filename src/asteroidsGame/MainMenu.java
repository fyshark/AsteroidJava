package asteroidsGame;

import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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

//import javafx.scene.control.Button;

public class MainMenu {
   Scene mainPageScene;


   MainMenu(Stage primaryStage, Scene gameScene) {
       int width = (int) Screen.getPrimary().getBounds().getWidth();
       int height = (int) Screen.getPrimary().getBounds().getHeight();


       // create a stack pane
       Pane r = new Pane();
       Pane playGamePane = new Pane();
       Pane highScoresPane = new Pane();
       Pane InputnamePane=new Pane();
       Pane ControlsPane=new Pane();
       Button backToPause = new Button("Back to Main Menu");
       backToPause.setOnAction(e -> primaryStage.setScene(mainPageScene));


       playGamePane.getChildren().addAll(new Label("playGame"), backToPause);
       highScoresPane.getChildren().add(new Label("highScores"));
       InputnamePane.getChildren().add(new Label("Player"));
       ControlsPane.getChildren().add(new Label("Controls"));


       // create a label
       Label gameName = new Label("ASTROIDS");
       gameName.setFont(Font.font("Lucida Sans Unicode", FontWeight.BOLD, 150));
       gameName.setTextFill(Color.WHITE);


       r.getChildren().add(gameName);
       r.setStyle("-fx-background-color: black");

       // create scenes
       mainPageScene = new Scene(r, width, height);
       //Scene playGameScene = new Scene(playGamePane, width, height);
       //Scene highScoresScene = new Scene(highScoresPane, width, height);
       // create buttons
       Button[] buttons = generateButtons(mainPageScene);
       Button playGame = buttons[0];
       Button highScores = buttons[1];
       Button InputName=buttons[2];
       Button Controls=buttons[3];

        // Create the VBox layout container just to center everything
       VBox InputNames = new VBox(10);

       //Cannot use Scanner as they don't work with JavaFx.So this is the javafx type of scanner object
       TextField name = new TextField();
       name.setText("Players name");
       name.setPrefHeight(25);
       name.setPrefWidth(50);
       name.setEditable(true);
       //This creates a button to submit the name to the leaderboard
       Button submitbutton=new Button("Submit");

       InputNames.getChildren().addAll(name,submitbutton,backToPause);
       InputNames.setAlignment(Pos.CENTER); // Center the VBox
       InputNames.setStyle("-fx-background-color: black");
       Scene inputname = new Scene(InputNames, width, height);

       // Button leaderboardButton
       //Button leaderboardButton = new Button("Leaderboard");

       VBox leaderContainer = new VBox(10);
       leaderContainer.setSpacing(10); // Set the spacing between buttons

       // Leaderboard Scene
       Label leaderboardTitle = new Label("Leaderboard");
       ListView<String> leaderboardList = new ListView<>();

       ObservableList<String> scoresList = FXCollections.observableArrayList(
               "Player1: 1000",
               "Player2: 900",
               "Player3: 800"
       );
       leaderContainer.getChildren().addAll(leaderboardTitle, leaderboardList, backToPause);
       leaderContainer.setAlignment(Pos.CENTER); // Center the VBox

       //This just adds the name to the leaderboard here
       //if submit button after name is pressed than input it into list.
       leaderboardList.setItems(scoresList);

       InputName.setOnAction(e->{
           String playerName = name.getText();
           scoresList.add(playerName);
           primaryStage.setScene(inputname);
       });

       backToPause.setOnAction(e -> primaryStage.setScene(mainPageScene));

       VBox leaderLayout = new VBox(10);
       leaderLayout.getChildren().addAll(leaderboardTitle, leaderboardList, backToPause);
       //Scene leaderScene = new Scene(leaderLayout, width, height);

       VBox leaderboardLayout = new VBox(10);
       leaderboardLayout.getChildren().addAll(leaderboardTitle, leaderboardList, backToPause);
       Scene leaderboardScene = new Scene(leaderboardLayout, width, height);

       // Create a VBox to hold the control description
       VBox ControlDescription = new VBox();
       ControlDescription.setPadding(new Insets(10, 10, 10, 10));
       ControlDescription.setSpacing(10);
       ControlDescription.setStyle("-fx-background-color: black");

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
       ControlDescription.getChildren().add(paragraph);
       ControlDescription.setAlignment(Pos.CENTER);
       // Create a scene and add the VBox to it
       Scene ControlsScene = new Scene(ControlDescription, 400, 200);

       Controls.setOnAction(e -> {
           primaryStage.setScene(ControlsScene);
       });
       playGame.setOnAction(e -> {
           primaryStage.setScene(gameScene);
       });


       highScores.setOnAction(e -> {
           primaryStage.setScene(leaderboardScene);
       });

       r.getChildren().addAll(buttons);


       // set the scene
       primaryStage.setScene(mainPageScene);

       playGame.setLayoutX(width / 2d);
       highScores.setLayoutX(width / 2d);
       gameName.setLayoutX(width / 2d);
       InputName.setLayoutX(width / 2d);
       Controls.setLayoutX(width / 2d);

       playGame.setLayoutY(height / 2f + 100);
       highScores.setLayoutY(height / 2f + 180);
       gameName.setLayoutY(height / 2f - 100);
       InputName.setLayoutY(height / 2f + 240);
       Controls.setLayoutX(height / 2d +300);

       mainPageScene.widthProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
           playGame.setLayoutX(newValue.doubleValue() / 2 - (playGame.widthProperty().getValue() / 2));
           highScores.setLayoutX(newValue.doubleValue() / 2 - (highScores.widthProperty().getValue() / 2));
           gameName.setLayoutX(newValue.doubleValue() / 2 - (gameName.widthProperty().getValue() / 2));
           InputName.setLayoutX(newValue.doubleValue() / 2 - ( InputName.widthProperty().getValue() / 2));
           Controls.setLayoutX(newValue.doubleValue() / 2 - (Controls.widthProperty().getValue() / 2));
       });
       mainPageScene.heightProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
           playGame.setLayoutY(newValue.doubleValue() / 2 - (playGame.heightProperty().getValue() / 2 - 100));
           highScores.setLayoutY(newValue.doubleValue() / 2 - (highScores.heightProperty().getValue() / 2 - 180));
           gameName.setLayoutY(newValue.doubleValue() / 2 - (gameName.heightProperty().getValue() / 2 + 100));
           InputName.setLayoutY(newValue.doubleValue() / 2 - (InputName.heightProperty().getValue() / 2 - 240));
           Controls.setLayoutY(newValue.doubleValue() / 2 - (Controls.heightProperty().getValue() / 2 - 300));
       });

       primaryStage.show();
   }


   private Button[] generateButtons(Scene sc) {
       // create a button
       Button playGame = new Button("Play Game");
       Button highScores = new Button("High Scores");
       Button inputNames=new Button("Your player");
       Button Controls=new Button("Control Description");

       playGame.setTextFill(Color.WHITE);
       highScores.setTextFill(Color.WHITE);
       inputNames.setTextFill(Color.WHITE);
       Controls.setTextFill(Color.WHITE);

       playGame.setStyle("-fx-focus-color: transparent; -fx-background-color: #000000; -fx-font-size:40");
       highScores.setStyle("-fx-focus-color: transparent; -fx-background-color: #000000; -fx-font-size:40");
       inputNames.setStyle("-fx-focus-color: transparent; -fx-background-color: #000000; -fx-font-size:40");
       Controls.setStyle("-fx-focus-color: transparent; -fx-background-color: #000000; -fx-font-size:40");
       playGame.setOnMouseEntered(new EventHandler() {
           @Override
           public void handle(Event event) {
               sc.setCursor(Cursor.HAND); //Change cursor to hand
           }
       });
       playGame.setStyle("-fx-focus-color: transparent; -fx-background-color: #000000; -fx-font-size:40");
       highScores.setStyle("-fx-focus-color: transparent; -fx-background-color: #000000; -fx-font-size:40");
       inputNames.setStyle("-fx-focus-color: transparent; -fx-background-color: #000000; -fx-font-size:40");

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


       return new Button[]{playGame, highScores,inputNames,Controls};
   }
}


