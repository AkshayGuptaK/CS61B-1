package graph;

import org.junit.Test;
import static org.junit.Assert.*;

/** Unit tests for the Graph class.
 *  @author Kelley
 */
public class GraphTest {

    @Test
    public void emptyGraph() {
        DirectedGraph g = new DirectedGraph();
        assertEquals("Initial graph has vertices", 0, g.vertexSize());
        assertEquals("Initial graph has edges", 0, g.edgeSize());
    }

    @Test
    public void addVertexEdgeDirect() {
        DirectedGraph g = new DirectedGraph();
        g.add();
        assertEquals(1, g.vertexSize());
        g.add();
        assertEquals(2, g.vertexSize());
        int edge = g.add(1, 2);
        int id = g.edgeId(1, 2);
        assertEquals(1, g.edgeSize());
        assertEquals(1, edge);
        assertEquals(1, id);
        assertTrue("graph doesn't have edge 1-2 ", g.contains(1, 2));
        g.add();
        g.add();
        g.add(3, 4);
        assertEquals(2, g.edgeId(3, 4));
        assertEquals(2, g.edgeSize());
        assertEquals(4, g.vertexSize());
        g.add();
        g.add();
        g.add();
        assertEquals(2, g.edgeSize());
        assertEquals(7, g.vertexSize());
        assertEquals(2, g.edgeSize());
        g.add(5, 2);
        assertTrue("graph doesn't have edge 5-2 ", g.contains(5, 2));
        assertEquals(3, g.edgeId(5, 2));
        g.add(1, 3);
        g.add(1, 6);
        assertEquals(3, g.outDegree(1));
        assertEquals(2, g.inDegree(2));
    }

    @Test
    public void removeDirect() {
        DirectedGraph g = new DirectedGraph();
        g.add();
        g.add();
        g.add();
        g.add();
        g.add();
        g.add();
        g.add();
        g.add();
        g.add();
        g.add();
        assertEquals(0, g.edgeSize());
        assertEquals(10, g.vertexSize());
        g.remove(2);
        assertEquals(9, g.vertexSize());
        g.add(1, 3);
        g.add(3, 4);
        assertEquals(2, g.edgeSize());
        g.add();
        assertTrue("2 is a vertex", g.contains(2));
        g.remove(3);
        assertEquals(9, g.vertexSize());
        int size = g.edgeSize();
        assertEquals(0, size);

    }

    @Test
    public void addVertexEdgeUnDirect() {
        UndirectedGraph g = new UndirectedGraph();
        g.add();
        assertEquals(1, g.vertexSize());
        g.add();
        assertEquals(2, g.vertexSize());
        int edge = g.add(1, 2);
        int id = g.edgeId(1, 2);
        assertEquals(1, g.edgeSize());
        assertEquals(1, edge);
        assertEquals(1, id);
        assertTrue("graph has edge 1-2 ", g.contains(1, 2));
        g.add();
        g.add();
        g.add(3, 4);
        assertEquals(2, g.edgeId(3, 4));
        assertEquals(2, g.edgeSize());
        assertEquals(4, g.vertexSize());
        g.add();
        g.add();
        g.add();
        assertEquals(2, g.edgeSize());
        assertEquals(7, g.vertexSize());
        g.add(5, 2);
        assertTrue("graph has edge 5-2", g.contains(5, 2));
        assertEquals(3, g.edgeId(5, 2));
        g.add(1, 3);
        g.add(1, 6);
        g.add(1, 7);
        g.add(2, 1);
        assertEquals(6, g.edgeSize());
        assertEquals(4, g.outDegree(1));
        assertEquals(4, g.inDegree(1));
    }

    @Test
    public void removeUndirect() {
        UndirectedGraph g = new UndirectedGraph();
        g.add();
        g.add();
        g.add();
        g.add();
        g.add();
        g.add();
        g.add();
        g.add();
        g.add();
        g.add();
        assertEquals(0, g.edgeSize());
        assertEquals(10, g.vertexSize());
        g.remove(2);
        assertEquals(9, g.vertexSize());
        g.add(1, 3);
        g.add(3, 4);
        assertEquals(2, g.edgeSize());
        g.add();
        assertTrue("2 is a vertex", g.contains(2));
        g.remove(3);
        assertEquals(9, g.vertexSize());
        assertEquals(0, g.edgeSize());

    }
}
