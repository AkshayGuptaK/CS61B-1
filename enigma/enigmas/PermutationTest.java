package enigma;

import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;
import static org.junit.Assert.*;
import java.util.ArrayList;

import static enigma.TestUtils.*;

/** The suite of all JUnit tests for the Permutation class.
 *  @author
 */
public class PermutationTest {

    /** Testing time limit. */
    @Rule
    public Timeout globalTimeout = Timeout.seconds(5);

    /* ***** TESTING UTILITIES ***** */

    private Permutation perm;
    private String alpha = UPPER_STRING;

    /** Check that perm has an alphabet whose size is that of
     *  FROMALPHA and TOALPHA and that maps each character of
     *  FROMALPHA to the corresponding character of FROMALPHA, and
     *  vice-versa. TESTID is used in error messages. */
    private void checkPerm(String testId,
                           String fromAlpha, String toAlpha) {
        int N = fromAlpha.length();
        assertEquals(testId + " (wrong length)", N, perm.size());
        for (int i = 0; i < N; i += 1) {
            char c = fromAlpha.charAt(i), e = toAlpha.charAt(i);
            assertEquals(msg(testId, "wrong translation of '%c'", c),
                         e, perm.permute(c));
            assertEquals(msg(testId, "wrong inverse of '%c'", e),
                         c, perm.invert(e));
            int ci = alpha.indexOf(c), ei = alpha.indexOf(e);
            assertEquals(msg(testId, "wrong translation of %d", ci),
                         ei, perm.permute(ci));
            assertEquals(msg(testId, "wrong inverse of %d", ei),
                         ci, perm.invert(ei));
        }
    }

    /* ***** TESTS ***** */

    @Test
    public void checkIdTransform() {
        perm = new Permutation("", UPPER);
        checkPerm("identity", UPPER_STRING, UPPER_STRING);
    }

    @Test
    public void cycleTest() {
        String whole = "(ABCD) (EFGHI) (SUV) (QL) (P)";
        Permutation cyc = new Permutation(whole, UPPER);

        ArrayList<String> parts = new ArrayList<>();
        parts.add("ABCD");
        parts.add("EFGHI");
        parts.add("SUV");
        parts.add("QL");
        parts.add("P");

        assertArrayEquals(parts.toArray(), cyc.getCycles().toArray());
    }

    @Test
    public void permuteTest() {
        String cycle = "(ACZ)";
        Permutation test = new Permutation(cycle, UPPER);

        int actual1 = test.permute(0);
        int actual2 = test.permute(25);

        assertEquals(2, actual1);
        assertEquals(0, actual2);

        Permutation p = new Permutation("(PNH) (ABDFIKLZYXW) (JC)",
                new CharacterRange('A', 'Z'));
        assertEquals(p.permute('B'), 'D');
        assertEquals(p.permute('G'), 'G');

    }

    @Test
    public void invertTest() {
        String cycle = "(ACZ)";
        Permutation test = new Permutation(cycle, UPPER);

        int actual1 = test.invert(0);
        int actual2 = test.invert(25);

        assertEquals(25, actual1);
        assertEquals(2, actual2);

        Permutation p = new Permutation("(PNH) (ABDFIKLZYXW) (JC)",
                new CharacterRange('A', 'Z'));
        assertEquals(p.invert('B'), 'A');
        assertEquals(p.invert('G'), 'G');
    }

    @Test
    public void derangementTest() {
        String someS = "(ABCD) (EFGHI) (SUV) (QL) (P)";
        String allS = "(ABCDEFGHIJKLMNOPQRSTUVWXYZ)";
        Permutation some = new Permutation(someS, UPPER);
        Permutation all = new Permutation(allS, UPPER);

        assertEquals(false, some.derangement());
        assertEquals(true, all.derangement());


    }
}
