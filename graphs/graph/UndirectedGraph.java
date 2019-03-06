package graph;

import java.util.ArrayList;
import java.util.List;
/* See restrictions in Graph.java. */

/** Represents an undirected graph.  Out edges and in edges are not
 *  distinguished.  Likewise for successors and predecessors.
 *
 *  @author Kelley
 */
public class UndirectedGraph extends GraphObj {

    @Override
    public boolean isDirected() {
        return false;
    }
    @Override
    public int outDegree(int v) {
        return inDegree(v);
    }

    @Override
    public int inDegree(int v) {
        int num = 0;
        if (!_verts.contains(v)) {
            return 0;
        } else {
            for (int[] e: edges()) {
                if (e[0] == v || e[1] == v || (e[0] == v && e[1] == v)) {
                    num++;
                }
            }
            return num;
        }
    }

    @Override
    public Iteration<Integer> successors(int v) {
        List<Integer> iterSucc = new ArrayList<>();
        for (int[] e: edges()) {
            if ((e[0] == v || (e[0] == v && e[1] == v))) {
                iterSucc.add(e[1]);
            } else if (e[1] == v) {
                iterSucc.add(e[0]);
            }
        }
        return Iteration.iteration(iterSucc);
    }

    @Override
    public Iteration<Integer> predecessors(int v) {
        return successors(v);
    }
}
