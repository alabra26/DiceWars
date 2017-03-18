import java.util.Scanner;
import java.io.*;

public class LoopMain {

	public static void main(String[] args) {
		int dim = 100;
		int numCountries = 30;
		int numPlayers = 5;
		int numHumans = 0;

		int counter = 100000;

		Writer output = null;

		while (counter < 1000000){

			if (counter%10000 == 0) {

				String f = "Output/output"+(int)(counter/10000)+".txt"; 

				File file = new File(f);
				try {output = new PrintWriter (file);}
				catch (Exception e) {System.out.println("Didn't initialize writer");}
			}

			counter++;



			int winner = -1;

			Board brd = new Board(dim, numCountries, numPlayers, numHumans);

			Scanner n = new Scanner(System.in);
			
			// for (int i = 0; i < 30; i++){
			// 	System.out.println(brd.countries[i].center);
			// }

			boolean someoneWon = false;

			boolean cont = true;
			
			int rounds = 0;

			try {output.write(brd.writeInitToFile());}
			catch (Exception e) {System.out.println("Didn't output initialized state");};

			//main game loop
			while (cont) {
				rounds ++;
				if (rounds > 500) cont = false;
				for (int i = 0; i < brd.noPlayers; i++) {

					if (brd.players[i].isAlive()) {

						boolean turnOver = false;

						//AI takes a turn
						while (!turnOver) {

							Country[] moveChoice = brd.players[i].chooseMove();

							if (moveChoice[0] != null) {
								moveChoice[0].fight(moveChoice[1]);
							}
							else turnOver = true;
						}

						brd.players[i].reinforce();

						if (brd.players[i].noTerritories == brd.noCountries){
							cont = false;
							someoneWon = true;
							winner = i;
						}
					}
				}
			}

			try {output.write(brd.writeWinnerToFile(winner)); output.flush();}
			catch (Exception e) {System.out.println("Didn't output winner");};
		}
	}
}