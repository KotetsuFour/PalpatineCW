package allegiance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import locations.InterestPoint;
import people.Combatant;
import people.Person;
import people.Politician;
import unit.Unit;
import utility.Purchasable.PurchasableType;
import utility.UtilitySet;

public class Faction {
	
	private String name;

	private UtilitySet utilities;
	
	private List<Politician> leaders;
	
	private List<Politician> representatives;
	
	private Faction suzerain;
	
	private int suzerainApproval;
	
	private Faction preferredSuzerain; //Only relevant if there are no representatives
	
	private int preferredSuzerainApproval; //Only relevant if there are no representatives
	
	private List<Faction> subordinates;
	
	private List<Combatant> combatants;
	
	private HashMap<String, Unit> units;
	
	private boolean organicUnits;
	
	private HashMap<String, InterestPoint> locations;
	
	private List<Faction> autoRecognizedEnemies;
	
	public static final String HQ = "HQ";
	
	public static final String BARRACKS = "Barracks";
	
	public static final String STORAGE = "Storage";
	
	public Faction(String name, long credits, Faction suzerain, int suzerainApproval,
			Faction preferredSuzerain, int preferredSuzerainApproval, boolean organicUnits) {
		this.name = name;
		this.suzerain = suzerain;
		this.suzerainApproval = suzerainApproval;
		this.preferredSuzerain = preferredSuzerain;
		this.preferredSuzerainApproval = preferredSuzerainApproval;
		this.organicUnits = organicUnits;
		this.utilities = new UtilitySet();
		utilities.receiveCredits(credits);
		this.leaders = new ArrayList<>();
		this.representatives = new ArrayList<>();
		this.subordinates = new ArrayList<>();
		this.combatants = new ArrayList<>();
		this.units = new HashMap<>();
		this.locations = new HashMap<>();
		this.autoRecognizedEnemies = new ArrayList<>();
	}

	public String getName() {
		return name;
	}
	
	public boolean isSubordinateOf(Faction f) {
		if (f == this) {
			return true;
		}
		Faction check = getSuzerain();
		while (check != null) {
			if (check == f) {
				return true;
			}
			check = check.getSuzerain();
		}
		return false;
	}
	
	public boolean addSubordinate(Faction f) {
		if (isSubordinateOf(f)) {
			return false;
		}
		f.suzerain = this;
		subordinates.add(f);
		return true;
	}
	
	public Faction getSuzerain() {
		return suzerain;
	}
	
	public void annex(Faction f) {
		f.suzerain = this;
		subordinates.add(f);
		f.suzerainApproval = 10;
		if (f.getCurrentLeader() != null) {
			Politician p = f.getCurrentRepresentative();
			p.setLoyalty(100 - p.getStubbornness());
		}
	}
	
	public Faction getUltimateSuzerain() {
		Faction ret = this;
		while (ret.getSuzerain() != null) {
			ret = ret.getSuzerain();
		}
		return ret;
	}
	
	public boolean isEnemyOf(Faction f) {
		Faction ourRuler = getUltimateSuzerain();
		Faction theirRuler = f.getUltimateSuzerain();
		return ourRuler.hasConflictWith(theirRuler);
	}
	
	private boolean hasConflictWith (Faction f) {
		return autoRecognizedEnemies.contains(f);
	}
	
	public void addSubordinate(Faction f, int approval) {
		subordinates.add(f);
		f.suzerain = this;
		f.suzerainApproval = approval;
	}
	
	public List<Faction> getSubordinates() {
		return subordinates;
	}
	
	public Politician getCurrentLeader() {
		if (leaders.isEmpty()) {
			if (representatives.isEmpty()) {
				return null;
			}
			return representatives.get(0);
		}
		return leaders.get(0);
	}
	
	public Politician getCurrentRepresentative() {
		if (representatives.isEmpty()) {
			if (leaders.isEmpty()) {
				return null;
			}
			return leaders.get(0);
		}
		return representatives.get(0);
	}
	
	public void appointLeader(Politician p) {
		//Assumes that the list of leaders is empty
		representatives.add(p);
		p.getFactionsRepresented().add(this);
	}
	
	public void appointRepresentative(Politician p) {
		//Assumes that the list of representatives is empty
		representatives.add(p);
		p.getFactionsRepresented().add(this);
	}
	
	public void removeCurrentRepresentative() {
		//Assumes this is a legal action
		representatives.remove(0);
	}
	
	public void setPreferredSuzerain(Faction f) {
		preferredSuzerain = f;
	}
	
	public UtilitySet getUtilities() {
		return utilities;
	}
	
	public List<InterestPoint> getPointsThatSellExport(PurchasableType type) {
		List<InterestPoint> points = new ArrayList<>(locations.size());
		for (int q = 0; q < locations.size(); q++) {
			if (locations.get(q).getExportType() == type) {
				points.add(locations.get(q));
			}
		}
		return points;
	}
	
	public List<Combatant> getCombatants() {
		return combatants;
	}
	
	public Map<String, Unit> getUnits() {
		return units;
	}
	
	public boolean usesOrganicUnits() {
		return organicUnits;
	}
	
	public Map<String, InterestPoint> getLocations() {
		return locations;
	}
	
	public void sendTo(String place, Unit u) {
		locations.get(name + place).sendHere(u);
	}
	
	public void sendTo(String place, Person p) {
		locations.get(name + place).sendHere(p);
	}
	
}
