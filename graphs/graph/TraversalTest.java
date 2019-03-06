package graph;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

/** Unit tests for the traversals.
 * @author Kelley
 * */
public class TraversalTest {
    @Test
    public void bFSDirect() {
        Graph g = new DirectedGraph();
        g.add();
        g.add();
        g.add();
        g.add();
        g.add();
        g.add(1, 2);
        g.add(1, 3);
        g.add(2, 4);
        g.add(2, 5);
        Traversal breadth = new BreadthFirstTraversal(g);
        breadth.traverse(1);
        assertEquals(4, ((DirectedGraph) g).edgeNum);

    }

    @Test
    public void bFSUndirect() {
        Graph g = new UndirectedGraph();
        g.add();
        g.add();
        g.add();
        g.add();
        g.add();
        g.add(1, 2);
        g.add(1, 3);
        g.add(2, 4);
        g.add(2, 5);
        Traversal breadth = new BreadthFirstTraversal(g);
        breadth.traverse(1);
        assertEquals(4, ((UndirectedGraph) g).edgeNum);

    }

    @Test
    public void dFSDirect() {
        Graph g = new DirectedGraph();
        g.add();
        g.add();
        g.add();
        g.add();
        g.add();
        g.add(1, 2);
        g.add(1, 3);
        g.add(2, 4);
        g.add(2, 5);
        Traversal pre = new DepthFirstTraversal(g);
        pre.traverse(1);
        assertEquals(4, ((DirectedGraph) g).edgeNum);

    }

    @Test
    public void dFSUndirect() {
        Graph g = new UndirectedGraph();
        g.add();
        g.add();
        g.add();
        g.add();
        g.add();
        g.add(1, 2);
        g.add(1, 3);
        g.add(2, 4);
        g.add(2, 5);
        Traversal pre = new DepthFirstTraversal(g);
        pre.traverse(1);
        assertEquals(4, ((UndirectedGraph) g).edgeNum);
    }

    @Test
    public void shortestPathSimpleTest() {
        DirectedGraph g = new DirectedGraph();
        g.add();
        g.add();
        g.add();
        g.add();
        g.add();
        g.add();
        g.remove(3);
        g.remove(5);
        List<Integer> test = new ArrayList<>();
        test.add(1);
        test.add(2);
        test.add(4);
        test.add(6);
        assertEquals(test, g.returnVerts());
        g.add(1, 2);
        g.add(1, 4);
        g.add(2, 4);
        g.add(2, 6);
        g.add(4, 6);
        assertEquals(5, g.edgeSize());
        ShortestPaths dummy = new SimpleShortestPaths(g, 1, 0) {
            @Override
            protected double getWeight(int u, int v) {
                return Math.abs(u - v);
            }
        };
        assertEquals(Arrays.toString(new int[]{0, 0, 0, 0, 0, 0, 0}),
                Arrays.toString(dummy.getPredArray()));
        dummy.setPaths();
        assertEquals(Arrays.toString(new int[]{0, 0, 1, 0, 1, 0, 2}),
                Arrays.toString(dummy.getPredArray()));
        List<Integer> path = Arrays.asList(1, 2, 6);
        assertEquals(path, dummy.pathTo(6));
        assertEquals(Arrays.asList(1), dummy.pathTo(1));

    }

    @Test
    public void shortestHarderTest() {
        DirectedGraph test2 = new DirectedGraph();
        test2.add();
        test2.add();
        test2.add();
        test2.add();
        test2.add();
        test2.add(1, 2);
        test2.add(2, 5);
        test2.add(1, 3);
        test2.add(2, 3);
        test2.add(3, 4);
        test2.add(4, 2);
        test2.add(4, 5);

        assertEquals(7, test2.edgeSize());
        ShortestPaths dumb = new SimpleShortestPaths(test2, 1, 5) {
            @Override
            protected double getWeight(int u, int v) {
                return v;
            }
        };

        dumb.setPaths();
        assertEquals(Arrays.asList(1, 2, 5), dumb.pathTo(5));

    }

    @Test
    public void shortestUndirectTest() {
        UndirectedGraph test3 = new UndirectedGraph();
        test3.add();
        test3.add();
        test3.add();
        test3.add();
        test3.add();
        test3.add(1, 3);
        test3.add(3, 2);
        test3.add(3, 5);
        test3.add(1, 4);
        test3.add(3, 4);
        test3.add(4, 2);
        test3.add(2, 5);

        assertEquals(7, test3.edgeSize());
        ShortestPaths dumb = new SimpleShortestPaths(test3, 1, 0) {
            @Override
            protected double getWeight(int u, int v) {
                return v;
            }
        };

        dumb.setPaths();
        assertEquals(Arrays.asList(1, 3, 5), dumb.pathTo(5));
    }

    @Test
    public void dFDirected() {
        DirectedGraph g = new DirectedGraph();
        for (int i = 0; i < 10; i++) {
            g.add();
        }
        g.add(1, 2);
        g.add(1, 3);
        g.add(1, 4);
        g.add(2, 3);
        g.add(2, 5);
        g.add(2, 6);
        g.add(3, 7);
        g.add(3, 8);
        g.add(8, 1);
        g.add(8, 9);
        g.add(8, 10);
        g.add(10, 7);

        DepthFirstTraversal trav = new DepthFirstTraversal(g) {
            @Override
            protected boolean shouldPostVisit(int v) {
                return true;
            }
        };

        trav.traverse(1);
        assertEquals(Arrays.asList(4, 7, 10, 9, 8, 3,
                6, 5, 2, 1), trav.getList());

    }

    @Test
    public void dFDirectedSelf() {
        DirectedGraph g = new DirectedGraph();
        for (int i = 0; i < 10; i++) {
            g.add();
        }
        g.add(1, 2);
        g.add(1, 1);
        g.add(1, 3);
        g.add(1, 4);
        g.add(2, 2);
        g.add(2, 3);
        g.add(2, 5);
        g.add(2, 6);
        g.add(3, 7);
        g.add(3, 8);
        g.add(8, 1);
        g.add(8, 8);
        g.add(8, 9);
        g.add(8, 10);
        g.add(10, 7);

        DepthFirstTraversal trav = new DepthFirstTraversal(g) {
            @Override
            protected boolean shouldPostVisit(int v) {
                return true;
            }
        };
        trav.traverse(1);
        assertEquals(Arrays.asList(4, 7, 10, 9, 8,
                3, 6, 5, 2, 1), trav.getList());
    }

    @Test
    public void dFUndirected() {
        UndirectedGraph g = new UndirectedGraph();
        for (int i = 0; i < 10; i++) {
            g.add();
        }
        g.add(1, 2);
        g.add(1, 8);
        g.add(1, 3);
        g.add(1, 4);
        g.add(2, 1);
        g.add(2, 3);
        g.add(2, 5);
        g.add(2, 6);
        g.add(3, 1);
        g.add(3, 2);
        g.add(3, 7);
        g.add(3, 8);
        g.add(8, 1);
        g.add(8, 3);
        g.add(8, 9);
        g.add(8, 10);
        g.add(10, 7);
        g.add(10, 8);
        g.add(4,  1);
        g.add(5,  2);
        g.add(6,  2);
        g.add(7,  3);
        g.add(7, 10);


    DepthFirstTraversal trav = new DepthFirstTraversal(g) {
        @Override
        protected boolean shouldPostVisit(int v) {
            return true;
        }
    };
        trav.traverse(1);
    assertEquals(Arrays.asList(4, 7, 10, 9, 8,
            6, 5, 2, 3, 1), trav.getList());
}

    @Test
    public void dFUndirectedSelf() {
        UndirectedGraph g = new UndirectedGraph();
        for (int i = 0; i < 10; i++) {
            g.add();
        }
        g.add(1, 2);
        g.add(1, 1);
        g.add(1, 8);
        g.add(1, 3);
        g.add(1, 4);
        g.add(2, 1);
        g.add(2, 2);
        g.add(2, 3);
        g.add(2, 5);
        g.add(2, 6);
        g.add(3, 1);
        g.add(3, 2);
        g.add(3, 7);
        g.add(3, 8);
        g.add(8, 1);
        g.add(8, 3);
        g.add(8, 8);
        g.add(8, 9);
        g.add(8, 10);
        g.add(10, 7);
        g.add(10, 8);
        g.add(4,  1);
        g.add(5,  2);
        g.add(6,  2);
        g.add(7,  3);
        g.add(7, 10);


        DepthFirstTraversal trav = new DepthFirstTraversal(g) {
            @Override
            protected boolean shouldPostVisit(int v) {
                return true;
            }
        };
        trav.traverse(1);
        assertEquals(Arrays.asList(4, 7, 10, 9, 8,
                6, 5, 2, 3, 1), trav.getList());
    }
}
