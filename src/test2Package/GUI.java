package test2Package;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.Pane;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class GUI extends Application {

    public final String AMBIENT = "src/resource/sound/ambient/";
    public final String MUSIC = "src/resource/sound/music/";
    public final String VIDEO = "src/resource/video/";
    private Pane root = new Pane();
    private Scene scene = new Scene(root, 800, 600);
    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
    private AudioPlayer ambient = new AudioPlayer(AMBIENT, AudioPlayer.AMBIENT);
    private AudioPlayer music = new AudioPlayer(MUSIC, AudioPlayer.MUSIC);
    private VideoPlayer normal = new VideoPlayer(VIDEO, VideoPlayer.NORMAL);
    private VideoPlayer rain = new VideoPlayer(VIDEO, VideoPlayer.RAIN);
    private MediaView view = new MediaView();
    private Button[] button = new Button[10];
    private Slider[] slider = new Slider[2];
    private Label[] label = new Label[2];
    private boolean timeFormat;

    public void initUI(Stage stage) {

        // INITIAL SETUP
        rain.play();
        normal.play();
        view.setId("view");
        view.setX(80);
        view.setY(60);
        view.setOpacity(0.5);
        view.setMediaPlayer(normal.player);
        root.setId("root");
        root.getChildren().add(view);
        scene.getStylesheets().add("resource/style.css");
        stage.setTitle("no title yet");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();

        // ADD LABELS
        for (int x = 0; x < label.length; x++)
            label[x] = new Label();
        addLabel(label[0], 710, 25, 60, "label-clock");
        addLabel(label[1], 25, 500, 60, "label-music");

        // UPDATE CLOCK AND MUSIC TITLE
        Thread timerThread = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(100); // 0.1 second
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Platform.runLater(() -> {
                    label[0].setText(LocalTime.now().format(dtf));
                    if (music.isPlaying()) {
                        label[1].setText((String) music.media.getMetadata().get("title"));
                    }
                });
            }
        });
        timerThread.start();

        // ADD VOLUME SLIDERS
        for (int x = 0; x < slider.length; x++)
            slider[x] = new Slider(0.0, 100, 50);
        addSlider(slider[0], 70, 25, Orientation.VERTICAL, ambient);
        addSlider(slider[1], 70, 70, Orientation.VERTICAL, music);

        // ADD BUTTONS
        for (int x = 0; x < button.length; x++)
            button[x] = new Button();
        addButton(button[0], 25, 25, 40, 40, "button-ambient");
        addButton(button[1], 25, 70, 40, 40, "button-music");
        addButton(button[2], 25, 114, 40, 10, "button-next");
        addButton(button[3], 670, 25, 38, 38, "button-clock");
        addButton(button[4], 25, 280, 40, 40, "button-task");

        button[0].setOpacity(0.5);
        button[1].setOpacity(0.5);
        button[2].setDisable(true);

        // AMBIENT BUTTON ON CLICK EVENT
        button[0].setOnAction((ActionEvent event) -> {
            slider[0].setDisable(false);
            ambient.play();
            if (ambient.player.isMute()) {
                view.setMediaPlayer(normal.player);
                button[0].setOpacity(0.5);
            } else {
                view.setMediaPlayer(rain.player);
                button[0].setOpacity(1);
            }

        });

        // MUSIC BUTTON ON CLICK EVENT
        button[1].setOnAction((ActionEvent event) -> {
            slider[1].setDisable(false);
            button[2].setDisable(false);
            music.play();
            if (music.player.isMute()) {
                button[1].setOpacity(0.5);
            } else {
                button[1].setOpacity(1);
            }
        });

        // NEXT BUTTON ON CLICK EVENT
        button[2].setOnAction((ActionEvent event) -> {
            music.next();
        });

        // CLOCK BUTTON ON CLICK EVENT
        button[3].setOnAction((ActionEvent event) -> {
            if (timeFormat) {
                dtf = DateTimeFormatter.ofPattern("HH:mm");
                timeFormat = false;
            } else {
                dtf = DateTimeFormatter.ofPattern("h:mm");
                timeFormat = true;
            }
        });

        // TASK BUTTON ON CLICK EVENT
        button[4].setOnAction((ActionEvent event) -> {

        });

    }

    @Override
    public void start(Stage stage) {
        initUI(stage);
        stage.setOnCloseRequest((EventHandler<WindowEvent>) new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                Platform.exit();
                System.exit(0);
            }
        });
    }

    /**
     * Method to add button with position, size and ID.
     * 
     * @param btn : Button variable
     * @param xc  : x-coordinate for button
     * @param yc  : y-coordinate for button
     * @param xd  : x-dimension for size
     * @param yd  : y-dimension for size
     * @param id  : ID for .css
     */
    void addButton(Button btn, double xc, double yc, double xd, double yd, String id) {
        btn.setLayoutX(xc);
        btn.setLayoutY(yc);
        btn.setPrefSize(xd, yd);
        btn.setId(id);
        root.getChildren().add(btn);
    }

    /**
     * Method to add volume slider for selected AudioPlayer.
     * 
     * @param slider : Slider variable
     * @param x      : x-coordinate
     * @param y      : y-coordinate
     * @param ori    : Set orientation to Horizontal or Vertical
     * @param player : Selected AudioPlayer variable
     */
    void addSlider(Slider sl, double x, double y, Orientation ori, AudioPlayer player) {

        sl.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> obs, Number old, Number newValue) {
                double newDouble = (double) newValue;
                System.out.printf("\nVolume: %.0f%%", newDouble);
                player.setCurrentVolume(newDouble / 100);
            }
        });
        sl.setOrientation(ori);
        sl.setLayoutX(x);
        sl.setLayoutY(y);
        sl.setDisable(true);
        sl.setPrefHeight(32);
        sl.setId("my-slider");
        root.getChildren().add(sl);
    }

    /**
     * Method to add label.
     * 
     * @param lb : Label variable
     * @param x  : x-coordinate
     * @param y  : y-coordinate
     * @param w  : Minimum width
     * @param id : ID for .css
     */
    void addLabel(Label lb, double x, double y, double w, String id) {
        lb.setLayoutX(x);
        lb.setLayoutY(y);
        lb.setMinWidth(w);
        lb.setId(id);
        root.getChildren().add(lb);
    }

}
