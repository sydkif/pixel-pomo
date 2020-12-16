package test2Package;

import java.nio.file.Paths;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class VideoPlayer extends MediaLibrary {

    public static int NORMAL = 1;
    public static int RAIN = 0;
    private int type;
    public Media media;
    public MediaPlayer player;

    public VideoPlayer() {
        super();
        type = NORMAL;
    }

    public VideoPlayer(String path, int type) {
        super(path);
        this.type = type;
    }

    public void play() {
        media = new Media(Paths.get(mediaList.get(getType())).toUri().toString());
        player = new MediaPlayer(media);
        player.setCycleCount(-1);
        player.play();
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

}
