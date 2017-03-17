import java.util.ArrayList;

public class Player {
	ArrayList<Country> territories;
	int noTerritories;
	int noReinforcements;
	Board brd;
	int playerNo;

	public Player(Board brd, int pn) {
		this.brd = brd;
		this.playerNo = pn;
		this.getTerritories();
		this.noReinforcements = this.noTerritories;
	}

	public void getTerritories() {
		territories = new ArrayList<>();
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
			if (territories.get(i).troops == 8) {
				available.remove(i);
			}
		}
	}
}