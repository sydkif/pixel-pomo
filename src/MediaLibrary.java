// FILENAME: MediaLibrary.java

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * The MediaLibrary class store data of a folder, number of media files and a
 * list of media files.
 */
public class MediaLibrary {

    public File folder; // File folder
    public int mediaFile; // Media files number
    public List<String> mediaList = new ArrayList<String>(); // Media files list

    /**
     * The no-arg constructor initializes folder with null, media file with 0 and
     * media list with null.
     */
    public MediaLibrary() {
        folder = null;
        mediaFile = 0;
        mediaList = null;
    }

    /**
     * The constructor initializes folder with an object File() with the folder path
     * string, the media list will be a list of full path of each media file in the
     * folder, the media file number will be based on the media list size.
     */
    public MediaLibrary(String path) {
        folder = new File(path);
        String[] media = folder.list();
        for (String mediaName : media)
            mediaList.add(path + mediaName);
        setMediaFile(mediaList.size());
    }

    /**
     * The setFolder method sets the File folder path.
     * 
     * @param folder The file folder path
     */
    public void setFolder(File folder) {
        this.folder = folder;
    }

    /**
     * 
     * The setMediaFile method sets the number of media files inside the folder.
     * 
     * @param mediaFile The number of media files
     */
    public void setMediaFile(int mediaFile) {
        this.mediaFile = mediaFile;
    }

    /**
     * The setMediaList method sets the list of media files inside the folder.
     * 
     * @param mediaList The list of media files
     */
    public void setMediaList(List<String> mediaList) {
        this.mediaList = mediaList;
    }

    /**
     * The getFolder method return the folder path.
     * 
     * @return folder path
     */
    public File getFolder() {
        return folder;
    }

    /**
     * The getMediaFile method return number of media files.
     * 
     * @return number of media files
     */
    public int getMediaFile() {
        return mediaFile;
    }

    /**
     * The getMediaList method return list of media files.
     * 
     * @return list of of media files
     */
    public List<String> getMediaList() {
        return mediaList;
    }

}
