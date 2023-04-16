package asteroidsGame.controllers;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


public class Recorder {
    private static List<HighScoreEntry> highScores = new ArrayList<>();
    private static final String recordFile = "highScores.txt";

    // Method to load high scores from file
    public static void loadHighScores() {
        // Clear the high scores list
        highScores.clear();
        File highScoresFile = new File(recordFile);

        // If the file doesn't exist, create a new one
        if (!highScoresFile.exists()) {
            try {
                highScoresFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }

        // Read high scores from file and add to the list
        try (BufferedReader br = new BufferedReader(new FileReader(highScoresFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                String name = parts[0];
                int score = Integer.parseInt(parts[1]);
                highScores.add(new HighScoreEntry(name, score));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Collections.sort(highScores, Collections.reverseOrder()); // Sort the high scores list in descending order
    }

    // Method to save high scores to the file
    public static void saveHighScores() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(recordFile))) {
            for (HighScoreEntry entry : highScores) {
                bw.write(entry.toString() + "\n"); // Write each high score entry to the file
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to add a new high score to the list
    public static void addHighScore(String playerName, AtomicInteger points) {
        HighScoreEntry entry = new HighScoreEntry(playerName, points.get()); // Create a new high score entry
        highScores.add(entry); // Add the new entry to the list
        Collections.sort(highScores, Collections.reverseOrder()); // Sort the high scores list in descending order
    }

    // Method to get the high scores list
    public static List<HighScoreEntry> getHighScores() {
        return highScores;
    }

    // Class to represent a high score entry
    public static class HighScoreEntry implements Comparable<HighScoreEntry> {
        private String name; // Player name
        private int score; // Player score

        // Constructor for HighScoreEntry
        public HighScoreEntry(String name, int score) {
            this.name = name;
            this.score = score;
        }

        // Getter for name
        public String getName() {
            return name;
        }

        // Getter for score
        public int getScore() {
            return score;
        }

        // Convert HighScoreEntry object to a string
        @Override
        public String toString() {
            return name + "," + score;
        }

        // Method to compare HighScoreEntry objects based on their scores
        @Override
        public int compareTo(HighScoreEntry o) {
            return
                    Integer.compare(this.score, o.score); // Compare scores of the two HighScoreEntry objects
        }
    }
}
