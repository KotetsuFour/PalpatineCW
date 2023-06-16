package utility;

public class Ship implements Purchasable {

	private String name;
	
	private int health;
	
	private int maxHealth;
	
	private int power;
	
	private int starfighters;
	
	private int maxStarfighters;
	
	private int carryingCapacity;
	
	public static final int COST_OF_ONE_STARFIGHTER = 20;
	
	public static final String ACCLAMATOR_NAME = "Acclamator-class Assault Ship";
	public static final String VENATOR_NAME = "Venator-class Star Destroyer";
	public static final String LUCREHULK_NAME = "Lucrehulk-class Battleship";
	public static final String PROVIDENCE_NAME = "Providence-class Dreadnought";
	public static final String SUBJUGATOR_NAME = "Subjugator-class Heavy Cruiser";
	public static final String MUNIFICENT_NAME = "Munificent-class Star Frigate";
	public static final String DH_OMNI_NAME = "DH-Omni Support Vessel";
	
	public Ship(String name, int maxHealth, int power,
			int maxStarfighters, int carryingCapacity) {
		this.name = name;
		this.health = maxHealth;
		this.maxHealth = maxHealth;
		this.power = power;
		this.starfighters = maxStarfighters;
		this.maxStarfighters = maxStarfighters;
		this.carryingCapacity = carryingCapacity;
	}
	
	public int getAttackStrength(int strategyBonus) {
		return power + (starfighters * strategyBonus);
	}
	
	/**
	 * Process the damage that a ship takes
	 * @param damage
	 * @param level
	 * @return number of soldiers to kill off, based on starfighters destroyed,
	 * 			or return -1 if ship is destroyed
	 */
	public int processDamage(double damage, int strategyBonus, int power) {
		//TODO starfighters absorb some damage and some are destroyed, then ship loses health
		double starfighterAbsorption = (starfighters * strategyBonus);
		health -= (damage - starfighterAbsorption);
		if (health <= 0) {
			return -1;
		}
		//TODO maybe rebalance the part starfighter part
		damage -= power;
		damage /= strategyBonus;
		int destroy = (int)Math.min(starfighters, Math.round(damage));
		starfighters -= destroy;
		return destroy;
	}
	
	public int costToReplenishStarfighters() {
		int replenish = maxStarfighters - starfighters;
		return (replenish * COST_OF_ONE_STARFIGHTER);
	}
	
	public void replenishStarfighters(int paid) {
		//paid should not be greater than the price to replish to maxStarfighters
		paid /= COST_OF_ONE_STARFIGHTER;
		starfighters += paid;
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	public void repairShip() {
		health = maxHealth;
	}
	
	public int getCarryingCapacity() {
		return carryingCapacity;
	}

	@Override
	public void deliver(UtilitySet us) {
		//Assuming that this faction's ability to purchase this kind of ship has already
		//been verified
		us.addShip(new Ship(name, maxHealth, power, maxStarfighters, carryingCapacity));
	}
}
