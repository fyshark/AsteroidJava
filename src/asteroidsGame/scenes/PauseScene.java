package asteroidsGame.scenes;

import asteroidsGame.constants.AppConstants;
import asteroidsGame.controllers.AnimationController;
import asteroidsGame.flyingobjects.Asteroid;
import asteroidsGame.flyingobjects.Player;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import static asteroidsGame.Main.timer;
import static asteroidsGame.flyingobjects.Alien.removeAliens;
import static asteroidsGame.scenes.GamePlayScene.*;
import static asteroidsGame.scenes.MainMenuScene.mainPageScene;

public class PauseScene extends Scene {

    public static Pane pausePane = new Pane();
    private static Label pauseSceneTit;
    Button resume = new Button("Resume");
    Button mainMenu = new Button("Main Menu");
    Button closeGame = new Button("Close Game");
    Button restartGame = new Button("Restart Game");
    VBox buttonContainer;

    public PauseScene(Player player, Stage primaryStage, GamePlayScene gamePlayScene) {
        super(pausePane);
        initPane();
        pauseSceneTit = new Label("Pause Menu");
        pauseSceneTit.setFont(AppConstants.AppFont.LABEL_FONT.getFont());
        pauseSceneTit.setTextFill(AppConstants.AppColor.SHAPE.getColor());
        pausePane.setStyle(AppConstants.ButtonStyle.BACKGROUND.getStyle());
        initButtons(player, primaryStage, gamePlayScene);
        pausePane.getChildren().addAll(buttonContainer);
    }

    private void initButtons(Player player, Stage primaryStage, GamePlayScene gamePlayScene) {
        buttonContainer = new VBox();
        buttonContainer.setSpacing(10); // Set the spacing between buttons
        buttonContainer.setAlignment(Pos.CENTER);
        buttonContainer.getChildren().addAll(pauseSceneTit, mainMenu, resume, closeGame, restartGame);
        for (Node node : buttonContainer.getChildren()) {
            if (node instanceof Button) {
                ((Button) node).setAlignment(Pos.CENTER);
                (node).setStyle(AppConstants.ButtonStyle.BUTTON_NODE.getStyle());

            }
        }
        buttonContainer.layoutBoundsProperty().addListener((obs, oldVal, newVal) -> {
            double x = (pausePane.getWidth() - newVal.getWidth()) / 2;
            double y = (pausePane.getHeight() - newVal.getHeight()) / 2;
            buttonContainer.relocate(x, y);
        });

        closeGame.setOnAction(event -> {
            //This closes the whole game!!! As in exits the whole javafx
            Platform.exit();
            System.exit(0);
        });

        mainMenu.setOnAction(event -> {
            player.resetPosition();
            points.set(0);
            levels.set(1);
            player.setLives(3);
            removeAliens();
            Asteroid.initAsteroids(levels.get());
            timer.stop();
            timerLabel.setText("Time: 0s");
            primaryStage.setScene(mainPageScene);
        });

        // Defines an action to be performed when the "restart" button is pressed
        // The action resets various game elements, such as......
        // and initializes asteroids for the current level before starting a new game
        restartGame.setOnAction(event -> {
            player.resetPosition();
            points.set(0);
            levels.set(1);
            player.setLives(3);
            removeAliens();
            AnimationController.lastAlienDeath = System.nanoTime() + (10000L * 1000000);
            Asteroid.initAsteroids(levels.get());
            primaryStage.setScene(gamePlayScene);
            timer.startWithNewTime();
            timerLabel.setText("Time: 0s");
        });

        resume.setOnAction(e -> {
            timer.start();
            primaryStage.setScene(gamePlayScene);
        });
    }

    private void initPane() {
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
    }


}
