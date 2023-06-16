package people;

import java.util.List;

import allegiance.Faction;
import locations.InterestPoint;

public interface Person {

	public String getName();
	
	public Person getMaster();
	
	public Faction getAllegiance();
	
	public void setLocation(InterestPoint ip);
	
	public void setAllegiance(Faction f);
	
	public List<Person> getSubordinates();
	
	public InterestPoint getLocation();
	
	public InterestPoint getDestination();
	
	public void setRoute(List<InterestPoint> route);
	
	public void die();
	
	public String imageName();
}
