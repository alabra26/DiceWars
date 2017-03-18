import java.io.*;

public class InReader {
	
	private FileReader reader = null;
	public int numCountries;
	public int numPlayers;

	public int[][] connectionMatrix;
	public int[][] ownerMatrix;
	public int[] troopMatrix;
	public int[] winner;

	public InReader(String path, int numCountries, int numPlayers) {
		try {
			File file = new File(path);
			this.reader = new FileReader(file);
		} catch (Exception ignored) {System.out.println("Failed to initialize FileReader");}

		this.numCountries = numCountries;
		this.numPlayers = numPlayers;

		this.connectionMatrix = new int[numCountries][numCountries];
		this.ownerMatrix = new int[numCountries][numPlayers];
		this.troopMatrix = new int[numCountries];
		this.winner = new int[numPlayers];
	}

	public void readAllIn() {

		for (int i = 0; i < numCountries; i++) {
			for (int j = 0; j < numCountries; j++) {
				char x = 0;
				try{x = (char)reader.read();}catch(Exception e){}
				connectionMatrix[i][j] = Character.getNumericValue(x);
			}
		}

		for (int i = 0; i<numCountries; i++) {
			for (int j = 0; j < numPlayers; j++) {
				char x = 0;
				try{x = (char)reader.read();}catch(Exception e){}
				ownerMatrix[i][j] = Character.getNumericValue(x);
			}
		}

		for (int i = 0; i<numCountries; i++) {
			char x = 0;
			try{x = (char)reader.read();}catch(Exception e){}
			troopMatrix[i] = Character.getNumericValue(x);
		}

		for (int i = 0; i < this.numPlayers; i++) {
			char x = 0;
			try{x = (char)reader.read();}catch(Exception e){}
			winner[i] = Character.getNumericValue(x);
		}
		


	}


	public static void main(String[] args) {
		InReader ir = new InReader("Output/output.txt", 30, 5);
		ir.readAllIn();

		for (int i = 0; i < ir.numPlayers; i++) {
			System.out.print(ir.winner[i]);
		}
		System.out.println();
	}

}