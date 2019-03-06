package enigma;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import java.util.ArrayList;
import java.util.Arrays;
import static org.junit.Assert.*;


/** JUnit tests for Machine.
 * @author Kelley
 * **/

public class MachineTest {

    /**
     * Testing time limit.*/
    @Rule
    public Timeout globalTimeout = Timeout.seconds(5);

    /** Helper method to get the string of the current Rotor settings */
    private String getSetting(Alphabet alph, Rotor[] machineRotors) {
        String currSetting = "";
        for (Rotor r : machineRotors) {
            currSetting += alph.toChar(r.setting());
        }
        return currSetting;
    }


    /* ***** TESTING UTILITIES ***** */

    @Test
    public void testDoubleStep() {
        Alphabet ac = new CharacterRange('A', 'D');
        Rotor one = new Reflector("R1", new Permutation("(AC) (BD)", ac));
        Rotor two = new MovingRotor("R2", new Permutation("(ABCD)", ac), "C");
        Rotor three = new MovingRotor("R3", new Permutation("(ABCD)", ac), "C");
        Rotor four = new MovingRotor("R4", new Permutation("(ABCD)", ac), "C");

        String setting = "AAA";
        Rotor[] machineRotors = {one, two, three, four};
        String[] rotor = {"R1", "R2", "R3", "R4"};

        Machine mach = new Machine(ac, 4, 3,
                new ArrayList<Rotor>(Arrays.asList(machineRotors)));
        mach.insertRotors(rotor);
        mach.setRotors(setting);

        assertEquals("AAAA", getSetting(ac, machineRotors));
        assertEquals(0, one.setting());
        assertEquals(0, two.setting());
        assertEquals(0, three.setting());
        assertEquals(0, four.setting());

        mach.convert('a');
        assertEquals("AAAB", getSetting(ac, machineRotors));

        mach.convert('b');
        assertEquals("AAAC", getSetting(ac, machineRotors));

        mach.convert('c');
        assertEquals("AABD", getSetting(ac, machineRotors));

        mach.convert('d');
        assertEquals("AABA", getSetting(ac, machineRotors));

        mach.convert('a');
        assertEquals("AABB", getSetting(ac, machineRotors));

        mach.convert('b');
        assertEquals("AABC", getSetting(ac, machineRotors));

        mach.convert('c');
        assertEquals("AACD", getSetting(ac, machineRotors));
    }

}
