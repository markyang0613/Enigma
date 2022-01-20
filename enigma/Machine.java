package enigma;


import java.util.ArrayList;
import java.util.Collection;

/** Class that represents a complete enigma machine.
 *  @author Mark Yang
 */
class Machine {

    /** A new Enigma machine with alphabet ALPHA, 1 < NUMROTORS rotor slots,
     *  and 0 <= PAWLS < NUMROTORS pawls.  ALLROTORS contains all the
     *  available rotors. */
    Machine(Alphabet alpha, int numRotors, int pawls,
            Collection<Rotor> allRotors) {
        alphabet = alpha;
        numrotors = numRotors;
        numpawls = pawls;
        allrotors = allRotors;
        if (numrotors <= 1 & (pawls >= numrotors || pawls < 0)) {
            throw new EnigmaException("Invalid number of Rotors");
        }
    }

    /** Return the number of rotor slots I have. */
    int numRotors() {
        return numrotors;
    }

    /** Return the number pawls (and thus rotating rotors) I have. */
    int numPawls() {
        return numpawls;
    }


    /** Set my rotor slots to the rotors named ROTORS from my set of
     *  available rotors (ROTORS[0] names the reflector).
     *  Initially, all rotors are set at their 0 setting. */
    void insertRotors(String[] rotors) {
        int movingrotors = 0;
        arrayrotor = new ArrayList<Rotor>();
        rotorlist = new Rotor[rotors.length];
        ArrayList<String> rotornames = new ArrayList<String>();
        for (Rotor rotor : allrotors) {
            rotornames.add(rotor.name());
        }
        for (int i = 0; i < rotors.length; i++) {
            if (rotornames.contains(rotors[i])) {
                for (Rotor rotor : allrotors) {
                    if (rotor.name().equals(rotors[i])) {
                        if (rotor.rotates()) {
                            movingrotors++;
                        }
                        rotorlist[i] = rotor;
                        arrayrotor.add(rotor);
                    }
                }
            }
        }
        if (movingrotors != numpawls) {
            throw new EnigmaException("Wrong number of argument.");
        }
        for (int i = 0;  i < rotors.length; i++) {
            Rotor rotor = arrayrotor.get(i);
            if (i == 0 && !rotor.reflecting()) {
                throw new EnigmaException("Reflector is not first.");
            }
            if (i != 0 && rotor.reflecting()) {
                throw new EnigmaException("more than one reflector.");
            }
        }
        for (int i = 0; i < arrayrotor.size(); i++) {
            for (int a = 0; a < arrayrotor.size() && a != i; a++) {
                if (arrayrotor.get(i) == arrayrotor.get(a) && a != i) {
                    throw new EnigmaException("Duplicate rotor name");
                }
            }
        }
    }

    /** Set my rotors according to SETTING, which must be a string of
     *  numRotors()-1 characters in my alphabet. The first letter refers
     *  to the leftmost rotor setting (not counting the reflector).  */
    void setRotors(String setting) {
        for (int i = 1; i < numRotors(); i++) {
            rotorlist[i].set(alphabet.toInt(setting.charAt(i - 1)));
        }
    }

    /** Set the plugboard to PLUGBOARD. */
    void setPlugboard(Permutation plugboard) {
        plug = plugboard;
    }

    /** Helper function that places the rotors that can
     * be advanced in the ArrayList Advanced Rotors. */
    void advancing() {
        /** clear advanced array list. */
        boolean currentnotch =
                arrayrotor.get(numRotors() - 1).atNotch();
        boolean nextnotch = arrayrotor.get(numRotors() - 2).atNotch();
        for (int i = numRotors() - 1; i > 0; i -= 1) {
            if (currentnotch && arrayrotor.get(i - 1).rotates()) {
                arrayrotor.get(i).advance();
                if (!nextnotch) {
                    arrayrotor.get(i - 1).advance();
                } else if (nextnotch && i == 2) {
                    arrayrotor.get(i - 1).advance();
                }
            } else if (i == numRotors() - 1) {
                arrayrotor.get(i).advance();
            }
            currentnotch = nextnotch;
            if (i != 1) {
                nextnotch = arrayrotor.get(i - 2).atNotch();
            }
        }
    }
    /** Returns the result of converting the input character C (as an
     *  index in the range 0..alphabet size - 1), after first advancing

     *  the machine. */
    int convert(int c) {
        advancing();
        int change = c;
        if (plug != null) {
            change = plug.permute(change);
        }
        for (int index = rotorlist.length - 1; index > 0; index -= 1) {
            change = rotorlist[index].convertForward(change);
        }
        for (int index2 = 0; index2
                < rotorlist.length; index2++) {
            change = rotorlist[index2].convertBackward(change);
        }

        if (plug != null) {
            change = plug.invert(change);
        }
        return change;
    }

    /** Returns the encoding/decoding of MSG, updating the state of
     *  the rotors accordingly. */
    String convert(String msg) {
        String copy = msg;
        copy = copy.replace(" ", "");
        for (int i = 0; i < copy.length(); i++) {
            if (!alphabet.contains(copy.charAt(i))) {
                throw new EnigmaException("Bad conf.");
            }
        }
        String result = "";
        for (int i = 0; i < msg.length(); i++) {
            if (msg.charAt(i) == ' ') {
                result = result + " ";
            } else {
                int charatindex = alphabet.toInt(msg.charAt(i));
                int convert = convert(charatindex);
                char answer = alphabet.toChar(convert);
                result = result + answer;
            }
        }
        return result;
    }

    /** Function that checks the setting of R. */
    void setRSetting(String r) {
        for (int i = 1; i < rotorlist.length; i++) {
            rotorlist[i].setRIndex(alphabet.toInt(r.charAt(i - 1)));
        }
    }

    /** Common alphabet of my rotors. */
    private final Alphabet alphabet;

    /** Common number of ROTORS. */
    private int numrotors;

    /** Common number of PAWLS. */
    private int numpawls;

    /** Collection of all the rotors. */
    private Collection<Rotor> allrotors;

    /** Saved array of valid rotors. */
    private Rotor[] rotorlist;

    /** Instance variable for the PLUG BOARD. */
    private Permutation plug;

    /** Instance Array List that holds the rotors. */
    private ArrayList<Rotor> arrayrotor = new ArrayList<Rotor>();

    /** Instance ArrayList that checks if it used to be on a notch. */
    private ArrayList<Rotor> mustturn = new ArrayList<Rotor>();
}
