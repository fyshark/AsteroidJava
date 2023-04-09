package asteroidsGame;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

// The Recorder class is responsible for managing and saving the player's scores,
// as well as loading and displaying high scores from a file.
public class Recorder {
    // Variable to store the current game score
    private static int score = 100000;
    // List to store high score entries
    private static List<HighScoreEntry> highScores = new ArrayList<>();
    // The file path where high scores are saved
    private static final String recordFile = "src\\highScores.txt";

    // Getter for the current game score
    public static int getScore() {
        return score;
    }

    // Setter for the current game score
    public static void setScore(int score) {
        Recorder.score = score;
    }

    public static void loadHighScores() {
        highScores.clear();
        File highScoresFile = new File("src/highScores.txt");

        // Check if the highScores.txt file exists, if not, create a new file
        if (!highScoresFile.exists()) {
            try {
                highScoresFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }

        try (BufferedReader br = new BufferedReader(new FileReader(highScoresFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(" ");
                String name = parts[0];
                int score = Integer.parseInt(parts[1]);
                highScores.add(new HighScoreEntry(name, score));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Collections.sort(highScores, Collections.reverseOrder());
    }

    // Method to save high scores to the recordFile
    public static void saveHighScores() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(recordFile))) {
            for (HighScoreEntry entry : highScores) {
                bw.write(entry.toString() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to add a new high score entry and sort the highScores list
    public static void addHighScore(String name, int score) {
        HighScoreEntry entry = new HighScoreEntry(name, score);
        highScores.add(entry);
        highScores.sort(Comparator.comparing(HighScoreEntry::getScore).reversed());
    }

    // Method to clear all high scores and delete the recordFile
    public static void clearHighScores() {
        highScores.clear();
        File file = new File(recordFile);
        if (file.exists()) {
            file.delete();
        }
    }

    // Getter for the highScores list
    public static List<HighScoreEntry> getHighScores() {
        return highScores;
    }

    // Inner class to represent a single high score entry with a player name and score
    private static class HighScoreEntry {
        private String name;
        private int score;

        // Constructor for HighScoreEntry class
        public HighScoreEntry(String name, int score) {
            this.name = name;
            this.score = score;
        }

        // Getter for the player name
        public String getName() {
            return name;
        }

        // Getter for the player score
        public int getScore() {
            return score;
        }

        // Override the toString() method to return a formatted high score entry
        @Override
        public String toString() {
            return name + "," + score;
        }
    }
}
