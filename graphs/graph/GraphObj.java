package graph;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

/* See restrictions in Graph.java. */

/** A partial implementation of Graph containing elements common to
 *  directed and undirected graphs.
 *
 *  @author Kelley
 */
abstract class GraphObj extends Graph {

    /** A new, empty Graph. */
    GraphObj() {
        newVert = 0;
        edgeNum = 0;
        _edge = new ArrayList<>();
        _edge.add(null);
        _verts = new ArrayList<>();

    }

    @Override
    public int vertexSize() {
        return _verts.size();
    }

    @Override
    public int maxVertex() {
        return Collections.max(_verts);
    }

    @Override
    public int edgeSize() {
        return edgeNum;
    }

    @Override
    public abstract boolean isDirected();

    @Override
    public abstract int outDegree(int v);

    @Override
    public abstract int inDegree(int v);

    @Override
    public boolean contains(int u) {
        return _verts.contains(u);
    }

    @Override
    public boolean contains(int u, int v) {
        if (contains(u) && contains(v)) {
            for (int[] e : edges()) {
                if (e[0] == u && e[1] == v
                        || (!isDirected() && (e[1] == u && e[0] == v))) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public int add() {
        newVert++;
        if (vertexSize() == 0) {
            _verts.add(newVert);
            return newVert;
        } else if (maxVertex() == vertexSize()) {
            newVert = vertexSize() + 1;
            _verts.add(newVert);
            return newVert;
        } else {
            for (int i = 1; i < _verts.size() + 1; i++) {
                if (_verts.contains(i)) {
                    continue;
                } else {
                    newVert = i;
                    _verts.add(newVert);
                    return newVert;
                }
            }
        }
        _verts.add(newVert);
        return newVert;
    }

    @Override
    public int add(int u, int v) {
        if (_verts.contains(u) && _verts.contains(v)) {
            int[] newEdge = getEdge(u, v);
            if (edgeSize() != 0) {
                if (contains(u, v)) {
                    for (int[] e : edges()) {
                        if ((e[0] == u && e[1] == v)
                                || (!isDirected() && e[0] == v && e[1] == u)) {
                            return _edge.indexOf(e);
                        }
                    }
                } else {
                    _edge.add(newEdge);
                    edgeNum++;
                    int index = _edge.indexOf(newEdge);
                    return index;
                }
            }
            _edge.add(newEdge);
            edgeNum++;
            int index = _edge.indexOf(newEdge);
            return index;
        }
        return -1;
    }

    @Override
    public void remove(int v) {
        if (_verts.contains(v)) {
            _verts.remove(_verts.indexOf(v));
            if (edgeSize() > 0) {
                for (int[] edge : edges()) {
                    if (edge[0] == v || edge[1] == v) {
                        int remove = _edge.indexOf(edge);
                        _edge.set(remove, null);
                        edgeNum--;
                    }
                }
            }
        }
    }

    @Override
    public void remove(int u, int v) {
        for (int[] e: edges()) {
            if (e != null && e[0] == u && e[1] == v) {
                int remove = _edge.indexOf(e);
                _edge.set(remove, null);
                edgeNum--;
            }
        }
    }

    @Override
    public Iteration<Integer> vertices() {
        List<Integer> copy = new ArrayList<>();
        copy.addAll(_verts);
        Collections.sort(copy);
        return Iteration.iteration(copy);
    }

    @Override
    public abstract Iteration<Integer> successors(int v);

    @Override
    public abstract Iteration<Integer> predecessors(int v);

    @Override
    public Iteration<int[]> edges() {
        List<int[]> copy = new ArrayList<>();
        for (int[] e: _edge) {
            if (e != null) {
                copy.add(e);
            }
        }
        return Iteration.iteration(copy);
    }

    @Override
    protected int edgeId(int u, int v) {
        for (int[] e: edges()) {
            if (e[0] == u && e[1] == v
                    || (!isDirected() && e[0] == v && e[1] == u)) {
                return _edge.indexOf(e);
            }
        }
        final int noEdge = 10000;
        return noEdge;
    }


    /** Return edge as int array with FROM and TO as parameters. */
    static int[] getEdge(int from, int to) {
        int[] edge = new int[] {from, to};
        return edge;
    }
    /** Return list of vertices in graph. **/
    protected List<Integer> returnVerts() {
        return _verts;
    }

    /** List of edges with the u, v values. **/
    protected List<int[]> _edge;
    /** Label of new vertex being added. **/
    private int newVert;
    /** List of Vertices. **/
    protected List<Integer> _verts;
    /** Num of edges. */
    protected int edgeNum;
}
