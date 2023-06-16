package locations;

public class SpaceLane {
	
    /**Endpoints of edge*/
    private World[] endpoints;
    
    /**
     * Constructor for edge
     * @param element is edge's element
     * @param v1 is vertex from
     * @param v2 is vertex to
     */
    @SuppressWarnings("unchecked")
    public SpaceLane(World w1, World w2) {
        endpoints = (World[])new World[]{w1, w2};
    }
    
    /**
     * Gives the connected vertices as an array
     * @return array of vertices
     */
    public World[] getEndpoints() {
        return endpoints;
    }
    
    /**
     * Gives edge as a string
     * @return edge as a string
     */
    @Override
    public String toString() {
        return "SpaceLane[" + endpoints[0].getName()
        		+ ", " + endpoints[1].getName() + "]";
    }

}
