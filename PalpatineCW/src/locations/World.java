package locations;

import java.util.ArrayList;
import java.util.List;

public class World {

	private String name;
	
	private InterestPoint surroundingSpace;
	
	private List<InterestPoint> locationsOnWorld;
	
	private List<SpaceLane> connectedSpaceLanes;
	
	public World(String name, InterestPoint space) {
		this.name = name;
		surroundingSpace = space;
		locationsOnWorld = new ArrayList<>();
		connectedSpaceLanes = new ArrayList<>();
		space.setWorld(this);
	}
	
	public String getName() {
		return name;
	}
	
	public InterestPoint getSurroundingSpace() {
		return surroundingSpace;
	}
	
	public List<SpaceLane> getConnectedSpaceLanes() {
		return connectedSpaceLanes;
	}
	
	public List<InterestPoint> getLocationsOnWorld() {
		return locationsOnWorld;
	}
	
	public void addInterestPoint(InterestPoint ip) {
		locationsOnWorld.add(ip);
		ip.setWorld(this);
	}

}
