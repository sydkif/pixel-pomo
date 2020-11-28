package test2Package;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MediaLibrary {

    public File folder;
    public int mediaFile;
    public List<String> mediaList = new ArrayList<String>();

    public MediaLibrary() {
        folder = null;
        mediaFile = 0;
        mediaList = null;
    }

    public MediaLibrary(String path) {
        folder = new File(path);
        String[] media = folder.list();
        for (String mediaName : media) {
            mediaList.add(path + mediaName);
        }
        setMediaFile(mediaList.size());
    }

    public void setFolder(File folder) {
        this.folder = folder;
    }

    public void setMediaFile(int mediaFile) {
        this.mediaFile = mediaFile;
    }

    public void setMediaList(List<String> mediaList) {
        this.mediaList = mediaList;
    }

    public File getFolder() {
        return folder;
    }

    public int getMediaFile() {
        return mediaFile;
    }

    public List<String> getMediaList() {
        return mediaList;
    }

}
