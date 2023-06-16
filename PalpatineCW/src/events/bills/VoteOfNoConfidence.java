package events.bills;

import allegiance.Democracy;
import allegiance.Faction;
import events.Bill;
import people.Politician;
import random.RNGStuff;

public class VoteOfNoConfidence extends Bill {

	private Politician leader;
	
	public VoteOfNoConfidence(Democracy senate) {
		super(senate);
		this.leader = senate.getCurrentRepresentative();
		for (Politician p : senate.getVoters()) {
			if (p.getUltimateMaster() == leader.getUltimateMaster()
					|| p.getLoyalty() >= RNGStuff.randomNumber0To99()) {
				opposed.add(p);
			} else {
				favor.add(p);
			}
		}
	}

	@Override
	protected String execute() {
		StringBuilder sb = new StringBuilder("The motion to remove\n");
		sb.append(leader.getName() + " from their duty as representative of " + senate.getName() + "\n");
		sb.append("passed!\n");
		if (senate.getCurrentRepresentative() == leader) {
			senate.removeCurrentRepresentative();
		} else {
			sb.append("However, " + leader.getName() + " had already left this position!\n");
		}
		return sb.toString();
	}

	@Override
	protected String reject() {
		StringBuilder sb = new StringBuilder("The motion to remove\n");
		sb.append(leader.getName() + " from their duty as representative of " + senate.getName() + "\n");
		sb.append("was rejected!\n");
		return sb.toString();
	}

}
