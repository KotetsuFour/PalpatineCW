package unit;

import allegiance.Faction;

public abstract class OrganicUnit extends Unit {

	protected int experience;
	
	protected OrganicUnitType type;
	
	public OrganicUnit(String name, int numSoldiers, int level, Faction allegiance,
			OrganicUnitType type) {
		super(name, numSoldiers, level, allegiance);
		this.type = type;
		// TODO Auto-generated constructor stub
	}

	@Override
	public int getMaximumSoldiers() {
		return type.getCapacity();
	}
	
	public void levelUp() {
		level++;
	}
	
	public void setLevel(int newLevel) {
		level = newLevel;
	}
	
	public void processEXP(int exp) {
		exp *= type.getEXPMultiplier();
		experience += exp;
		int req = level * 100;
		while (level <= Unit.MAXIMUM_LEVEL && experience >= req) {
			experience -= req;
			levelUp();
			req = level * 100;
		}
	}
	
	/**
	 * Add level 1 soldiers to this unit and update the level to be the average
	 * @param toAdd
	 * @return the soldiers that could not be added
	 */
	@Override
	public int addSoldiers(int toAdd) {
		//Find out how many soldiers will be added
		int maxCanAdd = getMaximumSoldiers() - numSoldiers;
		int numAdded = Math.min(maxCanAdd, toAdd);
		
		//Then average the unit's level
		setLevel(((numSoldiers * level) + numAdded) / (numSoldiers + numAdded));
		
		//Then add the new soldiers and return the remainder
		numSoldiers += numAdded;
		return toAdd - numAdded;
	}

	public enum OrganicUnitType {
		
		CORPS("Corps", 36864, 256, 1, 100),
		LEGION("Legion", 9216, 64, 2, 50),
		REGIMENT("Regiment", 2304, 16, 4, 25),
		BATTALION("Battalion", 576, 4, 8, 15),
		SPECIAL_SQUAD("Special Operations Squad", 4, 1, 16, 5);
		
		private String formation;
		
		private int capacity;
		
		private int commandValue;
		
		private int expMultiplier;
		
		private int expReward;
		
		private OrganicUnitType(String formation, int capacity, int commandValue, int expMultiplier,
				int expReward) {
			this.formation = formation;
			this.capacity = capacity;
			this.commandValue = commandValue;
			this.expMultiplier = expMultiplier;
			this.expReward = expReward;
		}
		
		public String getFormationName() {
			return formation;
		}
		
		public int getCapacity() {
			return capacity;
		}
		
		public int getCommandValue() {
			return commandValue;
		}
		
		public int getEXPMultiplier() {
			return expMultiplier;
		}
		
		public int getEXPReward() {
			return expReward;
		}
	}
	
	@Override
	public double getAttackStrength() {
		return getNumSoldiers() * getLevel();
	}
	
	@Override
	public int getCommandValue() {
		return type.getCommandValue();
	}
	
	@Override
	public String getFormationName() {
		return type.getFormationName();
	}
	
	@Override
	public int getEXPReward() {
		return type.getEXPReward() * level;
	}

}
