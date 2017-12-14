package gui;

import businesslogik.Playlist;
import com.mpatric.mp3agic.Mp3File;
import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.List;
import javafx.beans.value.ChangeListener;


public class Main extends Application {
	private Label interpret, title;
	private byte[] byteImg;
    private Image img;
    private ImageView iv;
    private Button skipBack, stop, skipFwd, sceneChange, sceneChange2, openM3U, openMultipleMP3;
    private ToggleButton loop, shuffle, play;
    private Slider volumeSlider;
    private VBox topVB;
    private HBox listHBox, buttonHB, progressHB;
	private BorderPane root, listScene;
	private Controller contr;
	private Slider progress;
	private GridPane grid;
	private Label time;
	private int totalFrames;
    public Main(){
    	this.iv = new ImageView();
	}

	private void setStaticGuiElements(){
        skipBack = new Button();
        play = new ToggleButton();
        stop = new Button();
        skipFwd = new Button();
        loop = new ToggleButton();
        shuffle = new ToggleButton();
        volumeSlider = new Slider(0, 100, 50);
        listScene = new BorderPane();
        sceneChange = new Button();
		sceneChange2 = new Button();
		listHBox = new HBox();
		root = new BorderPane();
		topVB = new VBox();
		buttonHB = new HBox();
		progressHB = new HBox();
		contr = new Controller();
		openM3U = new Button("+M3U");
		openMultipleMP3 = new Button("+MP3");
		progress = new Slider();
		grid = new GridPane();
		

    }
	

	private void setObservableGuiElements(Mp3File mp3File) {

		try {
			interpret.setText(mp3File.getId3v2Tag().getArtist());
			title.setText(mp3File.getId3v2Tag().getTitle());
			byteImg = mp3File.getId3v2Tag().getAlbumImage();
			img = new Image(new ByteArrayInputStream(byteImg));
			time.setText(formatTime(mp3File.getLengthInSeconds()));
			progress.setMax(mp3File.getLengthInSeconds());
		} catch (Exception e) {
			System.out.println("Kein Bild geladen");
		} finally {
			if (mp3File.getId3v2Tag() == null) {
				interpret.setText("Unbekannter Interpret");
				title.setText("Unbekannter Titel");
			}
			if (img == null) {
				img = new Image("album-placeholder.png");
			}
		}
		iv.setImage(img);
	}

    private static void configureFileChooser(final FileChooser fileChooser){
        fileChooser.setTitle("Open Playlist");
        fileChooser.setInitialDirectory(
                new File(System.getProperty("user.home"))
        );
    }



	@Override
	public void start(Stage primaryStage) {

		try {
			
			 
            setStaticGuiElements();
			setObservableGuiElements(contr.getMp3Property().get());

			title = new Label();
			title.getStyleClass().add("text");
			interpret = new Label();
			interpret.getStyleClass().add("text");
			time = new Label();
			time.getStyleClass().add("text");
            final ObservableList<String> titleList = contr.getActPlaylist().getPlaylist();
            final ListView<String> titleListView = new ListView(titleList);
            final FileChooser fileChooser = new FileChooser();

			//Top
			topVB.getChildren().addAll(title, interpret);
			topVB.setAlignment(Pos.CENTER);

            //Center
            contr.getMp3Property().addListener((observable, oldValue, newValue) -> {
                setObservableGuiElements(newValue);
            });
			iv.setFitHeight(300);
			iv.setFitWidth(300);
			
			//Right
            titleListView.setOnMouseClicked(event -> {
                if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2){
                    play.setSelected(true);
                    contr.setSongIndex(titleListView.getSelectionModel().getSelectedIndex());
                    contr.playByClick();
                }
            });
			sceneChange.getStyleClass().add("sceneChange");
			sceneChange2.getStyleClass().add("sceneChange");


			
			//Bottom
			skipBack.getStyleClass().add("skpbackbtn");
			play.getStyleClass().add("playbtn");
			skipFwd.getStyleClass().add("skpfwdbtn");
			loop.getStyleClass().add("loopbtn");
			shuffle.getStyleClass().add("shufflebtn");
			stop.getStyleClass().add("stopbtn");
			buttonHB.getChildren().addAll(shuffle,skipBack, play, skipFwd, stop, loop, volumeSlider, sceneChange);
			buttonHB.getStyleClass().add("hbox");
			buttonHB.setAlignment(Pos.BASELINE_CENTER);
			grid.setAlignment(Pos.BASELINE_CENTER);
			progressHB.getChildren().addAll(progress,time);
			progressHB.getStyleClass().add("hbox");
			progressHB.setAlignment(Pos.BASELINE_CENTER);
			progress.setPrefWidth(700);time.getStyleClass().add("text");
			grid.add(buttonHB,0,1);
			grid.add(progressHB,0,0);
			volumeSlider.valueProperty().addListener((observable, oldValue, newValue) -> contr.setVolume(volumeSlider.getValue()));
			volumeSlider.setTooltip(new Tooltip("Volume"));
			
			
		
			
			progress.valueProperty().addListener(new ChangeListener<Number>() {
		      @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {
		        if (newValue == null) {
		          time.setText("");
		          return;
		        }
		        time.setText(formatTime(Math.round(newValue.intValue())));
		      }
		    });
		    time.setPrefWidth(50);
		    
		    
			play.addEventHandler(ActionEvent.ACTION, event -> {
				contr.togglePlaybutton();
			});
			skipBack.addEventHandler(ActionEvent.ACTION, event -> {
			    contr.skipBack();
            });
			skipFwd.addEventHandler(ActionEvent.ACTION, event -> {
			    contr.skipFwd();
            });

			loop.addEventHandler(ActionEvent.ACTION, event -> contr.toggleLoop());
			stop.addEventHandler(ActionEvent.ACTION, event -> {
				contr.stop();
				play.setSelected(false);
			});

			root.setCenter(iv);
			root.setTop(topVB);
			root.setBottom(grid);
			root.getStyleClass().add("root");
			root.setStyle("-fx-background-color: #191919");
			root.setMaxSize(1024,768);

			//SecondScene
			contr.getSongIndex().addListener((observable, oldValue, newValue) ->{
				titleListView.getSelectionModel().select(newValue.intValue());
			});



			openM3U.setOnAction(event -> {
				configureFileChooser(fileChooser);
				FileChooser.ExtensionFilter extFilter =
						new FileChooser.ExtensionFilter("M3U files (*.m3u)", "*.m3u");
				fileChooser.getExtensionFilters().clear();
				fileChooser.getExtensionFilters().add(extFilter);
				File file = fileChooser.showOpenDialog(primaryStage);
				if (file != null) {
					contr.loadPlaylist(file.getAbsolutePath());
				}
			});

			openMultipleMP3.setOnAction(event -> {
				configureFileChooser(fileChooser);
				FileChooser.ExtensionFilter extFilter =
						new FileChooser.ExtensionFilter("MP3 files (*.mp3)", "*.mp3");
				fileChooser.getExtensionFilters().clear();
				fileChooser.getExtensionFilters().add(extFilter);
				List<File> list =
						fileChooser.showOpenMultipleDialog(primaryStage);
				if (list != null) {
					for (File file : list) {
						contr.addSong(file.getAbsolutePath());
					}
				}
			});

			openM3U.getStyleClass().add("noBgBtn");
			openMultipleMP3.getStyleClass().add("noBgBtn");

			listHBox.getChildren().addAll(openM3U, openMultipleMP3, sceneChange2);
			listScene.setTop(listHBox);
			listScene.setCenter(titleListView);
			
			Scene scene = new Scene(root,1024,786);
            Scene scene2 = new Scene(listScene,1024,786);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			scene2.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setTitle("MP3 Player");
			primaryStage.setScene(scene);
			primaryStage.show();
			primaryStage.setOnCloseRequest(event -> contr.closePlayer());

            sceneChange.addEventHandler(ActionEvent.ACTION, event -> primaryStage.setScene(scene2));
			sceneChange2.addEventHandler(ActionEvent.ACTION, event -> primaryStage.setScene(scene));



		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private String formatTime(long seconds) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(Long.toString(seconds / 60)).append(':');
		buffer.append(String.format("%02d", seconds % 60));
		return buffer.toString();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
