package pp.block1.cc.antlr.work;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import pp.block1.cc.dfa.Scanner;
import pp.block1.cc.dfa.State;

public class MyScanner implements Scanner {

    /**
     * Returns the list of tokens generated by a given DFA when scanning a given
     * input text; or <code>null</code> if the input text is not accepted.
     * Scanning is greedy; i.e., it should always return the longest acceptable
     * token.
     */

    @Override
    public List<String> scan(State dfa, String text) {
        System.out.println("Input: " + text);

        // Add all chars to a Character list to iterate over
        List<Character> charList = new ArrayList<>();
        for (char c : text.toCharArray()) {
            charList.add(c);
        }

        ListIterator<Character> listIterator = charList.listIterator();
        List<String> results = new ArrayList<>();
        String string = "";

        String intermediate = null;
        State acceptingState = null;
        State lastBacktracked = null;
        State state = dfa;

        // keep track of how long ago the last accepting word was found
        int lastAccepted = 0;

        while (listIterator.hasNext()) {
            // get character
            char c = listIterator.next();
            // DFA has char in outgoing arrow
            if (state.hasNext(c)) {
                // add current char to result
                string += c;

                state = state.getNext(c);
                lastAccepted++;
                if (state.isAccepting()) {
                    System.out.println("Accepted state: " + string);
                    acceptingState = state;
                    intermediate = string;
                    lastAccepted = 0;
                }
            } else {
                System.out.println("No longer has next state, last accepted state was: " + intermediate + " while current is " + string);
                // No next state possible, use last accepted state instead
                // If last accepted state is the same as a the previous state we backtracked to, we're in a loop
                if (lastBacktracked == acceptingState && lastBacktracked != null) {
                    System.out.println("Backtracked to this state before, would be in an infinite loop. Terminating...");
                    return results;
                }
                results.add(intermediate);
                string = "";
                state = dfa;
                lastBacktracked = acceptingState;

                // backtrack because we went lastaccepted chars too far and we did not find an accepting state, so go lastaccepted amount of chars back
                System.out.println("Going to backtrack " + lastAccepted + " states");
                for (int i = 0; i <= lastAccepted; i++) {
                    listIterator.previous();
                }
            }
        }

        // Add results from last accepted state
        if (lastAccepted == 0 && intermediate != null) {
            results.add(intermediate);
        }

        System.out.println("Input: " + text);
        System.out.println("Output: " + results);

        return results;
    }
}
