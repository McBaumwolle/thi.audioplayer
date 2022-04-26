package studiplayer.audio;

import java.util.Map;
import studiplayer.basic.TagReader;

public class TaggedFile extends SampledFile {

    //Attribute    
    protected String album;

    public String[] fields() {
        return new String[]{getAuthor(), getTitle(), getAlbum(), getFormattedDuration()};
    }

    public TaggedFile() {
        super();
    }

    //takes pathname
    public TaggedFile(String s) throws NotPlayableException {
        super(s);
        readAndStoreTags();
    }

    public String getAlbum() {
        /*if (album == null) {
            return "";
        } else {
            return album.trim();
        }*/
        return album;
    }
    
    //toString() for TaggedFile
    public String toString() {
        if (album == null || album.isEmpty()) {
            return super.toString().trim() + " - " + getFormattedDuration();

        } else {
            return super.toString().trim() + " - " + album.trim() + " - " + getFormattedDuration();
        }
    }
    
    //read the tags from an AudioFile if pissoble
    public void readAndStoreTags() throws NotPlayableException {
        //variables
        Map<String, Object> tags;

        try {
            // Code der Exception auslösen kann
            tags = TagReader.readTags(getPathname());

            String titleFile = (String) tags.get("title");
            String artistFile = (String) tags.get("author");
            String albumFile = (String) tags.get("album");
            duration = (long) tags.get("duration");

            if (titleFile != null && !titleFile.isEmpty() && !titleFile.equals("0")) {
                title = titleFile;
            }

            if (artistFile != null && !artistFile.isEmpty() && !artistFile.equals("0")) {
                author = artistFile;
            }

            ////TODO else if empty - not in getter
            if (albumFile != null && !albumFile.isEmpty() && !albumFile.equals("0")) {
                album = albumFile;
            } else if (albumFile == null) {
                album = "";
            }
            } catch (Exception e) {
            e.printStackTrace();
            throw new NotPlayableException(getPathname(), "file not playable");     //try
            }
    }
}
