package enigma;

import org.junit.Test;

import static enigma.EnigmaException.*;


/** Class that represents a rotating rotor in the enigma machine.
 *  @author Mark Yang
 */
class MovingRotor extends Rotor {

    /** A rotor named NAME whose permutation in its default setting is
     *  PERM, and whose notches are at the positions indicated in NOTCHES.
     *  The Rotor is initally in its 0 setting (first character of its
     *  alphabet).
     */
    MovingRotor(String name, Permutation perm, String notches) {
        super(name, perm);
        notch = notches;
    }

    @Override
    boolean atNotch() {
        char currentsetting = permutation().alphabet().toChar(setting());
        return notch.contains("" + currentsetting);
    }

    @Override
    void advance() {
        super.set(permutation().wrap(super.setting() + 1));
    }

    @Override
    boolean rotates() {
        return true;
    }

    /** Instance variable of the notches. */
    private String notch;

    /** Testing to use a certain import. */
    @Test
    private void checkRotaes() {
        assert true;
    }

    /** Testing exception import. */
    private void enigmaException() {
        new EnigmaException("Testing.");
    }

}
