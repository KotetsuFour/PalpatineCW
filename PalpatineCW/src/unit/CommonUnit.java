package unit;

import allegiance.Faction;
import unit.OrganicUnit.OrganicUnitType;

public class CommonUnit extends OrganicUnit {

	public CommonUnit(String name, int numSoldiers, int level, Faction allegiance,
			OrganicUnitType type) {
		super(name, numSoldiers, level, allegiance, type);
		// TODO Auto-generated constructor stub
	}

	@Override
	public int unitType() {
		return Unit.COMMON;
	}

}
