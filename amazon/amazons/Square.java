package amazons;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static amazons.Utils.*;

/** Represents a position on an Amazons board.  Positions are numbered
 *  from 0 (lower-left corner) to 99 (upper-right corner).  Squares
 *  are immutable and unique: there is precisely one square created for
 *  each distinct position.  Clients create squares using the factory method
 *  sq, not the constructor.  Because there is a unique Square object for each
 *  position, you can freely use the cheap == operator (rather than the
 *  .equals method) to compare Squares, and the program does not waste time
 *  creating the same square over and over again.
 *  @author Kelley
 */
final class Square {

    /** The regular expression for a square designation (e.g.,
     *  a3). For convenience, it is in parentheses to make it a
     *  group.  This subpattern is intended to be incorporated into
     *  other pattern that contain square designations (such as
     *  patterns for moves). */
    static final String SQ = "([a-j](?:[1-9]|10))";

    /** Return my row position, where 0 is the top row. Use for Board rep. */
    int row() {
        return _row;
    }

    /** Return row position, where 0 is the bottom row. **/
    int rowReverse() {
        return _rowRev;
    }

    /** Return my column position, where 0 is the leftmost column. */
    int col() {
        return _col;
    }

    /** Return my index position (0-99).  0 represents square a1, and 99
     *  is square j10. */
    int index() {
        return _index;
    }

    /** Return true iff THIS - TO is a valid queen move. */
    boolean isQueenMove(Square to) {
        if (to == null) {
            return false;
        } else {
            boolean validCol = this.col() == to.col();
            boolean validRow = this.row() == to.row();
            boolean validDiag = Math.abs(this.col() - to.col())
                    == Math.abs(this.row() - to.row());

            return this != to && (validCol || validRow || validDiag);
        }
    }

    /** Definitions of direction for queenMove.  DIR[k] = (dcol, drow)
     *  means that to going one step from (col, row) in direction k,
     *  brings us to (col + dcol, row + drow). */
    private static final int[][] DIR = {
        { 0, 1 }, { 1, 1 }, { 1, 0 }, { 1, -1 },
        { 0, -1 }, { -1, -1 }, { -1, 0 }, { -1, 1 }
    };

    /** Return the Square that is STEPS>0 squares away from me in direction
     *  DIR, or null if there is no such square.
     *  DIR = 0 for north, 1 for northeast, 2 for east,
     *  etc., up to 7 for northwest.
     *  If DIR has another value, return null. Thus, unless the result
     *  is null the resulting square is a queenmove away from me.*/

    Square queenMove(int dir, int steps) {
        if (dir > 7 || dir < 0) {
            return null;
        } else {
            int[] direc = DIR[dir];
            int x = (direc[1] * steps) + this.rowReverse(),
                    y = (direc[0] * steps) + this.col();
            if (x > 9 || y > 9 || x < 0 || y < 0) {
                return null;
            } else {
                return Square.sq(y, x);
            }
        }
    }

    /** Return the direction (an int as defined in the documentation
     *  for queenMove) of the queen move THIS-TO. */
    int direction(Square to) {
        assert isQueenMove(to);
        int yChange = to.row() - this.row();
        int xChange = to.col() - this.col();
        if (xChange == 0 || yChange == 0) {
            if (yChange == 0) {
                if (xChange > 0) {
                    return 2;
                }
                return 6;
            } else if (xChange == 0) {
                if (yChange > 0) {
                    return 4;
                }
                return 0;
            }
        } else if (Math.abs(xChange) == Math.abs(yChange)) {
            if (xChange > 0) {
                if (yChange > 0) {
                    return 3;
                }
                return 1;
            } else {
                if (yChange > 0) {
                    return 5;
                }
                return 7;
            }
        }
        return 0;
    }

    @Override
    public String toString() {
        return _str;
    }

    /** Return true iff COL ROW is a legal square. */
    static boolean exists(int col, int row) {
        return row >= 0 && col >= 0 && row < Board.SIZE && col < Board.SIZE;
    }

    /** Return the (unique) Square denoting COL ROW. */
    static Square sq(int col, int row) {
        if (!exists(row, col)) {
            throw error("row or column out of bounds");
        }
        return sq(col + 10 * row);
    }

    /** Return the (unique) Square denoting the position with index INDEX. */
    static Square sq(int index) {
        return SQUARES[index];
    }

    /** Return the (unique) Square denoting the position COL ROW, where
     *  COL ROW is the standard text format for a square (e.g., a4). */
    static Square sq(String col, String row) {
        int colInd = alph.indexOf(col);
        int rowInd = Integer.parseInt(row) - 1;
        return sq(colInd, rowInd);
    }

    /** Return the (unique) Square denoting the position in POSN, in the
     *  standard text format for a square (e.g. a4). POSN must be a
     *  valid square designation. */
    static Square sq(String posn) {
        assert posn.matches(SQ);
        String[] split = posn.split("(?<=\\D)(?=\\d+\\b)");
        return sq(split[0], split[1]);
    }

    /** Return an iterator over all Squares. */
    static Iterator<Square> iterator() {
        return SQUARE_LIST.iterator();
    }

    /** Return the Square with index INDEX. */
    private Square(int index) {
        alph  = "abcdefghij";
        _index = index;
        _row = Math.abs(index / 10 - 9);
        _rowRev = index / 10;
        _col = index % 10;
        char letter = (alph.charAt(_col));
        _str = String.format("%s%d", letter, index / 10 + 1);
    }

    /** The cache of all created squares, by index. */
    private static final Square[] SQUARES =
        new Square[Board.SIZE * Board.SIZE];

    /** SQUARES viewed as a List. */
    private static final List<Square> SQUARE_LIST = Arrays.asList(SQUARES);

    static {
        for (int i = Board.SIZE * Board.SIZE - 1; i >= 0; i -= 1) {
            SQUARES[i] = new Square(i);
        }
    }

    /** My index position. */
    private final int _index;

    /** My row and column (redundant, since these are determined by _index). */
    private final int _row, _col, _rowRev;

    /** My String denotation. */
    private final String _str;

    /** Column letters. **/
    private static String alph;

}
