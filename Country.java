import java.util.*;

public class Country {
	Point center;
	ArrayList<Point> land = new ArrayList<Point>();
	int area;
	int ruler;
	double id;
	ArrayList<Country> neighbors = new ArrayList<Country>();
	Board brd;
	Player player;
	int troops;


	public Country(int x, int y, int ruler, Board b) {
		this.ruler = ruler;
		this.center = new Point(x,y,0,this);
		area = 1;
		land.add(this.center);
		this.id = Math.random();
		this.brd = b;
		this.troops = (int)(Math.random() * 4) + 1;
	}

	@Override
	public String toString() {
		return "Center: ("+this.center.x+","+this.center.y+") \tSize: "+this.area;
	}

	public void setRuler(int r) {
		this.ruler = r;
	}

	public void getNeighbors() {

		int[][] checks = {{-1,0},{0,1},{1,0},{0,-1}};

		for (int pt = 0; pt < land.size(); pt++) {

			Point temp = land.get(pt);
			for (int a = 0; a<4; a++) {
				int i = checks[a][0];
				int j = checks[a][1];
				if (temp.x+i < this.brd.dim && temp.y+j < this.brd.dim && temp.y+j > -1 && temp.x+i > -1 &&
						this.brd.pointTiles[temp.x+i][temp.y+j] != null && this.brd.pointTiles[temp.x+i][temp.y+j].country != temp.country) {
					Country c = this.brd.pointTiles[temp.x+i][temp.y+j].country;
					if (!this.neighbors.contains(c)) neighbors.add(c);
				}
			}
		}
	}

	@Override
	public int hashCode() {
		return (int)(this.id*100000);
	}

	public int getNeighborIndex(Country c) {
		return this.neighbors.indexOf(c);
	}

	public void findCenter() {
		int xsum = 0;
		int ysum = 0;
		for (int i = 0; i < area; i++) {
			xsum += this.land.get(i).x;
			ysum += this.land.get(i).y;
		}
		this.center = new Point((int)(xsum/area), (int)(ysum/area), 0, this);
	}

	public void fight(Country other) {

		int thisSum = this.rollDice();
		int otherSum = other.rollDice();

		if (thisSum > otherSum) {
			System.out.println("Offensive country wins!");
			other.ruler = this.ruler;
			other.player = this.player;
			other.troops = this.troops - 1;
			this.troops = 1;

			this.player.getTerritories();
			other.player.getTerritories();
		} else {
			System.out.println("Defensive country wins!");
			this.troops = 1;
		}

	}

	public int rollDice() {
		int sum = 0;
		for (int i = 0; i < this.troops; i++) {
			sum += (int)(Math.random() * 6) + 1;
		}
		return sum;
	}

}

