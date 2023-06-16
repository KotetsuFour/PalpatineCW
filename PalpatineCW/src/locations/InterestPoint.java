package locations;

import java.util.ArrayList;
import java.util.List;

import allegiance.Faction;
import people.Combatant;
import people.Person;
import people.Politician;
import random.RNGStuff;
import unit.Unit;
import utility.Purchasable;
import utility.Purchasable.PurchasableType;
import utility.ReliefSupplies;
import utility.UtilitySet;

public class InterestPoint {

	private String name;
	private int structuralStability; //Percentage
	private PurchasableType export;
	private int priceOfExport;
	private LocationType locationType;
	private World world;
	private Faction owner;
	private int populationHealth; //Percentage
	//All politicians and combatants
	private List<Person> peoplePresent;
	//Only counts units not being led directly by a combatant
	private List<Unit> freeUnitsPresent;
	private List<Person> prisoners;
	private List<Combatant> blockadingCombatants;
	
	public InterestPoint(String name, PurchasableType export, int priceOfExport,
			LocationType type) {
		this.name = name;
		this.structuralStability = 100;
		this.export = export;
		this.priceOfExport = priceOfExport;
		this.locationType = type;
		
		peoplePresent = new ArrayList<>();
		freeUnitsPresent = new ArrayList<>();
		
		this.populationHealth = 100;
	}
	
	public PurchasableType getExportType() {
		return export;
	}
	
	public void purchaseExport(UtilitySet us) {
		us.spendCredits(export.getPriceInThousands());
		export.deliver(us);
	}
	
	public World getWorld() {
		return world;
	}
	
	public LocationType getType() {
		return locationType;
	}
	
	public void setWorld(World world2) {
		this.world = world;
	}
	
	public void setOwner(Faction f) {
		this.owner = f;
	}
	
	public List<Person> getPeoplePresent() {
		return peoplePresent;
	}
	
	public List<Unit> getFreeUnitsPresent() {
		return freeUnitsPresent;
	}
	
	public List<Combatant> getBlockadingCombatants() {
		return blockadingCombatants;
	}
	
	public void freePrisoners(Faction f) {
		for (int q = 0; q < prisoners.size(); q++) {
			Person p = prisoners.get(q);
			Faction pFaction = p.getAllegiance();
			if (pFaction == null) {
				prisoners.remove(q);
				q--;
				continue;
			}
			if (p.getAllegiance().getUltimateSuzerain() == f.getUltimateSuzerain()) {
				prisoners.remove(q);
				q--;
				peoplePresent.add(p);
			}
		}
	}
	
	public void sendHere(Unit u) {
		freeUnitsPresent.add(u);
		u.setLocation(this);
	}
	
	public void sendHere(Person p) {
		peoplePresent.add(p);
		p.setLocation(this);
	}
	
	public Faction getOwner() {
		return owner;
	}
	
	public int relieve(List<ReliefSupplies> list) {
		int appreciation = 0;
		
		for (ReliefSupplies r : list) {
			int rebuilding = Math.min(100 - structuralStability, r.getRebuilding());
			int healing = Math.min(100 - populationHealth, r.getHealing());
			structuralStability += rebuilding;
			populationHealth += healing;
			appreciation += (rebuilding + healing);
		}
		return appreciation;
	}
	
	public int calculateReliefMissionDanger(int appreciationFromRelief) {
		Politician rep = owner.getCurrentRepresentative();
		if (rep == null) {
			return 0;
		}
		return Math.max(0, rep.getStubbornness() - appreciationFromRelief);
	}
	
	public int receiveSpeech(int persuasiveness) {
		int effectiveness = persuasiveness / 2;
		effectiveness += RNGStuff.rng.nextInt((persuasiveness - effectiveness) + 1);
		double trueEffectiveness = effectiveness;
		trueEffectiveness *= ((populationHealth + 0.0) / 100);
		return (int) Math.round(trueEffectiveness); //Shouldn't be larger than an int
	}
	
	public int calculateSpeechMissionDanger(int effectivenessOfMission) {
		Politician rep = owner.getCurrentRepresentative();
		if (rep == null) {
			return 0;
		}
		return rep.getStubbornness() * effectivenessOfMission / 100;
	}
	
	public boolean isBlockadedAgainstFromLand(Person traveler) {
		if (traveler instanceof Combatant) {
			return false;
		}
		Faction f = traveler.getAllegiance().getUltimateSuzerain();
		for (Combatant c : blockadingCombatants) {
			if (c.getAllegiance().getUltimateSuzerain() != f) {
				return true;
			}
		}
		return false;
	}
	
	public boolean isBlockadedAgainstFromSpace(Person traveler) {
		Faction f = traveler.getAllegiance().getUltimateSuzerain();
		for (Combatant c : blockadingCombatants) {
			if (c.getAllegiance().getUltimateSuzerain() != f) {
				return true;
			}
		}
		return false;
	}
	
	public boolean isBlockadedAgainstExporting(Faction f) {
		//Assuming f is ultimate suzerain
		for (Combatant c : blockadingCombatants) {
			if (c.getAllegiance().getUltimateSuzerain() != f) {
				return true;
			}
		}
		return false;
	}
	
	public boolean canSendExport(Faction f) {
		Faction lead = f.getUltimateSuzerain();
		InterestPoint dest;
		if (export.getSource() instanceof ReliefSupplies) {
			dest = f.getLocations().get(Faction.STORAGE);
		} else {
			dest = f.getLocations().get(Faction.BARRACKS);
		}
		if (dest.getWorld() == world) {
			return true;
		}
		return !(
				world.getSurroundingSpace().isBlockadedAgainstExporting(lead)
				|| dest.getWorld().getSurroundingSpace().isBlockadedAgainstExporting(lead)
				);
	}

	
	public enum LocationType {
		SPACE, FLATLAND, WATER, FOREST, TOXIC, HOT, COLD, UNEVEN, STRUCTURE
	}

}
