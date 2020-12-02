package test2Package;

import java.nio.file.Paths;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class AudioPlayer extends MediaLibrary {

    public static final int AMBIENT = 0;
    public static final int MUSIC = 1;
    private boolean playing;
    private int currentMedia;
    private int mediaType;
    private double currentVolume = 0.5; // Default volume at 50%
    public Media media;
    public MediaPlayer player;

    public AudioPlayer() {
        super();
        currentMedia = 0;
        mediaType = AMBIENT;
    }

    public AudioPlayer(String path, int type) {
        super(path);
        currentMedia = 0;
        mediaType = type;
    }

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
            player.setCycleCount(-1);
            player.play();

            if (getMediaType() == 1) {
                player.setOnEndOfMedia(new Runnable() {
                    @Override
                    public void run() {
                        next();
                    }
                });
            }

            player.setVolume(currentVolume);
            setPlaying(true);

        }

    }

    public void stop() {
        player.stop();
        setPlaying(false);
    }

    public void next() {
        stop();
        if ((currentMedia + 1) < getMediaFile())
            setCurrentMedia(++currentMedia);
        else
            currentMedia = 0;
        play();
    }

    public void changeTo(int num) {
        stop();
        setCurrentMedia(num);
        play();
    }

    public String title() {
        return (String) media.getMetadata().get("title");
    }

    public void setMediaType(int mediaType) {
        this.mediaType = mediaType;
    }

    public void setCurrentMedia(int currentMedia) {
        this.currentMedia = currentMedia;
    }

    public void setCurrentVolume(double currentVolume) {
        this.currentVolume = currentVolume;
        player.setVolume(currentVolume);
    }

    public void setPlaying(boolean playing) {
        this.playing = playing;
    }

    public int getMediaType() {
        return mediaType;
    }

    public int getCurrentMedia() {
        return currentMedia;
    }

    public double getCurrentVolume() {
        return currentVolume;
    }

    public boolean isPlaying() {
        return playing;
    }

}
