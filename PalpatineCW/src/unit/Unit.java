package unit;

import java.util.List;
import java.util.ListIterator;

import allegiance.Faction;
import locations.InterestPoint;
import people.Combatant;
import people.Combatant.OrdersType;
import utility.Ship;
import utility.Weapon;

public abstract class Unit {

	private String name;
	
	protected int numSoldiers;
	
	protected int level;
	
	private Faction allegiance;
	
	private Combatant general;
	
	private InterestPoint location; //Null if the unit is being directly led by a combatant
	
	private Weapon weapon;
	
	private List<Ship> ships;
	
	private int totalShipCapacityCache;
	
	public static final int MAXIMUM_LEVEL = 10;
	
	public static final int CLONE = 0;
	public static final int DROID = 1;
	public static final int COMMON = 2;
	
	public abstract int getMaximumSoldiers();
	
	public abstract double getAttackStrength();
	
	public abstract int getCommandValue();
	
	public abstract int unitType();
	
	/**
	 * Should only ever be called if the unit is not directly controlled by a combatant.
	 * Process damage independent of combatant.
	 * @param damage
	 * @return
	 */
	public boolean processDamage(double damage) {
		double damageDealt = damage / level;
		return killSoldiers(Math.round(damageDealt));
	}
	
	public int getLevel() {
		return level;
	}
	
	public abstract int getEXPReward();
	
	public String getName() {
		return name;
	}
	
	public abstract String getFormationName();
	
	/**
	 * Add level 1 soldiers to this unit
	 * @param toAdd
	 * @return the soldiers that could not be added
	 */
	public int addSoldiers(int toAdd) {
		int maxCanAdd = getMaximumSoldiers() - numSoldiers;
		int numAdded = Math.min(maxCanAdd, toAdd);
		numSoldiers += numAdded;
		return toAdd - numAdded;
	}
	
	public Faction getAllegiance() {
		return allegiance;
	}
	
	public int getNumSoldiers() {
		return numSoldiers;
	}
	
	public void setGeneral(Combatant c) {
		general = c;
		location = null;
	}
	
	public void removeGeneral() {
		if (general != null) {
			location = general.getLocation();
		}
		general = null;
	}
	
	public Combatant getGeneral() {
		return general;
	}
	
	public InterestPoint getLocation() {
		if (general == null) {
			return location;
		}
		return general.getLocation();
	}
	
	/**
	 * Kill off a specified number of soldiers
	 * Return true if the unit survived, false if it didn't
	 * @param kill
	 * @return
	 */
	public boolean killSoldiers(long kill) {
		if (kill >= numSoldiers) {
			numSoldiers = 0;
			return false;
		}
		numSoldiers -= kill;
		return true;
	}
	
	public void reset() {
		numSoldiers = 0; //This line is useful when killing a ship's passengers
		level = 1;
		general = null;
		removeFromPastLocation();
		allegiance.sendTo(Faction.BARRACKS, this);
	}
	
	public void setLocation(InterestPoint ip) {
		removeFromPastLocation();
		location = ip;
		location.getFreeUnitsPresent().add(this);
	}
	
	private void removeFromPastLocation() {
		if (location != null) {
			location.getFreeUnitsPresent().remove(this);
		}
		location = null;
	}
	
	public int getWeaponStrength() {
		if (weapon == null) {
			return 0;
		}
		return weapon.use();
	}
	
	public List<Ship> getShips() {
		return ships;
	}
	
	public double[] getShipStrength() {
		double[] str = new double[2];
		str[1] = ships.size();
		str[0] = 0;
		for (Ship s : ships) {
			str[0] += s.getAttackStrength(1);
			totalShipCapacityCache += s.getCarryingCapacity();
		}
		return str;
	}
	
	public boolean processShipDamage(double damage) {
		damage /= ships.size();
		ListIterator<Ship> sli = ships.listIterator();
		while (sli.hasNext()) {
			Ship s = sli.next();
			int kill = s.processDamage(damage, 1, 0);
			if (kill == -1) {
				if (ships.size() > 1) {
					killSoldiers(totalShipCapacityCache / ships.size());
					sli.remove();
				} else {
					killSoldiers(numSoldiers);
					//TODO the above line is here just to make sure I didn't mess up with calculations
					//Verify that the last destroyed ship will always kill all remaining soldiers and fix
					//the calculation if necessary
				}
			} else {
				killSoldiers(kill);
			}
		}
		return numSoldiers > 0;
	}
	
	public Unit(String name, int numSoldiers, int level, Faction allegiance) {
		this.name = name;
		this.numSoldiers = numSoldiers;
		this.level = level;
		this.allegiance = allegiance;
	}
}
