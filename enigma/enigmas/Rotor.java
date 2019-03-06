package enigma;

import static enigma.EnigmaException.*;

/** Superclass that represents a rotor in the enigma machine.
 *  @author Kelley
 */
class Rotor {

    /** A rotor named NAME whose permutation is given by PERM. */
    Rotor(String name, Permutation perm) {
        _name = name;
        _permutation = perm;
        this.set(0);
    }

    /** Return my name. */
    String name() {
        return _name.toUpperCase();
    }

    /** Return my alphabet. */
    Alphabet alphabet() {
        return _permutation.alphabet();
    }

    /** Return my permutation. */
    Permutation permutation() {
        return _permutation;
    }

    /** Return the size of my alphabet. */
    int size() {
        return _permutation.size();
    }

    /** Return true iff I have a ratchet and can move. */
    boolean rotates() {
        return false;
    }

    /** Return true iff I reflect. */
    boolean reflecting() {
        return false;
    }

    /** Return my current setting. */
    int setting() {
        return nsetting;
    }

    /** Set setting() to POSN.  */
    void set(int posn) {
        nsetting = _permutation.wrap(posn);

    }

    /** Set setting() to character CPOSN. */
    void set(char cposn) {
        Alphabet alph = alphabet();
        nsetting = _permutation.wrap(alph.toInt(cposn));
    }

    /** Return the conversion of P (an integer in the range 0..size()-1)
     *  according to my permutation. */
    int convertForward(int p) {
        int in = permutation().permute(permutation().wrap(p + nsetting));
        int out = permutation().wrap(in - nsetting);
        return out;
    }

    /** Return the conversion of E (an integer in the range 0..size()-1)
     *  according to the inverse of my permutation. */
    int convertBackward(int e) {
        int in = permutation().invert(permutation().wrap(e + nsetting));
        return permutation().wrap(in - nsetting);
    }

    /** Returns true iff I am positioned to allow the rotor to my left
     *  to advance. */
    boolean atNotch() {
        return false;
    }

    /** Advance me one position, if possible. By default, does nothing. */
    void advance() {
    }

    /** My name. */
    private final String _name;

    /** The permutation implemented by this rotor in its 0 position. */
    private Permutation _permutation;

    /** Store current number setting of rotor.*/
    private int nsetting;
}
