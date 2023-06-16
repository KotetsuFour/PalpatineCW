package unit;

import allegiance.Faction;
import utility.Purchasable;
import utility.UtilitySet;

public class DroidUnit extends Unit implements Purchasable {

	private DroidUnitType type;
	
	public DroidUnit(String name, int numSoldiers, int level, Faction allegiance,
			DroidUnitType type) {
		super(name, numSoldiers, level, allegiance);
		this.type = type;
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * Used for Purchasable
	 */
	public DroidUnit(String name, int level) {
		super(name, 0, level, null);
	}
	
	@Override
	public int getMaximumSoldiers() {
		return type.getCapacity();
	}

	@Override
	public double getAttackStrength() {
		//Hopefully this is balanced enough
		return getNumSoldiers() * (0.03 + (getLevel() * 0.01));
	}

	public enum DroidUnitType {
		
		ARMY("Army", 218000, 1946),
		CORPS("Corps", 109200, 975),
		DIVISION("Division", 21840, 195),
		REGIMENT("Regiment", 4368, 39),
		VANGUARD("Vanguard", 1232, 11),
		BATTALION("Battalion", 784, 7),
		SPECIAL_SQUAD("Special Operations Squad", 8, 1);
		
		private String formation;
		
		private int capacity;
		
		private int commandValue;
		
		private int expReward;
		
		private DroidUnitType(String formation, int capacity, int commandValue) {
			this.formation = formation;
			this.capacity = capacity;
			this.commandValue = commandValue;
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
		
		public int getEXPReward() {
			return expReward;
		}
	}

	@Override
	public int getCommandValue() {
		return type.getCommandValue();
	}

	@Override
	public int getEXPReward() {
		return type.getEXPReward() * level;
	}

	@Override
	public String getFormationName() {
		return type.getFormationName();
	}

	@Override
	public void deliver(UtilitySet us) {
		//Assumes this has already been verified as a droid-using faction
		us.getTroops()[level - 1] += 100;
	}

	@Override
	public int unitType() {
		return Unit.DROID;
	}

}
