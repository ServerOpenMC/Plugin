package fr.communaywen.core.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DiscordWebhook {

    private final String webhookUrl;
    private final String botName;
    private final String botAvatarUrl;

    public DiscordWebhook(String webhookUrl, String botName, String botAvatarUrl) {
        this.webhookUrl = webhookUrl;
        this.botName = botName;
        this.botAvatarUrl = botAvatarUrl;
    }

    public void sendMessage(String username, String avatarUrl, String message) {
        try {
            URL url = new URL(webhookUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            JsonObject jsonPayload = new JsonObject();
            jsonPayload.addProperty("username", username);
            jsonPayload.addProperty("avatar_url", avatarUrl);
            jsonPayload.addProperty("content", "``" + removeColorCodes(message) + "``");

            try (OutputStream os = connection.getOutputStream()) {
                os.write(jsonPayload.toString().getBytes());
                os.flush();
            }

            int responseCode = connection.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_NO_CONTENT) {
                System.err.println("Failed to send message to Discord webhook. Response code: " + responseCode);
            }

            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendBroadcast(String message) {
        try {
            URL url = new URL(webhookUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            JsonObject jsonPayload = new JsonObject();
            jsonPayload.addProperty("username", botName);
            jsonPayload.addProperty("avatar_url", botAvatarUrl);

            JsonObject embed = new JsonObject();
            embed.addProperty("description", removeColorCodes(message));
            
            if (message.startsWith("[a] PROUT !!!")) {
                embed.addProperty("color", 65280);
            }
            
            JsonArray embeds = new JsonArray();
            embeds.add(embed);
            jsonPayload.add("embeds", embeds);

            try (OutputStream os = connection.getOutputStream()) {
                os.write(jsonPayload.toString().getBytes());
                os.flush();
            }

            int responseCode = connection.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_NO_CONTENT) {
                System.err.println("Failed to send broadcast message to Discord webhook. Response code: " + responseCode);
            }

            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String removeColorCodes(String message) {
        return message.replaceAll("§[0-9a-fk-or]", "");
    }
}
