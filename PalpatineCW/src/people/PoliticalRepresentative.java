package people;

import java.util.ArrayList;
import java.util.List;

import allegiance.Democracy;
import allegiance.Faction;
import events.PriorityAction;
import locations.InterestPoint;

public class PoliticalRepresentative implements Politician {

	private String name;
	
	private Person master;
	
	private List<Person> subordinates;
	
	private Faction allegiance; //Not the faction they lead, but the one they are under
	
	private int loyalty; //Percentage level of loyalty to their faction
	
	private List<Faction> factionsRepresented; //Factions that this politician represents
	
	private int persuasiveness;
	
	private int stubbornness;
	
	private int respectability; //Maybe use this for planetary civil wars, or just delete it
	
	private int survivability;
	
	private double warVotingPreference; //Percentage of time they will vote for a pro-war bill
	
	private InterestPoint location;
	
	private int actionsLeft;
	
	private List<InterestPoint> route;
	
	private List<PriorityAction> priorities;
	
	private String imageName;
	
	public PoliticalRepresentative(String name, Person master, Faction allegiance, int loyalty, int persuasiveness,
			int stubbornness, int respectability, int survivability, double warVotingPreference, InterestPoint location,
			String imageName) {
		this.name = name;
		this.master = master;
		this.allegiance = allegiance;
		this.loyalty = loyalty;
		this.persuasiveness = persuasiveness;
		this.stubbornness = stubbornness;
		this.respectability = respectability;
		this.survivability = survivability;
		this.warVotingPreference = warVotingPreference;
		this.location = location;
		this.imageName = imageName;
		this.subordinates = new ArrayList<>();
		this.factionsRepresented = new ArrayList<>();
		this.priorities = new ArrayList<>();
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Person getMaster() {
		return master;
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
	public Faction getAllegiance() {
		if (allegiance == null) {
			if (factionsRepresented.isEmpty()) {
				return null;
			} else {
				return factionsRepresented.get(0);
			}
		}
		return allegiance;
	}
	
	@Override
	public Faction mainFactionRepresented() {
		if (factionsRepresented.isEmpty()) {
			return null;
		} else {
			return factionsRepresented.get(0);
		}
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
	public void setLoyalty(int loyalty) {
		this.loyalty = loyalty;
	}
	
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
	public int getGeneralRespectability() {
		return respectability;
	}
	
	@Override
	public double getWarVotingPreference() {
		return warVotingPreference;
	}

	@Override
	public void setLocation(InterestPoint ip) {
		location = ip;
	}

	@Override
	public void setAllegiance(Faction f) {
		this.allegiance = f;
	}
	
	@Override
	public void alterWarVotingPreference(double change) {
		warVotingPreference += change;
	}

	@Override
	public List<Person> getSubordinates() {
		return subordinates;
	}

	@Override
	public InterestPoint getLocation() {
		return location;
	}

	@Override
	public void setRoute(List<InterestPoint> route) {
		this.route = route;
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
	public InterestPoint getDestination() {
		if (route == null || route.isEmpty()) {
			return null;
		}
		return route.get(0);
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
		// TODO Auto-generated method stub
	}

	@Override
	public String imageName() {
		return imageName;
	}
	

}
