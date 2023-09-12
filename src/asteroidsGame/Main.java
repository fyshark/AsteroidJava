package asteroidsGame;

import asteroidsGame.controllers.AnimationController;
import asteroidsGame.flyingobjects.Asteroid;
import asteroidsGame.flyingobjects.Player;
import asteroidsGame.scenes.GameOverScene;
import asteroidsGame.scenes.GamePlayScene;
import asteroidsGame.scenes.MainMenuScene;
import asteroidsGame.scenes.PauseScene;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.stage.Stage;

import static asteroidsGame.constants.AppConstants.STAGE_HEIGHT;
import static asteroidsGame.constants.AppConstants.STAGE_WIDTH;
import static asteroidsGame.scenes.GamePlayScene.levels;
import static asteroidsGame.scenes.MainMenuScene.mainPageScene;

public class Main extends Application {
    public static AnimationController timer;
    public static PauseScene pauseScene;

    // entry point of the game
    @Override
    public void start(Stage primaryStage) {
        initPrimaryStage(primaryStage);
        Player player = new Player();
        Asteroid.initAsteroids(levels.get());
        GamePlayScene gamePlayScene = new GamePlayScene(player, primaryStage);
        GameOverScene gameOverScene = new GameOverScene(player, primaryStage);

        timer = new AnimationController(player, primaryStage, gameOverScene);
        pauseScene = new PauseScene(player, primaryStage, gamePlayScene);
        new MainMenuScene(primaryStage, gamePlayScene, timer);
        primaryStage.setScene(mainPageScene);
    }

    // initialise the primary stage
    private void initPrimaryStage(Stage primaryStage) {
        Rectangle2D screenSize = Screen.getPrimary().getBounds();
        STAGE_WIDTH = screenSize.getWidth();
        STAGE_HEIGHT = screenSize.getHeight();
        primaryStage.setTitle("Group 10 Asteroids Game");
        primaryStage.setWidth(STAGE_WIDTH);
        primaryStage.setHeight(STAGE_HEIGHT);
        //These are listeners. Depend on if its resized of not
        primaryStage.widthProperty().addListener((obs, oldVal, newVal) -> {
            STAGE_WIDTH = (double) newVal;
        });

        primaryStage.heightProperty().addListener((obs, oldVal, newVal) -> {
            STAGE_HEIGHT = (double) newVal;
        });

        primaryStage.setOnCloseRequest(event -> {
            Platform.exit(); // Stop the JavaFX application
            System.exit(0); // Stop the entire application
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
