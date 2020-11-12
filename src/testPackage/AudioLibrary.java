package testPackage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AudioLibrary {

    public final String AMBIENT = "src/resource/sound/ambient/";
    public final String MUSIC = "src/resource/sound/music/";
    private List<String> audioList = new ArrayList<String>();
    private File folder;
    private String folderPath;
    private int trackList;

    AudioLibrary() {
        folder = null;
        folderPath = null;
        trackList = 0;
    }

    /**
     * Set folder location and make a list of audio files
     * 
     * @param TYPE decide which folder to use, AMBIENT or MUSIC
     */
    public void setAudioFolder(String TYPE) {
        folderPath = TYPE;
        folder = new File(folderPath);
        String[] audio = folder.list();
        for (String fileName : audio) {
            audioList.add(folderPath + fileName);
        }
        setTrackList(audioList.size());
    }

    public String selectAudio(int i) {
        String audio = audioList.get(i);
        return audio;
    }

    public void setTrackList(int trackList) {
        this.trackList = trackList;
    }

    public int getTrackList() {
        return trackList;
    }

}
