package studiplayer.audio;

public class AudioFileFactory {
    
    public static AudioFile getInstance(String pathname) throws NotPlayableException {
        //if .wav file, return WavFile
        //if .mp3 or .ogg file, return TaggedFile
        String fileEnd = pathname.substring(pathname.length() - 3).toLowerCase();

        if (fileEnd.equals("wav")) {
            return new WavFile(pathname);
        } else if (fileEnd.equals("mp3") || fileEnd.equals("ogg")) {
            return new TaggedFile(pathname);
        } else {
            throw new NotPlayableException(pathname, "unknow suffix for AudioFile - " + fileEnd);
        }
    }
}
