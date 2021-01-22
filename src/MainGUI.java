// FILENAME: MainGUI.java

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import org.ini4j.Ini;

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

/**
 * MainGUI class for the whole applications
 */
public class MainGUI extends Application {
    
    /*
     * private static String user_name = System.getProperty("user.name"); private
     * File file = new File("C:/Users/" + user_name + "/Music/PixelPomoPlaylist");
     * private File[] list_file = file.listFiles(); boolean dirCreated =
     * file.mkdir();
     */

    private File config = new File("config.ini");
    private Ini ini;

    public static String AMBIENT = "src/resource/sound/ambient/"; // Default location of ambient sound file
    public static String MUSIC = ""; // Music playlist path
    public static String ALARM = "src/resource/sound/alarm/"; // Default location of alarm sound file
    public static String VIDEO = "src/resource/video/"; // Default location of background video file
    private Pane root = new Pane(); // Create a layout for the application
    private Scene scene = new Scene(root, 640, 360); // Create a window for the application with 640x360 resolution
    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm"); // Create a 24hrs time format
    private AudioPlayer ambient = new AudioPlayer(AMBIENT, AudioPlayer.AMBIENT); // Audio player for ambient
    private AudioPlayer alarm = new AudioPlayer(ALARM, AudioPlayer.ALARM); // Audio player for alarm
    private VideoPlayer norm_bg = new VideoPlayer(VIDEO, VideoPlayer.NORMAL); // Video (background) player for normal
    private VideoPlayer rain_bg = new VideoPlayer(VIDEO, VideoPlayer.RAIN); // Video (background) player for raining
    private TaskTimer taskTimer = new TaskTimer(); // Timer for the task
    private TaskPaper task = new TaskPaper(); // Task information detail
    private MediaView view = new MediaView(); // Media viewer for background video
    private Button[] button = new Button[6]; // Array of buttons
    private Slider[] slider = new Slider[2]; // Array of volume sliders
    private Label[] label = new Label[5]; // Array of text labels
    private CheckBox checkBox = new CheckBox(); // A check box for alarm (on/off)
    private TextField textField = new TextField(); // A text field to enter task details
    private Spinner<Integer> spinner = new Spinner<Integer>(1, 120, 1); // A spinner to set timer duration (120 min max)
    private boolean toggle; // A boolean for toggling task pop up
    private Image image; // Image for task pop up background
    private ImageView imageView; // Viewer for the image

    public void initUI(Stage stage) {

        try {
            ini = new Ini(config);
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        System.out.println(ini.get("music", "path"));
        String configured_path = ini.get("music", "path");
        MUSIC = configured_path + "/";

        File file = new File(ini.get("music", "path"));
        File[] list_file = file.listFiles();
        boolean dirCreated = file.mkdir();

        AudioPlayer music = new AudioPlayer(MUSIC, AudioPlayer.MUSIC); // Audio player for music

        // INITIAL SETUP
        rain_bg.play(); // Play both background video at start
        norm_bg.play(); // Later on just swap between front or back
        view.setId("view"); // CSS id
        view.setOpacity(0.5); // Set MediaView opacity to 50%
        view.setMediaPlayer(norm_bg.player); // Set default background to normal (not rain)
        root.setId("root"); // CSS id
        root.getChildren().add(view); // Add MediaView into Pane
        scene.getStylesheets().add("resource/vc.css"); // Get CSS file for styling
        stage.setTitle("Pixel Pomo"); // Set the windows title
        stage.setResizable(false); // Disable user ability to resize apps window
        stage.setScene(scene); // Create the window
        stage.getIcons().add(new Image("resource/icon/app_icon.png"));
        stage.show(); // Show the window

        Instant start = Instant.now();

        try {
            File myObj = new File("log.txt");
            if (myObj.createNewFile())
                System.out.println("File created: " + myObj.getName());
            else
                System.out.println("File already exists.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        // ADD LABELS (label[x], x-pos, y-pos, min-width, css-id)
        for (int x = 0; x < label.length; x++)
            label[x] = new Label();

        addLabel(label[0], 530, 25, 60, "label-clock"); // Clock label
        addLabel(label[1], 25, 270, 60, "label-text"); // Current music playing label
        addLabel(label[2], 25, 310, 60, "label-text"); // Task and timer label
        addLabel(label[3], 25, 220, 60, "label-text");
        addLabel(label[4], 163, 76, 200, "label-time");
        root.getChildren().remove(label[4]);

        label[1].setText("Playing : n/a"); // Default text for played music
        label[2].setText("Task : n/a"); // Default text for task info

        if (list_file.length == 0) {
            label[1].setVisible(false);
            label[3].setText("No .mp3 files detected in :\n" + MUSIC + "\n(Must restart after adding the files)");
        }

        // ADD VOLUME SLIDERS (slider[x], x-pos, y-pos, orientation, audio-player)
        for (int x = 0; x < slider.length; x++)
            slider[x] = new Slider(0.0, 100, 50); // Slider value from 0 to 100 with default at 50
        addSlider(slider[0], 70, 25, Orientation.VERTICAL, ambient); // Volume slider for ambient
        addSlider(slider[1], 70, 70, Orientation.VERTICAL, music); // Volume slider for music

        // ADD BUTTONS (button[x], x-pos, y-pos, x-dimension, y-dimension, css-id)
        for (int x = 0; x < button.length; x++)
            button[x] = new Button();
        addButton(button[0], 25, 25, 40, 40, "button-ambient"); // Toggle ambient button
        addButton(button[1], 25, 70, 40, 40, "button-music"); // Toggle music button
        addButton(button[2], 25, 115, 40, 20, "button-next"); // Next music button
        addButton(button[3], 25, 150, 40, 40, "button-task"); // Toggle task menu button
        addButton(button[4], 379, 288, 120, 24, "button-confirm"); // Confirm button inside task menu
        addButton(button[5], 260, 168, 120, 24, "button-stop"); // Stop alarm button

        // AMBIENT BUTTON
        button[0].setOpacity(0.5); // Set default opacity to 50%
        button[0].setOnAction((ActionEvent event) -> { // On-click event
            slider[0].setDisable(false); // Enable the volume slider
            ambient.play(); // Play ambient audio
            if (ambient.player.isMute()) { // If ambient muted
                view.setMediaPlayer(norm_bg.player); // Play normal background video
                button[0].setOpacity(0.5); // Set button opacity to 50%
            } else { // Ambient is not muted
                view.setMediaPlayer(rain_bg.player); // Play rain background video
                button[0].setOpacity(1); // Set button opacity to 100%
            }
        });

        // MUSIC BUTTON
        button[1].setOpacity(0.5); // Set default opacity to 50%
        button[1].setOnAction((ActionEvent event) -> { // On-click event
            slider[1].setDisable(false); // Enable the volume slider
            button[2].setDisable(false); // Enable next-music button
            music.play(); // Play music audio
            if (music.player.isMute()) { // If music muted
                button[1].setOpacity(0.5); // Set button opacity to 50%
            } else { // Music is not muted
                button[1].setOpacity(1); // Set button opacity to 100%
            }
        });

        // NEXT BUTTON
        button[2].setDisable(true); // By default button is disabled
        button[2].setOnAction((ActionEvent event) -> { // On-click event
            music.next(); // Play the next music in the folder
            if (music.player.isMute()) { // If music muted
                button[1].setOpacity(0.5); // Set button opacity to 50%
            } else { // Music is not muted
                button[1].setOpacity(1); // Set button opacity to 100%
            }
        });

        // TASK BUTTON
        button[3].setOnAction((ActionEvent event) -> {
            textField.setText("Activity"); // Default filled text
            // Toggle between show/hide task pop out
            if (toggle)
                root.getChildren().removeAll(imageView, button[4], textField, spinner, checkBox, label[4]); // Hide
            else
                root.getChildren().addAll(imageView, button[4], textField, spinner, checkBox, label[4]); // Show
            toggle = !toggle; // Toggle between TRUE/FALSE (HIDE/SHOW)
        });

        // CONFIRM BUTTON
        button[4].setText("Confirm"); // Set button label as Confirm
        button[4].setOnAction((ActionEvent event) -> { // On-click event
            if (taskTimer.isTimerOn()) // If timer already on
                taskTimer.stop(); // Stop it first to avoid error

            task.setLabel(textField.getText()); // Set task label based on user input on text field
            task.setDuration(spinner.getValue()); // Set task duration based on spinner user input (in minutes)
            taskTimer.setDuration(spinner.getValue()); // Set timer based on spinner user input (in minutes)
            taskTimer.setAlarmOn(checkBox.isSelected()); // Set alarm TRUE/FALSE based on check box
            System.out.println(); // For testing on console
            System.out.println("Task Label\t: " + task.getLabel()); // Print task label
            System.out.println("Task Duration\t: " + task.getDuration() + " min"); // Print task duration
            System.out.println("Enable Alarm\t: " + taskTimer.isAlarmOn()); // Print alarm status
            root.getChildren().removeAll(imageView, button[4], textField, spinner, checkBox, label[4]); // Clear
            toggle = !toggle; // Toggle switch for TASK BUTTON event
            textField.setText(""); // Reset text field
            taskTimer.start(); // Start the timer
        });

        // STOP BUTTON
        button[5].setText("STOP"); // Set button label as STOP
        button[5].setOnAction((ActionEvent event) -> { // On-click event
            alarm.stop(); // Stop the beeping sound (alarm)
            root.getChildren().remove(button[5]); // Remove this button once clicked
        });

        // By default remove CONFIRM and STOP button
        root.getChildren().removeAll(button[4], button[5]);

        // TASK PAPER PROPERTIES
        FileInputStream stream = null; // Initialize file input stream as null
        try {
            stream = new FileInputStream("src/resource/image/taskpaper.png"); // Load file
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        image = new Image(stream); // Load image
        imageView = new ImageView(image); // Put image into viewer

        // Image position
        imageView.setLayoutX(95); // x-pos
        imageView.setLayoutY(15); // y-pos

        // TextField size & position
        textField.setPrefSize(200, 32); // size
        textField.setLayoutX(242); // x-pos
        textField.setLayoutY(111); // y-pos
        textField.setId("text-fff"); // css-id

        // Spinner size & position
        spinner.setPrefSize(80, 32); // size
        spinner.setLayoutX(289); // x-pos
        spinner.setLayoutY(151); // y-pos
        spinner.setId("text-fff"); // css-id

        // CheckBox position
        checkBox.setLayoutX(378); // x-pos
        checkBox.setLayoutY(232); // y-pos
        checkBox.setSelected(true); // Default state to selected
        checkBox.setId("checkbox"); // css-id

        // UPDATE LABELS
        Thread timerThread = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(500); // 0.5 second interval
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Platform.runLater(() -> {
                    // Get current local time and update to HH:mm format
                    label[0].setText(LocalTime.now().format(dtf));

                    Instant current = Instant.now();
                    Duration timeElapsed = Duration.between(start, current);
                    String timeElapsedLabel = "Time elapsed : " + timeElapsed.toMinutesPart() + " min";
                    label[4].setText(timeElapsedLabel);

                    // Check if music is playing and if title is not same, update title
                    if (music.isPlaying() && (label[1].getText() != music.title()))
                        label[1].setText("Playing : " + music.title()); // Update title label
                    if (taskTimer.isTimerOn()) // Check if timer is on
                        label[2].setText(task.getLabel() + " : " + taskTimer.getLabel()); // Update task label
                    else
                        label[2].setText("Task : n/a");
                    // Play alarm sound once timer is finished
                    if (taskTimer.isBeeping()) {
                        taskTimer.setBeeping(false); // To avoid codes below run more than once
                        root.getChildren().add(button[5]); // Add stop button

                        // Write log
                        try {
                            BufferedWriter bw = new BufferedWriter(new FileWriter("log.txt", true));
                            bw.append("\n" + task.log());
                            bw.close();
                            System.out.println("Successfully wrote to the file.");
                        } catch (IOException e) {
                            System.out.println("An error occurred.");
                            e.printStackTrace();
                        }

                        System.out.println(task.log());
                        alarm.play(); // Play alarm sound
                    }
                });
            }
        });
        timerThread.start();
    }

    /**
     * To start the application and completely exit once press the closed button.
     */
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

        /**
         * To enable the control for the volume of sound based on slider value
         */
        sl.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> obs, Number old, Number newValue) {
                double newDouble = (double) newValue; // Get new value
                player.setCurrentVolume(newDouble / 100); // Change sound volume based on slider value
            }
        });
        sl.setOrientation(ori); // Orientation
        sl.setLayoutX(x); // x-pos
        sl.setLayoutY(y); // y-pos
        sl.setDisable(true); // Disabled by default
        sl.setPrefHeight(32); // Preferred height
        sl.setId("my-slider"); // css-id
        root.getChildren().add(sl); // Add into root (Pane)
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
        lb.setLayoutX(x); // x-pos
        lb.setLayoutY(y); // y-pos
        lb.setMinWidth(w); // Minimum width
        lb.setId(id); // css-id
        root.getChildren().add(lb); // Add into root (Pane)
    }

    /**
     * Just a main function to launch the application.
     */
    public static void main(String[] args) {
        launch(args);
    }
}
