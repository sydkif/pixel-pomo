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
    private static Slider ambVol = new Slider(0.0, 50, 50);
    private static Slider mscVol = new Slider(0.0, 50, 50);
    private static AudioPlayer ambient = new AudioPlayer("AMBIENT", 0);
    private static AudioPlayer music = new AudioPlayer("MUSIC", 0);
    private static String STUDY = "src/resource/video/study.mp4";
    private static String RAIN = "src/resource/video/rain-study.mp4";
    private static Media rain_clip = new Media(Paths.get(RAIN).toUri().toString());
    private static Media study_clip = new Media(Paths.get(STUDY).toUri().toString());
    private static MediaPlayer video = new MediaPlayer(study_clip);
    private static MediaView view = new MediaView(video);
    private static  Button amb = new Button("Ambient");
    private static  Button change = new Button("Change");
    private static  Button msc = new Button("Music");
    private static  Button next = new Button("Next");

    @Override
    public void start(Stage stage) {
        initUI(stage);
    }

    public void initUI(Stage stage) {
        video.setCycleCount(-1);
        video.play();
        next.setDisable(true);
        change.setDisable(true);

        mySlider(ambVol, 120, 55, Orientation.HORIZONTAL, ambient);
        mySlider(mscVol, 120, 155, Orientation.HORIZONTAL, music);

        amb.setId("Button");
        amb.setLayoutX(25);
        amb.setLayoutY(50);
        amb.setPrefSize(80, 20);
        amb.setOnAction((ActionEvent event) -> {
            ambVol.setDisable(false);
            ambient.play();
            if (ambient.player.isMute())
                changeVideo(2);
            else
                changeVideo(1);
            video.setCycleCount(-1);
            change.setDisable(false);
        });

        change.setId("Button");
        change.setLayoutX(25);
        change.setLayoutY(80);
        change.setPrefSize(80, 20);
        change.setOnAction((ActionEvent event) -> {

            System.out.println();
            ambient.nextTrack();

        });

        msc.setId("Button");
        msc.setLayoutX(25);
        msc.setLayoutY(150);
        msc.setPrefSize(80, 20);
        msc.setOnAction((ActionEvent event) -> {
            music.play();
            mscVol.setDisable(false);
            next.setDisable(false);

        });

        next.setId("Button");
        next.setLayoutX(25);
        next.setLayoutY(180);
        next.setPrefSize(80, 20);
        next.setOnAction((ActionEvent event) -> {
            System.out.println();
            music.nextTrack();

        });

        root.setId("root");
        root.getChildren().addAll(view, msc, amb, change, next, ambVol, mscVol);
        stage.setResizable(false);
        scene.getStylesheets().add("resource/style.css");
        stage.setTitle("no title yet");
        stage.setScene(scene);
        stage.show();

    }

    void changeVideo(int n) {
        if (video.getStatus().toString() == "PLAYING") {
            System.out.println("Video will be stopped.");
            video.stop();
            // root.getChildren().remove(view);
        }
        if (n == 1)
            video = new MediaPlayer(rain_clip);
        if (n == 2)
            video = new MediaPlayer(study_clip);

        view.setMediaPlayer(video);
        video.play();
    }

    /**
     * Method to generate a volume slider for selected AudioPlayer.
     * 
     * MUST be manually include into the Pane();
     * 
     * @param sliderName  Slider variable
     * @param x           x-coordinate
     * @param y           y-coordinate
     * @param orientation Horizontal or Vertical
     * @param playerName  AudioPlayer variable
     */
    void mySlider(Slider sliderName, double x, double y, Orientation orientation, AudioPlayer playerName) {

        sliderName.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> obs, Number old, Number newValue) {
                double newDouble = (double) newValue;
                System.out.printf("\nVolume: %.0f%%", newDouble);
                playerName.changeVolume(newDouble / 100);
            }
        });
        sliderName.setOrientation(orientation);
        sliderName.setLayoutX(x);
        sliderName.setLayoutY(y);
        sliderName.setDisable(true);
    }

}