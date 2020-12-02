package test2Package;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
    private TaskPaper task = new TaskPaper();
    private Button[] button = new Button[10];
    private Slider[] slider = new Slider[2];
    private Label[] label = new Label[3];
    private CheckBox checkBox = new CheckBox();
    private TextField textField = new TextField();
    private Spinner<Integer> spinner = new Spinner<Integer>(1, 120, 1);
    private boolean timeFormat;
    private boolean toggle;
    private Image image;
    private ImageView imageView;

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
        addLabel(label[1], 25, 535, 60, "label-music");
        addLabel(label[2], 25, 460, 60, "label-music");

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
        addButton(button[5], 305, 425, 200, 48, "button-confirm");

        // AMBIENT BUTTON
        button[0].setOpacity(0.5);
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

        // MUSIC BUTTON
        button[1].setOpacity(0.5);
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

        // NEXT BUTTON
        button[2].setDisable(true);
        button[2].setOnAction((ActionEvent event) -> {
            music.next();
            button[1].setOpacity(1);
        });

        // CLOCK BUTTON
        button[3].setOnAction((ActionEvent event) -> {
            if (timeFormat) {
                dtf = DateTimeFormatter.ofPattern("HH:mm");
                timeFormat = false;
            } else {
                dtf = DateTimeFormatter.ofPattern("h:mm");
                timeFormat = true;
            }
        });

        // TASK BUTTON
        button[4].setOnAction((ActionEvent event) -> {
            textField.setText("Activity"); // Default
            if (toggle) {
                root.getChildren().removeAll(imageView, button[5], textField, spinner, checkBox);
                toggle = false;
            } else {
                root.getChildren().addAll(imageView, button[5], textField, spinner, checkBox);
                toggle = true;
            }
        });

        // CONFIRM BUTTON
        button[5].setText("CONFIRM");
        button[5].setRotate(-3.5);
        button[5].setOnAction((ActionEvent event) -> {
            task.setLabel(textField.getText());
            task.setDuration(spinner.getValue());
            task.setAlarmOn(checkBox.isSelected());
            label[2].setText(task.getLabel() + ", " + task.getDuration() + " minute(s)");

            // TODO Add start timer

            System.out.println();
            System.out.println("Task Label\t: " + task.getLabel());
            System.out.println("Task Duration\t: " + task.getDuration() + " min");
            System.out.println("Enable Alarm\t: " + task.isAlarmOn());
            root.getChildren().removeAll(imageView, button[5], textField, spinner, checkBox);
            toggle = false;
            textField.setText("");
        });
        root.getChildren().remove(button[5]);

        // TASK PAPER PROPERTIES
        FileInputStream stream = null;
        try {
            stream = new FileInputStream("src/resource/image/task-paper-clone.png");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        image = new Image(stream);
        imageView = new ImageView(image);
        imageView.setLayoutX(209);
        imageView.setLayoutY(70);

        textField.setPrefSize(280, 36);
        textField.setLayoutX(255);
        textField.setLayoutY(250);
        textField.setRotate(-3.5);

        spinner.setPrefSize(80, 36);
        spinner.setLayoutX(455);
        spinner.setLayoutY(300);
        spinner.setRotate(-3.5);

        checkBox.setLayoutX(500);
        checkBox.setLayoutY(350);
        checkBox.setRotate(-3.5);
        checkBox.setId("checkbox");

        // UPDATE LABELS
        label[1].setText("Music Title");
        label[2].setText("Task, Duration");
        Thread timerThread = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(100); // 0.1 second
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Platform.runLater(() -> {
                    label[0].setText(LocalTime.now().format(dtf));
                    if (music.isPlaying() && (label[1].getText() != music.title())) {
                        label[1].setText(music.title());
                    }
                });
            }
        });
        timerThread.start();

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
     * @param x   : x-coordinate for button
     * @param y   : y-coordinate for button
     * @param X   : x-dimension for size
     * @param Y   : y-dimension for size
     * @param id  : ID for .css
     */
    void addButton(Button btn, double x, double y, double X, double Y, String id) {
        btn.setLayoutX(x);
        btn.setLayoutY(y);
        btn.setPrefSize(X, Y);
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
