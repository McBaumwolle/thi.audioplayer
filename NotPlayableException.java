package studiplayer.audio;

public class NotPlayableException extends Exception {

    //Attribute
    private String pathname;

    public NotPlayableException (String pathname, String msg) {
        super(msg);
        this.pathname = pathname;
    }

    public NotPlayableException (String pathname, Throwable t) {
        super(t);
        this.pathname = pathname;
    }

    public NotPlayableException (String pathname, String msg, Throwable t) {
        super(msg, t);
        this.pathname = pathname;
    }

    //overrides toString() with 'pathname + ": " + super.toString()'
    @Override
    public String toString() {
        //super.toString();
        return pathname + ": " + super.toString();
    }
}

