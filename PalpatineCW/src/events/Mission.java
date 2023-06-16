package events;

import java.util.List;

import locations.InterestPoint;
import people.Politician;
import utility.ReliefSupplies;

public class Mission {

	private Politician diplomat;
	
	private InterestPoint destination;
	
	private boolean relief;
	
	private boolean speech;
	
	private List<ReliefSupplies> supplies;

	public Mission(Politician diplomat, InterestPoint destination, boolean relief, boolean speech,
			List<ReliefSupplies> supplies) {
		super();
		this.diplomat = diplomat;
		this.destination = destination;
		this.relief = relief;
		this.speech = speech;
		this.supplies = supplies; //The list should be initialized before creating the mission
	}
	
	public void completeMission() {
		//TODO should probably return a report
		int danger = 0;
		boolean deliveringToEnemy = diplomat.getAllegiance().isEnemyOf(destination.getOwner());
		if (relief) {
			int appreciation = destination.relieve(supplies);
			//TODO appreciation sways allegiance
			if (deliveringToEnemy) {
				danger += destination.calculateReliefMissionDanger(appreciation);
			}
		}
		if (speech) {
			int effectiveness = destination.receiveSpeech(diplomat.getPersuasiveness());
			//TODO effectiveness sways allegiance
			if (deliveringToEnemy) {
				danger += destination.calculateSpeechMissionDanger(effectiveness);
			}
		}
		//TODO danger may cause capture or execution
	}
	
	
}
