package businesslogik;

import de.hsrm.mi.prog.util.StaticScanner;
import exceptions.InputException;

public class KeyboardController {
	
	MP3Player player;
	
	public void start(){

		String input;
		String [] commands;
		player = new MP3Player();
		System.out.println("Willkommen im MP3 Player. HELP für eine Auflistung von Anweisungen\n");
		
		do {
			input = StaticScanner.nextString();
			commands = input.split(" ");
			try {
				doCommand(commands);
			} catch (InputException e) {
				e.printStackTrace();
			}
		} while (!commands[0].equalsIgnoreCase("quit"));
	}
	
	public void doCommand(String [] commands)throws InputException{
		if (commands != null){
			commands[0] = commands[0].toLowerCase();
			switch (commands[0]){
				case "playlist":
					if (commands.length > 1) {
						player.loadPlaylist(commands[1]);
					}
				case "play":
					if (commands.length > 1) {
						player.loadSong();
					} else {
						player.play();
					}
					break;
				case "pause":
					player.pause();
					break;
				case "stop":
					player.stop();
					break;
				case "volume":
					if (commands[1] != null) {
						player.volume(Float.parseFloat(commands[1]));
					} else throw new InputException();
					break;
				case "balance":
					if (commands[1] != null) {
						player.balance(Float.parseFloat(commands[1]));
					} else throw new InputException();
					break;
				case "next":
					player.nextSong();
					break;
				case "previous":
					player.previousSong();
					break;
				case "skip":
					if (commands.length > 1) {
						player.skipTo(Integer.parseInt(commands[1]));
					}
					break;
				case "quit":
					System.out.println("Wird beendet");
					player.stop(); // Muss gestoppt werden, damit sich das Programm beendet
					break;
				case "help":
					System.out.println("play + filename\t\tlädt eine MP3-Datei in den Player\n" +
							"play\t\t\tAbspielen der Datei.\npause\t\t\tPausieren der Datei\n" +
							"volume +- value\t\tändert die Lautstärke der Ausgabe\n" +
							"balance +- value\tändert den Balance-Wert der Ausgabe\n" +
							"quit\t\t\tBeendet das Programm");
				default:
					break;
			}
		} else throw new InputException();
	}
}
