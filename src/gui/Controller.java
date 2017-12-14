package gui;


import businesslogik.MP3Player;
import businesslogik.Playlist;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import com.mpatric.mp3agic.Mp3File;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

public class Controller {
    private boolean playing;
    private MP3Player player;
    private SimpleObjectProperty<Mp3File> song = null;

    
    public Controller(){
        this.player = new MP3Player();
        this.playing = false;
        this.player.loadPlaylist("default.m3u");
        this.song = new SimpleObjectProperty<>();
      
    }

    public void togglePlaybutton(){
        if(!playing){
            player.play();
        } else {
            player.pause();
        }
        playing = !playing;

    }

    public void playByClick(){
        player.loadSong();
        player.play();
        playing = true;
    }

    public void skipFwd() {
        player.nextSong();
    }

    public void skipBack() {
        player.previousSong();
    }

    public void toggleLoop() {
        player.setLoop();
    }

    public void stop(){
        player.stop();
        playing = false;
    }

    public void closePlayer(){
        player.closePlayer();
        playing = false;
    }

    public SimpleObjectProperty<Mp3File> getMp3Property() {
        return player.getMp3();
    }

    public Playlist getActPlaylist() {
        return player.getActPlaylist();
    }

    public SimpleIntegerProperty getSongIndex(){
        return player.getSongIndex();
    }

    public void setSongIndex(int index) {
        player.setSongIndex(index);
    }

    public void setVolume(double volume){

        player.volume((float) volume);
    }

    public void loadPlaylist(String playlist){
        player.loadPlaylist(playlist);
    }

    public void addSong(String name) {
        player.addSong(name);
    }
    
    public int getPosition(){
    	return player.getPosition();
    }
    
    	

}