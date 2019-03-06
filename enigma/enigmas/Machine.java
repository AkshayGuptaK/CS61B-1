package enigma;

import java.util.Collection;
import static enigma.EnigmaException.*;

/** Class that represents a complete enigma machine.
 *  @author Kelley
 */
class Machine {

    /** A new Enigma machine with alphabet ALPHA, 1 < NUMROTORS rotor slots,
     *  and 0 <= PAWLS < NUMROTORS pawls.  ALLROTORS contains all the
     *  available rotors. */
    Machine(Alphabet alpha, int numRotors, int pawls,
            Collection<Rotor> allRotors) {
        _alphabet = alpha;
        _numRotors = numRotors;
        _numPawls = pawls;
        _allRotors = allRotors;
        machineRotors = new Rotor[_numRotors];
    }

    /** Return the number of rotor slots I have. */
    int numRotors() {
        return _numRotors;
    }

    /** Return the number pawls (and thus rotating rotors) I have. */
    int numPawls() {
        return _numPawls;
    }

    /** Set my rotor slots to the rotors named ROTORS from my set of
     *  available rotors (ROTORS[0] names the reflector).
     *  Initially, all rotors are set at their 0 setting. */
    void insertRotors(String[] rotors) {
        int ind = 0;
        while (ind < _numRotors) {
            for (String r : rotors) {
                for (Rotor rot : _allRotors) {
                    if (rot.name().equals(r)) {
                        machineRotors[ind] = rot;
                        ind++;
                        break;
                    }
                }
            }
        }
        if (machineRotors.length != rotors.length) {
            throw error("not the right number of rotors");
        }
        if (!machineRotors[0].reflecting()) {
            throw error("first rotor must be reflector");
        }
        int count = 0;
        for (Rotor r : machineRotors) {
            if (r.rotates()) {
                count += 1;
            }
        }
        if (count != numPawls()) {
            throw error("wrong number of moving rotors");
        }
    }

    /** Set my rotors according to SETTING, which must be a string of
     *  numRotors()-1 upper-case letters. The first letter refers to the
     *  leftmost rotor setting (not counting the reflector).  */
    void setRotors(String setting) {
        if (setting.length() != (numRotors() - 1)) {
            throw error("wrong number of initial positions");
        }

        for (int i = 0; i < numRotors() - 1; i++) {
            if (!_alphabet.contains(setting.charAt(i))) {
                throw error("position setting not in alphabet");
            }
        }
        int settingInd = 0;
        for (int i = 1; i < numRotors(); i++) {
            char c = setting.charAt(settingInd);
            int set = _alphabet.toInt(c);
            machineRotors[i].set(set);
            settingInd++;
        }
    }

    /** Set the plugboard to PLUGBOARD. */
    void setPlugboard(Permutation plugboard) {
        _plugboard = plugboard;
    }

    /** Returns the result of converting the input character C (as an
     *  index in the range 0..alphabet size - 1), after first advancing
     *  the machine. */

    int convert(int c) {
        boolean[] hasAdvanced = new boolean[numRotors()];
        boolean[] atNotch = new boolean[numRotors()];

        for (int i = 0; i < numRotors(); i++) {
            atNotch[i] = machineRotors[i].atNotch();

        } machineRotors[numRotors() - 1].advance();
        hasAdvanced[numRotors() - 1] = true;

        for (int ind = numRotors() - 1; ind > 0; ind--) {
            if (atNotch[ind] && machineRotors[ind - 1].rotates()) {
                if (!hasAdvanced[ind]) {
                    machineRotors[ind].advance();
                    hasAdvanced[ind] = true;
                }
                if (!hasAdvanced[ind - 1]) {
                    machineRotors[ind - 1].advance();
                    hasAdvanced[ind - 1] = true;
                }
            }
        }
        int result = c;
        if (_plugboard != null) {
            result = _plugboard.permute(c);
        }

        for (int ind = numRotors() - 1; ind > -1; ind--) {
            result = machineRotors[ind].convertForward(result);
        }

        for (int back = 1; back < numRotors(); back++) {
            result = machineRotors[back].convertBackward(result);
        }
        if (_plugboard != null) {
            result = _plugboard.invert(result);
        }
        return result;
    }

    /** Returns the encoding/decoding of MSG, updating the state of
     *  the rotors accordingly. */
    String convert(String msg) {
        String upper = msg.toUpperCase();
        String noSpace = upper.replace(" ", "");
        String result = "";
        for (int i = 0; i < noSpace.length(); i++) {
            char convertedLet = _alphabet.toChar(convert(
                    _alphabet.toInt(noSpace.charAt(i))));
            result += convertedLet;
        }
        return result;
    }

    /** Common alphabet of my rotors. */
    private  Alphabet _alphabet;

    /** Number of roters in enigma machine. **/
    private int _numRotors;

    /** Number of pawls in enigma machines. **/
    private int _numPawls;

    /** All rotors. **/
    private Collection<Rotor> _allRotors;

    /** Rotors for this machine. **/
    private Rotor[] machineRotors;

    /** Permutations of plugboard. **/
    private Permutation _plugboard;
}
