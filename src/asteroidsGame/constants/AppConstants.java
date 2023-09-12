package asteroidsGame.constants;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class AppConstants {
    public static double STAGE_WIDTH, STAGE_HEIGHT;

    // contains various constant values used throughout the whole game
    // define font values
    public enum AppFont {
        LABEL_FONT("Lucida Sans Unicode", FontWeight.BOLD, 50);

        private final String family;
        private final FontWeight weight;
        private final double size;

        AppFont(String family, FontWeight weight, double size) {
            this.family = family;
            this.weight = weight;
            this.size = size;
        }

        public Font getFont() {
            return Font.font(family, weight, size);
        }
    }

    // define color values
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

    // define style values for buttons in the game interface
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


