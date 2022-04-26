package studiplayer.audio;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Scanner;

public class PlayList extends LinkedList<AudioFile> {

    //Attribute
    int current;
    boolean randomOrder;

    public PlayList() {
        super();
        setCurrent(0);
    }

    public PlayList(String pathname) {
        super();
        setCurrent(0);
        try {
			loadFromM3U(pathname);
		} catch (NotPlayableException e) {
			e.printStackTrace();
            System.out.println("PlayList: " + e.getMessage());      //added
		}
    }

    public int getCurrent() {
        return this.current;
    } 

    public void setCurrent(int current) {
        this.current = current;
    }


    public void sort(SortCriterion order) {
        //sort this PlayList according to the given order
        //throw RuntimeException if order is unknown
        if (order == SortCriterion.AUTHOR) {
            Collections.sort(this, new AuthorComparator());
        } else if (order == SortCriterion.TITLE) {
            Collections.sort(this, new TitleComparator());
        } else if (order == SortCriterion.ALBUM) {
            Collections.sort(this, new AlbumComparator());
        } else if (order == SortCriterion.DURATION) {
            Collections.sort(this, new DurationComparator());
        } else {
            throw new RuntimeException("unknown sort criterion");
        }
    }


    public void setRandomOrder(boolean bol) {
        //if randomOrder is true, the order of the list is randomized
        //if randomOrder is false, the order of the list is not randomized
        this.randomOrder = bol;
        if (bol) {
            Collections.shuffle(this);
        } 
    }


    public void changeCurrent() {
        //if current is end of list (or invalid), set current to 0
        //else set current to current + 1
        if (current == this.size()-1 || current < 0 || current >= this.size()) {
            //also check if it's random
            //if it is, shuffle again
            if (randomOrder == true) {
                setCurrent(0);
                setRandomOrder(true);
            } else {
                setCurrent(0);
            } 
        //continue and just go to next one
        } else {
            setCurrent(getCurrent() + 1);
        }
    }

    
    public AudioFile getCurrentAudioFile() {
        //returns null if current index is empty
        if (this.isEmpty()) {
            return null;
        }
        //returns null if current index is out of bounds
        if (current < 0 || current >= this.size()) {
            return null;
        } else {
            return this.get(current);
        }   
    }


    public void saveAsM3U(String pathname){
        FileWriter writer = null;
        String fname = pathname;
        String linesep = System.getProperty("line.separator");

        //save the list as an m3u file
        try {
            writer = new FileWriter(fname);
        for (int i = 0; i < this.size(); i++) {
            //write pathname of each audio file to file
            writer.write(this.get(i).getPathname() + linesep);
        }
        writer.write("\n\n#Matthew " + java.time.LocalDate.now() + linesep); 
    } catch (IOException e) {
            throw new RuntimeException(
                "unable to write to file " + fname + ":" + e.getMessage()); 
        } finally {
            try {
                writer.close();
            } catch (Exception e) {
                //ignore
            }
        }
    }


    public void loadFromM3U(String pathname) throws NotPlayableException {
        //empty list first
        this.clear();
        Scanner scanner = null;
        String line;
        
        try {
            //check if file is readable
            File f = new File(pathname);
            if (!f.canRead()) {
                throw new RuntimeException("file " + pathname + " is not readable");
            }

            //create a scanner
            scanner = new Scanner(new File(pathname));
            //create new AudioFIle
            while (scanner.hasNextLine()) {
                line = scanner.nextLine();
                
                if (line.startsWith("#")) {
                    continue;
                } else if (line.trim().equals("")) {
                    continue;
                } else {
                    //AudioFileFactory.getInstance(line);
                    try {
                        // Code der Exception auslösen kann
                        this.add(AudioFileFactory.getInstance(line));
                        } catch (Exception e) {
                        e.printStackTrace();
                        }
                    
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
            //TODO is this "file not readable or found"?
        } finally {
            try {
                scanner.close();
            } catch (Exception e) {
                //ignore
            }
        }
    }
}