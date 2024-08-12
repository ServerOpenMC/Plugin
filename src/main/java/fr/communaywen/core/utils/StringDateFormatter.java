package fr.communaywen.core.utils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class StringDateFormatter {
    public static String formatRelativeDate(LocalDateTime dateTime) {
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(dateTime, now);
        long minutes = duration.toMinutes();

        if (minutes < 1) {
            return "À l'instant";
        } else if (minutes < 60) {
            return "Il y a " + minutes + " minute" + (minutes > 1 ? "s" : "");
        } else if (duration.toHours() < 24) {
            long hours = duration.toHours();
            return "Il y a " + hours + " heure" + (hours > 1 ? "s" : "");
        } else if (duration.toDays() <= 5) {
            long days = duration.toDays();
            return "Il y a " + days + " jour" + (days > 1 ? "s" : "");
        } else {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("'Le' dd/MM/yyyy 'à' HH:mm");
            return dateTime.format(formatter);
        }
    }
}
