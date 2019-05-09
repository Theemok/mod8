package pp.block1.cc.antlr.work;

import pp.block1.cc.dfa.Checker;
import pp.block1.cc.dfa.State;

public class MyChecker implements Checker {

    @Override
    public boolean accepts(State start, String word) {
        State out = start;
        // for each word in the string
        for (char c : word.toCharArray())
        {
            // if the outgoing state has a transition to next state update outgoing state
            if (out.hasNext(c)) {
                out = out.getNext(c);
            } else {
                // no state out going so this state does not accept the word
                return false;
            }
        }

        // return whether it is an accepting state
        return out.isAccepting();
    }

}
