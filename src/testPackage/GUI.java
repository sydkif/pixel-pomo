package testPackage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javafx.animation.Transition;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class GUI extends Application {

    private static String RAIN = "src/resource/image/rain-animation/";
    private static Transition animation;
    private static ImageView imageView = new ImageView();
    private static Slider ambVol = new Slider(0.0, 100, 100);
    private static Slider mscVol = new Slider(0.0, 100, 100);
    private static AudioPlayer ambient = new AudioPlayer("AMBIENT", 0);
    private static AudioPlayer music = new AudioPlayer("MUSIC", 0);

    public void initUI(Stage stage) {

        mySlider(ambVol, 120, 55, Orientation.HORIZONTAL, ambient);
        mySlider(mscVol, 120, 110, Orientation.HORIZONTAL, music);

        Button amb = new Button("Ambient");
        amb.setLayoutX(25);
        amb.setLayoutY(50);
        amb.setOnAction((ActionEvent event) -> {

            ambVol.setDisable(false);
            playAnimation(imageView, RAIN);
            ambient.play();
            animation.play();

        });

        Button msc = new Button("Music");
        msc.setLayoutX(25);
        msc.setLayoutY(100);
        msc.setOnAction((ActionEvent event) -> {

            music.play();
            mscVol.setDisable(false);

        });

        Button testButton1 = new Button("Next track {cycle}");
        testButton1.setLayoutX(25);
        testButton1.setLayoutY(200);
        testButton1.setOnAction((ActionEvent event) -> {

            ambient.nextTrack();

        });

        Button testButton = new Button("Change to selected {track 2}");
        testButton.setLayoutX(25);
        testButton.setLayoutY(230);
        testButton.setOnAction((ActionEvent event) -> {

            ambient.changeTrackTo(1);

        });

        Pane root = new Pane();
        root.setId("root");
        root.getChildren().addAll(imageView, msc, amb, testButton, testButton1, ambVol, mscVol);

        Scene scene = new Scene(root, 500, 280);
        stage.setResizable(false);
        scene.getStylesheets().add("resource/style.css");
        stage.setTitle("Play music with button + animation");
        stage.setScene(scene);
        stage.show();

    }

    void playAnimation(ImageView imageView, String path) {

        List<Image> images = new ArrayList<>();

        String framePath = path;
        File f = new File(framePath);
        String[] frameList = f.list();
        for (String frameName : frameList) {
            images.add(new Image(new File(framePath + frameName).toURI().toString()));
        }

        animation = new Transition() {
            {
                setCycleDuration(Duration.millis(500)); // total time for animation
            }

            @Override
            protected void interpolate(double fraction) {
                int index = (int) (fraction * (images.size() - 1));
                imageView.setImage(images.get(index));
            }
        };
        animation.setCycleCount(-1); // infinite loop
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

    @Override
    public void start(Stage stage) {
        initUI(stage);
    }
}
