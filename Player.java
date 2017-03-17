import java.util.ArrayList;

public class Player {
	ArrayList<Country> territories;
	int noTerritories;
	int noReinforcements;
	Board brd;
	int playerNo;
	boolean isHuman;

	public Player(Board brd, int pn, boolean isHuman) {
		this.brd = brd;
		this.playerNo = pn;
		this.getTerritories();
		this.noReinforcements = this.noTerritories;
		this.isHuman = isHuman;
	}

	public void getTerritories() {
		this.territories = new ArrayList<>();
		this.noReinforcements = 0;

		for (int i = 0; i < this.brd.noCountries; i++) {
			if (this.brd.countries[i].ruler == this.playerNo) {
				this.territories.add(this.brd.countries[i]);
				this.noTerritories++;
				this.noReinforcements++;
				this.brd.countries[i].player = this;
			}
		}
	}

	@Override
	public String toString() {
		String rtn = "";
		rtn += "Player "+this.playerNo+":\n";
		rtn += "Controls " +this.noTerritories+ " territories\n";
		rtn += "Will receive " + this.noReinforcements + " reinforcements\n";
		return rtn;
	}

	public int getCountryIndex(Country c) {
		return this.territories.indexOf(c);
	}

	public void reinforce() {
		int n = 0;
		ArrayList<Country> available = new ArrayList<>();
		for (int i = 0; i < territories.size(); i++) {
			if (territories.get(i).troops < 8) {
				available.add(territories.get(i));
			}
		}

		while (n < noReinforcements) {

			int i = (int) (Math.random() * available.size());
			available.get(i).troops++;
			n++;
			this.brd.display();
			if (territories.get(i).troops > 7) {
				available.remove(i);
			}
		}
	}

	public Country[] chooseMove() {
		Country[] returnable = new Country[2];

		for (int i = 0; i < this.territories.size(); i++) {
			Country temp = this.territories.get(i);
			for (int j = 0; j < temp.neighbors.size(); j++) {
				if (temp.neighbors.get(j).troops <= temp.troops && temp.troops > 1) {
					returnable[0] = temp;
					returnable[1] = temp.neighbors.get(j);
					return returnable;
				}
			}
		}

		return returnable;
	}

}