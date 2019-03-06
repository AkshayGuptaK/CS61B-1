package amazons;

import static amazons.Move.isGrammaticalMove;

/** A Player that takes input as text commands from the standard input.
 *  @author Kelley
 */
class TextPlayer extends Player {

    /** A new TextPlayer with no piece or controller (intended to produce
     *  a template). */
    TextPlayer() {
        this(null, null);
    }

    /** A new TextPlayer playing PIECE under control of CONTROLLER. */
    private TextPlayer(Piece piece, Controller controller) {
        super(piece, controller);
    }

    @Override
    Player create(Piece piece, Controller controller) {
        return new TextPlayer(piece, controller);
    }

    @Override
    String myMove() {
        while (true) {
            String line = _controller.readLine();
            if (line == null) {
                return "quit";
            } else if (line.matches("(new)|(quit)|(dump)|"
                    +
                    "((manual)\\s+(([wW][hH][iI][tT][eE])|"
                    +
                    "([bB][lL][aA][cC][kK])$))|"
                    +
                    "(auto\\s+(([wW][hH][iI][tT][eE])|"
                    +
                    "([bB][lL][aA][cC][kK])$))|"
                    +
                    "seed\\s+(\\d+)$|[a-j]\\d+-[a-j]\\d+\\([a-j]\\d+\\)|"
                    +
                    "(#.*)")) {
                return line;
            } else if (!isGrammaticalMove(line)) {
                _controller.reportError("Invalid move. "
                                        + "Please try again.");
                continue;
            } else if (isGrammaticalMove(line)) {
                return line;
            }
        }
    }
}
