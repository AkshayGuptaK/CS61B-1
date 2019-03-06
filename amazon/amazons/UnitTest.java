package amazons;

import org.junit.Test;

import static amazons.Piece.*;
import static amazons.Piece.WHITE;
import static org.junit.Assert.*;
import ucb.junit.textui;

/** The suite of all JUnit tests for the enigma package.
 *  @author Kelley
 */
public class UnitTest {

    /** Run the JUnit tests in this package. Add xxxTest.class entries to
     *  the arguments of runClasses to run other JUnit tests. */
    public static void main(String[] ignored) {
        textui.runClasses(UnitTest.class, IteratorTests.class);
    }

    /** A dummy test as a placeholder for real ones. */
    @Test
    public void dummyTest() {
        assertTrue("There are no unit tests!", true);
    }

    /** Tests basic correctness of put and get on the initialized board. */
    @Test
    public void testBasicPutGet() {
        Board b = new Board();
        b.put(BLACK, Square.sq(3, 5));
        assertEquals(BLACK, b.get(3, 5));
        b.put(WHITE, Square.sq(9, 9));
        assertEquals(WHITE, b.get(9, 9));
        b.put(EMPTY, Square.sq(3, 5));
        assertEquals(EMPTY, b.get(3, 5));
        b.put(BLACK, 3, 2);
        assertEquals(BLACK, b.get(3, 2));
    }

    /** Tests proper identification of legal/illegal queen moves. */
    @Test
    public void testIsQueenMove() {
        assertFalse(Square.sq(1, 5).isQueenMove(Square.sq(1, 5)));
        assertFalse(Square.sq(1, 5).isQueenMove(Square.sq(2, 7)));
        assertFalse(Square.sq(0, 0).isQueenMove(Square.sq(5, 1)));
        assertTrue(Square.sq(1, 1).isQueenMove(Square.sq(9, 9)));
        assertTrue(Square.sq(2, 7).isQueenMove(Square.sq(8, 7)));
        assertTrue(Square.sq(3, 0).isQueenMove(Square.sq(3, 4)));
        assertTrue(Square.sq(7, 9).isQueenMove(Square.sq(0, 2)));
    }

    /** Tests toString for initial board state and a smiling board state. :) */
    @Test
    public void testToString() {
        Board b = new Board();
        assertEquals(INIT_BOARD_STATE, b.toString());
        makeSmile(b);
        assertEquals(SMILE, b.toString());
    }

    private void makeSmile(Board b) {
        b.put(EMPTY, Square.sq(0, 3));
        b.put(EMPTY, Square.sq(0, 6));
        b.put(EMPTY, Square.sq(9, 3));
        b.put(EMPTY, Square.sq(9, 6));
        b.put(EMPTY, Square.sq(3, 0));
        b.put(EMPTY, Square.sq(3, 9));
        b.put(EMPTY, Square.sq(6, 0));
        b.put(EMPTY, Square.sq(6, 9));
        for (int col = 1; col < 4; col += 1) {
            for (int row = 6; row < 9; row += 1) {
                b.put(SPEAR, Square.sq(col, row));
            }
        }
        b.put(EMPTY, Square.sq(2, 7));
        for (int col = 6; col < 9; col += 1) {
            for (int row = 6; row < 9; row += 1) {
                b.put(SPEAR, Square.sq(col, row));
            }
        }
        b.put(EMPTY, Square.sq(7, 7));
        for (int lip = 3; lip < 7; lip += 1) {
            b.put(WHITE, Square.sq(lip, 2));
        }
        b.put(WHITE, Square.sq(2, 3));
        b.put(WHITE, Square.sq(7, 3));
    }

    static final String INIT_BOARD_STATE =
            "   - - - B - - B - - -\n"
                    +
            "   - - - - - - - - - -\n"
                    +
            "   - - - - - - - - - -\n"
                    +
            "   B - - - - - - - - B\n"
                    +
            "   - - - - - - - - - -\n"
                    +
            "   - - - - - - - - - -\n"
                    +
            "   W - - - - - - - - W\n"
                    +
            "   - - - - - - - - - -\n"
                    +
            "   - - - - - - - - - -\n"
                    +
            "   - - - W - - W - - -\n";

    static final String SMILE =
            "   - - - - - - - - - -\n"
                    +
            "   - S S S - - S S S -\n"
                    +
            "   - S - S - - S - S -\n"
                    +
            "   - S S S - - S S S -\n"
                    +
            "   - - - - - - - - - -\n"
                    +
            "   - - - - - - - - - -\n"
                    +
            "   - - W - - - - W - -\n"
                    +
            "   - - - W W W W - - -\n"
                    +
            "   - - - - - - - - - -\n"
                    +
            "   - - - - - - - - - -\n";

    @Test
    public void testQueenMove() {
        Square test1 = Square.sq("d", "3");
        Square test1Exp = Square.sq("f", "5");
        Square test2Exp = Square.sq("g", "3");
        Square test3Exp = Square.sq(0, 2);

        assertEquals(test1Exp, test1.queenMove(1, 2));
        assertEquals(test2Exp, test1.queenMove(2, 3));
        assertEquals(test3Exp, test1.queenMove(6, 3));
    }

    @Test
    public void testDirection() {
        Square test1 = Square.sq("d", "3");
        int direc1Exp = 3;
        int direc2Exp = 7;
        int direc3Exp = 0;
        int direc4Exp = 1;

        assertEquals(direc1Exp, test1.direction(Square.sq("f", "1")));
        assertEquals(direc2Exp, test1.direction(Square.sq("b", "5")));
        assertEquals(direc3Exp, test1.direction(Square.sq(3, 9)));
        assertEquals(direc4Exp, test1.direction(Square.sq("f", "5")));

    }

    @Test
    public void testIsLegalMakeMove() {
        Board testBoard = new Board();

        assertTrue(testBoard.isLegal(Square.sq("a", "4")));
        assertTrue(testBoard.isLegal(Square.sq("c", "5")));
        assertTrue(testBoard.isLegal(Square.sq("a", "7")));
        assertTrue(testBoard.isLegal(Square.sq("a", "4"), Square.sq("b", "4")));
        testBoard.makeMove(Square.sq("a", "4"),
                Square.sq("c", "4"),
                Square.sq("e", "6"));
        assertEquals(1, testBoard.numMoves());
        assertEquals(1, testBoard.movesStack().size());
        assertEquals(Move.mv(Square.sq("a", "4"), Square.sq("c", "4"),
                Square.sq("e", "6")),
                testBoard.movesStack().peek());
        assertEquals(BLACK, testBoard.turn());
        assertTrue(testBoard.isLegal(Square.sq("a", "7")));

    }

}


