// FILENAME: AudioPlayer.java

import java.nio.file.Paths;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/**
 * AudioPlayer class store the data of playing status, current media value,
 * media type value, current volume, JavaFX Media and MediaPlayer. This class
 * also inherit data from MediaLibrary class.
 */
public class AudioPlayer extends MediaLibrary {

    // Constant for type of audio files
    public static final int AMBIENT = 0;
    public static final int MUSIC = 1;
    public static final int ALARM = 2;

    private boolean playing; // Status of the player
    private int currentMedia; // The value of current media that being played
    private int mediaType; // The media type
    private double currentVolume = 0.5; // The volume, default at 50%
    public Media media; // JavaFX Media
    public MediaPlayer player; // JavaFX MediaPlayer

    /**
     * The no-arg constructor initializes an object with null for folder, 0 for
     * media file and null for media list. The current media set to 0, and the media
     * type seb to ambient as default.
     */
    public AudioPlayer() {
        super();
        currentMedia = 0;
        mediaType = AMBIENT;
    }

    /**
     * This constructor initializes an object with a string path for folder, media
     * list with list of media in the folder, and media number will be based on the
     * media files number. The current media set to 0 and media type with type.
     * 
     * @param path The folder path
     * @param type The audio type
     */
    public AudioPlayer(String path, int type) {
        super(path);
        currentMedia = 0;
        mediaType = type;
    }

    /**
     * The play method will play the media player.
     */
    public void play() {

        if (isPlaying()) {

            if (player.isMute()) {
                player.setMute(false);
            } else {
                player.setMute(true);
            }

        } else {
            media = new Media(Paths.get(mediaList.get(getCurrentMedia())).toUri().toString());
            player = new MediaPlayer(media);
            player.setCycleCount(-1); // Set the player to stay on loop
            player.play(); // Start player

            if (getMediaType() == 1) { // If it is MUSIC
                player.setOnEndOfMedia(new Runnable() { // At the end of each media
                    @Override
                    public void run() {
                        next(); // Play the next file in the folder.
                    }
                });
            }
            if (getMediaType() == 2) // ALARM, max volume
                player.setVolume(1);
            else // MUSIC or AMBIENT, user control
                player.setVolume(currentVolume);
            setPlaying(true);

        }
    }

    /**
     * The stop method will stop the media player.
     */
    public void stop() {
        player.stop();
        setPlaying(false);
    }

    /**
     * The next method will play the next media available inside the folder.
     */
    public void next() {
        stop();
        if ((currentMedia + 1) < getMediaFile())
            setCurrentMedia(++currentMedia);
        else
            currentMedia = 0;
        if (player.isMute()) {
            play();
            player.setMute(true);
        } else {
            play();
        }

    }

    /**
     * The changeTo method will change the current played media file to other media
     * file based on num.
     * 
     * @param num the value of selected media
     */
    public void changeTo(int num) {
        stop();
        setCurrentMedia(num);
        play();
    }

    /**
     * The setMediaType method sets the type of media.
     * 
     * @param mediaType the type of media (AMBIENT or MUSIC or ALARM)
     */
    public void setMediaType(int mediaType) {
        this.mediaType = mediaType;
    }

    /**
     * The setCurrentMedia method sets the current media.
     * 
     * @param currentMedia the value of current media
     */
    public void setCurrentMedia(int currentMedia) {
        this.currentMedia = currentMedia;
    }

    /**
     * The setCurrentVolume method sets the current volume.
     * 
     * @param currentVolume the value of current volume
     */
    public void setCurrentVolume(double currentVolume) {
        this.currentVolume = currentVolume;
        player.setVolume(currentVolume);
    }

    /**
     * The setPlaying method sets the player status.
     * 
     * @param playing player status
     */
    public void setPlaying(boolean playing) {
        this.playing = playing;
    }

    /**
     * The getMediaType method returns the media type.
     * 
     * @return type of media (int)
     */
    public int getMediaType() {
        return mediaType;
    }

    /**
     * The getCurrentMedia method returns the current media.
     * 
     * @return current media (int)
     */
    public int getCurrentMedia() {
        return currentMedia;
    }

    /**
     * The getCurrentVolume method returns the current volume.
     * 
     * @return current volume (double)
     */
    public double getCurrentVolume() {
        return currentVolume;
    }

    /**
     * The isPlaying method returns the current player status.
     * 
     * @return playing status
     */
    public boolean isPlaying() {
        return playing;
    }

    /**
     * The title method will display the tile of current media file based on the
     * file metadata
     * 
     * @return current media title from its metadata
     */
    public String title() {
        // return (String) media.getMetadata().get("title");
        // return mediaList.get(currentMedia).substring(MainGUI.MUSIC.length());
        return mediaList.get(currentMedia).substring(MainGUI.MUSIC.length(),
                (mediaList.get(currentMedia).length() - 4));
    }

}
