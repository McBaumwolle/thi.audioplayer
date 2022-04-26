package studiplayer.audio;
import java.io.File;
import java.util.LinkedList;
import java.util.List;

public abstract class AudioFile {
    // Attribute
	private String filename;
	private String pathname;
	protected String author;
	protected String title;
    protected long duration;
    private char separatorChar = System.getProperty("file.separator").charAt(0);

    //constructor
    List<AudioFile> liste = new LinkedList<AudioFile>();

    //constructor
    public AudioFile() { 
        
    }

    //constructor with pathname
    public AudioFile(String pathname) throws NotPlayableException {
        this.pathname = pathname;
        parsePathname(pathname);
        parseFilename(pathname);
        getFilename();

        //create new file
        File file = new File(getPathname());
        
        //checks if file is readable
        if(file.canRead()) {
            //nothing all good
        } else {
            throw new NotPlayableException(getPathname(), "cannot read file");
        }
    }


    @Override
    public String toString() {
        //if getAuthor() == null then only return title
        //else return author + " - " + title
        if (getAuthor().equals("")) {
            return getTitle().trim();
        } else {
            return getAuthor().trim() + " - " + getTitle().trim();     
        }

    }

    //Getter
    public String getFilename() {
        return filename;
    }

    public String getPathname() {
    	return pathname;
    }

    public String getAuthor() {
        return author.trim();
    }

    public String getTitle() {
        return title.trim();
    }


    //gibt Dateiname und Pfad zurueck
    public void parsePathname(String str) {
    	//when Windows
    	if (isWindows()) {
			str = str.replace('/', '\\');
            //reduce multiple backslashes to one
            while (str.contains("\\\\")) {
                str = str.replace("\\\\", "\\");
            }
        //when Unix
		} else {
			str = str.replace('\\', '/');
            
            //reduce multiple forward slashes to one
            while (str.contains("//")) {
                str = str.replace("//", "/");
            }
		}

        //ad leading slahes if necessary
        if (!isWindows() && str.length() >= 2) {
			char char1 = str.charAt(0);
			char char2 = str.charAt(1);
			char1 = Character.toLowerCase(char1);       
			
			if (char1 >= 'a' && char1 <= 'z' && char2 == ':') {
                char1 = Character.toUpperCase(char1);
				str = "/" + char1 + str.substring(2);
			}
		}

        //set pathname
        pathname = str;

        //set filename 
        int lastslash = str.lastIndexOf(separatorChar);
		
		if (lastslash == -1) {
			// kein Pfad
			filename = str;
		} else {
			filename = str.substring(lastslash + 1);
		}
    }

    //gibt Autor und Titel zurueck
    public void parseFilename(String str) {

        //filename = str;

        //analyse sting
        int lastMinus = filename.lastIndexOf(" - ");        //edited
        int lastDot = filename.lastIndexOf('.');
        int lastSlash = filename.lastIndexOf(separatorChar);

        
        if (filename.isEmpty()) {
            title = "";
            author = "";
        }

        else if (filename.equals("-")) {
            title = "-";                    //"-"
            author = "";
        } else if (filename.trim().equals("-") && filename.charAt(0) == ' ') {
            title = "";
            author = "";
        } else if (lastSlash == lastDot - 1) {
            title = "";
            author = "";
        } else if (filename.charAt(filename.length()-1) == '\\') {
            title = "";
            author = "";
        } else if (lastDot == -1) {
            if (lastMinus == -1) {
                title = "";
                author = "";
            } else if (lastMinus == 0) {
                title = "-";
                author = "";
            } else {
                title = "";
                author = "";
            }

        } else if (lastMinus == -1) {
            //kein -
            if (lastDot == 0) {
                //kein - aber . auf 0
                title = "";
                author = "";
            } else {
                //ein '-'' aber kein '.'' an 0
                title = filename.substring(0, lastDot);
                author = "";
                
            }

        } else {            
            if (lastDot == -1) {
                //hat kein '.'
                title = filename.substring(lastMinus + 3).trim();       //edited
                author = filename.substring(0, lastMinus).trim();
            } else {
                //hat ein '.'
                title = filename.substring(lastMinus + 3, lastDot).trim();  //editd
                author = filename.substring(0, lastMinus).trim();
                
            }
        }
    }
    

    //for OS testing
    //für APA false
    private static boolean isWindows() {
		return true;
	}
    

    public abstract void play() throws NotPlayableException;
    public abstract void togglePause();
    public abstract void stop();
    public abstract String getFormattedDuration();
    public abstract String getFormattedPosition();
    
    public abstract String[] fields();

}

