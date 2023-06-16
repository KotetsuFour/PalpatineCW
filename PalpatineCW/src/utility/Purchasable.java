package utility;

import unit.CloneUnit;
import unit.OrganicUnit.OrganicUnitType;
import unit.DroidUnit;
import unit.DroidUnit.DroidUnitType;;

public interface Purchasable {

	public enum PurchasableType {
		HUNDRED_LV1_CLONES(2700, new CloneUnit("100 Phase I Clone Troopers", 1)),
		HUNDRED_LV2_CLONES(2700, new CloneUnit("100 Phase II Clone Troopers", 2)), //When Phase II armor comes out, this replaces HUNDRED_LV1_CLONES
		HUNDRED_LV5_CLONES(270000, new CloneUnit("100 Clone Commandos", 5)),
		HUNDRED_B1_DROIDS(180, new DroidUnit("100 OOM Battle Droids", 1)),
		HUNDRED_SPECIAL_B1_DROIDS(190, new DroidUnit("100 B1 Battle Droids", 2)),
		HUNDRED_B2_DROIDS(300, new DroidUnit("100 B2 Super Battle Droids", 3)),
		HUNDRED_DROIDEKAS(2100, new DroidUnit("100 Destroyer Droids", 4)),
		HUNDRED_SPECIAL_B2_DROIDS(3000, new DroidUnit("100 B2-HA Super Battle Droids", 5)),
		HUNDRED_DWARF_SPIDERS(10000, new DroidUnit("100 DSD1 Dwarf Spider Droids", 6)),
		HUNDRED_CRAB_DROIDS(20000, new DroidUnit("100 LM-432 Crab Droids", 7)),
		HUNDRED_COMMANDO_DROIDS(25000, new DroidUnit("100 BX Droid Commandos", 8)),
		HUNDRED_ASSASSIN_DROIDS(61000, new DroidUnit("100 E522 Assassin Droids", 9)),
		HUNDRED_MAGNA_GUARDS(19000, new DroidUnit("100 IG-100 MagnaGuards", 10)), //probably not purchasable
		ACCLAMATOR(110000, new Ship(Ship.ACCLAMATOR_NAME, 1000, 326, 10, 2 * OrganicUnitType.LEGION.getCommandValue())),
		VENATOR(59000, new Ship(Ship.VENATOR_NAME, 3500, 996, 420, OrganicUnitType.REGIMENT.getCommandValue())),
		LUCREHULK(500000, new Ship(Ship.LUCREHULK_NAME, 4000, 2420, 1500, DroidUnitType.ARMY.getCommandValue())), //Not actually purchasable
		PROVIDENCE(400585, new Ship(Ship.PROVIDENCE_NAME, 1200, 1308, 250, 2 * DroidUnitType.DIVISION.getCommandValue())),
		SUBJUGATOR(875500, new Ship(Ship.SUBJUGATOR_NAME, 2000, 1412, 192, DroidUnitType.CORPS.getCommandValue())),
		MUNIFICENT(12000, new Ship(Ship.MUNIFICENT_NAME, 700, 172, 48, DroidUnitType.CORPS.getCommandValue())),
		DH_OMNI(500000, new Ship(Ship.DH_OMNI_NAME, 4000, 1800, 1000, DroidUnitType.ARMY.getCommandValue())), //Very similar to the Lucrehulk, but is actually purchasable
		MEDICAL_CONVOY(4000, new Convoy("Medical Convoy", 0, 50)),
		CONSTRUCTION_CONVOY(10000, new Convoy("Construction Convoy", 50, 0)),
		FOOD_CONVOY(500, new Convoy("Foodstuffs Convoy", 5, 15)),
		LUXURY_CONVOY(1000, new Convoy("Luxury Convoy", 15, 5)),
		ATTE_WALKER_COMPANY(10000, new Weapon(Weapon.ATTE_NAME, 30, 3400)),
		TX_130T_COMPANY(8500, new Weapon(Weapon.TX_130T_NAME, 10, 6000)),
		HAVW_A6_JUGGERNAUT_COMPANY(18000, new Weapon(Weapon.JUGGERNAUT_NAME, 20, 12000)),
		HOMING_SPIDER_DROID_COMPANY(7000, new Weapon(Weapon.SPIDER_NAME, 15, 3200)),
		HAILFIRE_DROID_COMPANY(6000, new Weapon(Weapon.HAILFIRE_NAME, 5, 7000)),
		ARMORED_ASSAULT_TANK_COMPANY(7500, new Weapon(Weapon.AAT_NAME, 10, 1500)),
		SUPER_TANK_COMPANY(10000, new Weapon(Weapon.SUPER_TANK_NAME, 50, 1200)),
		OCTUPTARRA_TRI_DROID_COMPANY(10000, new Weapon(Weapon.OCTUPTARRA_NAME, 30, 3000));

		private int priceInThousands;
		
		private Purchasable source;
		
		private PurchasableType(int priceInThousands, Purchasable source) {
			this.priceInThousands = priceInThousands;
			this.source = source;
		}
		
		public int getPriceInThousands() {
			return priceInThousands;
		}
		
		public String getDisplayName() {
			return source.getName();
		}
		
		public void deliver(UtilitySet us) {
			source.deliver(us);
		}
		
		public Purchasable getSource() {
			return source;
		}
	}
	
	public void deliver(UtilitySet us);
	
	public String getName();
}
