package studiplayer.ui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import studiplayer.audio.PlayList;
import studiplayer.audio.AudioFile;
import studiplayer.audio.NotPlayableException;

import java.net.URL;
import java.util.List;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;


public class Player extends Application {
    //variables
	private String songDesc = "no current song";			//songDescription
	private String playT = "--:--";							//playTime
	private volatile boolean stopped;						//playing status 
	private boolean editorVisible;							//state of PlayListEditor
	
	//attribute playListPathname
	private String playListPathname = "";

    //buttons
	Button playButton = createButton("play.png");
	Button pauseButton = createButton("pause.png");
	Button stopButton = createButton("stop.png");
	Button nextButton = createButton("next.png");
	Button editorButton = createButton("pl_editor.png");

    //fields
	BorderPane mainPane = new BorderPane();			//main window apparently
	private Label songDescription = new Label(songDesc);	//label for main text	//songLabel
	private Label playTime = new Label(playT);		//label for time
	HBox buttonlableHBox = new HBox();				//box for buttons and playTime
	
	//other
	private PlayList playList;						//playlist 
	private Stage primaryStage;						//for naming window
	public static final String DEFAULT_PLAYLIST = "C:/Users/matth/OneDrive/Desktop/project5/playlists/myList.m3u";
	//public static final String DEFAULT_PLAYLIST = "C:/Users/matth/OneDrive/Desktop/project5/playlists/DefaultPlayList";
	//public static final String DEFAULT_PLAYLIST = "playlists/DefaultPlayList.m3u";		//for APA
	private static final String startPos = "00:00";
	private static final String currentSong = "current song: ";
	private PlayListEditor playListEditor;			//for the Editor


    public Player() {
		//empty 
	}


	@Override
	public void start(Stage primaryStage) throws Exception {		
		//read from command line arguments
		List<String> parameters = getParameters().getRaw();
		if (parameters.size() > 0) {
			//try to read from command line
			try {
				playList = new PlayList(parameters.get(0));
				//set songDescription to current song
				songDesc = playList.getCurrentAudioFile().toString();
				songDescription.setText(songDesc + " CL");
				playTime.setText(startPos);
				this.primaryStage = primaryStage;
				this.primaryStage.setTitle(currentSong + songDescription.getText());
			} catch (Exception e) {
				//no playlist found
				System.out.println("no playlist found from given path");
				this.primaryStage = primaryStage;
				this.primaryStage.setTitle(songDesc);
			}
		} else {
			//no playlist given
			//try to read from default path
			try {
				playList = new PlayList(DEFAULT_PLAYLIST);
				playListPathname = DEFAULT_PLAYLIST;
				//set songDescription to current song
				songDesc = playList.getCurrentAudioFile().toString();
				songDescription.setText(songDesc);
				playTime.setText(startPos);
				this.primaryStage = primaryStage;
				this.primaryStage.setTitle(currentSong + songDescription.getText());
			} catch (Exception e) {
				//no playlist found
				playListPathname = DEFAULT_PLAYLIST;
				//set empty PlayList
				playList = new PlayList();

				this.primaryStage = primaryStage;
				this.primaryStage.setTitle(songDesc);
			}
		} 

		//place songLabel and move
		mainPane.setTop(songDescription);
		//songLabel.setTranslateX(5);
		//songLabel.setTranslateY(5);
		BorderPane.setMargin(songDescription, new Insets(5, 0, 0, 5));

		//placement and adding of buttons
		buttonlableHBox.getChildren().addAll(playTime, playButton, pauseButton, stopButton, nextButton, editorButton);
		//buttonlableHBox.setAlignment(Pos.BASELINE_CENTER);
		mainPane.setBottom(buttonlableHBox);
		BorderPane.setMargin(buttonlableHBox, new Insets(5, 5, 5, 5));
		HBox.setMargin(playTime, new Insets(20, 10, 10, 5));

		//set initial button state
		setButtonstates(true, false, true, false, true);

		//initiate PlayListEditor and visibility
		editorVisible = false;
		playListEditor = new PlayListEditor(this, this.playList);

		//create and show scene aka window
		Scene scene = new Scene(mainPane, 700, 90);		//700, 90
		primaryStage.setScene(scene);
		primaryStage.show();

		//click actions
		playButton.setOnAction(e -> {
			playCurrentSong();
		});

		pauseButton.setOnAction(e -> {
			pauseCurrentSong();
		});

		stopButton.setOnAction(e -> {
			stopCurrentSong();
		});

		nextButton.setOnAction(e -> {
			nextSong();
		});

		editorButton.setOnAction(e -> {
			if (editorVisible) {
				editorVisible = false;
				playListEditor.hide();
			} else {
				editorVisible = true;
				playListEditor.show();
			}
		});

    }

	//set Action Command for buttons
	void playButton() {
		playCurrentSong();
	}

	void pauseButton() {
		pauseCurrentSong();
	}

	void stopButton() {
		stopCurrentSong();
	}

	void nextButton() {
		nextSong();
	}

	void editorButton() {
		//TODO - duplicated code
		if (editorVisible) {
			editorVisible = false;
			playListEditor.hide();
		} else {
			editorVisible = true;
			playListEditor.show();
		}
	}

	//methods for click actions
    void playCurrentSong() {
		//update labels and button states
		updateSongInfo(playList.getCurrentAudioFile());
		setButtonstates(false, true, true, true, true);

		//play song
		stopped = false;
		if (playList.getCurrentAudioFile() != null) {
			//start threads
			(new TimerThread()).start();
			(new PlayerThread()).start();
		}
	}

	void pauseCurrentSong() {
		//dummy action
		//System.out.println("Pausing " + playList.getCurrentAudioFile().toString());
		//System.out.println("Current index is " + playList.getCurrent());

		if (playList.getCurrentAudioFile() != null) {
			//pause the song
			playList.getCurrentAudioFile().togglePause();
		}

		//TODO - smashing the button fasts the player
		//update button states  
		setButtonstates(false, true, true, true, true);
	}

	void stopCurrentSong() {
		//stop song if stopped is false
		if (!stopped) {
			stopped = true;
			playList.getCurrentAudioFile().stop();
		}
		 
		//update labels and button states
		updateSongInfo(playList.getCurrentAudioFile());
		setButtonstates(true, false, true, false, true);
	}

	void nextSong() {
		//go to next song
		stopCurrentSong();
		playList.changeCurrent();
		playList.getCurrentAudioFile();
		playTime.setText(startPos);
		
		//change labels and button states
		updateSongInfo(playList.getCurrentAudioFile());
		setButtonstates(false, true, true, true, true);

		//start playing new song
		playCurrentSong();
	}

	//set lables 
	private void updateSongInfo(AudioFile af) {
		//if af is null reset
		//otherwise update labels
		if (af == null) {
			songDesc = "no current song";
			songDescription.setText(songDesc);
			this.primaryStage.setTitle("no current song");
			playTime.setText("--:--");
		} else {
			songDesc = af.toString();
			songDescription.setText(songDesc);
			playTime.setText(startPos);
			this.primaryStage.setTitle(currentSong + songDescription.getText());
		}
	}

	//set state of buttons
	private void setButtonstates(boolean playBtSt, boolean stopBtSt, boolean nextBtSt, boolean pauseBtSt, boolean editorBtSt) {
		//set state of buttons
		playButton.setDisable(!playBtSt);
		pauseButton.setDisable(!pauseBtSt);
		stopButton.setDisable(!stopBtSt);
		nextButton.setDisable(!nextBtSt);
	}

	//for testing
	public void setPlayList(String playListPath) {
		if (playListPath == null || playListPath.trim().equals("")) {
			playList = new PlayList(DEFAULT_PLAYLIST);
		} else {
			playList = new PlayList(playListPath);
		}
		refreshUI();
	}

	//refresh the UI
	public void refreshUI() {
		Platform.runLater(() -> {
			if (playList != null && playList.size() > 0) {
				updateSongInfo(playList.getCurrentAudioFile());
				playTime.setText(playT);
				//setButtonstates(false, true, false, true, false);
			} else {
				updateSongInfo(null);
				//positionLabel.setText(startPos);
				setButtonstates(true, true, true, true, false);
			}
		});
	}

	//thread for timer
	private class TimerThread extends Thread {
		public void run() {
			while (!stopped && !playList.isEmpty()) {
				//update label for time
				playT = playList.getCurrentAudioFile().getFormattedPosition();
				refreshUI();
				
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					//e.printStackTrace();
				}
			}
		} 
	}

	//thread for player
	private class PlayerThread extends Thread {
		public void run() {
			while (!stopped && !playList.isEmpty()) {
				//
				try {
					playList.getCurrentAudioFile().play();
					if (!stopped) {
						playList.changeCurrent();
						updateSongInfo(playList.getCurrentAudioFile());
					}
				} catch (NotPlayableException e) {
					//e.printStackTrace();
				}
			}
		} 
	}

	//setter for editorVisible
	public void setEditorVisible(boolean editorVisible) {
		this.editorVisible = editorVisible;
	}

	//getter for playListPathname
	public String getPlayListPathname() {
		return playListPathname;
	}
	


	//main start
    public static void main(String[] args) {
		launch(args);
	}

    //button creator
	private Button createButton(String iconfile) {
		Button button = null;
		try {
			URL url = getClass().getResource("/icons/" + iconfile);
			Image icon = new Image(url.toString());
			ImageView imageView = new ImageView(icon);
			imageView.setFitHeight(48);
			imageView.setFitWidth(48);
			button = new Button("", imageView);
			button.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
		} catch (Exception e) {
			System.out.println("image " + "icons/" + iconfile + " not found!");
			System.exit(-1);
		}
		return button;
	}
    
}