package manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import locations.SpaceLane;
import locations.World;

/**
 * Graph using an adjacency list
 * @author Avery Hawkins
 *
 */
public class GalaxyGraph {

	/**List of vertices*/
    private List<World> vertexList;
    
    /**List of edges*/
    private List<SpaceLane> edgeList;
    
    /**
     * Constructor for graph with direction option
     * @param directed is graph's directed status
     */
    public GalaxyGraph() {
        vertexList = new ArrayList<>();
        edgeList = new ArrayList<>();
    }
    
    /**
     * Tells number of vertices
     * @return number of vertices
     */
    public int numVertices() {
        return vertexList.size();
    }

    /**
     * Gives an iterable for the vertices
     * @return iterable for vertices
     */
    public Iterable<World> vertices() {
        return vertexList;
    }

    /**
     * Tells number of edges
     * @return number of edges
     */
    public int numEdges() {
        return edgeList.size();
    }

    /**
     * Gives an iterable for the edges
     * @return iterable for edges
     */
    public Iterable<SpaceLane> edges() {
        return edgeList;
    }
    
    /**
     * Gives iterable of outgoing edges for a vertex
     * @param vertex is requested vertex
     * @return the iterable for the outgoing edges
     */
    public Iterable<SpaceLane> connectedEdges(World vertex) {
        return vertex.getConnectedSpaceLanes();
    }
    
    /**
     * Gives requested edge
     * @param vertex1 is vertex extended from
     * @param vertex2 is vertex extended to
     * @return the edge connecting the two vertices
     */
    public SpaceLane getEdge(World vertex1, World vertex2) {
        Iterator<SpaceLane> it = edgeList.iterator();
        while (it.hasNext()) {
            SpaceLane current = it.next();
            World[] ends = current.getEndpoints();
            if(ends[1] == vertex1 && ends[0] == vertex2) {
                return current;
            }
            if (ends[0] == vertex1 && ends[1] == vertex2) {
                return current;
            }
        }
        return null;
    }

    /**
     * Gives number of outgoing edges of a vertex
     * @param vertex is the vertex to examine
     * @return number of outgoing edges
     */
    public int degree(World vertex) {
        return vertex.getConnectedSpaceLanes().size();
    }

    /**
     * Puts a vertex into the graph
     * @param vertexData is data for the vertex
     * @return the new vertex
     */
    public void insertVertex(World vertex) {
        vertexList.add(vertex);
    }

    /**
     * Inserts an edge into the graph
     * @param v1 from this vertex
     * @param v2 to this vertex
     * @param edgeData data of the edge
     * @return the new edge
     */
    public SpaceLane insertEdge(World v1, World v2) {
        SpaceLane edge = new SpaceLane(v1, v2);
        edgeList.add(edge);
        // Remember to set the edge's positions in the outgoingEdges 
        //    and incomingEdges lists for the appropriate vertices
        v1.getConnectedSpaceLanes().add(edge);
        v2.getConnectedSpaceLanes().add(edge);
        return edge;
    }

    /**
     * Removes a vertex and all of its edges from the graph
     * @param vertex is the vertex to remove
     * @return the removed vertex
     */
    public void removeVertex(World v) {
        Iterator<SpaceLane> it = v.getConnectedSpaceLanes().iterator();
        while (it.hasNext()) {
        	removeEdge(it.next());
        }
        it = v.getConnectedSpaceLanes().iterator();
        while (it.hasNext()) {
        	removeEdge(it.next());
        }
        vertexList.remove(v);
    }

    /**
     * Removes an edge from the graph
     * @param edge is to remove
     * @return the removed edge
     */
    public void removeEdge(SpaceLane e) {
        World[] ends = e.getEndpoints();
        World origin = ends[0];
        World dest = ends[1];
        origin.getConnectedSpaceLanes().remove(e);
        dest.getConnectedSpaceLanes().remove(e);
        edgeList.remove(e);
    }
    
    
    /**
     * Gives vertices that an edge connects
     * @param edge is the requested edge
     * @return array of the two vertices
     */
    public World[] endVertices(SpaceLane edge) {
        return edge.getEndpoints();
    }

    /**
     * Gives the opposite vertex of an edge
     * @param vertex is known vertex
     * @param edge is the edge
     * @return the opposite vertex
     */
    public World opposite(World vertex, SpaceLane edge) {
        World[] ends = edge.getEndpoints();
        if(ends[0] == vertex) {
            return ends[1];
        }
        if(ends[1] == vertex) {
            return ends[0];
        }
        throw new IllegalArgumentException("Vertex is not incident on this edge.");
    }
    
}