import java.util.*;
import java.awt.Color;
import java.awt.Font;

public class Board {

	public int[][] tiles;
	public Point[][] pointTiles;
	public int dim;
	public Country[] countries;
	public int noCountries;
	public int noPlayers;
	public Player[] players;

	public Board(int dim, int noCountries, int noPlayers) {
		this.tiles = new int[dim][dim];
		this.pointTiles = new Point[dim][dim];
		this.dim = dim;
		this.noCountries = noCountries;
		this.noPlayers = noPlayers;
		players = new Player[noPlayers];
		initialize();
	}

	public void initialize() {


		int count = 0;
	    int tiles_filled = 1;
	    int i = this.dim / 2;
	    int j = this.dim / 2;

	    this.tiles[i][j] = -1;

	    while (tiles_filled < (this.dim*this.dim) / 2){
	    	
			if (i < 0) i = 0;
			if (i >= this.dim) i = this.dim-1;
			if (j < 0) j = 0;
			if (j >= this.dim) j = this.dim-1;
	    	// count++;
	    	// if (count == 10000) {
	    	// 	i = (int)(Math.random() * this.dim);
	     //    	j = (int)(Math.random() * this.dim);
	    	// 	count = count % 10000;
	    	// }
	    	Random r = new Random();
	    	if (Math.random() < 0.5) {
				if(r.nextBoolean()){
     				i += -1;
				} else i += 1;

	      	} else {
				if(r.nextBoolean()){
     				j += -1;
				} else j += 1;
	      	}
	      	
	      	if (i<this.dim && i > 0 && j < this.dim && j > 0){
	        	if (this.tiles[i][j] == 0) {
	        		this.tiles[i][j] = -1;
	          		tiles_filled += 1;
        		}
      		}
		}

		countries = new Country[noCountries];
		int ptr = 0;

		PriorityQueue<Point> tileQueue = new PriorityQueue<>();

		//create 30 random country centers and queue them
		while (ptr < noCountries) {
			int tx = (int)(Math.random()*this.dim);
			int ty = (int)(Math.random()*this.dim);
			if (this.tiles[tx][ty] == -1){
				countries[ptr] = new Country(tx, ty, ptr, this);
				tileQueue.add(countries[ptr].center);
				this.tiles[tx][ty] = 2;
				ptr++;
				// this.pointTiles[tx][ty] = countries[ptr].center;
			}
		}

		int[][] checks = {{-1,0},{0,1},{1,0},{0,-1}};

		while (!tileQueue.isEmpty()) {
			Point p = tileQueue.poll();
			this.pointTiles[p.x][p.y] = p;

			for (int a = 0; a<4; a++) {
				i = checks[a][0];
				j = checks[a][1];
				
				if (!(p.x+i >= this.dim || p.x+i < 0 || p.y+j >= this.dim || p.y+j < 0) && (this.tiles[p.x+i][p.y+j] == -1)) { 
					this.tiles[p.x+i][p.y+j] = 1;
					
					Point temp = new Point(p.x+i, p.y+j, p.distFromCtr + 1, p.country);

					p.country.land.add(temp);
					p.country.area++;

					tileQueue.add(temp);
				}
			}
		}

		ArrayList<Integer> rulersToAssign = new ArrayList<>();
		for (i = 0; i < this.countries.length; i++) {
			rulersToAssign.add(i % this.noPlayers);
		}

		Collections.shuffle(rulersToAssign);

		for (i = 0; i < this.countries.length; i++) {
			countries[i].getNeighbors();
			countries[i].findCenter();
			countries[i].setRuler(rulersToAssign.get(i));
		}

		for (i = 0; i < this.noPlayers; i++) {
			players[i] = new Player(this, i, (i==0));
		}

		System.out.println(players[0]);
	}

	public void display() {

		this.drawBasic();
		StdDraw.show();
		StdDraw.pause(100);

	}

	public void display(Player person) {

		this.drawBasic();
		int whoseTurn = person.playerNo;

		Font font = new Font("Arial", Font.BOLD, 30);
   		StdDraw.setFont(font);

		StdDraw.setPenColor(StdDraw.BLACK);
		for (int i = 0; i < this.noCountries; i++) {
			if (this.countries[i].ruler == whoseTurn && this.countries[i].troops > 1) {
				StdDraw.text(this.countries[i].center.x + 0.5, this.countries[i].center.y + 0.5, 
						Integer.toString(person.getCountryIndex(this.countries[i])));
			}
		}
		double[] xs = {this.dim + 9, this.dim+9.5, this.dim+9};
		double[] ys = {this.dim-10-3*person.playerNo - 0.3,this.dim-10-3*person.playerNo + 0.2, this.dim-10-3*person.playerNo + 0.7};
		StdDraw.filledPolygon(xs, ys);
		StdDraw.text(this.dim + 12, 8, "Type number of");
		StdDraw.setPenColor(200,0,0);
		StdDraw.text(this.dim + 12, 6.5, "country to attack from");
		StdDraw.setPenColor(StdDraw.BLACK);
		StdDraw.text(this.dim + 12, 5, "(type -1 to end turn)");
		
		StdDraw.show();
		StdDraw.pause(100);
	}

	public void display (Player person, Country country) {

		this.drawBasic();

		int whoseTurn = person.playerNo;

		Font font = new Font("Arial", Font.BOLD, 30);
   		StdDraw.setFont(font);

		StdDraw.setPenColor(StdDraw.BLACK);
		for (int i = 0; i < country.neighbors.size(); i++) {
			if (country.neighbors.get(i).ruler != country.ruler)
				StdDraw.text(country.neighbors.get(i).center.x + 0.5, country.neighbors.get(i).center.y + 0.5, Integer.toString(i));
		}

		double[] xs = {this.dim + 9, this.dim+9.5, this.dim+9};
		double[] ys = {this.dim-10-3*person.playerNo - 0.3,this.dim-10-3*person.playerNo + 0.2, this.dim-10-3*person.playerNo + 0.7};
		StdDraw.filledPolygon(xs, ys);

		int x = country.center.x;
		int y = country.center.y;
		StdDraw.setPenColor(StdDraw.BLACK);
		StdDraw.filledEllipse(x,y,0.5,0.5);

		StdDraw.text(this.dim + 12, 8, "Type number of");
		StdDraw.setPenColor(200,0,0);
		StdDraw.text(this.dim + 12, 6.5, "country to attack");
		StdDraw.show();
		StdDraw.pause(100);

	}

	public void drawBasic() {
		for (int i = 0; i < this.dim; i++) {
			for (int j = 0; j < this.dim; j++) {

				if (this.pointTiles[i][j]!= null) {
					int ruler = this.pointTiles[i][j].country.ruler;

					int[] rgb = this.getRGB((int)(ruler*(360/noPlayers) + 5),(float)0.8,(float)0.5);

					StdDraw.setPenColor((int)rgb[0],(int)rgb[1], (int)rgb[2]);
					
					StdDraw.filledRectangle(i+0.5, j+0.5, 0.5, 0.5);

					//draw a red line at country borders	
					StdDraw.setPenRadius(0.01);
					StdDraw.setPenColor(StdDraw.WHITE);
					if (i < dim-1 && this.pointTiles[i][j] != null && this.pointTiles[i+1][j] != null &&
						this.pointTiles[i][j].country.id != this.pointTiles[i+1][j].country.id) 
						StdDraw.line(i+1, j, i+1, j+1);
					if (j < dim-1 && this.pointTiles[i][j] != null && this.pointTiles[i][j+1] != null &&
						this.pointTiles[i][j].country.id != this.pointTiles[i][j+1].country.id) 
						StdDraw.line(i, j+1, i+1, j+1);
				} else {
					StdDraw.setPenColor(StdDraw.WHITE);
					StdDraw.filledRectangle(i+0.5, j+0.5, 0.5, 0.5);
				}
			} 
		}

		double[][] combos = {{0,1},{-1,0},{1,0},{0,-1},{-1.1,-1.1},{-1.1,1.1},{-1.1,-1.1},{1.1,-1.1}};

		StdDraw.setPenColor(StdDraw.BLACK);
		for (int i = 0; i < noCountries; i++) {
			if (countries[i] != null)
			for (int j = 0; j < countries[i].troops; j++) {
				StdDraw.filledEllipse(countries[i].center.x + 1.8 +combos[j][0]*0.5, countries[i].center.y + combos[j][1]*0.5 + 0.5, 0.3,0.3);
			}
		}
		
		Font font = new Font("Arial", Font.PLAIN, 20);
		StdDraw.setFont(font);
		for (int i = 0; i < noPlayers; i++) {
			int[] rgb = this.getRGB((int)(i*(360/noPlayers) + 5),(float)0.8,(float)0.5);

			StdDraw.setPenColor((int)rgb[0],(int)rgb[1], (int)rgb[2]);
			StdDraw.filledRectangle(this.dim + 12, this.dim-10-3*i, 10, 1);
			StdDraw.setPenColor(StdDraw.BLACK);
			StdDraw.text(this.dim + 12, this.dim-10-3*i, "Player "+i + " : " + this.players[i].noReinforcements);
		}

		StdDraw.setPenColor(StdDraw.WHITE);
		StdDraw.filledRectangle(this.dim + 12, 0, 10, 10);
	}

	public int[] getRGB(int h, float s, float b){

		int[] returnable = new int[3];

		int[] x = {(int)(255*(s)), (int)(255*(s)), (int)(255*(s))};

		float t1; float t2;
		if (s == 0) return x;

		if (b < 0.5) t1 = b * (1+s);
		else t1 = (b+s) - (b*s);

		t2 = 2*b - t1;

		float hue = ((float)h / 360);


		float tr, tb, tg;

		tr = (float)((hue + 0.333) % 1);
		tb = (float)((hue));
		tg = (float)((hue - 0.333 + 1) % 1);

		x[0] = (int)(255*tr);
		x[1] = (int)(255*tb);
		x[2] = (int)(255*tg);
		// return x;

		//set Red
		if (6*tr < 1) returnable[0] = (int) (255 * ((t2 + (t1-t2) * 6 * tr) ));
		else if (2*tr<1) returnable[0] = (int) (255 * ((t1) ));
		else if (3*tr<2) returnable[0] = (int) (255 * ((t2 + (t1-t2) * (0.666 - tr) * 6) ));
		else returnable[0] = (int) (255 * (t2));

		//set Green
		if (6*tg < 1) returnable[1] = (int) (255 * ((t2 + (t1-t2) * 6 * tg) ));
		else if (2*tg<1) returnable[1] = (int) (255 * ((t1)));
		else if (3*tg<2) returnable[1] = (int) (255 * ((t2 + (t1-t2) * (0.666 - tg) * 6)));
		else returnable[1] = (int) (255 * (t2));

		//set Blue
		if (6*tb < 1) returnable[2] = (int) (255 * ((t2 + (t1-t2) * 6 * tb)));
		else if (2*tb<1) returnable[2] = (int) (255 * ((t1)));
		else if (3*tb<2) returnable[2] = (int) (255 * ((t2 + (t1-t2) * (0.666 - tb) * 6)));
		else returnable[2] = (int) (255 * (t2));

		return returnable;
	}
}






