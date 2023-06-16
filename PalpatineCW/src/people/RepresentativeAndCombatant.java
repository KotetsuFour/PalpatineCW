package people;

import java.util.ArrayList;
import java.util.List;

import allegiance.Democracy;
import allegiance.Faction;
import events.PriorityAction;
import locations.InterestPoint;
import locations.InterestPoint.LocationType;

public class RepresentativeAndCombatant extends Combatant implements Politician {
	
	private int loyalty;
	
	private int persuasiveness;
	
	private int stubbornness;
	
	private int respectability;
	
	private int survivability;
	
	private double warVotingPreference;
	
	private int actionsLeft;
	
	private List<PriorityAction> priorities;
	
	private List<Faction> factionsRepresented;

	public RepresentativeAndCombatant(String name, int commandCapacity, int unitType, Person master, Faction allegiance,
			int power, int strategy, int powerGrowthRate, LocationType specialty, InterestPoint location,
			boolean forceSensitive, int loyalty, int persuasiveness, int stubbornness,
			int respectability, int survivability, double warVotingPreference, String imageName) {
		super(name, commandCapacity, unitType, master, allegiance, power, strategy, powerGrowthRate, specialty, location,
				forceSensitive, imageName);
		this.loyalty = loyalty;
		this.persuasiveness = persuasiveness;
		this.stubbornness = stubbornness;
		this.respectability = respectability;
		this.survivability = survivability;
		this.warVotingPreference = warVotingPreference;
		this.factionsRepresented = new ArrayList<>();
	}

	@Override
	public List<Faction> getFactionsRepresented() {
		return factionsRepresented;
	}

	@Override
	public int getPersuasiveness() {
		return persuasiveness;
	}

	@Override
	public int getStubbornness() {
		return stubbornness;
	}

	@Override
	public double getWarVotingPreference() {
		return warVotingPreference;
	}

	@Override
	public void alterWarVotingPreference(double change) {
		warVotingPreference += change;
	}

	@Override
	public int getActions() {
		return actionsLeft;
	}

	@Override
	public void resetActions() {
		actionsLeft = ACTIONS_PER_DAY;
	}

	@Override
	public List<Politician> getAllVoters() {
		List<Politician> ret = new ArrayList<>();
		for (Faction f : getFactionsRepresented()) {
			if (f instanceof Democracy) {
				ret.addAll(((Democracy)f).getVoters());
			} else {
				ret.add(f.getCurrentLeader());
			}
		}
		return ret;
	}

	@Override
	public List<PriorityAction> getPriorities() {
		return priorities;
	}
	
	@Override
	public void addPriority(PriorityAction pa) {
		Politician p = this;
		while (p.getMaster() != null && p.getMaster() instanceof Politician) {
			p = (Politician) p.getMaster();
		}
		//TODO if Palpatine, add to queue. Otherwise, just answer it immediately
	}
	
	@Override
	public void die() {
		//TODO
	}

	@Override
	public int getLoyalty() {
		return loyalty;
	}

	@Override
	public void alterLoyalty(int change) {
		loyalty += change;
	}

	@Override
	public int getGeneralRespectability() {
		return respectability;
	}

	@Override
	public void setLoyalty(int loyalty) {
		this.loyalty = loyalty;
	}

	@Override
	public Person getUltimateMaster() {
		if (master == null) {
			return this;
		}
		Person ret = master;
		while (ret.getMaster() != null) {
			ret = ret.getMaster();
		}
		return ret;
	}

	@Override
	public boolean isSubordinateOf(Person p) {
		if (this == p) {
			return true;
		}
		Person check = master;
		while (check != null) {
			if (check == p) {
				return true;
			}
		}
		return false;
	}

	@Override
	public Faction mainFactionRepresented() {
		if (factionsRepresented.isEmpty()) {
			return null;
		} else {
			return factionsRepresented.get(0);
		}
	}

}
