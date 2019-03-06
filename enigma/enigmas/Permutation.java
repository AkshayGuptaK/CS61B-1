package enigma;

import java.util.ArrayList;

/** Represents a permutation of a range of integers starting at 0 corresponding
 *  to the characters of an alphabet.
 *  @author Kelley
 */
class Permutation {

    /** Set this Permutation to that specified by CYCLES, a string in the
     *  form "(cccc) (cc) ..." where the c's are characters in ALPHABET, which
     *  is interpreted as a permutation in cycle notation.  Characters in the
     *  alphabet that are not included in any cycle map to themselves.
     *  Whitespace is ignored. */
    Permutation(String cycles, Alphabet alphabet) {
        _alphabet = alphabet;
        _cycles = new ArrayList<String>();
        String tem = cycles;
        tem = tem.replace("(", "");
        tem = tem.replace(")", "");
        String[] split = tem.split(" ");
        for (String string : split) {
            addCycle(string);
        }
    }

    /** Add the cycle c0->c1->...->cm->c0 to the permutation, where CYCLE is
     *  c0c1...cm. */
    private void addCycle(String cycle) {
        boolean found = false;
        for (String current : _cycles) {
            for (char c : current.toCharArray()) {
                found = cycle.indexOf(c) > 0;
            }
        }
        if (!found) {
            _cycles.add(cycle);
        }
    }

    /** Return permutation in a ArrayList. **/
    ArrayList<String> getCycles() {
        return _cycles;
    }

    /** Return the value of P modulo the size of this permutation. */
    final int wrap(int p) {
        int r = p % size();
        if (r < 0) {
            r += size();
        }
        return r;
    }

    /** Returns the size of the alphabet I permute. */
    int size() {
        return _alphabet.size();
    }

    /** Return the value of P modulo the SIZE of the pattern.*/
    final int modP(int p, int size) {
        int r = p % size;
        if (r < 0) {
            r += size;
        }
        return r;
    }

    /** Return the result of applying this permutation to P modulo the
     *  alphabet size. */
    int permute(int p) {
        int next;
        char enter = _alphabet.toChar(wrap(p));
        for (int pattern = 0; pattern < _cycles.size(); pattern++) {
            for (int car = 0; car < _cycles.get(pattern).length(); car++) {
                if (enter == _cycles.get(pattern).charAt(car)) {
                    next = wrap
                            (_alphabet.toInt(_cycles.get(pattern).charAt(modP
                                    (car + 1, _cycles.get(pattern).length()))));
                    return next;
                }
            }
        }
        return p;

    }


    /** Return the result of applying the inverse of this permutation
     *  to  C modulo the alphabet size. */
    int invert(int c) {
        int nex;
        char ent = _alphabet.toChar(wrap(c));
        for (int pattern = 0; pattern < _cycles.size(); pattern++) {
            for (int car = 0; car < _cycles.get(pattern).length(); car++) {
                if (ent == _cycles.get(pattern).charAt(car)) {
                    nex = wrap(_alphabet.toInt
                            (_cycles.get(pattern).charAt(modP
                                    (car - 1, _cycles.get(pattern).length()))));
                    return nex;
                }

            }

        }
        return c;
    }

    /** Return the result of applying this permutation to the index of P
     *  in ALPHABET, and converting the result to a character of ALPHABET. */
    char permute(char p) {
        int perm = _alphabet.toInt(p);
        return _alphabet.toChar(permute(perm));
    }

    /** Return the result of applying the inverse of this permutation to C. */
    int invert(char c) {
        int inv = _alphabet.toInt(c);
        return _alphabet.toChar(invert(inv));
    }

    /** Return the alphabet used to initialize this Permutation. */
    Alphabet alphabet() {
        return _alphabet;
    }

    /** Return true iff this permutation is a derangement (i.e., a
     *  permutation for which no value maps to itself). */
    boolean derangement() {
        int size = 0;
        for (String pattern : _cycles) {
            size += pattern.length();
        }
        if (size < size()) {
            return false;
        }
        return true;
    }

    /** Alphabet of this permutation. */
    private Alphabet _alphabet;

    /** Cycle of permutation. */
    private ArrayList<String> _cycles;

}
