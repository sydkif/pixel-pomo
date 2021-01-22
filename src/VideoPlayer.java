// FILENAME: VideoPlayer.java

import java.nio.file.Paths;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/**
 * The VideoPlayer class stores data of video type, JavaFX Media and
 * MediaPlayer. This class also inherit data from MediaLibrary class.
 */
public class VideoPlayer extends MediaLibrary {

    // Constant for type of video
    public static final int NORMAL = 0;
    public static final int RAIN = 1;

    private int type; // The video type
    public Media media; // JavaFX Media
    public MediaPlayer player; // JavaFX MediaPlayer

    /**
     * The no-arg constructor initializes an object with null for folder, 0 for
     * media file and null for media list. The type is set to normal as default.
     */
    public VideoPlayer() {
        super();
        type = NORMAL;
    }

    /**
     * The constructor initializes an object with a string path for folder, media
     * list with list of media in the folder, and media number will be based on the
     * media files number. The this.type will be set based on type.
     * 
     * @param path The folder path
     * @param type The video type
     */
    public VideoPlayer(String path, int type) {
        super(path);
        this.type = type;
    }

    /**
     * The play method will play the media player
     */
    public void play() {
        media = new Media(Paths.get(mediaList.get(getType())).toUri().toString());
        player = new MediaPlayer(media);
        player.setCycleCount(-1); // Set the player to stay on loop
        player.play(); // Start player
    }

    /**
     * The setType method sets the type of media
     * 
     * @param type The type of media (NORMAL or RAIN)
     */
    public void setType(int type) {
        this.type = type;
    }

    /**
     * The getType method return the type of media.
     * 
     * @return type of media (int)
     */
    public int getType() {
        return type;
    }

}
