package unit;

import allegiance.Faction;
import unit.OrganicUnit.OrganicUnitType;
import utility.Purchasable;
import utility.UtilitySet;

public class CloneUnit extends OrganicUnit implements Purchasable {
	
	public CloneUnit(String name, int numSoldiers, int level, Faction allegiance,
			OrganicUnitType type) {
		super(name, numSoldiers, level, allegiance, type);
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * Used for Purchasable
	 */
	public CloneUnit(String name, int level) {
		super(name, 0, level, null, null);
	}
	
	@Override
	public void deliver(UtilitySet us) {
		//Assumes this is already verified as a clone-using faction
		us.getTroops()[level - 1] += 100;
	}

	@Override
	public int unitType() {
		return Unit.CLONE;
	}

}
