import java.util.Scanner;
import java.io.*;

public class TempMain {

	public static void main(String[] args) {
		int dim = 100;
		int numCountries = 30;
		int numPlayers = 5;
		int numHumans = 0;

		Writer output = null;

		String f = "Output/output.txt"; 

		File file = new File(f);
		try {output = new PrintWriter (file);}
		catch (Exception e) {System.out.println("Didn't initialize writer");}

		

		//kill all humans :D

		// StdDraw.setCanvasSize(1200,800);
		// StdDraw.setYscale(-1,dim + 2);
		// StdDraw.setXscale(-1,dim*1.5 + 2);
		// StdDraw.enableDoubleBuffering();

		int winner = -1;

		Board brd = new Board(dim, numCountries, numPlayers, numHumans);

		Scanner n = new Scanner(System.in);
		
		// brd.display();

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
					// System.out.println(brd.players[i]);

					boolean turnOver = false;

					brd.players[1].getTerritories();

					// Human takes a turn
					if (brd.players[i].isHuman) while (!turnOver){
						brd.display(brd.players[i]);
						Country defense = null, offense = null;
						int x;
						boolean done = false;
						
						while (!done) {
							x  = n.nextInt();

							if (x == -1) {
								turnOver = true;
								done = true;
								break;
							}

							try {
								offense = brd.players[i].territories.get(x);
								if (offense.troops > 1 && offense.hasValidTarget())
									done = true;
								else System.out.println("Invalid choice");
							} catch (Exception e) {
								System.out.println("Invalid choice");
							}
						}
						if (!turnOver) {

							brd.display(brd.players[i], offense);

							done = false;

							while (!done) {
								x  = n.nextInt();
								try {
									defense = offense.neighbors.get(x);
									done = true;
								} catch (Exception e) {
									System.out.println("Invalid choice");
								}
							}

							offense.fight(defense);
						}
					} 

					//AI takes a turn
					else while (!turnOver) {
						// brd.display();

						Country[] moveChoice = brd.players[i].chooseMove();

						if (moveChoice[0] != null) {
							moveChoice[0].fight(moveChoice[1]);
							// brd.display(brd.players[i]);
							// brd.display(brd.players[i], moveChoice[0]);
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