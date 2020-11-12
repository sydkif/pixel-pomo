package testPackage;

import java.nio.file.Paths;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;

public class GUI extends Application {

    private static Pane root = new Pane();
    private static Scene scene = new Scene(root, 640, 360);
    private static Slider music_vol = new Slider(0.0, 100, 100);
    private static Slider ambient_vol = new Slider(0.0, 100, 100);
    private static String STUDY = "src/resource/video/study.mp4";
    private static String RAIN = "src/resource/video/rain-study.mp4";
    private static AudioPlayer music_player = new AudioPlayer("MUSIC", 0);
    private static AudioPlayer ambient_player = new AudioPlayer("AMBIENT", 0);
    private static Media rain_clip = new Media(Paths.get(RAIN).toUri().toString());
    private static Media study_clip = new Media(Paths.get(STUDY).toUri().toString());
    private static MediaPlayer video_player = new MediaPlayer(study_clip);
    private static MediaView view = new MediaView(video_player);
    private static Button ambient_btn = new Button("Ambient");
    private static Button change_btn = new Button("Change");
    private static Button music_btn = new Button("Music");
    private static Button next_btn = new Button("Next");
    private static Button test_btn_1 = new Button("Test 1");
    private static Button test_btn_2 = new Button("Test 2");
    private static Button test_btn_3 = new Button("Test 3");

    @Override
    public void start(Stage stage) {
        initUI(stage);
    }

    public void initUI(Stage stage) {

        // Initial setup
        video_player.setCycleCount(-1);
        next_btn.setDisable(true);
        change_btn.setDisable(true);

        // Simplified volume sliders creation
        mySlider(ambient_vol, 120, 55, Orientation.HORIZONTAL, ambient_player);
        mySlider(music_vol, 120, 155, Orientation.HORIZONTAL, music_player);

        // Simplified buttons creation
        myButton(ambient_btn, 25, 50, 80, 20, "glass-grey");
        myButton(change_btn, 25, 80, 80, 20, "glass-grey");
        myButton(music_btn, 25, 150, 80, 20, "glass-grey");
        myButton(next_btn, 25, 180, 80, 20, "glass-grey");
        myButton(test_btn_1, 25, 250, 80, 20, "bevel-grey");
        myButton(test_btn_2, 25, 280, 80, 20, "bevel-grey");
        myButton(test_btn_3, 25, 310, 80, 20, "bevel-grey");

        // Ambient button event properties
        ambient_btn.setOnAction((ActionEvent event) -> {

            ambient_vol.setDisable(false);
            ambient_player.play();
            if (ambient_player.player.isMute()) {
                changeVideo(2);
            } else {
                changeVideo(1);
            }
            video_player.setCycleCount(-1);
            change_btn.setDisable(false);

        });

        // Change (Ambient) button event properties
        change_btn.setOnAction((ActionEvent event) -> {

            System.out.println();
            ambient_player.nextTrack();

        });

        // Music button event properties
        music_btn.setOnAction((ActionEvent event) -> {

            video_player.play();
            music_player.play();
            music_vol.setDisable(false);
            next_btn.setDisable(false);

        });

        // Next (Music) button event properties
        next_btn.setOnAction((ActionEvent event) -> {

            System.out.println();
            music_player.nextTrack();

        });

        // Test button 1 event properties
        test_btn_1.setOnAction((ActionEvent event) -> {
            System.out.println("Test button 1 clicked!");

        });

        // Test button 2 event properties
        test_btn_2.setOnAction((ActionEvent event) -> {
            System.out.println("Test button 2 clicked!");

        });

        // Test button 3 event properties
        test_btn_3.setOnAction((ActionEvent event) -> {
            System.out.println("Test button 3 clicked!");

        });

        // Main window properties
        root.setId("root");
        root.getChildren().addAll(view, ambient_vol, music_vol);
        root.getChildren().addAll(music_btn, ambient_btn, change_btn, next_btn);
        root.getChildren().addAll(test_btn_1, test_btn_2, test_btn_3);
        stage.setResizable(false);
        scene.getStylesheets().add("resource/style.css");
        stage.setTitle("no title yet");
        stage.setScene(scene);
        stage.show();

    }

    /**
     * Method to easily change background video clip
     * 
     * @param n A selector for which clip to play
     */
    void changeVideo(int n) {
        if (video_player.getStatus().toString() == "PLAYING") {
            System.out.println("Video will be stopped.");
            video_player.stop();
            // root.getChildren().remove(view);
        }
        if (n == 1)
            video_player = new MediaPlayer(rain_clip);
        if (n == 2)
            video_player = new MediaPlayer(study_clip);

        view.setMediaPlayer(video_player);
        video_player.play();
    }

    /**
     * Method to simplify button creation with position, size and ID
     * 
     * Must be manually added into Pane().
     * 
     * @param btnName : Button variable
     * @param xc      : x-coordinate for button
     * @param yc      : y-coordinate for button
     * @param xd      : x-dimension for size
     * @param yd      : y-dimension for size
     * @param id      : Id for CSS styling
     */
    void myButton(Button btnName, double xc, double yc, double xd, double yd, String id) {

        btnName.setLayoutX(xc);
        btnName.setLayoutY(yc);
        btnName.setPrefSize(xd, yd);
        btnName.setId(id);
    }

    /**
     * Method to simplify volume slider creation for selected AudioPlayer.
     * 
     * Must be manually added into the Pane();
     * 
     * @param slider : Slider variable
     * @param x      : x-coordinate
     * @param y      : y-coordinate
     * @param ori    : Set orientation to Horizontal or Vertical
     * @param player : Selected AudioPlayer variable
     */
    void mySlider(Slider slider, double x, double y, Orientation ori, AudioPlayer player) {

        slider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> obs, Number old, Number newValue) {
                double newDouble = (double) newValue;
                System.out.printf("\nVolume: %.0f%%", newDouble);
                player.changeVolume(newDouble / 100);
            }
        });
        slider.setOrientation(ori);
        slider.setLayoutX(x);
        slider.setLayoutY(y);
        slider.setDisable(true);
    }

}