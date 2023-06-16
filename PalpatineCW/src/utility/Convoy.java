package utility;

public class Convoy implements ReliefSupplies {

	private String name;
	
	private int rebuilding;
	
	private int healing;
	
	public Convoy(String name, int rebuilding, int healing) {
		this.name = name;
		this.rebuilding = rebuilding;
		this.healing = healing;
	}
	
	@Override
	public void deliver(UtilitySet us) {
		us.addConvoy(new Convoy(name, rebuilding, healing));
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public int getRebuilding() {
		return rebuilding;
	}

	@Override
	public int getHealing() {
		return healing;
	}

}
