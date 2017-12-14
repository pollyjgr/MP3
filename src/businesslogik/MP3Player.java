package businesslogik;

import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;
import de.hsrm.mi.eibo.simpleplayer.SimpleAudioPlayer;
import de.hsrm.mi.eibo.simpleplayer.SimpleMinim;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;

public class MP3Player {

    private float db;
    private boolean loop, paused, init;
    private SimpleIntegerProperty songIndex;
    private Thread playingCheckerThread;
    private Mp3File mp3file = null;
    private SimpleMinim minim;
    private SimpleAudioPlayer audioPlayer;
    private Playlist actPlaylist;
    private SimpleStringProperty title;
    private SimpleObjectProperty<Mp3File> propMp3 = null;
    private SimpleIntegerProperty position;

    public MP3Player() {
        this.actPlaylist = new Playlist();
        this.minim = new SimpleMinim(true);
        this.loop = false;
        this.paused = true;
        this.songIndex = new SimpleIntegerProperty(0);
        this.propMp3 = new SimpleObjectProperty<>();
        this.title = new SimpleStringProperty();
        this.init = false;
        this.position = new SimpleIntegerProperty();
    }

    public void loadPlaylist(String name) {
        actPlaylist.setPlaylist(name);
        loadSong();
    }

    public void addSong(String name){
        actPlaylist.addSong(name);
    }

    public void loadSong() {
        try {
            minim.stop();
            audioPlayer = minim.loadMP3File(actPlaylist.getSongPath(songIndex));
            setMp3();
            if (!paused) {
                play();
            }
        } catch (Exception e) {
            System.out.println("No file loaded");
        }
    }

    public synchronized void play() {
        if (!init) {
            loadSong();
        }
        init = true;
        paused = false;
        if (!audioPlayer.isPlaying() || !paused) {
            try {
                audioPlayer.setGain(db);
                audioPlayer.play();
            } catch (Exception e) {
                System.out.println("No file loaded");
            }
            this.playingCheckerThread = new Thread(() -> {
                while (audioPlayer.isPlaying() && !playingCheckerThread.isInterrupted()) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        System.out.println("Sorry ich schlafe");
                        playingCheckerThread.interrupt();
                    }
                }
                if ((actPlaylist != null) && !playingCheckerThread.isInterrupted()) {
                    if (!(audioPlayer.isPlaying()) && !paused) {
                        Platform.runLater(() -> nextSong());
                    }
                }
            });
            this.playingCheckerThread.start();
        }
    }

    public void pause() {
        paused = true;
        playingCheckerThread.interrupt();
        audioPlayer.pause();
    }

    public void stop() {
        if (init){
            playingCheckerThread.interrupt();
        }
        paused = true;
        loadSong();
    }

    public void closePlayer() {
        if (init) {
            playingCheckerThread.interrupt();
            minim.stop();
        } else {
            minim.stop();
        }
    }

    public void nextSong() {
        if (init){
            playingCheckerThread.interrupt();
        }
        if (songIndex.get() < actPlaylist.getPlaylistLength() - 1) {
            songIndex.set(songIndex.get() + 1);
            loadSong();
        } else {
            if (loop) {
                songIndex.set(0);
                loadSong();
            } else {
                System.out.println("Playlist zu Ende");
            }

        }
    }

    public void previousSong() {
        if (init){
            playingCheckerThread.interrupt();
        }
        if (songIndex.get() > 0) {
            songIndex.set(songIndex.get() - 1);
            loadSong();
        } else {
            if (loop) {
                songIndex.set(actPlaylist.getPlaylistLength() - 1);
                loadSong();
            } else {
                System.out.println("Playlist zu Ende");
            }
        }
    }

    public void setLoop() {
        loop = !loop;
    }

    public void volume(float value) {
        if(audioPlayer != null) {
            double volume = value / 100;
            db = (float) (Math.log(volume) / Math.log(10.0) * 20);
            audioPlayer.setGain(db);
        }

    }

    public void balance(float value) {
        audioPlayer.setBalance(value);
    }

    public void setMp3() {
        try {
            mp3file = new Mp3File(audioPlayer.getMetaData().fileName());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (UnsupportedTagException e) {
            e.printStackTrace();
        } catch (InvalidDataException e) {
            e.printStackTrace();
        }

        propMp3.setValue(mp3file);

        title.setValue(mp3file.getId3v2Tag().getTitle());
    }

    public SimpleObjectProperty<Mp3File> getMp3() {
        return propMp3;
    }

    public void skipTo(int milliseconds) {
        audioPlayer.skip(milliseconds);
    }

    public Playlist getActPlaylist(){
        return actPlaylist;
    }

    public SimpleIntegerProperty getSongIndex() {
        return songIndex;
    }

    public void setSongIndex(int index) {
        songIndex.set(index);
    }
    
    public int getPosition(){
    	return audioPlayer.position();
    }
   


    
    
    
}

