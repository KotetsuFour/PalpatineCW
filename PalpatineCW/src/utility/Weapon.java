package utility;

public class Weapon implements Purchasable {

	private String name;
	
	private int uses;
	
	private int power;
	
	public static final String ATTE_NAME = "ATTE Walker Company";
	public static final String TX_130T_NAME = "TX-130T Fighter Tank Company";
	public static final String JUGGERNAUT_NAME = "HAVw A6 Juggernaut Company";
	public static final String SPIDER_NAME = "OG-9 Homing Spider Droid Company";
	public static final String HAILFIRE_NAME = "IG-227 Hailfire Droid Tank Company";
	public static final String AAT_NAME = "AAT Company";
	public static final String SUPER_TANK_NAME = "Super Tank Company";
	public static final String OCTUPTARRA_NAME = "Octuptarra Tri Droid Company";
	
	public Weapon(String name, int uses, int power) {
		this.name = name;
		this.uses = uses;
		this.power = power;
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	public int use() {
		if (uses == 0) {
			return 0;
		}
		uses--;
		return power;
	}

	public int getUsesLeft() {
		return uses;
	}
	
	public int getPower() {
		return power;
	}

	@Override
	public void deliver(UtilitySet us) {
		us.addWeapon(new Weapon(name, uses, power));
	}
}
