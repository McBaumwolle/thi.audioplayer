package studiplayer.audio;
import studiplayer.basic.BasicPlayer;

public abstract class SampledFile extends AudioFile {

    public long getDuration() {
        return duration;
    }

    public SampledFile() {
        super();
    }

    public SampledFile(String s) throws NotPlayableException {
        super(s);
    }

    public void play() throws NotPlayableException {
        //play an AudioFile
        try {
            // Code der Exception auslösen kann
            BasicPlayer.play(getPathname());
            } catch (Exception e) {
            e.printStackTrace();
            throw new NotPlayableException(getPathname(), "file not playable");
            }
    }


    //togglePause
    public void togglePause() {
        BasicPlayer.togglePause();
    }

    public void stop() {
        BasicPlayer.stop();
    }


    public String getFormattedDuration() {
        return timeFormatter(duration);
    }

    public String getFormattedPosition() {
        long pos = studiplayer.basic.BasicPlayer.getPosition();
        return TaggedFile.timeFormatter(pos);
    }


    public static String timeFormatter(long microtime) {
        //variables
        String min;
        String sec;

        //less than zero aka negative
        if (microtime < 0L) {
            throw new RuntimeException("Negativ time value provided");
        }

        //maximum value
        if (microtime > 5999999999L) {
            throw new RuntimeException("Time value exceeds allowed format");
        }

        microtime = microtime / 1000000;

        min = String.valueOf(microtime / 60);
        sec = String.valueOf(microtime % 60);

        if (min.length() == 1) {
            min = "0" + min;
        }
        if (sec.length() == 1) {
            sec = "0" + sec;
        }

        return min + ":" + sec;
    }
}
