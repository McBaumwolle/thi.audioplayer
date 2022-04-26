package studiplayer.audio;
import studiplayer.basic.WavParamReader;

public class WavFile extends SampledFile{
    
    public String[] fields() {
        return new String[]{getAuthor(), getTitle(), "", getFormattedDuration()};
    }

    public WavFile() {
        super();
    }

    public WavFile(String s) throws NotPlayableException {
        super(s);
        readAndSetDurationFromFile(getPathname());
    }

    //long and float
    public static long computeDuration(float numberOfFrames, float framerate) {
        return (long) (numberOfFrames * 1000000 / framerate);
    }

    public void readAndSetDurationFromFile(String pathname) throws NotPlayableException {

        try {
            // Code der Exception auslösen kann
            WavParamReader.readParams(pathname);
            } catch (Exception e) {
            e.printStackTrace();
            throw new NotPlayableException(getPathname(), "wav file not playable");     //try
            }

        float framerate = WavParamReader.getFrameRate();
        float numberOfFrames = WavParamReader.getNumberOfFrames();
        duration = computeDuration(numberOfFrames, framerate);
    }

    //toString
    public String toString() {
        return super.toString().trim() + " - " + getFormattedDuration();
    }
}
