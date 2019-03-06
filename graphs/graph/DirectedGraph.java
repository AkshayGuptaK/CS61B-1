package graph;

/* See restrictions in Graph.java. */

import java.util.ArrayList;
import java.util.List;

/** Represents a general unlabeled directed graph whose vertices are denoted by
 *  positive integers. Graphs may have self edges.
 *
 *  @author Kelley
 */
public class DirectedGraph extends GraphObj {

    @Override
    public boolean isDirected() {
        return true;
    }

    @Override
    public int outDegree(int v) {
        if (_verts.contains(v)) {
            int num = 0;
            for (int [] edge: edges()) {
                if (edge[0] == v || (edge[0] == v && edge[1] == v)) {
                    num++;
                }
            }
            return num;
        }
        return 0;
    }

    @Override
    public int inDegree(int v) {
        int num = 0;
        if (!_verts.contains(v)) {
            return 0;
        } else {
            for (int[] e:edges()) {
                if (e[1] == v) {
                    num++;
                }
            }
            return num;
        }
    }

    @Override
    public Iteration<Integer> successors(int v) {
        List<Integer> iterSucc = new ArrayList<>();
        for (int[] e : edges()) {
            if (e[0] == v || (e[0] == v && e[1] == v)) {
                iterSucc.add(e[1]);
            }
        }
        return Iteration.iteration(iterSucc);
    }

    @Override
    public Iteration<Integer> predecessors(int v) {
        List<Integer> iterPred = new ArrayList<>();
        for (int[] e : edges()) {
            if (e[1] == v || (e[0] == v && e[1] == v)) {
                iterPred.add(e[0]);
            }
        }
        return Iteration.iteration(iterPred);
    }
}
