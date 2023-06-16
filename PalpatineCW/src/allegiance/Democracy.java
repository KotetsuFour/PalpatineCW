package allegiance;

import java.util.ArrayList;
import java.util.List;

import people.Politician;

public class Democracy extends Faction {


	private List<Politician> voters;
	
	public Democracy(String name, long credits, Faction suzerain, int suzerainApproval,
			Faction preferredSuzerain, int preferredSuzerainApproval, boolean organicUnits) {
		super(name, credits, suzerain, suzerainApproval, preferredSuzerain,
				preferredSuzerainApproval, organicUnits);
		this.voters = new ArrayList<>();
	}
	
	public boolean addVoter(Politician p) {
		if (isSubordinateOf(p.getAllegiance())) {
			return false;
		}
		p.setAllegiance(this);
		voters.add(p);
		return true;
	}
	
	public List<Politician> getVoters() {
		return voters;
	}

}
