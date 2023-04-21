package asteroidsGame.scenes;

import asteroidsGame.constants.AppConstants;
import asteroidsGame.controllers.AnimationController;
import asteroidsGame.flyingobjects.Bullet;
import asteroidsGame.flyingobjects.Player;
import asteroidsGame.soundeffets.AePlayWave;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import static asteroidsGame.Main.pauseScene;
import static asteroidsGame.Main.timer;
import static asteroidsGame.constants.AppConstants.STAGE_WIDTH;
import static asteroidsGame.flyingobjects.Asteroid.asteroids;
import static asteroidsGame.flyingobjects.Bullet.bullets;

public class GamePlayScene extends Scene {
    private Scene gameScene;
    private BorderPane borderPane;
    public static Label timerLabel;
    public static Label pointsLabel;
    public static Label livesLabel;
    public static Label levelLabel;
    public static Pane gamePane = new Pane();
    private ToggleButton pause;
    //Used to store the currently pressed key and is a collection that does not allow duplicate elements
    Set<KeyCode> pressedKeys = new HashSet<>();

    public static AtomicInteger levels = new AtomicInteger(1);
    public static AtomicInteger points = new AtomicInteger(0);

    public GamePlayScene(Player player, Stage primaryStage) {
        super(gamePane);
        initPauseButton();
        gamePane.setStyle(AppConstants.ButtonStyle.BACKGROUND.getStyle());
        this.gameScene = super.getRoot().getScene();
        displayGameInfo(player);
        addEventFilterToScene(player);
        pause(primaryStage);
        gamePane.getChildren().addAll(borderPane, pause, timerLabel, player.getCharacter());
    }

    private void initPauseButton() {
        pause = new ToggleButton("Pause");
        pause.setStyle(AppConstants.ButtonStyle.BUTTON_BG.getStyle());
        pause.setLayoutX(STAGE_WIDTH - 100);
        pause.setLayoutY(20);
    }

    private void displayGameInfo(Player player) {
        pointsLabel = new Label("Points: 0");
        pointsLabel.setFont(AppConstants.AppFont.LABEL_FONT.getFont());
        pointsLabel.setTextFill(AppConstants.AppColor.SHAPE.getColor());

        VBox pointcard = new VBox(pointsLabel);
        pointcard.setAlignment(Pos.CENTER_LEFT);

// Create the Livescard object
        livesLabel = new Label("Lives: " + player.getLives());
        livesLabel.setFont(AppConstants.AppFont.LABEL_FONT.getFont());
        livesLabel.setTextFill(AppConstants.AppColor.SHAPE.getColor());

        VBox Livescard = new VBox(livesLabel);
        Livescard.setAlignment(Pos.CENTER_LEFT);

// Create the Levels object
        levelLabel = new Label("Level: " + levels.get());
        levelLabel.setFont(AppConstants.AppFont.LABEL_FONT.getFont());
        levelLabel.setTextFill(AppConstants.AppColor.SHAPE.getColor());

        VBox levelsCard = new VBox(levelLabel);
        levelsCard.setAlignment(Pos.CENTER_LEFT);


// Stack the objects vertically using a VBox
        VBox vbox = new VBox(pointcard, levelsCard, Livescard);
        vbox.setAlignment(Pos.TOP_LEFT);
        vbox.setSpacing(10);

// Add the VBox to the left-hand side of the screen using a BorderPane
        borderPane = new BorderPane();
        borderPane.setLeft(vbox);

// Timer label in VBox
        timerLabel = new Label();
        timerLabel.setText("Time: 0s");
        timerLabel.setFont(AppConstants.AppFont.LABEL_FONT.getFont());
        timerLabel.setTextFill(AppConstants.AppColor.SHAPE.getColor());
        timerLabel.setLayoutX(gamePane.getWidth() + 600);
        timerLabel.setLayoutY(20);
    }

    private void pause(Stage primaryStage) {
        pause.setOnAction(event -> {
            if (pause.isSelected()) {
                primaryStage.setScene(pauseScene);
                timer.stop();
            } else {
                timer.start();
                primaryStage.setScene(gameScene);
            }
        });
    }

    private void addEventFilterToScene(Player player) {
        //Adding event filters to game scenes for handling key press events
        gameScene.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            KeyCode code = event.getCode();
            pressedKeys.add(code);

            if (pressedKeys.contains(KeyCode.LEFT)) {
                player.turnLeft();
            }
            if (pressedKeys.contains(KeyCode.RIGHT)) {
                player.turnRight();
            }
            if (pressedKeys.contains(KeyCode.UP)) {
                player.accelerate();
            }
            if (pressedKeys.contains(KeyCode.Z)) {
                Bullet bullet = player.shoot("playerBullet");
                if (bullet != null) {
                    bullets.add(bullet);
                    gamePane.getChildren().add(bullet);

                    //Shooting sounds
                    new AePlayWave("src/shoot.wav").start();
                }
            }
            if (pressedKeys.contains(KeyCode.SHIFT)) {
                player.hyperspace(asteroids, bullets, AnimationController.alien, AnimationController.alienAdded);
            }
        });

        //When a key is released, it is removed from the pressedKeys collection
        gameScene.addEventFilter(KeyEvent.KEY_RELEASED, event -> {
            KeyCode code = event.getCode();
            pressedKeys.remove(code);
        });
    }


}
