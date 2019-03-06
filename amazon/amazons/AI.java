package amazons;

import java.util.Iterator;

import static amazons.Piece.*;

/** A Player that automatically generates moves.
 *  @author Kelley
 */
class AI extends Player {

    /** A position magnitude indicating a win (for white if positive, black
     *  if negative). */
    private static final int WINNING_VALUE = Integer.MAX_VALUE - 1;
    /** A magnitude greater than a normal value. */
    private static final int INFTY = Integer.MAX_VALUE;

    /** A new AI with no piece or controller (intended to produce
     *  a template). */
    AI() {
        this(null, null);
    }

    /** A new AI playing PIECE under control of CONTROLLER. */
    AI(Piece piece, Controller controller) {
        super(piece, controller);
    }

    @Override
    Player create(Piece piece, Controller controller) {
        return new AI(piece, controller);
    }

    @Override
    String myMove() {
        Move move = findMove();
        _controller.reportMove(move);
        return move.toString();
    }

    /** Return a move for me from the current position, assuming there
     *  is a move. */
    private Move findMove() {
        Board b = new Board(board());
        if (_myPiece == WHITE) {
            findMove(b, maxDepth(b), true, 1, -INFTY, INFTY);
        } else {
            findMove(b, maxDepth(b), true, -1, -INFTY, INFTY);
        }
        return _lastFoundMove;
    }

    /** The move found by the last call to one of the ...FindMove methods
     *  below. */
    private Move _lastFoundMove;

    /** Find a move from position BOARD and return its value, recording
     *  the move found in _lastFoundMove iff SAVEMOVE. The move
     *  should have maximal value or have value > BETA if SENSE==1,
     *  and minimal value or value < ALPHA if SENSE==-1. Searches up to
     *  DEPTH levels.  Searching at level 0 simply returns a static estimate
     *  of the board value and does not set _lastMoveFound. */
    private int findMove(Board board, int depth, boolean saveMove,
                         int sense, int alpha, int beta) {
        if (depth == 0 || board.winner() != null) {
            return staticScore(board);
        }
        if (sense == 1) {
            Iterator<Move> white = board.legalMoves(WHITE);
            int bestVal = Integer.MIN_VALUE;
            while (white.hasNext()) {
                Move next = white.next();
                board.makeMove(next);
                int responseMove = findMove(board,
                        depth - 1,
                        false, -1,
                        alpha, beta);
                board.undo();
                if (responseMove >= bestVal) {
                    bestVal = responseMove;
                    if (saveMove) {
                        _lastFoundMove = next;
                    }
                    alpha = Math.max(alpha, responseMove);
                    if (beta <= alpha) {
                        break;
                    }
                }
            }
            return bestVal;

        } else {
            Iterator<Move> black = board.legalMoves(BLACK);
            int bestVal = Integer.MAX_VALUE;

            while (black.hasNext()) {
                Move next = black.next();
                board.makeMove(next);
                int responseMove = findMove(board,
                        depth - 1,
                        false, 1,
                        alpha, beta);
                board.undo();
                if (responseMove <= bestVal) {
                    bestVal = responseMove;
                    if (saveMove) {
                        _lastFoundMove = next;
                    }
                    beta = Math.min(beta, responseMove);
                    if (beta <= alpha) {
                        break;
                    }
                }
            }
            return bestVal;
        }
    }

    /** Return a heuristically determined maximum search depth
     *  based on characteristics of BOARD. */
    private int maxDepth(Board board) {
        int N = board.numMoves();
        final int A = 30;
        final int B = 38;
        final int C = 44;
        final int D = 50;
        if (N < A) {
            return 1;
        } else if (N > A && N < B) {
            return 2;
        } else if (N > B && N < C) {
            return 3;
        } else if (N > C && N < D) {
            return 4;
        } else {
            return 5;
        }

    }

    /** Return a heuristic value for BOARD. */
    private int staticScore(Board board) {
        Piece winner = board.winner();
        if (winner == BLACK) {
            return -WINNING_VALUE;
        } else if (winner == WHITE) {
            return WINNING_VALUE;
        }
        int numMovesW = 0;
        Iterator<Move> white = board.legalMoves(WHITE);
        while (white.hasNext()) {
            white.next();
            numMovesW++;
        }
        int numMovesB = 0;
        Iterator<Move> black = board.legalMoves(BLACK);
        while (black.hasNext()) {
            black.next();
            numMovesB++;
        }
        return numMovesW - numMovesB;
    }
}
