package graph;

/* See restrictions in Graph.java. */
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;

/** The shortest paths through an edge-weighted graph.
 *  By overrriding methods getWeight, setWeight, getPredecessor, and
 *  setPredecessor, the client can determine how to represent the weighting
 *  and the search results.  By overriding estimatedDistance, clients
 *  can search for paths to specific destinations using A* search.
 *  @author Kelley
 */
public abstract class ShortestPaths {

    /** The shortest paths in G from SOURCE. */
    public ShortestPaths(Graph G, int source) {
        this(G, source, 0);
    }

    /** A shortest path in G from SOURCE to DEST. */
    public ShortestPaths(Graph G, int source, int dest) {
        _G = G;
        _source = source;
        _dest = dest;
        vertWeights = new double[G.maxVertex() + 1];
        predecessors = new int[G.maxVertex() + 1];
        tree = new TreeSet<>(new NewComparator());
    }

    /** Initialize the shortest paths.  Must be called before using
     *  getWeight, getPredecessor, and pathTo. */
    public void setPaths() {
        for (Integer v : _G.vertices()) {
            setWeight(v, Double.POSITIVE_INFINITY);
        }
        setWeight(_source, 0.0);
        tree.add(_source);
        while (!tree.isEmpty()) {
            int vert = tree.pollFirst();
            if (vert == _dest) {
                break;
            }
            for (int succ : _G.successors(vert)) {
                double weight = getWeight(vert, succ) + getWeight(vert);
                if (getWeight(succ) > weight) {
                    setPredecessor(succ, vert);
                    setWeight(succ, weight);
                    if (tree.contains(succ)) {
                        tree.remove(succ);
                        tree.add(succ);
                    } else {
                        tree.add(succ);

                    }
                }
            }
        }
    }

    /** Returns the starting vertex. */
    public int getSource() {
        return _source;
    }

    /** Returns the target vertex, or 0 if there is none. */
    public int getDest() {
        if (_dest == 0) {
            return 0;
        }
        return _dest;
    }

    /** Returns the current weight of vertex V in the graph.  If V is
     *  not in the graph, returns positive infinity. */
    public abstract double getWeight(int v);

    /** Set getWeight(V) to W. Assumes V is in the graph. */
    protected abstract void setWeight(int v, double w);

    /** Returns the current predecessor vertex of vertex V in the graph, or 0 if
     *  V is not in the graph or has no predecessor. */
    public abstract int getPredecessor(int v);

    /** Set getPredecessor(V) to U. */
    protected abstract void setPredecessor(int v, int u);

    /** Returns an estimated heuristic weight of the shortest path from vertex
     *  V to the destination vertex (if any).  This is assumed to be less
     *  than the actual weight, and is 0 by default. */
    protected double estimatedDistance(int v) {
        return 0.0;
    }
    /** Returns the current weight of edge (U, V) in the graph.  If (U, V) is
     *  not in the graph, returns positive infinity. */
    protected abstract double getWeight(int u, int v);

    /** Returns a list of vertices starting at _source and ending
     *  at V that represents a shortest path to V.  Invalid if there is a
     *  destination vertex other than V. */
    public List<Integer> pathTo(int v) {
        LinkedList<Integer> path = new LinkedList<>();
        int curr = v;
        path.addFirst(curr);
        while (curr != _source) {
            curr = getPredecessor(curr);
            path.addFirst(curr);
            if (curr == 0) {
                throw new Error("no path");
            }
        }
        return path;
    }

    /** Returns a list of vertices starting at the source and ending at the
     *  destination vertex. Invalid if the destination is not specified. */
    public List<Integer> pathTo() {
        return pathTo(getDest());
    }

    /** Return list of predecessors. */
    protected int[] getPredArray() {
        return predecessors;
    }

    /** Return list of vertex weights. */
    protected double[] getVertWeights() {
        return vertWeights;
    }

    /** Compare class.
     * Return a -1, 0, or a 1 as the first argument
     * is less than, equal to, or greater than the second. */
    class NewComparator implements Comparator<Integer> {
        @Override
        public int compare(Integer o1, Integer o2) {
            double edge1 = getWeight(o1) + estimatedDistance(o1);
            double edge2 = getWeight(o2) + estimatedDistance(o2);
            if (edge1 == edge2) {
                if (o1 < o2) {
                    return -1;
                } else if (o1 > o2) {
                    return 1;
                }
                return 0;
            } else {
                if (edge1 < edge2) {
                    return -1;
                }
            }
            return 1;
        }
    }

    /** The graph being searched. */
    protected final Graph _G;
    /** The starting vertex. */
    private final int _source;
    /** The target vertex. */
    private final int _dest;
    /** All shortest paths. */
    protected double[] vertWeights;
    /** List of updated predecessors, index is vertex, value is pred. **/
    protected int[] predecessors;
    /** Fringe for dijkstras. */
    protected TreeSet<Integer> tree;
}


