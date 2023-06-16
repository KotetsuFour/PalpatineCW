package people;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import allegiance.Faction;
import locations.InterestPoint;
import locations.InterestPoint.LocationType;
import random.RNGStuff;
import unit.OrganicUnit;
import unit.Unit;
import utility.Ship;

public class Combatant implements Person {

	private List<Unit> units;
	//Remember, this refers to the command values of units, not their sizes
	private int commandCapacity;
	private int unitType;
	private String name;
	protected Person master;
	private List<Person> subordinates;
	private Faction allegiance;
	private int power;
	private int strategy;
	private int level;
	private int experience;
	private int powerGrowthRate;
	private int expReward;
	private LocationType specialty; //TODO incorporate this in battle
	private InterestPoint location;
	private List<InterestPoint> route;
	private boolean alive;
	private boolean wantsSurrender;
	private List<Ship> ships; //TODO maybe switch ship info to be based in combatant instead of unit
	private boolean forceSensitive;
	private OrdersType orders;
	private double shipSizeCache;
	private String imageName;

	public Combatant(String name, int commandCapacity, int unitType, Person master, Faction allegiance, int power,
			int strategy, int powerGrowthRate, LocationType specialty, InterestPoint location, boolean forceSensitive,
			String imageName) {
		this.commandCapacity = commandCapacity;
		this.unitType = unitType;
		this.name = name;
		this.master = master;
		this.allegiance = allegiance;
		this.power = power;
		this.strategy = strategy;
		this.powerGrowthRate = powerGrowthRate;
		this.specialty = specialty;
		this.location = location;
		this.forceSensitive = forceSensitive;
		this.imageName = imageName;
		this.subordinates = new ArrayList<>();
		this.ships = new ArrayList<>();
	}

	@Override
	public String getName() {
		return name;
	}
	
	public List<Unit> getUnits() {
		return units;
	}
	
	public int getCommandCapacity() {
		return commandCapacity;
	}
	
	public int currentlyCommanding() {
		int ret = 0;
		for (Unit u : units) {
			ret += u.getCommandValue();
		}
		return ret;
	}
	
	public int getStrategy() {
		return strategy;
	}
	
	public int getStrategyBonus() {
		if (location.getType() == specialty) {
			return strategy;
		}
		int bonus = strategy / 3;
		bonus += RNGStuff.rng.nextInt((strategy - bonus) + 1);
		return bonus;
	}
	
	public int getPower() {
		return power;
	}
	
	public boolean isForceSensitive() {
		return forceSensitive;
	}
	
	public boolean isAlive() {
		return alive;
	}
	
	public int getUnitType() {
		return unitType;
	}
	
	public void assignUnit(Unit u) {
		//Assumes the legality of this action has already been confirmed
		units.add(u);
		u.setGeneral(this);
	}
	
	public void dismissUnit(Unit u) {
		if (units.remove(u)) {
			u.removeGeneral();
			if (location.getType() == LocationType.SPACE) {
				//TODO give ships
			}
		}
	}

	@Override
	public Person getMaster() {
		return master;
	}

	@Override
	public Faction getAllegiance() {
		return allegiance;
	}
	
	public double[] getAttackStrength() {
		double str[] = new double[2];
		if (orders == OrdersType.SNEAK
				//&& ...
				//TODO If orders are SNEAK, maybe don't deal damage
				) {
			str[0] = 0;
			str[1] = 0;
			return str;
		}
		str[0] = power;
		for (int q = 0; q < units.size(); q++) {
			Unit u = units.get(q);
			str[0] += u.getAttackStrength();
			str[1] += u.getWeaponStrength();
		}
		return str;
	}
	
	public double[] getShipStrength() {
		double str[] = new double[2];
		str[0] = ships.size();
		str[1] = 0;
		if (orders == OrdersType.SNEAK
				//&& ...
				//TODO If orders are SNEAK, maybe don't deal damage
				) {
			str[0] = 0;
			return str;
		}
		for (Ship s : ships) {
			str[0] += s.getAttackStrength(getStrategyBonus());
		}
		//TODO include specialty
		return str;
	}
	
	public double getShipSize() {
		return ships.size();
	}
	
	public int getTotalShipCapacity() {
		int ret = 0;
		for (Ship s : ships) {
			ret += s.getCarryingCapacity();
		}
		return ret;
	}
	
	public void addShip(Ship s) {
		ships.add(s);
	}
	
	public boolean canTravel() {
		return getTotalShipCapacity() >= getUnitSize();
	}
	
	public boolean processShipDamage(double damage) {
		if (orders == OrdersType.SNEAK
				//&& ...
				//TODO if orders are SNEAK, maybe don't take damage
				) {
			return true;
		}
		if (ships.isEmpty()) { //Which means units is also empty
			if (specialty == LocationType.SPACE) {
				damage /= strategy;
			}
			return damage <= power;
		}
		
		//If all units die, so does the combatant
		ListIterator<Ship> si = ships.listIterator();
		while (si.hasNext()) {
			Ship s = si.next();
			int kill = s.processDamage(damage, getStrategyBonus(), power);
			if (kill == -1) {
				//TODO appropriate num of deaths
			}
			int idx = 0;
			while (kill > 0 && idx < units.size()) {
				Unit u = units.get(idx);
				int toKill = Math.min(kill, u.getNumSoldiers());
				if (! u.killSoldiers(toKill)) {
					expReward += u.getEXPReward();
					u.reset();
					units.remove(idx);
					idx--;
				}
				kill -= toKill;
				idx++;
			}
		}
		alive = !(units.isEmpty()) && !(ships.isEmpty());
		if (!alive) {
			expReward += (strategy * 10);
		}
		return alive;
	}
	
	public int getUnitSize() {
		int size = 1;
		for (int q = 0; q < units.size(); q++) {
			size += units.get(q).getNumSoldiers();
		}
		return size;
	}
	
	public void setOrders(OrdersType type) {
		orders = type;
	}
	
	public OrdersType getOrders() {
		return orders;
	}
	
	public boolean processDamage(double damage) {
		if (orders == OrdersType.SNEAK
				//&& ...
				//TODO if orders are SNEAK, maybe don't take damage
				) {
			return true;
		}
		if (units.isEmpty()) {
			return damage <= power;
		}
		
		//TODO should probably return a report array
		long damageDealt = Math.round(damage);
		damageDealt = Math.max(0, damageDealt - power);
		int save = getStrategyBonus();
		damageDealt /= save;
		damageDealt /= units.size();
		
		//If this single iteration wipes out half your soldiers,
		// you should probably surrender
		wantsSurrender = (damageDealt * units.size()) >= getUnitSize() / 2;
		
		//If all units die, so does the combatant
		ListIterator<Unit> li = units.listIterator();
		while (li.hasNext()) {
			Unit u = li.next();
			if (! u.killSoldiers(damageDealt)) {
				expReward += u.getEXPReward();
				u.reset();
				li.remove();
			}
		}
		alive = !(units.isEmpty());
		if (!alive) {
			expReward += (strategy * 10);
		}
		return alive;
	}
	
	public void processEXP(int exp) {
		for (int q = 0; q < units.size(); q++) {
			Unit u = units.get(q);
			if (u instanceof OrganicUnit) {
				((OrganicUnit) u).processEXP(exp);
			}
		}
		experience += exp;
		int req = level * 100;
		while (experience >= req) {
			experience -= req;
			levelUp();
			req = level * 100;
		}
	}
	
	public void levelUp() {
		level++;
		power += powerGrowthRate;
	}
	
	public int getEXPReward() { //EXPReward is dynamic, depending on dead units
		int ret = expReward;
		expReward = 0;
		return ret;
	}
	
	public boolean shouldAttemptSurrender() {
		return wantsSurrender;
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
	public InterestPoint getDestination() {
		if (route == null || route.isEmpty()) {
			return null;
		}
		return route.get(0);
	}

	@Override
	public void die() {
		//If you died is space, all your units get cleaned up (it's simpler that way)
		ListIterator<Unit> uit = units.listIterator();
		while (uit.hasNext()) {
			Unit u = uit.next();
			u.reset();
			uit.remove();
		}
		ships = null;
		if (orders == OrdersType.BLOCKADE) {
			location.getBlockadingCombatants().remove(this);
		} else {
			location.getPeoplePresent().remove(this);
		}
		// TODO remove from any other lists
	}
	
	public enum OrdersType {
		HOLD, BLOCKADE, SNEAK, DESTROY, SIEZE //TODO more
	}

	@Override
	public String imageName() {
		return imageName;
	}
}
