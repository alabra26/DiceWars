import java.util.Scanner;

public class TempMain {

	public static void main(String[] args) {
		int dim = 45;

		StdDraw.setCanvasSize(1200,800);
		StdDraw.setYscale(-1,dim + 2);
		StdDraw.setXscale(-1,dim*1.5 + 2);
		StdDraw.enableDoubleBuffering();

		Board brd = new Board(dim, 30, 5);

		Scanner n = new Scanner(System.in);
		
		brd.display();

		boolean cont = true;
		

		//main game loop
		while (cont) {
			for (int i = 0; i < brd.noPlayers; i++) {
				boolean turnOver = false;
				while (!turnOver){
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
							if (offense.troops > 1) 
								done = true;
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

				brd.players[i].reinforce();
			}
		}




	}
}