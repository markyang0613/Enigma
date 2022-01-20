package enigma;



import java.util.ArrayList;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/** Represents a permutation of a range of integers starting at 0 corresponding
 *  to the characters of an alphabet.
 *  @author Mark Yang
 */
class Permutation {

    /** Set this Permutation to that specified by CYCLES, a string in the
     *  form "(cccc) (cc) ..." where the c's are characters in ALPHABET, which
     *  is interpreted as a permutation in cycle notation.  Characters in the
     *  alphabet that are not included in any cycle map to themselves.
     *
     *  (abc) (def)
     *  Whitespace is ignored. */

    /** Saved Array List of Permutation. */
    private ArrayList<String> savedarray = new ArrayList<String>();
    /** Permutation function that includes CYCLES and ALPHABET. */
    Permutation(String cycles, Alphabet alphabet) {
        _alphabet = alphabet;
        Pattern p = Pattern.compile("\\(([a-zA-Z0-9._]*$*)\\)");
        Matcher m = p.matcher(cycles);
        while (m.find()) {
            savedarray.add(m.group());
        }
        mapper = new HashMap<>();
        if (cycles.length() != 0) {
            for (int one = 0; one < savedarray.size(); one++) {
                int start = 0;
                _cycles = savedarray.get(one);
                for (int i = 0; i < _cycles.length(); i++) {
                    if (_cycles.charAt(i) == "(".toCharArray()[0]) {
                        start = i;
                    }
                }
                for (int index = 0; index < _cycles.length() - 1; index++) {
                    if (_cycles.charAt(index) != '(') {
                        if (_cycles.charAt(index + 1) != ')'
                                && !checker.contains(_cycles.charAt(index))) {
                            Character next = _cycles.charAt(index + 1);
                            checker.add(_cycles.charAt(index));
                            mapper.put(_cycles.charAt(index), next);
                        }
                    }
                    if (_cycles.charAt(index + 1) == ")".toCharArray()[0]
                            && !checker.contains(_cycles.charAt(index))
                            && _cycles.length() != 3) {
                        Character next = _cycles.charAt(start + 1);
                        checker.add(_cycles.charAt(index));
                        mapper.put(_cycles.charAt(index), next);
                    }
                    if (_cycles.length() == 3 && _cycles.charAt(index) == '('
                        && _cycles.charAt(index + 2) == ')') {
                        Character boss = _cycles.charAt(index + 1);
                        mapper.put(boss, boss);
                    }
                }
            }
        }
        if (cycles.length() == 0) {
            for (int i = 0; i < alphabet.size(); i++) {
                mapper.put(alphabet.toChar(i), alphabet.toChar(i));
            }
        }
        if (mapper.keySet().size() != alphabet.size()) {
            ArrayList<Character> pikachu = new ArrayList<Character>();
            for (int nose = 0; nose < alphabet.size(); nose++) {
                if (!mapper.keySet().contains(alphabet.toChar(nose))) {
                    pikachu.add(alphabet.toChar(nose));
                }
            }
            for (int boi = 0; boi < pikachu.size(); boi++) {
                mapper.put(pikachu.get(boi), pikachu.get(boi));
            }
        }
    }

    /** Add the cycle c0->c1->...->cm->c0 to the permutation, where CYCLE is
     *  c0c1...cm. */
    public void addCycle(String cycle) {
        savedarray.add(cycle);
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
        return alphabet().size();
    }

    /** Return the result of applying this permutation to P modulo the
     *  alphabet size. */
    int permute(int p) {
        if (mapper.containsKey(_alphabet.toChar(wrap(p)))) {
            return alphabet().toInt(mapper.get(_alphabet.toChar(p)));
        } else {
            throw new EnigmaException("Not exist in the cycle.");
        }
    }

    /** Applying the inverse of this permutation to C. @return integer. */
    int invert(int c) {
        for (Character key : mapper.keySet()) {
            if (permute(key) == alphabet().toChar(wrap(c))) {
                return wrap(alphabet().toInt(key));
            }
        }
        throw new EnigmaException("Not in the hashmap.");
    }

    /** Return the result of applying this permutation to the index of P
     *  in ALPHABET, and converting the result to a character of ALPHABET. */
    char permute(char p) {
        if (mapper.containsKey(p)) {
            return mapper.get(p);
        }
        throw new EnigmaException("character not in cycle");
    }

    /** Return the result of applying the inverse of this permutation to C. */
    char invert(char c) {
        if (!mapper.keySet().contains(c) && alphabet().contains(c)) {
            return c;
        } else {
            for (Character key : mapper.keySet()) {
                if (permute(key) == c) {
                    return key;
                }
            }
        }
        throw new EnigmaException("Not contained");
    }

    /** Return the alphabet used to initialize this Permutation. */
    Alphabet alphabet() {
        return _alphabet;
    }

    /** Return true iff this permutation is a derangement (i.e., a
     *  permutation for which no value maps to itself). */
    boolean derangement() {
        boolean flag = true;
        for (Character key : mapper.keySet()) {
            if (mapper.get(key) == key) {
                flag = false;
            }
        }
        return flag;
    }

    /** Alphabet of this permutation. */
    private Alphabet _alphabet;

    /** Cycle of the permutation. */
    private String _cycles;

    /** HashMap that I made for each cycle.*/
    private HashMap<Character, Character> mapper;

    /** ArrayList that checks to make sure there are no duplicates. */
    private ArrayList<Character> checker = new ArrayList<Character>();

}



