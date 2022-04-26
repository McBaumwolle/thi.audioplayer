package studiplayer.audio;
import java.util.Comparator;

public class TitleComparator implements Comparator<AudioFile> {
    //first check if AudioFiles have tags or are null
    public int compare(AudioFile o1, AudioFile o2) {
        //if o1 is null, return -1
        if (o1 == null) {
            return -1;
        }
        //if o2 is null, return 1
        if (o2 == null) {
            return 1;
        }

        //if o1 is not an AudioFile, return -1
        if (!(o1 instanceof AudioFile)) {
            return -1;
        }
        //if o2 is not an AudioFile, return 1
        if (!(o2 instanceof AudioFile)) {
            return 1;
        }

        //compare both valid AudioFiles
        if (o1.getTitle().compareTo(o2.getTitle()) < 0) {
            return -1;
        } else if (o1.getTitle().compareTo(o2.getTitle()) > 0) {
            return 1;
        } else {
            return 0;
        }
    }
}
