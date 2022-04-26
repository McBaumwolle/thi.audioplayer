package studiplayer.audio;
import java.util.Comparator;

public class AlbumComparator implements Comparator<AudioFile> {
    //compare two AudioFile objects
    //return -1 if o1 is less than o2
    //return 1 if o1 is greater than o2
    //return 0 if o1 is equal to o2
    public int compare(AudioFile o1, AudioFile o2) {
        //if o1 is null, throw new NullPointerException
        if (o1 == null) {
            throw new NullPointerException("o1 is null");
        }

        //if o2 is null, throw new NullPointerException
        if (o2 == null) {
            throw new NullPointerException("o2 is null");
        }

        //if o1 is not TaggedFile and o2 is TaggedFile, return -1
        if (!(o1 instanceof TaggedFile) && o2 instanceof TaggedFile) {
            return -1;
        }

        //if o1 is TaggedFile and o2 is not TaggedFile, return 1
        if (o1 instanceof TaggedFile && !(o2 instanceof TaggedFile)) {
            return 1;
        }

        //if both are not TaggedFile, return 0
        if (!(o1 instanceof TaggedFile) && !(o2 instanceof TaggedFile)) {
            return 0;
        }

        //if both are TaggedFile, compare album
        if (o1 instanceof TaggedFile && o2 instanceof TaggedFile) {
            //if o1's album is null, return -1
            if (((TaggedFile) o1).getAlbum() == null) {
                return -1;
            }
            //if o2's album is null, return 1
            if (((TaggedFile) o2).getAlbum() == null) {
                return 1;
            }
            //if o1's album is not null and o2's album is not null, compare album
            //if o1's album is less than o2's album, return -1
            //if o1's album is greater than o2's album, return 1
            //if o1's album is equal to o2's album, return 0
            if (((TaggedFile) o1).getAlbum().compareTo(((TaggedFile) o2).getAlbum()) < 0) {
                return -1;
            } else if (((TaggedFile) o1).getAlbum().compareTo(((TaggedFile) o2).getAlbum()) > 0) {
                return 1;
            } else {
                return 0;
            }
        }

        //to find error 
        //return 3;    
        throw new RuntimeException("cannot compare AudioFiles - album");    
    }
}
