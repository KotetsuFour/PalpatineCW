package events;

import allegiance.Faction;

public class PriorityAction {

	private Conflict conflict;
	private Faction wantsAssistance;
	
	
	
	public PriorityAction() {
		//TODO
	}
	
	public PriorityAction(Conflict c, Faction asker) {
		conflict = c;
		wantsAssistance = asker;
	}
	
	public void assistInConflict(Faction answerer) {
		conflict.setSupporterOf(wantsAssistance, answerer);
	}
}
