package pp.block1.cc.dfa;

import java.util.Map;
import java.util.TreeMap;

/**
 * State of a DFA.
 */
public class State {
	/** State number */
	private final int nr;

	/** Flag indicating if this state is accepting. */
	private final boolean accepting;

	/** Mapping to next states. */
	private final Map<Character, State> next;

	/**
	 * Constructs a new, possibly accepting state with a given number. The
	 * number is meant to identify the state, but there is no check for
	 * uniqueness.
	 */
	public State(int nr, boolean accepting) {
		this.next = new TreeMap<>();
		this.nr = nr;
		this.accepting = accepting;
	}

	/** Returns the state number. */
	public int getNumber() {
		return this.nr;
	}

	/** Indicates if the state is accepting. */
	public boolean isAccepting() {
		return this.accepting;
	}

	/**
	 * Adds an outgoing transition to a next state. This overrides any previous
	 * transition for that character.
	 */
	public void addNext(Character c, State next) {
		this.next.put(c, next);
	}

	/** Indicates if there is a next state for a given character. */
	public boolean hasNext(Character c) {
		return getNext(c) != null;
	}

	/**
	 * Returns the (possibly <code>null</code>) next state for a given
	 * character.
	 */
	public State getNext(Character c) {
		return this.next.get(c);
	}

	@Override
	public String toString() {
		String trans = "";
		for (Map.Entry<Character, State> out : this.next.entrySet()) {
			if (!trans.isEmpty()) {
				trans += ", ";
			}
			trans += "--" + out.getKey() + "-> " + out.getValue().getNumber();
		}
		return String.format("State %d (%s) with outgoing transitions %s",
				this.nr, this.accepting ? "accepting" : "not accepting", trans);
	}

	static final public State DFA_LALA;
    static {
        DFA_LALA = new State(0, false);
        State id61 = new State(1, false);
        State id62 = new State(2, true);
        State id63 = new State(3, true);
        State id64 = new State(4, false);
        State id65 = new State(5, true);
        State id66 = new State(6, true);
        State id67 = new State(7, false);
        State id68 = new State(8, false);
        State id69 = new State(9, false);
        State id70 = new State(10, false);
        State id71 = new State(11, true);
        DFA_LALA.addNext('L', id61);
        id61.addNext('a', id62);
        id62.addNext('a', id62);
        id62.addNext(' ', id63);
        id62.addNext('L', id64);
        id63.addNext(' ', id63);
        id63.addNext('L', id64);
        id64.addNext('a', id65);
        id65.addNext('a', id65);
        id65.addNext(' ', id65);
        id65.addNext('L', id67);
        id66.addNext(' ', id66);
        id66.addNext('L', id67);
        id67.addNext('a', id68);
        id68.addNext('a', id68);
        id68.addNext(' ', id69);
        id68.addNext('L', id70);
        id69.addNext(' ', id69);
        id69.addNext('L', id70);
        id70.addNext('i', id71);
        id71.addNext(' ', id71);
    }
	
	static final public State ID6_DFA;
	static {
		ID6_DFA = new State(0, false);
		State id61 = new State(1, false);
		State id62 = new State(2, false);
		State id63 = new State(3, false);
		State id64 = new State(4, false);
		State id65 = new State(5, false);
		State id66 = new State(6, true);
		State[] states = { ID6_DFA, id61, id62, id63, id64, id65, id66 };
		for (char c = 'a'; c < 'z'; c++) {
			for (int s = 0; s < states.length - 1; s++) {
				states[s].addNext(c, states[s + 1]);
			}
		}
		for (char c = 'A'; c < 'Z'; c++) {
			for (int s = 0; s < states.length - 1; s++) {
				states[s].addNext(c, states[s + 1]);
			}
		}
		for (char c = '0'; c < '9'; c++) {
			for (int s = 1; s < states.length - 1; s++) {
				states[s].addNext(c, states[s + 1]);
			}
		}
	}
	

}
