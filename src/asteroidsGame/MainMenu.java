package asteroidsGame;


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
       Pane InputnamePane=new Pane();
       Pane ControlsPane=new Pane();
       Button backToPause = new Button("Back to Main Menu");
       backToPause.setOnAction(e -> primaryStage.setScene(mainPageScene));


       playGamePane.getChildren().addAll(new Label("playGame"), backToPause);
       highScoresPane.getChildren().add(new Label("highScores"));
       InputnamePane.getChildren().add(new Label("Player"));
       ControlsPane.getChildren().add(new Label("Controls"));

       VBox OpeningPage = new VBox();
       OpeningPage.setSpacing(10);
      // OpeningPage.setStyle("-fx-background-color: black");
// create a label
       Label gameName = new Label("ASTROIDS");
       gameName.setFont(Font.font("Lucida Sans Unicode", FontWeight.BOLD, 150));
       gameName.setTextFill(Color.WHITE);

// create buttons
       Button[] buttons = generateButtons(mainPageScene);
       Button playGame = buttons[0];
       Button highScores = buttons[1];
       Button inputName = buttons[2];
       Button controls = buttons[3];

// add nodes to the VBox
       OpeningPage.getChildren().addAll(gameName, playGame, highScores, inputName, controls);

// set the alignment of the VBox and the buttons

       for (Node node : OpeningPage.getChildren()) {
           if (node instanceof Button) {
               ((Button)node).setAlignment(Pos.CENTER);
           }
       }
       OpeningPage.setAlignment(Pos.CENTER);
// add the VBox to the root node and create the scene
       r.setStyle("-fx-background-color: black");
       r.getChildren().add(OpeningPage);
       //This is to center the container box in the middle of the Pane.
       //This is not resizable
//       OpeningPage.layoutBoundsProperty().addListener((obs, oldVal, newVal) -> {
//           OpeningPage.setLayoutX((r.getWidth() - newVal.getWidth()) / 2);
//           OpeningPage.setLayoutY((r.getHeight() - newVal.getHeight()) / 2);
//       });
       OpeningPage.layoutBoundsProperty().addListener((obs, oldVal, newVal) -> {
           double x = (r.getWidth() - newVal.getWidth()) / 2;
           double y = (r.getHeight() - newVal.getHeight()) / 2;
           OpeningPage.relocate(x, y);
       });
//This is for it to be resizable
       r.widthProperty().addListener((obs, oldVal, newVal) -> {
           double x = (r.getWidth() - OpeningPage.getLayoutBounds().getWidth()) / 2;
           double y = (r.getHeight() - OpeningPage.getLayoutBounds().getHeight()) / 2;
           OpeningPage.relocate(x, y);
       });
       //This is for resizable to get height
       r.heightProperty().addListener((obs, oldVal, newVal) -> {
           double x = (r.getWidth() - OpeningPage.getLayoutBounds().getWidth()) / 2;
           double y = (r.getHeight() - OpeningPage.getLayoutBounds().getHeight()) / 2;
           OpeningPage.relocate(x, y);
       });

       mainPageScene = new Scene(r, width, height);


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
       Button Backitup = new Button("Back to Main Menu");
       InputNames.getChildren().addAll(name,submitbutton,Backitup);
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
       Button BacktoMain = new Button("Back to Main Menu");
       leaderContainer.getChildren().addAll(leaderboardTitle, leaderboardList,BacktoMain);
       leaderContainer.setAlignment(Pos.CENTER); // Center the VBox

       //This just adds the name to the leaderboard here
       //if submit button after name is pressed than input it into list.
       leaderboardList.setItems(scoresList);
//So this is when someone types their name in the textfield. It will add it to scoreslist
       name.textProperty().addListener((observable, oldValue, newValue) -> {
           if (!newValue.trim().isEmpty()) {
               scoresList.add(newValue);
           }
       });
       inputName.setOnAction(e->{
           primaryStage.setScene(inputname);
       });

       Scene leaderboardScene = new Scene(leaderContainer, width, height);


        // Create a VBox to hold the control description
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
       Button BackMain = new Button("Back to Main Menu");
        ControlDescription.getChildren().addAll(paragraph,BackMain);
        ControlDescription.setAlignment(Pos.CENTER);
        // Create a scene and add the VBox to it
        Scene ControlsScene = new Scene(ControlDescription, 400, 200);

//Setonactions here
       Backitup.setOnAction(e -> primaryStage.setScene(mainPageScene));
       BacktoMain.setOnAction(e -> primaryStage.setScene(mainPageScene));
       BackMain.setOnAction(e -> primaryStage.setScene(mainPageScene));


       playGame.setOnAction(e -> {
            primaryStage.setScene(gameScene);
        });
        controls.setOnAction(e -> primaryStage.setScene(ControlsScene));
        playGame.setOnAction(e ->
                primaryStage.setScene(gameScene));



       highScores.setOnAction(e -> {
           primaryStage.setScene(leaderboardScene);
       });

// set the scene
        primaryStage.setScene(mainPageScene);
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


