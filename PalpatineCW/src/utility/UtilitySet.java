package utility;

import java.util.ArrayList;
import java.util.List;

import unit.Unit;


public class UtilitySet {

	private long[] troops; //Each index x tells the number of level x+1 soldiers there are
	
	private List<Ship> ships; //Ordered from least to greatest capacity
	
	private List<Weapon> weapons;
	
	private List<Convoy> convoys;
	
	private long creditsInThousands;
	
	public UtilitySet() {
		troops = new long[Unit.MAXIMUM_LEVEL];
		ships = new ArrayList<Ship>();
		weapons = new ArrayList<Weapon>();
	}
	
	public long[] getTroops() {
		return troops;
	}
	
	public List<Ship> getShips() {
		return ships;
	}
	
	public void addShip(Ship s) {
		int q = ships.size() - 1;
		while (ships.get(q).getCarryingCapacity() > s.getCarryingCapacity()) {
			q--;
		}
		ships.add(q, s);
	}
	
	public void addWeapon(Weapon w) {
		weapons.add(w);
	}
	
	public void addConvoy(Convoy c) {
		convoys.add(c);
	}
	
	public List<Weapon> getWeapons() {
		return weapons;
	}
	
	public List<Convoy> getConvoys() {
		return convoys;
	}
	
	public long numOfTroopsAtLevel(int level) {
		return troops[level - 1];
	}
	
	public void receiveCredits(long credits) {
		creditsInThousands += credits;
	}
	
	public long getCreditsInThousands() {
		return creditsInThousands;
	}
	
	public void spendCredits(int spent) {
		creditsInThousands -= spent;
	}
}
