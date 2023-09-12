package asteroidsGame.scenes;


import asteroidsGame.constants.AppConstants;
import asteroidsGame.controllers.AnimationController;
import asteroidsGame.controllers.HighScoreRecorder;
import asteroidsGame.soundeffets.AePlayWave;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import static asteroidsGame.constants.AppConstants.STAGE_HEIGHT;
import static asteroidsGame.constants.AppConstants.STAGE_WIDTH;

public class MainMenuScene {
    public static Scene mainPageScene;
    Button backToPause;
    public MainMenuScene(Stage primaryStage, Scene gameScene, AnimationController timer) {

        // create a stack pane
        Pane mainMenuPane = new Pane();
        Pane playGamePane = new Pane();
        backToPause = new Button("Back to Main Menu");
        backToPause.setOnAction(e -> primaryStage.setScene(mainPageScene));

        playGamePane.getChildren().addAll(new Label("playGame"), backToPause);

        // create scenes
        mainPageScene = new Scene(mainMenuPane, STAGE_WIDTH, STAGE_HEIGHT);

        VBox OpeningPage = new VBox();
        OpeningPage.setSpacing(10);

        // create a label
        Label gameName = new Label("ASTEROIDS");
        gameName.setFont(Font.font("Lucida Sans Unicode", FontWeight.BOLD, 150));
        gameName.setTextFill(AppConstants.AppColor.SHAPE.getColor());

        mainMenuPane.getChildren().add(gameName);
        mainMenuPane.setStyle(AppConstants.ButtonStyle.BACKGROUND.getStyle());

        // create buttons
        Button[] buttons = generateButtons(mainPageScene);
        Button playGame = buttons[0];
        Button highScores = buttons[1];
        Button info = buttons[2];

        // Leaderboard Scene


        Pane ControlsPane = new Pane();
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
                "Press Z to shoot your bullets \n" +
                "Press shift to use hyperspace. \n");
        // Add the label to the VBox
        paragraph.setTextFill(AppConstants.AppColor.SHAPE.getColor());
        Button BackMain = new Button("Back to Main Menu");
        ControlDescription.getChildren().addAll(paragraph, BackMain);
        ControlDescription.setAlignment(Pos.CENTER);
        // Create a scene and add the VBox to it
        Scene ControlsScene = new Scene(ControlDescription, 400, 200);
        BackMain.setOnAction(e -> {
            primaryStage.setScene(mainPageScene);
        });

        info.setOnAction(e -> primaryStage.setScene(ControlsScene));


        mainMenuPane.getChildren().addAll(buttons);

        playGame.setOnAction(e -> {
            primaryStage.setScene(gameScene);

            timer.startWithNewTime();

            AnimationController.lastAlienDeath = System.nanoTime() + (10000L * 1000000);
            //The game starts with BGM
            new AePlayWave("src/start.wav").start();
        });

        highScores.setOnAction(e -> {
            primaryStage.setScene(generateLeaderBoardScene());
        });

        initElementsPosition(gameName, playGame, highScores, info);
        centerElements(gameName, playGame, highScores, info);


        primaryStage.show();
    }

    private Scene generateLeaderBoardScene() {
        Label leaderboardTitle = new Label("Leaderboard");
        TableView leaderBoardTableView = generateLeaderBoardTableView();


        leaderboardTitle.setFont(Font.font("Lucida Sans Unicode", FontWeight.BOLD, 45));
        leaderboardTitle.setTextFill(AppConstants.AppColor.SHAPE.getColor());


        VBox leaderboardLayout = new VBox(10);
        leaderboardLayout.getChildren().addAll(leaderboardTitle, leaderBoardTableView, backToPause);
        leaderboardLayout.setAlignment(Pos.CENTER);
        leaderboardLayout.setStyle(AppConstants.ButtonStyle.BACKGROUND.getStyle());
        return new Scene(leaderboardLayout, STAGE_WIDTH, STAGE_HEIGHT);
    }

    private TableView generateLeaderBoardTableView() {
        TableView leaderboardTable = new TableView();

        TableColumn<HighScoreRecorder.HighScoreEntry, String> column1 = new TableColumn<>("Name");
        column1.setCellValueFactory(new PropertyValueFactory<>("name"));
        column1.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                    setAlignment(Pos.CENTER); // Set alignment to the center
                } else {
                    setText(item);
                    setAlignment(Pos.CENTER); // Set alignment to the center
                }
            }
        });

        TableColumn<HighScoreRecorder.HighScoreEntry, Integer> column2 = new TableColumn<>("Scores");
        column2.setCellValueFactory(new PropertyValueFactory<>("score"));
        column2.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                    setAlignment(Pos.CENTER); // Set alignment to the center
                } else {
                    setText(Integer.toString(item));
                    setAlignment(Pos.CENTER); // Set alignment to the center
                }
            }
        });

        leaderboardTable.getColumns().add(column1);
        leaderboardTable.getColumns().add(column2);

        leaderboardTable.getItems().addAll(HighScoreRecorder.getRecorder().getHighScores());
        leaderboardTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        leaderboardTable.setPrefWidth(50);
        return leaderboardTable;
    }

    // initialise the position of labels
    private void initElementsPosition(Label gameName, Button playGame, Button highScores, Button info) {
        playGame.setLayoutY(STAGE_HEIGHT / 3f + 100);
        highScores.setLayoutY(STAGE_HEIGHT / 3f + 180);
        gameName.setLayoutY(STAGE_HEIGHT / 3f - 130);
        info.setLayoutY(STAGE_HEIGHT / 3f + 250);

        playGame.widthProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue o, Object oldVal,
                                Object newVal) {
                playGame.setLayoutX(STAGE_WIDTH / 2d - (double) newVal / 2d);
            }
        });

        highScores.widthProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue o, Object oldVal,
                                Object newVal) {
                highScores.setLayoutX(STAGE_WIDTH / 2d - (double) newVal / 2d);
            }
        });

        gameName.widthProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue o, Object oldVal,
                                Object newVal) {
                gameName.setLayoutX(STAGE_WIDTH / 2d - (double) newVal / 2d);
            }
        });
        info.widthProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue o, Object oldVal,
                                Object newVal) {
                info.setLayoutX(STAGE_WIDTH / 2d - (double) newVal / 2d);
            }
        });
    }

    // Each element in the scene is positioned using centerElements function to centre and align it
    private void centerElements(Label gameName, Button playGame, Button highScores, Button Info) {

        mainPageScene.widthProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue o, Object oldVal,
                                Object newVal) {
                gameName.setLayoutX((double) newVal / 2d - gameName.getWidth() / 2);
                playGame.setLayoutX((double) newVal / 2d - playGame.getWidth() / 2);
                highScores.setLayoutX((double) newVal / 2d - highScores.getWidth() / 2);
                Info.setLayoutX((double) newVal / 2d - Info.getWidth() / 2);
            }
        });
    }

    // generate several buttons
    // and use event handler to change cursor when hovering over the buttons
    private Button[] generateButtons(Scene sc) {
        // create a button
        Button playGame = new Button("Play Game");
        Button highScores = new Button("High Scores");
        Button Info = new Button("Controls");

        playGame.setTextFill(AppConstants.AppColor.SHAPE.getColor());
        highScores.setTextFill(AppConstants.AppColor.SHAPE.getColor());
        Info.setTextFill(AppConstants.AppColor.SHAPE.getColor());

        playGame.setStyle(AppConstants.ButtonStyle.BUTTON_STYLE.getStyle());
        highScores.setStyle(AppConstants.ButtonStyle.BUTTON_STYLE.getStyle());
        Info.setStyle(AppConstants.ButtonStyle.BUTTON_STYLE.getStyle());


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

        Info.setOnMouseEntered(new EventHandler() {
            @Override
            public void handle(Event event) {
                sc.setCursor(Cursor.HAND); //Change cursor to hand
            }
        });


        Info.setOnMouseExited(new EventHandler() {
            public void handle(Event event) {
                sc.setCursor(Cursor.DEFAULT); //Change cursor to default
            }
        });

        return new Button[]{playGame, highScores, Info};
    }
}


