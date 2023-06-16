package people;

import java.util.List;

import allegiance.Faction;
import events.PriorityAction;

public interface Politician extends Person {

	public static final int ACTIONS_PER_DAY = 5;
	
	public List<Faction> getFactionsRepresented();
	
	public int getPersuasiveness();
	
	public int getStubbornness();
	
	public double getWarVotingPreference();
	
	public void alterWarVotingPreference(double change);
	
	public int getActions();
	
	public void resetActions();
	
	public List<Politician> getAllVoters();
	
	public void addPriority(PriorityAction pa);
	
	public List<PriorityAction> getPriorities();

	public int getLoyalty();

	public void alterLoyalty(int change);

	public int getGeneralRespectability();

	public void setLoyalty(int loyalty);

	public Person getUltimateMaster();

	public boolean isSubordinateOf(Person p);
	
	public Faction mainFactionRepresented();
}
