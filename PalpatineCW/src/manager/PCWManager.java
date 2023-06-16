package manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import allegiance.Faction;
import events.Conflict;
import events.Conflict.ConflictType;
import events.PriorityAction;
import locations.InterestPoint;
import locations.InterestPoint.LocationType;
import locations.Navigation;
import locations.World;
import people.Combatant;
import people.Person;
import people.RepresentativeAndCombatant;
import unit.CloneUnit;
import unit.CommonUnit;
import unit.DroidUnit;
import unit.DroidUnit.DroidUnitType;
import unit.OrganicUnit.OrganicUnitType;
import unit.Unit;
import utility.Ship;

public class PCWManager {

	private int daysPast;
	private HashMap<String, World> worlds;
	private ArrayList<World> worldsAsList; //All of the map entries, but in alphabetical order
	private ArrayList<Conflict> conflicts;
	private HashMap<String, Faction> factions;
	private RepresentativeAndCombatant palpatine;
	private Faction republic; //Also stored in the factions map
	private Faction separatists; //Also stored in the factions map
	private Faction banking_clan; //Also stored in the factions map
	private GalaxyGraph galaxyGraph;
	private boolean autoFillShips;
	private boolean isEmpire;
	
	public PCWManager() {
		worlds = new HashMap<>();
		worldsAsList = new ArrayList<>();
		conflicts = new ArrayList<>();
		factions = new HashMap<>();
		initialize();
	}
	
	public int daysPast() {
		return daysPast;
	}
	
	public ArrayList<World> getWorlds() {
		return worldsAsList;
	}
	
	public World getWorldByName(String name) {
		World w = worlds.get(name.toLowerCase());
		if (w == null) {
			throw new IllegalArgumentException("Either this world is not included in the\n"
					+ "list, or you have spelled it incorrectly");
		}
		return w;
	}
	
	public ArrayList<Conflict> getConflicts() {
		return conflicts;
	}
	
	public Faction getRepublic() {
		return republic;
	}
	
	public Faction getSeparatists() {
		return separatists;
	}
	
	public Faction getBankingClan() {
		return banking_clan;
	}
	
	public RepresentativeAndCombatant getPalpatine() {
		return palpatine;
	}
	
	public void createRepublicUnit(String name, OrganicUnitType type, int level) throws IllegalArgumentException {
		long numAvailable = republic.getUtilities().numOfTroopsAtLevel(level);
		long numAssign = type.getCapacity();
		if (numAvailable < numAssign) {
			throw new IllegalArgumentException("Not enough clones to create this unit.");
		}
		if (republic.getUnits().get(name.toLowerCase()) != null) {
			throw new IllegalArgumentException("There is already a clone unit with this name.");
		}
		republic.getUnits().put(name.toLowerCase(),
				new CloneUnit(name, (int) numAssign, level, republic, type));
		republic.getUtilities().getTroops()[level - 1] -= numAssign;
	}

	public void createCommonUnit(Faction faction, String name, OrganicUnitType type, int level)
			throws IllegalArgumentException {
		long numAvailable = faction.getUtilities().numOfTroopsAtLevel(level);
		long numAssign = type.getCapacity();
		if (numAvailable < numAssign) {
			throw new IllegalArgumentException("Not enough soldiers to create this unit.");
		}
		if (faction.getUnits().get(name.toLowerCase()) != null) {
			throw new IllegalArgumentException("There is already a unit with this name.");
		}
		faction.getUnits().put(name.toLowerCase(),
				new CommonUnit(name, (int) numAssign, level, faction, type));
		faction.getUtilities().getTroops()[level - 1] -= numAssign;
	}
	
	public void createDroidUnit(Faction faction, String name, DroidUnitType type, int level)
			throws IllegalArgumentException {
		long numAvailable = faction.getUtilities().numOfTroopsAtLevel(level);
		long numAssign = type.getCapacity();
		if (numAvailable < numAssign) {
			throw new IllegalArgumentException("Not enough droids to create this unit.");
		}
		if (faction.getUnits().get(name.toLowerCase()) != null) {
			throw new IllegalArgumentException("There is already a droid unit with this name.");
		}
		faction.getUnits().put(name.toLowerCase(),
				new DroidUnit(name, (int) numAssign, level, faction, type));
		faction.getUtilities().getTroops()[level - 1] -= numAssign;
	}

	public void disbandUnit(Unit u) {
		u.getAllegiance().getUtilities().getTroops()[u.getLevel() - 1] += u.getNumSoldiers();
		u.reset();
		u.getAllegiance().getUnits().remove(u.getName().toLowerCase());
	}
	
	public List<Person> getPalpatinesSubordinates() {
		return palpatine.getSubordinates();
	}
	
	public List<Person> getSubordinatesOf(Person p) {
		return p.getSubordinates();
	}
	
	public void startConflict(Faction init, Faction defd, ConflictType type) {
		Conflict c = new Conflict(init, defd, type);
		conflicts.add(c);
		Faction ius = init.getUltimateSuzerain();
		Faction dus = defd.getUltimateSuzerain();
		if (init != ius) {
			ius.getCurrentRepresentative().getPriorities().add(new PriorityAction(c, init));
		}
		if (defd != defd.getUltimateSuzerain()) {
			dus.getCurrentRepresentative().getPriorities().add(new PriorityAction(c, defd));
		}
	}
	
	public List<Combatant> getCombatantsOfFaction(Faction f) {
		return f.getCombatants();
	}
	
	public boolean assignUnitToCombatant(Combatant c, Unit u) {
		if (c.currentlyCommanding() + u.getCommandValue() > c.getCommandCapacity()) {
			return false;
		}
		if (u.unitType() != c.getUnitType()) {
			return false;
		}
		c.assignUnit(u);
		if (autoFillShips) {
			int shipCapacity = c.getTotalShipCapacity();
			int numUnits = c.getUnitSize();
			List<Ship> set = c.getAllegiance().getUtilities().getShips();
			while (shipCapacity < numUnits && !set.isEmpty()) {
				c.addShip(set.remove(set.size() - 1)); //The last one should have the greatest capacity
			}
		}
		return true;
	}
	
	public void assignShipToCombatant(Combatant c, Ship s) {
		c.addShip(s);
	}
	
	public void autoAssignShips(boolean choice) {
		autoFillShips = choice;
	}
	
	public boolean sendCombatantToLocation(Combatant c, InterestPoint ip) {
//		if (c.getLocation().getWorld() == ip.getWorld()) {
//			ip.sendHere(c);
//			return true;
//		}
		List<InterestPoint> route = findRoute(c.getLocation(), ip, c);
		if (route == null) {
			return false;
		}
		c.setRoute(route);
		return true;
	}
	
	public List<InterestPoint> findRoute(InterestPoint cur, InterestPoint dest, Person traveler) {
		return Navigation.findPath(galaxyGraph, cur, dest, traveler);
	}
	
	public void endDaySequence() {
		//TODO before
		Iterator<Conflict> cit = conflicts.iterator();
		while (cit.hasNext()) {
			Conflict c = cit.next();
			if (c.isStillActive()) {
				c.iterate();
			} else {
				cit.remove();
			}
		}
		//TODO move all people who are moving
		//TODO after
	}
	
	public boolean checkVictory() {
		if (separatists.isSubordinateOf(republic) && isEmpire
				&& republic.getCombatants().size() <= 2) {
			return true;
		}
		return false;
	}
	
	public boolean checkDefeat() {
		if (!palpatine.isAlive() || republic.isSubordinateOf(separatists)
				// || the republic won, but you don't have enough approval
				) {
			return true;
		}
		return false;
	}
	
	public void initialize() {
		//Worlds first
		InterestPoint coruscantSpace = new InterestPoint("Coruscant Space", null, 0, LocationType.SPACE);
		World coruscant = new World("Coruscant", coruscantSpace);
		worlds.put(coruscant.getName().toLowerCase(), coruscant);
		InterestPoint repSenate = new InterestPoint("Republic Senate Building", null, 0, LocationType.STRUCTURE);
		coruscant.addInterestPoint(repSenate);
		InterestPoint repMilitary = new InterestPoint("Republic Center for Military Operations", null, 0, LocationType.STRUCTURE);
		coruscant.addInterestPoint(repMilitary);
		InterestPoint jediTemple = new InterestPoint("Jedi Temple", null, 0, LocationType.STRUCTURE);
		coruscant.addInterestPoint(jediTemple);
		
		//Then factions
		republic = new Faction("Galactic Republic", 1000000, null, 0, null, 0, true);
		factions.put(republic.getName(), republic);
		separatists = new Faction("Confederacy of Independent Systems", 1000000, null, 0, null, 0, false);
		factions.put(separatists.getName(), separatists);
		banking_clan = new Faction("Intergalactic Banking Clan", 10000000, null, 0, separatists, 50, false);
		factions.put(banking_clan.getName(), banking_clan);
		
		//Then assign factions to locations
		repSenate.setOwner(republic);
		repMilitary.setOwner(republic);
		jediTemple.setOwner(republic);
		
		//Then people
		palpatine = new RepresentativeAndCombatant("Sheev Palpatine", OrganicUnitType.LEGION.getCapacity(),
				0, null, republic, 500, 13, 2, LocationType.STRUCTURE, repSenate, true, 100, 80, 100, 85, 90,
				80, "palpatine");
		
	}

}
