package events.bills;

import allegiance.Democracy;
import allegiance.Faction;
import events.Bill;
import people.Politician;

public class Appointment extends Bill {
	
	private Faction position;
	
	private Politician leader;
	
	public Appointment(Democracy senate, Faction position, Politician leader) {
		super(senate);
		this.position = position;
		this.leader = leader;
		for (Politician p : senate.getVoters()) {
			if ((p.getUltimateMaster() == leader.getUltimateMaster())

				|| (Math.abs(p.getWarVotingPreference() - leader.getWarVotingPreference())
					<= (leader.getPersuasiveness() - p.getStubbornness()))

				|| leader.getGeneralRespectability() > p.getStubbornness()
				) {
				favor.add(p);
			} else {
				opposed.add(p);
			}
		}
	}

	@Override
	protected String execute() {
		StringBuilder sb = new StringBuilder("The motion to appoint\n");
		sb.append(leader.getName() + " as the representative for " + position.getName() + "\n");
		sb.append("passed!");
		if (position.isSubordinateOf(senate) && leader.getAllegiance().isSubordinateOf(senate)) {
			position.appointRepresentative(leader);
			leader.alterLoyalty(10);
		} else {
			sb.append("However, " + senate.getName() + " no longer has the authority to make this appointment!\n");
		}
		return sb.toString();
	}

	@Override
	protected String reject() {
		StringBuilder sb = new StringBuilder("The motion to appoint\n");
		sb.append(leader.getName() + " as the representative for " + position.getName() + "\n");
		sb.append("was rejected!");
		return sb.toString();
	}
}
