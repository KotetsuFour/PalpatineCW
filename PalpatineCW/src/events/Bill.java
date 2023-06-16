package events;

import java.util.ArrayList;
import java.util.List;

import allegiance.Democracy;
import allegiance.Faction;
import people.Politician;
import random.RNGStuff;

public abstract class Bill {

	protected int daysUntilFinalVote;
	
	protected Democracy senate;
	
	protected List<Politician> favor;
	
	protected List<Politician> opposed;
	
	protected int weight;
	
	private static final int DAYS_FOR_DELIBERATION = 7;
	
	public Bill(Democracy senate) {
		this.senate = senate;
		this.daysUntilFinalVote = DAYS_FOR_DELIBERATION;
		favor = new ArrayList<>(senate.getVoters().size());
		opposed = new ArrayList<>(senate.getVoters().size());
	}
		
	/**
	 * A politician will give a speech supporting their side or the debate
	 * 
	 * Note that no matter what percentage of politicians support each side, each side has an
	 * equal chance of speaking
	 * 
	 * @return whether or not it's time for the final vote
	 */
	public boolean deliberate() {
		if (favor.isEmpty() || opposed.isEmpty()) {
			return true;
		}
		Politician p;
		if (RNGStuff.rng.nextBoolean()) {
			p = favor.get(RNGStuff.rng.nextInt(favor.size()));
			convince(p, opposed, favor);
		} else {
			p = opposed.get(RNGStuff.rng.nextInt(opposed.size()));
			convince(p, favor, opposed);
		}
		daysUntilFinalVote--;
		return daysUntilFinalVote == 0;
	}
	
	public void convince(Politician p, List<Politician> audience, List<Politician> team) {
		for (Politician m : audience) {
			int convincingProbability = p.getPersuasiveness() - m.getStubbornness();
			if (RNGStuff.randomNumber0To99() < convincingProbability) {
				audience.remove(m);
				team.add(m);
			}
		}
	}

	/**
	 * Take the final vote
	 * @return a report string
	 */
	public String finalVote() {
		for (int q = 0; q < favor.size(); q++) {
			if (favor.get(q).getAllegiance() != senate) {
				favor.remove(q);
				q--;
			}
		}
		for (int q = 0; q < opposed.size(); q++) {
			if (opposed.get(q).getAllegiance() != senate) {
				opposed.remove(q);
				q--;
			}
		}
		if (favor.size() > opposed.size()) {
			for (Politician p : favor) {
				p.alterLoyalty(1);
			}
			return execute();
		} else {
			for (Politician p : opposed) {
				p.alterLoyalty(-1);
			}
			return reject();
		}
	}
	
	/**
	 * Pass the bill
	 * @return a report string
	 */
	protected abstract String execute();
	
	/**
	 * Reject the bill
	 * @return a report string
	 */
	protected abstract String reject();
	
}
