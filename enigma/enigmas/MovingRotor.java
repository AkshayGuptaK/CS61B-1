package enigma;

import java.util.ArrayList;

import static enigma.EnigmaException.*;

/** Class that represents a rotating rotor in the enigma machine.
 *  @author Kelley
 */
class MovingRotor extends Rotor {

    /** A rotor named NAME whose permutation in its default setting is
     *  PERM, and whose notches are at the positions indicated in NOTCHES.
     *  The Rotor is initally in its 0 setting (first character of its
     *  alphabet).
     */
    MovingRotor(String name, Permutation perm, String notches) {
        super(name, perm);
        _notches = new ArrayList<>();
        for (char notch:notches.toCharArray()) {
            _notches.add(notch);
        }
    }


    @Override
    boolean rotates() {
        return true;
    }


    @Override
    boolean atNotch() {
        for (char notch: _notches) {
            if (alphabet().toInt(notch) == this.setting()) {
                return true;
            }
        }
        return false;
    }

    @Override
    void advance() {
        set(permutation().wrap(setting() + 1));
    }
    /** String array of the notch(es) of a moving rotor. **/
    private ArrayList<Character> _notches;
}
