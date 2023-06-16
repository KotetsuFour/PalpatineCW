package events.bills;

import allegiance.Democracy;
import allegiance.Faction;
import events.Bill;
import events.Conflict;
import people.Politician;
import random.RNGStuff;

public class Interferance extends Bill {

	private Conflict conflict;
	
	private boolean joiningInitiator;
	
	public Interferance(Democracy senate, Conflict conflict, boolean joiningInitiator) {
		super(senate);
		this.conflict = conflict;
		this.joiningInitiator = joiningInitiator;
		for (Politician p : senate.getVoters()) {
			if (p.getWarVotingPreference() > RNGStuff.randomNumber0To99()) {
				favor.add(p);
			} else {
				opposed.add(p);
			}
		}
	}

	@Override
	protected String execute() {
		Faction helped;
		Faction target;
		if (joiningInitiator) {
			helped = conflict.getStakeholdingInitiate();
			target = conflict.getStakeholdingDefender();
		} else {
			helped = conflict.getStakeholdingDefender();
			target = conflict.getStakeholdingInitiate();
		}
		StringBuilder sb = new StringBuilder("The motion to engage in combat against\n");
		sb.append(target.getName() + " on the behalf of " + helped.getName() + "\n");
		sb.append("passed!\n");
		if (! conflict.isStillActive()) {
			sb.append("However, the conflict is already resolved!\n");
			return sb.toString();
		}
		if (helped.isSubordinateOf(senate)) {
			conflict.setSupporterOf(helped, senate);
		} else if (helped.getUltimateSuzerain() == helped) {
			senate.annex(helped);
			conflict.setSupporterOf(helped, senate);
		} else {
			sb.append("However, " + senate.getName() + " no longer has the authority to carry this out!\n");
		}
		return sb.toString();
	}

	@Override
	protected String reject() {
		Faction helped;
		Faction target;
		if (joiningInitiator) {
			helped = conflict.getStakeholdingInitiate();
			target = conflict.getStakeholdingDefender();
		} else {
			helped = conflict.getStakeholdingDefender();
			target = conflict.getStakeholdingInitiate();
		}
		StringBuilder sb = new StringBuilder("The motion to engage in combat against\n");
		sb.append(target.getName() + " on the behalf of " + helped.getName() + "\n");
		sb.append("was rejected!\n");
		return sb.toString();
	}

}
