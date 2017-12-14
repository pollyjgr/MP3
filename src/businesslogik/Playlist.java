package businesslogik;

import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Playlist {
    private ObservableList<String> playlist, titleList;
    private BufferedReader reader;
    private Mp3File track = null;


    public Playlist() {
        this.playlist = FXCollections.observableArrayList();
        this.titleList = FXCollections.observableArrayList();

    }

    public void readM3UFile(String path) {
        String filePath = path;
        File file = new File(filePath);

        try {
            String line;
            reader = new BufferedReader(new FileReader(file));

            while (null != (line = reader.readLine())){
                if (!line.contains("#EXTINF") && !line.contains("EXTM3U")){
                    playlist.add(line);
                    track = new Mp3File(line);
                    titleList.add(track.getId3v2Tag().getTitle());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidDataException e) {
            e.printStackTrace();
        } catch (UnsupportedTagException e) {
            e.printStackTrace();
        } finally {
            if (null != reader) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        for(Iterator<String> it = playlist.iterator(); it.hasNext();) {
            System.out.println(it.next().toString());
        }
    }

    public void addSong(String path){
        try {
            track = new Mp3File(path);
        } catch  (UnsupportedTagException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidDataException e) {
            e.printStackTrace();
        }
        playlist.add(path);
        titleList.add(track.getId3v2Tag().getTitle());
    }

    public void setPlaylist(String name) {
        playlist.clear();
        titleList.clear();
        readM3UFile(name);
    }

    public ObservableList<String> getPlaylist() {
        return titleList;
    }

    public String getSongPath(SimpleIntegerProperty songindex) {
        return playlist.get(songindex.get());
    }

    public int getPlaylistLength(){
        return playlist.size();
    }
}
