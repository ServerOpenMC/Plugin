package fr.communaywen.core.utils;

public class ProgressBar {
    public static String createProgressBar(double percentage, int barLength, String color) {
        StringBuilder progressBar = new StringBuilder();
        int filledLength = (int) Math.round(percentage / 100.0 * barLength);

        progressBar.append(color).append("§m");
        for (int i = 0; i < filledLength; i++) {
            progressBar.append(" ");
        }

        progressBar.append("§7§m");
        for (int i = filledLength; i < barLength; i++) {
            progressBar.append(" ");
        }

        progressBar.append("§r");

        return progressBar.toString();
    }
}
