package amazons;

import java.util.Stack;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Collections;
import static amazons.Piece.*;
import static amazons.Move.mv;
import static amazons.Utils.*;


/** The state of an Amazons Game.
 *  @author Kelley
 */
class Board {

    /**
     * The number of squares on a side of the board.
     */
    static final int SIZE = 10;

    /**
     * Initializes a game board with SIZE squares on a side in the
     * initial position.
     */
    Board() {
        init();
    }

    /**
     * Initializes a copy of MODEL.
     */
    Board(Board model) {
        copy(model);
    }

    /**
     * Copies MODEL into me.
     */
    void copy(Board model) {
        if (model == this) {
            return;
        } else {
            this._board = new Piece[SIZE][SIZE];
            for (int i = 0; i < _board.length; i++) {
                for (int j = 0; j < _board[i].length; j++) {
                    this._board[i][j] = model._board[i][j];
                }
            }
            this._turn = model._turn;
            this._winner = model._winner;
            this._moves = model._moves;
            this._movesStack = model._movesStack;
        }
    }

    /**
     * Clears the board to the initial position.
     */
    void init() {
        _turn = WHITE;
        _winner = null;
        _moves = 0;
        _movesStack = new Stack<>();

        this._board = new Piece[SIZE][SIZE];
        for (int i = 0; i < _board.length; i++) {
            for (int j = 0; j < _board[i].length; j++) {
                _board[i][j] = EMPTY;
            }

        }
        _board[0][3] = BLACK;
        _board[0][6] = BLACK;
        _board[3][0] = BLACK;
        _board[3][9] = BLACK;

        _board[6][0] = WHITE;
        _board[6][9] = WHITE;
        _board[9][3] = WHITE;
        _board[9][6] = WHITE;
    }

    /**
     * Return the Piece whose move it is (WHITE or BLACK).
     */
    Piece turn() {
        return _turn;
    }

    /**
     * Return the number of moves (that have not been undone) for this
     * board.
     */
    int numMoves() {
        return _moves;
    }

    /**
     * Return the winner in the current position, or null if the game is
     * not yet finished.
     */
    Piece winner() {
        Iterator<Move> white = legalMoves(WHITE);
        Iterator<Move> black = legalMoves(BLACK);
        if (_turn.equals(BLACK) && black.hasNext() && !white.hasNext()) {
            return BLACK;
        } else if (_turn.equals(WHITE) && white.hasNext() && !black.hasNext()) {
            return WHITE;
        }
        return null;
    }

    /**
     * Return the contents the square at S.
     */
    final Piece get(Square s) {
        return this._board[s.row()][s.col()];
    }

    /**
     * Return the contents of the square at (COL, ROW), where
     * 0 <= COL, ROW <= 9.
     */
    final Piece get(int col, int row) {
        return get(Square.sq(col, row));
    }

    /**
     * Return the contents of the square at 0 <= COL, ROW <= 9.
     */
    final Piece get(char col, char row) {
        return get(col - 'a', row - '1');
    }

    /**
     * Set square S to P.
     */
    final void put(Piece p, Square s) {
        this._board[s.row()][s.col()] = p;
    }

    /**
     * Set square (COL, ROW) to P.
     */
    final void put(Piece p, int col, int row) {
        put(p, Square.sq(col, row));
    }

    /**
     * Set square COL ROW to P.
     */
    final void put(Piece p, char col, char row) {
        put(p, col - 'a', row - '1');
    }

    /**
     * Return true iff FROM - TO is an unblocked queen move on the current
     * board, ignoring the contents of ASEMPTY, if it is encountered.
     * For this to be true, FROM-TO must be a queen move and the
     * squares along it, other than FROM and ASEMPTY, must be
     * empty. ASEMPTY may be null, in which case it has no effect.
     */
    boolean isUnblockedMove(Square from, Square to, Square asEmpty) {
        if (from.isQueenMove(to)) {
            Square currQ = from;
            int direc = currQ.direction(to);
            int dy = to.row() - from.row();
            int dx = to.col() - from.col();

            if (Math.abs(dx) == Math.abs(dy)) {
                for (int i = 1; i < Math.abs(dx) + 1; i++) {
                    currQ = from.queenMove(direc, i);
                    Piece currP = get(currQ);
                    if (currP == EMPTY || currQ == asEmpty) {
                        continue;
                    } else {
                        return false;
                    }
                }
            } else if (dx != 0) {
                for (int i = 1; i < Math.abs(dx) + 1; i++) {
                    currQ = from.queenMove(direc, i);
                    Piece currP = get(currQ);
                    if (currP == EMPTY || currQ == asEmpty) {
                        continue;
                    } else {
                        return false;
                    }
                }
            } else if (dy != 0) {
                for (int i = 1; i < Math.abs(dy) + 1; i++) {
                    currQ = from.queenMove(direc, i);
                    Piece currP = get(currQ);
                    if (currP == EMPTY || currQ == asEmpty) {
                        continue;
                    } else {
                        return false;
                    }
                }
            }
            return true;
        }
        return false;

    }

    /**
     * Return true iff FROM is a valid starting square for a move.
     */
    boolean isLegal(Square from) {
        return Square.exists(from.col(), from.rowReverse());
    }


    /**
     * Return true iff FROM-TO is a valid
     * first part of move, ignoring spear throwing.*/
    boolean isLegal(Square from, Square to) {
        if (isLegal(from) && isLegal(to) && get(to) == EMPTY) {
            if (from.isQueenMove(to)) {
                return isUnblockedMove(from, to, null);
            }
            return false;
        }
        return false;
    }

    /**
     * Return true iff FROM-TO(SPEAR) is a legal move in the current
     * position.
     */
    boolean isLegal(Square from, Square to, Square spear) {
        if (isLegal(from, to)) {
            return isUnblockedMove(to, spear, from);
        }
        return false;
    }

    /**
     * Return true iff MOVE is a legal move in the current
     * position.
     */
    boolean isLegal(Move move) {
        return isLegal(move.from(), move.to(), move.spear());
    }

    /**
     * Move FROM-TO(SPEAR), assuming this is a legal move.
     */
    void makeMove(Square from, Square to, Square spear) {
        if (isLegal(from, to, spear)) {
            Move currMove = mv(from, to, spear);
            _movesStack.push(currMove);
            put(EMPTY, from);
            put(turn(), to);
            put(SPEAR, spear);
            _moves += 1;
            _turn = turn().opponent();
        }
    }
    /** Return stack of moves made in the game. **/
    public Stack<Move> movesStack() {
        return _movesStack;
    }
    /** Move according to MOVE, assuming it is a legal move.
     */
    void makeMove(Move move) {
        makeMove(move.from(), move.to(), move.spear());
    }

    /**
     * Undo one move.  Has no effect on the initial board.
     */
    void undo() {
        if (numMoves() > 0) {
            _moves -= 1;
            Move undo1 = _movesStack.pop();
            put(get(undo1.to()), undo1.from());
            put(EMPTY, undo1.spear());
            put(EMPTY, undo1.to());
            _turn = turn().opponent();
        }
    }

    /**
     * Return an Iterator over the Squares that are reachable by an
     * unblocked queen move from FROM. Does not pay attention to what
     * piece (if any) is on FROM, nor to whether the game is finished.
     * Treats square ASEMPTY (if non-null) as if it were EMPTY.  (This
     * feature is useful when looking for Moves, because after moving a
     * piece, one wants to treat the Square it came from as empty for
     * purposes of spear throwing.)
     */
    Iterator<Square> reachableFrom(Square from, Square asEmpty) {
        return new ReachableFromIterator(from, asEmpty);
    }
    /**Return an Iterator over all legal moves on the current board.*/
    Iterator<Move> legalMoves() {
        return new LegalMoveIterator(_turn);
    }
    /**Return an Iterator over all legal moves on the current board for SIDE
     * (regardless of whose turn it is).*/
    Iterator<Move> legalMoves(Piece side) {
        return new LegalMoveIterator(side);
    }
    /**An iterator used by reachableFrom.*/
    private class ReachableFromIterator implements Iterator<Square> {

        /**Iterator of all squares reachable by queen move from FROM,
         * treating ASEMPTY as empty.*/
        ReachableFromIterator(Square from, Square asEmpty) {
            _from = from;
            _dir = 0;
            _steps = 0;
            _asEmpty = asEmpty;
            next();
        }

        @Override
        public boolean hasNext() {
            toNext();
            return _dir < 8;
        }

        @Override
        public Square next() {
            if (_dir < 8) {
                return _from.queenMove(_dir, _steps);
            } else {
                throw error("queen does not have another move");
            }

        }

        /**Advance _dir and _steps,
         * so that the next valid Square is_steps steps
         * in direction _dir from _from.*/
        private void toNext() {
            _steps += 1;
            Square to = _from.queenMove(_dir, _steps);
            while (to == null) {
                _dir += 1;
                _steps = 1;
                if (_dir < 8) {
                    to = _from.queenMove(_dir, _steps);
                } else {
                    break;
                }
            }
            while (_dir < 8 && !isUnblockedMove(_from, to, _asEmpty)) {
                _dir += 1;
                _steps = 1;
                if (_dir < 8) {
                    to = _from.queenMove(_dir, _steps);
                }
            }
        }

        /**Starting square.*/
        private Square _from;
        /**Current direction.*/
        private int _dir;
        /**Current distance.*/
        private int _steps;
        /**Square treated as empty.*/
        private Square _asEmpty;

    }

    /**An iterator used by legalMoves.*/
    private class LegalMoveIterator implements Iterator<Move> {

        /**All legal moves for SIDE (WHITE or BLACK).*/
        LegalMoveIterator(Piece side) {
            _startingSquares = Square.iterator();
            _fromPiece = side;
            allQueens = new ArrayList<>();
            _spearThrows = NO_SQUARES;
            _pieceMoves = NO_SQUARES;
            while (_startingSquares.hasNext()) {
                Square sq = _startingSquares.next();
                if (get(sq).equals(_fromPiece)) {
                    allQueens.add(sq);
                }
            }
            numMoves = 0;
            toNext();
        }
        @Override
        public boolean hasNext() {
            return numMoves <  _allMoves.size();
        }

        @Override
        public Move next() {
            if (hasNext()) {
                Move move = _allMoves.get(numMoves);
                numMoves++;
                return move;
            }
            return null;
        }

        /** Advance so that the next valid Move is
         * _start-_nextSquare(sp), where sp is the next value of
         * _spearThrows. **/
        public void toNext() {
            _allMoves = new ArrayList<>();
            for (Square queen : allQueens) {
                _start = queen;
                _pieceMoves = reachableFrom(_start, null);
                while (_pieceMoves.hasNext()) {
                    _nextSquare = _pieceMoves.next();
                    _spearThrows = reachableFrom(_nextSquare, _start);
                    while (_spearThrows.hasNext()) {
                        _spear = _spearThrows.next();
                        Move next = mv(_start, _nextSquare, _spear);
                        _allMoves.add(next);
                    }
                }
            }
        }

        /**Color of side whose moves we are iterating.*/
        private Piece _fromPiece;
        /**Current starting square.*/
        private Square _start;
        /**Remaining starting squares to consider.*/
        private Iterator<Square> _startingSquares;
        /**Current piece's new position.*/
        private Square _nextSquare;
        /**Remaining moves from _start to consider.*/
        private Iterator<Square> _pieceMoves;
        /**Remaining spear throws from _piece to consider.*/
        private Iterator<Square> _spearThrows;
        /**Current spear location. */
        private Square _spear;
        /** Possible moves for queens on board. */
        private ArrayList<Move> _allMoves;
        /** Number of moves made. **/
        private int numMoves;
        /** Queens for this board. **/
        private ArrayList<Square> allQueens;
    }

    @Override
    public String toString() {
        String printed = "";
        for (Piece[] row : _board) {
            printed += "   ";
            int ind = 0;
            for (Piece p : row) {
                if (ind == _board.length - 1) {
                    printed += p.toString() + '\n';
                } else {
                    printed += p.toString() + ' ';
                }
                ind++;
            }
        }
        return printed;
    }

    /**An empty iterator for initialization.*/
    private static final Iterator<Square> NO_SQUARES =
            Collections.emptyIterator();
    /**Piece whose turn it is (BLACK or WHITE). */
    private Piece _turn;
    /**Cached value of winner on this board, or EMPTY if not been computed. */
    private Piece _winner;
    /**Game board. **/
    private Piece[][] _board;
    /**Number of moves made in game. */
    private int _moves;
    /**Stack to keep track of moves.*/
    private Stack<Move> _movesStack;
}
