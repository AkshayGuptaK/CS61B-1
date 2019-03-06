package amazons;
import org.junit.Test;
import static org.junit.Assert.*;
import ucb.junit.textui;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import static amazons.Square.*;
import static amazons.Piece.*;

/** Junit tests for our Board iterators.
 *  @author
 */
public class IteratorTests {

    /**
     * Run the JUnit tests in this package.
     */
    public static void main(String[] ignored) {
        textui.runClasses(IteratorTests.class);
    }

    /**
     * Tests reachableFromIterator to make sure it returns all reachable
     * Squares. This method may need to be changed based on
     * your implementation.
     */
    @Test
    public void testReachableFrom() {
        Board b = new Board();
        buildBoard(b, REACHABLEFROMTESTBOARD);
        int numSquares = 0;
        Set<Square> squares = new HashSet<>();
        Iterator<Square> reachableFrom = b.reachableFrom(Square.sq(5, 4), null);
        while (reachableFrom.hasNext()) {
            Square s = reachableFrom.next();
            assertTrue(REACHABLEFROMTTESTSQUARES.contains(s));
            numSquares += 1;
            squares.add(s);
        }
        assertEquals(REACHABLEFROMTTESTSQUARES.size(), numSquares);
        assertEquals(REACHABLEFROMTTESTSQUARES.size(), squares.size());
    }

    @Test
    public void testReachableFrom2() {
        Board b = new Board();
        buildBoard(b, TESTBOARD3);
        int numSquares = 0;
        Set<Square> squares = new HashSet<>();
        Iterator<Square> reachableFrom = b.reachableFrom(
                Square.sq("a", "10"), null);
        while (reachableFrom.hasNext()) {
            Square s = reachableFrom.next();
            numSquares += 1;
            squares.add(s);
        }
        assertEquals(0, numSquares);
        assertEquals(0, squares.size());
    }

    /**
     * Tests legalMovesIterator to make sure it returns all legal Moves.
     * This method needs to be finished and may need to be changed
     * based on your implementation.
     */
    @Test
    public void testLegalMoves() {
        Board b = new Board();
        buildBoard(b, LEGALTESTBOARD);
        int numMoves = 0;
        Set<Move> moves = new HashSet<>();
        Iterator<Move> legalMoves = b.legalMoves(Piece.WHITE);
        while (legalMoves.hasNext()) {
            Move m = legalMoves.next();
            assertTrue(LEGALMOVESTESTSQUARES.contains(m));
            numMoves += 1;
            moves.add(m);
        }
        assertEquals(8, numMoves);
        assertEquals(8, moves.size());
    }

    @Test
    public void testLegalMoves2() {
        Board b = new Board();
        buildBoard(b, LEGALTESTBOARD2);
        int numMoves = 0;
        Set<Move> moves = new HashSet<>();
        Iterator<Move> legalMoves = b.legalMoves(Piece.WHITE);
        while (legalMoves.hasNext()) {
            Move m = legalMoves.next();
            numMoves += 1;
            moves.add(m);
        }
        assertEquals(1, numMoves);
        assertEquals(1, moves.size());
    }

    @Test
    public void testLegalMovesCorner() {
        Board b = new Board();
        buildBoard(b, TESTBOARD3);
        int numMoves = 0;
        Set<Move> moves = new HashSet<>();
        Iterator<Move> legalMoves = b.legalMoves(Piece.WHITE);
        while (legalMoves.hasNext()) {
            Move m = legalMoves.next();
            numMoves += 1;
            moves.add(m);
        }
        assertEquals(0, numMoves);
        assertEquals(0, moves.size());
    }

    @Test
    public void testLegalMovesInit() {
        Board b = new Board();
        buildBoard(b, INITTESTBOARD);
        int numMoves = 0;
        Set<Move> moves = new HashSet<>();
        Iterator<Move> legalMoves = b.legalMoves(Piece.WHITE);
        while (legalMoves.hasNext()) {
            Move m = legalMoves.next();
            numMoves += 1;
            moves.add(m);
        }
        assertEquals(2176, numMoves);
        assertEquals(2176, moves.size());
    }

    @Test
    public void testLegalMovesSmile() {
        Board b = new Board();
        buildBoard(b, INITTESTBOARDSMILE);
        int numMoves = 0;
        Set<Move> moves = new HashSet<>();
        Iterator<Move> legalMoves = b.legalMoves(Piece.WHITE);
        while (legalMoves.hasNext()) {
            Move m = legalMoves.next();
            numMoves += 1;
            moves.add(m);
        }
        assertEquals(1942, numMoves);
        assertEquals(1942, moves.size());
    }

    @Test
    public void testWinner() {
        Board b = new Board();
        buildBoard(b, TESTWINNERBOARD);
        assertEquals(Piece.WHITE, b.winner());
    }

    private void buildBoard(Board b, Piece[][] target) {
        for (int col = 0; col < Board.SIZE; col++) {
            for (int row = Board.SIZE - 1; row >= 0; row--) {
                Piece piece = target[Board.SIZE - row - 1][col];
                b.put(piece, Square.sq(col, row));
            }
        }
        System.out.println(b);
    }

    static final Piece E = Piece.EMPTY;

    static final Piece W = Piece.WHITE;

    static final Piece B = Piece.BLACK;

    static final Piece S = Piece.SPEAR;

    static final Piece[][] REACHABLEFROMTESTBOARD =
    {
        {E, E, E, E, E, E, E, E, E, E},
        {E, E, E, E, E, E, E, E, W, W},
        {E, E, E, E, E, E, E, S, E, S},
        {E, E, E, S, S, S, S, E, E, S},
        {E, E, E, S, E, E, E, E, B, E},
        {E, E, E, S, E, W, E, E, B, E},
        {E, E, E, S, S, S, B, W, B, E},
        {E, E, E, E, E, E, E, E, E, E},
        {E, E, E, E, E, E, E, E, E, E},
        {E, E, E, E, E, E, E, E, E, E},
    };

    static final Set<Square> REACHABLEFROMTTESTSQUARES =
            new HashSet<>(Arrays.asList(
                    Square.sq(5, 5),
                    Square.sq(4, 5),
                    Square.sq(4, 4),
                    Square.sq(6, 4),
                    Square.sq(7, 4),
                    Square.sq(6, 5),
                    Square.sq(7, 6),
                    Square.sq(8, 7)));

    static final Piece[][] LEGALTESTBOARD =
    {
        {S, S, S, S, S, E, E, E, E, E},
        {S, E, W, E, S, E, E, E, E, E},
        {S, S, S, S, S, E, E, E, E, E},
        {S, E, W, E, S, E, E, E, E, E},
        {S, S, S, S, S, E, E, E, E, E},
        {E, E, E, E, E, E, E, E, E, E},
        {E, E, E, E, E, E, E, E, E, E},
        {E, E, E, E, E, E, E, E, E, E},
        {E, E, E, E, E, E, E, E, E, E},
        {E, E, E, E, E, E, E, E, E, E},
    };
    static final Set<Move> LEGALMOVESTESTSQUARES =
            new HashSet<>(Arrays.asList(
                    Move.mv(Square.sq("c", "9"),
                            Square.sq("d", "9"), Square.sq("c", "9")),
                    Move.mv(Square.sq("c", "9"),
                            Square.sq("d", "9"), Square.sq("b", "9")),
                    Move.mv(Square.sq("c", "9"),
                            Square.sq("b", "9"), Square.sq("c", "9")),
                    Move.mv(Square.sq("c", "9"),
                            Square.sq("b", "9"), Square.sq("d", "9")),
                    Move.mv(Square.sq("c", "7"),
                            Square.sq("d", "7"), Square.sq("c", "7")),
                    Move.mv(Square.sq("c", "7"),
                            Square.sq("d", "7"), Square.sq("b", "7")),
                    Move.mv(Square.sq("c", "7"),
                            Square.sq("b", "7"), Square.sq("c", "7")),
                    Move.mv(Square.sq("c", "7"),
                            Square.sq("b", "7"), Square.sq("d", "7")))
            );
    static final Piece[][] LEGALTESTBOARD2 =
    {
        {E, S, E, S, E, E, E, E, E, E},
        {S, S, E, S, E, E, E, E, E, E},
        {W, S, S, S, E, E, E, E, E, E},
        {E, S, E, E, E, E, E, E, E, E},
        {S, S, E, E, E, E, E, E, E, E},
        {E, E, E, E, E, E, E, E, E, E},
        {E, E, E, E, E, E, E, E, E, E},
        {E, E, E, E, E, E, E, E, E, E},
        {E, E, E, E, E, E, E, E, E, E},
        {E, E, E, E, E, E, E, E, E, E},
    };

    static final Piece[][] TESTBOARD3 =
    {
        {W, S, E, S, E, E, E, E, E, E},
        {S, S, S, S, E, E, E, E, E, E},
        {E, S, S, S, E, E, E, E, E, E},
        {E, E, E, E, E, E, E, E, E, E},
        {E, E, E, E, E, E, E, E, E, E},
        {E, E, E, E, E, E, E, E, E, E},
        {E, E, E, E, E, E, E, E, E, E},
        {E, E, E, E, E, E, E, E, E, E},
        {E, E, E, E, E, E, E, E, E, E},
        {E, E, E, E, E, E, E, E, E, E},
    };

    static final Piece[][] INITTESTBOARD =
    {
        {E, E, E, B, E, E, B, E, E, E},
        {E, E, E, E, E, E, E, E, E, E},
        {E, E, E, E, E, E, E, E, E, E},
        {B, E, E, E, E, E, E, E, E, B},
        {E, E, E, E, E, E, E, E, E, E},
        {E, E, E, E, E, E, E, E, E, E},
        {W, E, E, E, E, E, E, E, E, W},
        {E, E, E, E, E, E, E, E, E, E},
        {E, E, E, E, E, E, E, E, E, E},
        {E, E, E, W, E, E, W, E, E, E},
    };
    static final Piece[][] INITTESTBOARDSMILE =
    {
        {E, E, E, E, E, E, E, E, E, E},
        {E, S, S, S, E, E, S, S, S, E},
        {E, S, E, S, E, E, S, E, S, E},
        {E, S, S, S, E, E, S, S, S, E},
        {E, E, E, E, E, E, E, E, E, E},
        {E, E, E, E, E, E, E, E, E, E},
        {E, E, W, E, E, E, E, W, E, E},
        {E, E, E, W, W, W, W, E, E, E},
        {E, E, E, E, E, E, E, E, E, E},
        {E, E, E, E, E, E, E, E, E, E},
    };

    static final Piece[][] TESTWINNERBOARD =
    {
        {B, B, S, B, B, S, E, E, E, E},
        {S, S, S, S, S, S, E, E, E, E},
        {S, S, S, W, S, E, E, E, E, E},
        {E, E, E, E, E, E, E, E, E, E},
        {W, E, E, E, S, E, E, E, E, E},
        {E, E, E, E, E, E, E, E, E, E},
        {W, E, E, E, E, E, E, E, E, W},
        {E, E, E, E, E, E, E, E, E, E},
        {E, E, E, E, E, E, E, E, E, E},
        {E, E, E, E, E, E, E, E, E, E},
    };

}
