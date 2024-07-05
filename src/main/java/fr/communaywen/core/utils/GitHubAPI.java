package fr.communaywen.core.utils;
// Coucou maman, je passe à la télé 05/07/2024 20:06 (UTC+2)

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GitHubAPI {

    public String getContributors() throws IOException {
        URL url = new URL("https://api.github.com/repos/Margouta/PluginOpenMC/contributors");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            return response.toString();
        } else {
            return "Error: " + connection.getResponseCode();
        }
    }
}
