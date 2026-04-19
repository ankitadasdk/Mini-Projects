import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Main extends Application {

    // IMPORTANT: Provide your OpenWeatherMap API Key here
    private static final String API_KEY = "XYZ";
    @Override
    public void start(Stage primaryStage) {
        VBox root = new VBox();
        root.getStyleClass().add("root-pane");
        root.setAlignment(Pos.TOP_CENTER);
        HBox topBar = new HBox(15);
        topBar.setAlignment(Pos.CENTER);
        topBar.getStyleClass().add("top-bar");
        TextField searchField = new TextField();
        searchField.setPromptText("ENTER CITY...");
        searchField.getStyleClass().add("search-field");
        HBox.setHgrow(searchField, Priority.ALWAYS);
        Button loadBtn = new Button("LOAD DATA");
        loadBtn.getStyleClass().add("load-btn");
        topBar.getChildren().addAll(searchField, loadBtn);
        VBox centerDisplay = new VBox(20);
        centerDisplay.setAlignment(Pos.CENTER);
        VBox.setVgrow(centerDisplay, Priority.ALWAYS);
        Label tempLabel = new Label("--°C");
        tempLabel.getStyleClass().add("temp-label");
        Label conditionLabel = new Label("[ WAITING... ]");
        conditionLabel.getStyleClass().add("condition-label");
        centerDisplay.getChildren().addAll(tempLabel, conditionLabel);
        root.getChildren().addAll(topBar, centerDisplay);
        Scene scene = new Scene(root, 700, 500);
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        primaryStage.setTitle("Retro-Synth Weather");
        primaryStage.setScene(scene);
        primaryStage.show();
        loadBtn.setOnAction(e -> {
            String city = searchField.getText().trim();
            if (!city.isEmpty()) {
                fetchWeather(city, tempLabel, conditionLabel);
            }
        });
        searchField.setOnAction(e -> loadBtn.fire());
    }
    private void fetchWeather(String city, Label tempLabel, Label conditionLabel) {
        conditionLabel.setText("[ FETCHING... ]");
        new Thread(() -> {
            try {
                String encodedCity = java.net.URLEncoder.encode(city, "UTF-8");
                URL url = new URL("https://api.openweathermap.org/data/2.5/weather?q=" + encodedCity + "&appid=" + API_KEY + "&units=metric");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(5000);
                conn.setReadTimeout(5000);
                int status = conn.getResponseCode();
                if (status == 200) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String inputLine;
                    StringBuilder content = new StringBuilder();
                    while ((inputLine = in.readLine()) != null) {
                        content.append(inputLine);
                    }
                    in.close();
                    JSONObject jsonResponse = new JSONObject(content.toString());
                    double temp = jsonResponse.getJSONObject("main").getDouble("temp");
                    String condition = jsonResponse.getJSONArray("weather").getJSONObject(0).getString("main");
                    Platform.runLater(() -> {
                        tempLabel.setText(String.format("%.1f°C", temp));
                        conditionLabel.setText("[ " + condition.toUpperCase() + " ]");
                    });
                } else if (status == 401) {
                    Platform.runLater(() -> {
                        conditionLabel.setText("[ INV KEY ]");
                        tempLabel.setText("API ERR");
                    });
                } else if (status == 404) {
                    Platform.runLater(() -> {
                        conditionLabel.setText("[ NOT FOUND ]");
                        tempLabel.setText("404");
                    });
                } else {
                    Platform.runLater(() -> {
                        conditionLabel.setText("[ ERROR " + status + " ]");
                        tempLabel.setText("--°C");
                    });
                }
                conn.disconnect();
            } catch (Exception ex) {
                Platform.runLater(() -> {
                    conditionLabel.setText("[ REQ FAILED ]");
                    tempLabel.setText("--°C");
                });
                ex.printStackTrace();
            }
        }).start();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
