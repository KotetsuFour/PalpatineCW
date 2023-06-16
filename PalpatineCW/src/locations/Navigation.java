package locations;

import java.util.ArrayList;
import java.util.List;

import locations.InterestPoint.LocationType;
import manager.GalaxyGraph;
import people.Combatant;
import people.Person;

public class Navigation {

	public static List<InterestPoint> findPath(GalaxyGraph gg, InterestPoint start,
			InterestPoint dest, Person traveler) {
		World w1 = start.getWorld();
		World w2 = dest.getWorld();
		if (w1 != w2) {
			if (traveler instanceof Combatant && ! ((Combatant) traveler).canTravel()) {
				return null;
			}
			if (start.getType() != LocationType.SPACE
					&& w1.getSurroundingSpace().isBlockadedAgainstFromLand(traveler)) {
				return null;
			}
			if (dest.getType() != LocationType.SPACE
					&& w2.getSurroundingSpace().isBlockadedAgainstFromSpace(traveler)) {
				return null;
			}
		} else {
			if (dest.getType() == LocationType.SPACE && dest.isBlockadedAgainstFromLand(traveler)) {
				return null;
			} else {
				List<InterestPoint> ret = new ArrayList<>(1);
				ret.add(dest);
				return ret;
			}
		}
		List<NavigationalNode> openList = new ArrayList<>();
		List<NavigationalNode> closedList = new ArrayList<>();
		openList.add(new NavigationalNode(0, null, w1));
		while (! openList.isEmpty()) {
			NavigationalNode nn = openList.remove(0);
			Iterable<SpaceLane> lsl = gg.connectedEdges(nn.getWorld());
			for (SpaceLane lane : lsl) {
				NavigationalNode child = new NavigationalNode(nn.getDepth() + 1, nn,
						gg.opposite(nn.getWorld(), lane));
				
				if (child.getWorld() == dest.getWorld()) {
					return finalStep(start, dest, child);
				}
				boolean addToList = true;
				for (int q = 0; q < openList.size(); q++) {
					if (openList.get(q).getWorld() == child.getWorld()) {
						addToList = false;
						break;
					}
				}
				if (addToList) {
					for (int q = 0; q < closedList.size(); q++) {
						if (closedList.get(q).getWorld() == child.getWorld()) {
							addToList = false;
							break;
						}
					}
				}
				if (addToList) {
					openList.add(child);
				}
			}
			closedList.add(nn);
		}
		
		return null;
	}
	
	private static List<InterestPoint> finalStep(InterestPoint start, InterestPoint dest,
			NavigationalNode goal) {
		//We should have already checked to make sure the destination world is not blockaded
		List<InterestPoint> ret = new ArrayList<>(goal.getDepth() + 5);
		ret.add(dest);
		if (dest.getType() != LocationType.SPACE) {
			ret.add(dest.getWorld().getSurroundingSpace());
		}
		NavigationalNode nn = goal.getParent();
		while (nn != null && nn.getWorld().getSurroundingSpace() != start) {
			ret.add(nn.getWorld().getSurroundingSpace());
			nn = nn.getParent();
		}
		
		return ret;
	}
	
	private static class NavigationalNode {
		
		private int depth;
		
		private NavigationalNode parent;
		
		private World world;
		
		public NavigationalNode(int depth, NavigationalNode parent, World world) {
			this.depth = depth;
			this.parent = parent;
			this.world = world;
		}
		
		public int getDepth() {
			return depth;
		}
		
		public NavigationalNode getParent() {
			return parent;
		}
		
		public World getWorld() {
			return world;
		}
	}
}
