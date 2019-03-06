package enigma;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.NoSuchElementException;
import java.util.Collection;

import static enigma.EnigmaException.*;


/** Enigma simulator.
 *  @author Kelley
 */
public final class Main {

    /** Process a sequence of encryptions and decryptions, as
     *  specified by ARGS, where 1 <= ARGS.length <= 3.
     *  ARGS[0] is the name of a configuration file.
     *  ARGS[1] is optional; when present, it names an input file
     *  containing messages.  Otherwise, input comes from the standard
     *  input.  ARGS[2] is optional; when present, it names an output
     *  file for processed messages.  Otherwise, output goes to the
     *  standard output. Exits normally if there are no errors in the input;
     *  otherwise with code 1. */
    public static void main(String... args) {
        try {
            new Main(args).process();
            return;
        } catch (EnigmaException excp) {
            System.err.printf("Error: %s%n", excp.getMessage());
        }
        System.exit(1);
    }

    /** Check ARGS and open the necessary files (see comment on main). */
    Main(String[] args) {
        if (args.length < 1 || args.length > 3) {
            throw error("Only 1, 2, or 3 command-line arguments allowed");
        }

        _config = getInput(args[0]);

        if (args.length > 1) {
            _input = getInput(args[1]);
        } else {
            _input = new Scanner(System.in);
        }

        if (args.length > 2) {
            _output = getOutput(args[2]);
        } else {
            _output = System.out;
        }
        _allRotors = new ArrayList<>();
    }

    /** Return a Scanner reading from the file named NAME. */
    private Scanner getInput(String name) {
        try {
            return new Scanner(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Return a PrintStream writing to the file named NAME. */
    private PrintStream getOutput(String name) {
        try {
            return new PrintStream(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Configure an Enigma machine from the contents of configuration
     *  file _config and apply it to the messages in _input, sending the
     *  results to _output. */
    private void process() {
        Machine m = readConfig();
        String first = _input.next();
        if (!first.equals("*")) {
            throw error("setting not in the right format");
        }
        String sets = first + _input.nextLine();
        setUp(m, sets);

        String next = _input.nextLine();
        while (_input.hasNext()) {
            while (!(next.contains("*"))) {
                String msg = m.convert(next);
                if (next.isEmpty()) {
                    _output.println();
                } else {
                    printMessageLine(msg);
                }
                if (!_input.hasNextLine()) {
                    break;
                } else {
                    next = _input.nextLine();
                }
            }
            if (next.contains("*")) {
                setUp(m, next);
                if (!_input.hasNextLine()) {
                    break;
                } else {
                    next = _input.nextLine();
                }
                if (next.isEmpty()) {
                    _output.println();
                } else {
                    String newMsg = m.convert(next);
                    printMessageLine(newMsg);
                    if (_input.hasNextLine()) {
                        next = _input.next();
                        if (next.isEmpty()) {
                            _output.println();
                        }
                    }

                }
            }

        }
    }

    /** Return an Enigma machine configured from the contents of configuration
     *  file _config. */
    private Machine readConfig() {
        try {
            if (!_config.hasNext()) {
                throw error("config file is empty");
            }
            String alph = _config.nextLine();
            if (Character.isDigit(alph.charAt(1))) {
                throw error("wrong config");
            }
            alphArray = alph.toCharArray();
            _alphabet = new CharacterRange(alphArray[0], alphArray[2]);
            if (!(alphArray[0] <= alphArray[2])) {
                throw error("character range not in the correct format");
            }
            char[] symbols = new char[] {'-', '(', ')'};
            for (char c: symbols) {
                if (alphArray[0] == c | alphArray[2] == c) {
                    throw error("character range is wrong");
                }
            }
            if (!_config.hasNextInt()) {
                throw error("wrong configuration");
            }
            numRot = Integer.valueOf(_config.next());
            if (!_config.hasNextInt()) {
                throw error("wrong configuration");
            }
            numPawls = Integer.valueOf(_config.next());
            if (numPawls > numRot) {
                throw error("can't have more pawls than number of rotors");
            }
            if (numPawls < 0 | numRot < 0) {
                throw error("need at least 1 rotor, the reflector");
            }

            current = _config.next();
            while (_config.hasNext()) {
                Rotor temp = readRotor();
                _allRotors.add(temp);
            }
            return new Machine(_alphabet, numRot, numPawls, _allRotors);
        } catch (NoSuchElementException excp) {
            throw error("configuration file truncated");
        }
    }

    /** Return a rotor, reading its description from _config. */
    private Rotor readRotor() {
        try {
            String perm = "";
            String notches = "";
            String name = current;
            String temp = _config.next();

            current = _config.next();
            while (current.toCharArray()[0] == '(') {
                if (!(current.toCharArray()[current.length() - 1] == ')')) {
                    throw error("no closing parentheses for perm");
                }
                if (current.substring(1).contains("(")) {
                    String[] split = current.split("\\)");
                    perm += split[0] + ") " + split[1] + ") ";
                    if (!_config.hasNext()) {
                        break;
                    } else {
                        current = _config.next();
                    }

                } else {
                    perm += current + " ";
                    if (!_config.hasNext()) {
                        break;
                    } else {
                        current = _config.next();
                    }
                }
            }
            if (temp.charAt(0) != 'R') {
                notches = temp.substring(1);

            }
            if (temp.charAt(0) == 'N') {
                return new FixedRotor(name, new Permutation(perm, _alphabet));
            } else if (temp.charAt(0) == 'M') {
                return new MovingRotor(name,
                        new Permutation(perm, _alphabet), notches);
            } else if (temp.charAt(0) == 'R') {
                return new Reflector(name, new Permutation(perm, _alphabet));
            } else {
                throw error("wrong rotor type format");
            }

        }   catch (NoSuchElementException excp) {
            throw error("bad rotor description");

        }

    }

    /** Set M according to the specification given on SETTINGS,
     *  which must have the format specified in the assignment. */
    private void setUp(Machine M, String settings) {

        String[] names = new String[numRot];
        String[] splitSet = settings.split(" ");

        int let = 1;
        for (int i = 0; i < (numRot + 1); i++) {
            if (splitSet[i].equals("*")) {
                continue;
            } else {
                names[i - 1] = splitSet[let];
                let++;
            }
        }

        for (int i = 0; i < names.length - 1; i++) {
            for (int j = i + 1; j < names.length; j++) {
                if (names[i].equals(names[j])) {
                    throw error("rotors are repeated");
                }

            }

        }
        if (splitSet.length < numRot + 2) {
            throw error("no setting available");
        }
        String letters = splitSet[numRot + 1];
        M.insertRotors(names);
        M.setRotors(letters);

        int untilEnd = numRot + 2;
        if (splitSet.length > untilEnd) {
            String plug = "";
            for (int i = numRot + 2; i < splitSet.length; i++) {
                plug += splitSet[i] + " ";
            }
            Permutation plugb = new Permutation(plug, _alphabet);
            M.setPlugboard(plugb);
            untilEnd = splitSet.length;
        }
    }

    /** Print MSG in groups of five (except that the last group may
     *  have fewer letters). */
    private void printMessageLine(String msg) {
        int ind = 0;
        String message = "";
        for (char c: msg.toCharArray()) {
            if (ind < 4) {
                message += c;
                ind++;
                continue;
            }
            if (ind == 4) {
                message += c + " ";
                ind = 0;
            }
        }
        _output.println(message);
    }

    /** Alphabet used in this machine. */
    private Alphabet _alphabet;

    /** Source of input messages. */
    private Scanner _input;

    /** Source of machine configuration. */
    private Scanner _config;

    /** File for encoded/decoded messages. */
    private PrintStream _output;

    /** Range of alphabet. */
    private char[] alphArray;

    /** Number of rotors. **/
    private int numRot;

    /** Number of pawls. **/
    private int numPawls;

    /** Current _config. **/
    private String current;

    /** Collection of all Rotors. **/
    private Collection<Rotor> _allRotors;

}
