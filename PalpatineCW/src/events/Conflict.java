package events;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import allegiance.Faction;
import locations.InterestPoint;
import locations.InterestPoint.LocationType;
import people.Combatant;
import people.Person;
import unit.OrganicUnit;
import unit.Unit;

public class Conflict {

	private List<InterestPoint> locations;
	
	private Faction initiate;
	
	private Faction defender;
	
	private Faction stakeholdingInitiate;
	
	private Faction stakeholdingDefender;
	
	private ConflictType type;
	
	private boolean resolved;
	
	public Conflict(Faction initiate, Faction defender, ConflictType type) {
		locations = new ArrayList<>();
		this.initiate = initiate;
		this.defender = defender;
		this.stakeholdingInitiate = initiate;
		this.stakeholdingDefender = defender;
		this.type = type;
		this.resolved = false;
		
		locations.addAll(stakeholdingDefender.getLocations().values());
//		if (isMutualWar) { //TODO if conflict type requires it, then add initiate locations
//			locations.addAll(stakeholdingInitiate.getLocations().values());
//		}
	}
	
	public Faction getStakeholdingInitiate() {
		return stakeholdingInitiate;
	}
	
	public Faction getStakeholdingDefender() {
		return stakeholdingDefender;
	}
	
	public List<InterestPoint> getLocations() {
		return locations;
	}
	
	public void iterate() {
		//Should have already checked if conflict is still active before iterating

		InterestPointIteratorThread[] ipits = new InterestPointIteratorThread[locations.size()];
		for (int q = 0; q < ipits.length; q++) {
			ipits[q] = new InterestPointIteratorThread(locations.get(q));
			ipits[q].run();
		}
		for (int q = 0; q < locations.size(); q++) {
			try {
				ipits[q].join();
			} catch (InterruptedException e) {
				throw new IllegalArgumentException("Something interrupted the conflict management process");
			}
		}
	}
	
	public boolean setSupporterOf(Faction asker, Faction responder) {
		Faction ius = stakeholdingInitiate.getUltimateSuzerain();
		Faction dus = stakeholdingDefender.getUltimateSuzerain();
		if (asker.getUltimateSuzerain() != responder || ius == dus) {
			return false;
		}
		if (stakeholdingInitiate == asker) {
			initiate = responder;
			return true;
		} else if (stakeholdingDefender == asker) {
			defender = responder;
			return true;
		}
		return false;
	}
	
	public boolean isStillActive() {
		//TODO for each type, check conditions
		return false;
	}
	
	private class InterestPointIteratorThread extends Thread {
		
		private InterestPoint ip;
		
		@Override
		public void start() {
			//Maybe return a report array?
			if (ip.getType() == LocationType.SPACE) {
				performSpaceConflict();
			} else {
				performTerrestrialConflict();
			}
		}
		
		public InterestPointIteratorThread(InterestPoint ip) {
			this.ip = ip;
		}
		
		private void performSpaceConflict() {
			//TODO
			//TODO remember to sort through blockades
			
			//Sort through all personnel involved
			List<Combatant> initiateCombatants = new ArrayList<>();
			List<Combatant> defenderCombatants = new ArrayList<>();
			List<Person> initiateNonCombatants = new ArrayList<>();
			List<Person> defenderNonCombatants = new ArrayList<>();
			List<Unit> initiateUnits = new ArrayList<>();
			List<Unit> defenderUnits = new ArrayList<>();
			double initiateAttackStrength = 0.0;
			double initiateNumbers = 0.0;
			double defenderAttackStrength = 0.0;
			double defenderNumbers = 0.0;
			int expFromInitiate = 0;
			int expFromDefender = 0;
			
			List<Person> people = ip.getPeoplePresent();
			List<Unit> units = ip.getFreeUnitsPresent();
			List<Combatant> block = ip.getBlockadingCombatants();
			//TODO change to ship damage
			for (Person p : people) {
				if (p instanceof Combatant) {
					Combatant c = (Combatant) p;
					if (c.getAllegiance().isSubordinateOf(initiate)) {
						initiateCombatants.add(c);
						double[] str = c.getShipStrength();
						initiateAttackStrength += str[0];
						initiateNumbers += str[1];
					} else if (c.getAllegiance().isSubordinateOf(defender)) {
						defenderCombatants.add(c);
						double[] str = c.getShipStrength();
						defenderAttackStrength += str[0];
						defenderNumbers += str[1];
					}
				} else {
					if (p.getAllegiance().isSubordinateOf(initiate)) {
						initiateNonCombatants.add(p);
					} else if (p.getAllegiance().isSubordinateOf(defender)) {
						defenderNonCombatants.add(p);
					}
				}
			}
			for (Unit u : units) {
				if (u.getAllegiance().isSubordinateOf(initiate)) {
					initiateUnits.add(u);
					double[] shipData = u.getShipStrength();
					initiateAttackStrength += shipData[0];
					initiateNumbers += shipData[1];
				} else if (u.getAllegiance().isSubordinateOf(defender)) {
					defenderUnits.add(u);
					double[] shipData = u.getShipStrength();
					defenderAttackStrength += shipData[0];
					defenderNumbers += shipData[1];
				}
			}
			for (Combatant c : block) {
				if (c.getAllegiance().isSubordinateOf(initiate)) {
					initiateCombatants.add(c);
					double[] shipData = c.getShipStrength();
					initiateAttackStrength += shipData[0];
					initiateNumbers += shipData[1];
				} else if (c.getAllegiance().isSubordinateOf(defender)) {
					defenderCombatants.add(c);
					double[] shipData = c.getShipStrength();
					defenderAttackStrength += shipData[0];
					defenderNumbers += shipData[1];
				}
			}
			
			//TODO calculate structural damage and non-combatant survival
			//This is dependent on ship strength (AKA just strength) and non-combatant survivability
			
			ListIterator<Combatant> initCom = initiateCombatants.listIterator();
			while (initCom.hasNext()) {
				Combatant c = initCom.next();
				boolean survived = c.processShipDamage((c.getShipSize() / initiateNumbers) * defenderAttackStrength);
				expFromInitiate += c.getEXPReward();
				if (! survived) {
					c.die();
					initCom.remove(); //Remove so they don't get EXP
					//TODO make sure there's no other step that needs to be taken besides die()
				}
			}
			ListIterator<Unit> initUn = initiateUnits.listIterator();
			while (initUn.hasNext()) {
				Unit u = initUn.next();
				if (! u.processShipDamage((u.getShips().size() / initiateNumbers) * defenderAttackStrength)) {
					expFromInitiate += u.getEXPReward();
					u.reset();
					initUn.remove(); //Remove so they don't get EXP
				}
			}
			ListIterator<Combatant> defCom = defenderCombatants.listIterator();
			while (defCom.hasNext()) {
				Combatant c = defCom.next();
				boolean survived = ! c.processShipDamage((c.getShipSize() / defenderNumbers) * initiateAttackStrength);
				expFromDefender +=  c.getEXPReward();
				if (! survived) {
					c.die();
					defCom.remove(); //Remove so they don't get EXP
					//TODO make sure there's no other step that needs to be taken besides die()
				}
			}
			ListIterator<Unit> defUn = defenderUnits.listIterator();
			while (defUn.hasNext()) {
				Unit u = defUn.next();
				if (! u.processShipDamage((u.getShips().size() / defenderNumbers) * initiateAttackStrength)) {
					expFromDefender += u.getEXPReward();
					u.reset();
					defUn.remove(); //Remove so they don't get EXP
				}
			}
			
			if (expFromInitiate > 0) {
				expFromInitiate /= (defenderCombatants.size() + defenderUnits.size());
				for (Combatant c : defenderCombatants) {
					c.processEXP(expFromInitiate);
				}
				for (Unit u : defenderUnits) {
					if (u instanceof OrganicUnit) {
						((OrganicUnit) u).processEXP(expFromInitiate);
					}
				}
			}
			if (expFromDefender > 0) {
				expFromDefender /= (initiateCombatants.size() + initiateUnits.size());
				for (Combatant c : initiateCombatants) {
					c.processEXP(expFromDefender);
				}
				for (Unit u : initiateUnits) {
					if (u instanceof OrganicUnit) {
						((OrganicUnit) u).processEXP(expFromDefender);
					}
				}
			}
			//TODO figure out movement and victory
			//(upon victory, do nothing, as there are no prisoners,
			//and common units cannot be replenished in space)
			
			
			
			
			
		}
		
		private void performTerrestrialConflict() {
			//Sort through all personnel involved
			List<Combatant> initiateCombatants = new ArrayList<>();
			List<Combatant> defenderCombatants = new ArrayList<>();
			List<Person> initiateNonCombatants = new ArrayList<>();
			List<Person> defenderNonCombatants = new ArrayList<>();
			List<Unit> initiateUnits = new ArrayList<>();
			List<Unit> defenderUnits = new ArrayList<>();
			double initiateAttackStrength = 0.0;
			double initiateWeaponStrength = 0.0;
			double initiateNumbers = 0.0;
			double defenderAttackStrength = 0.0;
			double defenderWeaponStrength = 0.0;
			double defenderNumbers = 0.0;
			int expFromInitiate = 0;
			int expFromDefender = 0;
			
			List<Person> people = ip.getPeoplePresent();
			List<Unit> units = ip.getFreeUnitsPresent();
			for (Person p : people) {
				if (p instanceof Combatant) {
					Combatant c = (Combatant) p;
					if (c.getAllegiance().isSubordinateOf(initiate)) {
						initiateCombatants.add(c);
						double[] str = c.getAttackStrength();
						initiateAttackStrength += str[0];
						initiateWeaponStrength += str[1];
						initiateNumbers += c.getUnitSize();
					} else if (c.getAllegiance().isSubordinateOf(defender)) {
						defenderCombatants.add(c);
						double[] str = c.getAttackStrength();
						defenderAttackStrength += str[0];
						defenderWeaponStrength += str[1];
						defenderNumbers += c.getUnitSize();
					}
				} else {
					if (p.getAllegiance().isSubordinateOf(initiate)) {
						initiateNonCombatants.add(p);
					} else if (p.getAllegiance().isSubordinateOf(defender)) {
						defenderNonCombatants.add(p);
					}
				}
			}
			for (Unit u : units) {
				if (u.getAllegiance().isSubordinateOf(initiate)) {
					initiateUnits.add(u);
					initiateAttackStrength += u.getAttackStrength();
					initiateNumbers += u.getNumSoldiers();
				} else if (u.getAllegiance().isSubordinateOf(defender)) {
					defenderUnits.add(u);
					defenderAttackStrength += u.getAttackStrength();
					defenderNumbers += u.getNumSoldiers();
				}
			}
			
			initiateAttackStrength += initiateWeaponStrength;
			defenderAttackStrength += defenderWeaponStrength;
			
			//TODO calculate structural damage and non-combatant survival
			//This is dependent on weapon strength (not person strength) and non-combatant survivability
			
			ListIterator<Combatant> initCom = initiateCombatants.listIterator();
			while (initCom.hasNext()) {
				Combatant c = initCom.next();
				boolean survived = c.processDamage((c.getUnitSize() / initiateNumbers) * defenderAttackStrength);
				expFromInitiate += c.getEXPReward();
				if (! survived) {
					c.die();
					initCom.remove(); //Remove so they don't get EXP
					//TODO make sure there's no other step that needs to be taken besides die()
				}
			}
			ListIterator<Unit> initUn = initiateUnits.listIterator();
			while (initUn.hasNext()) {
				Unit u = initUn.next();
				if (! u.processDamage((u.getNumSoldiers() / initiateNumbers) * defenderAttackStrength)) {
					expFromInitiate += u.getEXPReward();
					u.reset();
					initUn.remove();
				}
			}
			ListIterator<Combatant> defCom = defenderCombatants.listIterator();
			while (defCom.hasNext()) {
				Combatant c = defCom.next();
				boolean survived = ! c.processDamage((c.getUnitSize() / defenderNumbers) * initiateAttackStrength);
				expFromDefender +=  c.getEXPReward();
				if (! survived) {
					c.die();
					defCom.remove(); //Remove so they don't get EXP
					//TODO make sure there's no other step that needs to be taken besides die()
				}
			}
			ListIterator<Unit> defUn = defenderUnits.listIterator();
			while (defUn.hasNext()) {
				Unit u = defUn.next();
				if (! u.processDamage((u.getNumSoldiers() / defenderNumbers) * initiateAttackStrength)) {
					expFromDefender += u.getEXPReward();
					u.reset();
					defUn.remove();
				}
			}
			
			if (expFromInitiate > 0) {
				expFromInitiate /= (defenderCombatants.size() + defenderUnits.size());
				for (Combatant c : defenderCombatants) {
					c.processEXP(expFromInitiate);
				}
				for (Unit u : defenderUnits) {
					if (u instanceof OrganicUnit) {
						((OrganicUnit) u).processEXP(expFromInitiate);
					}
				}
			}
			if (expFromDefender > 0) {
				expFromDefender /= (initiateCombatants.size() + initiateUnits.size());
				for (Combatant c : initiateCombatants) {
					c.processEXP(expFromDefender);
				}
				for (Unit u : initiateUnits) {
					if (u instanceof OrganicUnit) {
						((OrganicUnit) u).processEXP(expFromDefender);
					}
				}
			}
			//TODO figure out movement, surrender, victory
			//(upon victory, free prisoners and replenish common units)
			
			
			
			
			
		}
	}
	
	public enum ConflictType {
		SIEGE_AND_OCCUPATION, FEUD, ANNEXATION //TODO add more
	}
}
