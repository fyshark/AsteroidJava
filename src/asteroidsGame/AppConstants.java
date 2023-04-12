package asteroidsGame;

import javafx.scene.paint.Color;

public class AppConstants {

    public enum AppColor {
        BACKGROUND(Color.BLACK),
        SHAPE(Color.WHITE),
        FILL(Color.TRANSPARENT);

        private final Color color;

        AppColor(Color color) {
            this.color = color;
        }

        public Color getColor() {
            return color;
        }
    }

    public enum ButtonStyle {

        BUTTON_STYLE("-fx-focus-color: transparent; -fx-background-color: #000000; -fx-font-size:40"),
        BACKGROUND("-fx-background-color: black;"),
        BUTTON_BG("-fx-background-color: white;"),
        BUTTON_NODE("-fx-font-size: 16pt; -fx-pref-width: 200px;");

        private final String style;

        ButtonStyle(String style) {
            this.style = style;
        }

        public String getStyle() {
            return style;
        }
    }

}


