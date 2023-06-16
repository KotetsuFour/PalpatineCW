package events.bills;

import java.util.List;

import allegiance.Democracy;
import allegiance.Faction;
import events.Bill;
import locations.InterestPoint;
import people.Politician;
import unit.Unit;
import utility.Purchasable;
import utility.ReliefSupplies;
import utility.Ship;
import utility.Weapon;
import utility.Purchasable.PurchasableType;

public class Purchase extends Bill {
	
	private InterestPoint manufacturer;
	
	private int count;
	
	/**
	 * Constructor for purchases
	 * @param type
	 * @param senate
	 * @param manufacturer
	 */
	public Purchase(Democracy senate, InterestPoint manufacturer, int count) {
		//Initializes faction and favor vs opposed lists
		super(senate);
		this.manufacturer = manufacturer;
		this.count = count;
		PurchasableType item = manufacturer.getExportType();
		Purchasable source = item.getSource();
		List<Politician> voters = senate.getVoters();
		//TODO modify to make all of these below 100
		if (source instanceof ReliefSupplies) {
			ReliefSupplies rs = (ReliefSupplies) source;
			weight = rs.getHealing() + rs.getRebuilding();
			weight *= count;
			//weight /= constant?
			for (Politician p : voters) {
				if (p.getWarVotingPreference() < weight) {
					favor.add(p);
				} else {
					opposed.add(p);
				}
			}
			return;
			//Reverse support for relief supplies
		} else if (source instanceof Unit) {
			Unit u = (Unit) item.getSource();
			weight = 100 * u.getLevel() * count;
			//weight /= constant
		} else if (source instanceof Ship) {
			Ship s = (Ship) source;
			weight = s.getAttackStrength(1);
			//weight /= constant
		} else if (source instanceof Weapon) {
			Weapon w = (Weapon) source;
			weight = w.getPower() * count;
			//weight /= constant
		}
		for (Politician p : voters) {
			if (p.getWarVotingPreference() >= weight) {
				favor.add(p);
			} else {
				opposed.add(p);
			}
		}
	}

	@Override
	protected String execute() {
		StringBuilder sb = new StringBuilder("The bill to purchase\n");
		sb.append(count + " * " + manufacturer.getExportType().getDisplayName() + "\n");
		sb.append("passed!\n");
		if (manufacturer.canSendExport(senate)) {
			for (int q = 0; q < count; q++) {
				manufacturer.purchaseExport(senate.getUtilities());
			}
			//TODO gain some approval from manufacturer
		} else {
			sb.append("However, a blockade has prevented the transaction!\n");
		}
		return sb.toString();
	}

	@Override
	protected String reject() {
		StringBuilder sb = new StringBuilder("The bill to purchase\n");
		sb.append(count + " * " + manufacturer.getExportType().getDisplayName() + "\n");
		sb.append("was rejected!\n");
		return sb.toString();
	}

}
