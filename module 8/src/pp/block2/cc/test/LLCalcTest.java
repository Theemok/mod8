package pp.block2.cc.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import pp.block2.cc.NonTerm;
import pp.block2.cc.Symbol;
import pp.block2.cc.Term;
import pp.block2.cc.ll.Grammar;
import pp.block2.cc.ll.Grammars;
import pp.block2.cc.ll.If;
import pp.block2.cc.ll.L;
import pp.block2.cc.ll.LLCalc;
import pp.block2.cc.ll.MyllCalc;
import pp.block2.cc.ll.Rule;
import pp.block2.cc.ll.Sentence;

public class LLCalcTest {
	Grammar sentenceG = Grammars.makeSentence();
	// Define the non-terminals
	NonTerm subj = sentenceG.getNonterminal("Subject");
	NonTerm obj = sentenceG.getNonterminal("Object");
	NonTerm sent = sentenceG.getNonterminal("Sentence");
	NonTerm mod = sentenceG.getNonterminal("Modifier");
	// Define the terminals
	Term adj = sentenceG.getTerminal(Sentence.ADJECTIVE);
	Term noun = sentenceG.getTerminal(Sentence.NOUN);
	Term verb = sentenceG.getTerminal(Sentence.VERB);
	Term end = sentenceG.getTerminal(Sentence.ENDMARK);
	// Now add the last rule, causing the grammar to fail
	Grammar sentenceXG = Grammars.makeSentence();
	{    sentenceXG.addRule(mod, mod, mod);
	}
	LLCalc sentenceXLL = createCalc(sentenceXG);

	
	Grammar ifG = Grammars.makeIf(); // to be defined (Ex. 2-CC.4.1)
	
	// Define the non-terminals
	NonTerm stat = ifG.getNonterminal("Stat" );
	NonTerm elsePart = ifG.getNonterminal("ElsePart");
	
	// Define the terminals (take from the right lexer grammar!)
	Term ifT = ifG.getTerminal (If.IF);
	Term assT = ifG.getTerminal(If.ASSIGN);
	Term condT = ifG.getTerminal(If.COND);
	Term ELSE = ifG.getTerminal(If.ELSE);
	Term thenT = ifG.getTerminal(If.THEN);

	Term eof = Symbol . EOF ;
	Term empty = Symbol . EMPTY ;
	LLCalc ifLL = createCalc ( ifG );
	@Test
	public void testIfFirst () {
	Map<Symbol, Set<Term>> first = ifLL.getFirst ();
	assertEquals(set(assT, ifT), first . get ( stat ));
	assertEquals(set(ELSE, empty), first.get(elsePart));
	}
	@Test
	public void testIfFollow () {
	Map < NonTerm , Set < Term >> follow = ifLL . getFollow ();
	assertEquals(set(Symbol.EOF, ELSE), follow . get ( stat ));
	assertEquals(set(Symbol.EOF, ELSE), follow . get ( elsePart ));
	}
	@Test
	public void testIfFirstPlus () {
	Map < Rule , Set < Term >> firstp = ifLL . getFirstp ();
	List < Rule > elseRules = ifG . getRules ( elsePart );
	List < Rule > statRules = ifG . getRules ( stat );
	assertEquals(set(ELSE), firstp . get ( elseRules . get (0)));
	assertEquals(set(empty, eof, ELSE), firstp . get ( elseRules . get (1)));
	assertEquals(set(ifT), firstp . get ( statRules . get (0)));
	assertEquals(set(assT), firstp . get ( statRules . get (1)));
	}
	
	@Test
	public void testIfLL1 () {
	assertFalse (ifLL.isLL1());
	}

	
	
	
Grammar LG = Grammars.makeL(); // to be defined (Ex. 2-CC.4.1)
	
	// Define the non-terminals
	NonTerm l = LG.getNonterminal("L");
	NonTerm r = LG.getNonterminal("R");
	NonTerm r2 = LG.getNonterminal("R2");
	NonTerm q = LG.getNonterminal("Q");
	NonTerm q2 = LG.getNonterminal("Q2");
	
	// Define the terminals (take from the right lexer grammar!)
	Term a = LG.getTerminal(L.A);
	Term b = LG.getTerminal(L.B);
	Term c = LG.getTerminal(L.C);

	LLCalc GLL = createCalc ( LG );
	
	@Test
	public void testLFirst () {
	Map<Symbol, Set<Term>> first = GLL.getFirst ();
	System.out.println(first);
	assertEquals(set(a, b, c), first.get(l));
	assertEquals(set(a, c), first.get(r));
	assertEquals(set(b, empty), first.get(r2));
	assertEquals(set(b), first.get(q));
	assertEquals(set(b, c), first.get(q2));
	}
	@Test
	public void testLFollow () {
	Map < NonTerm , Set < Term >> follow = GLL.getFollow ();
	assertEquals(set(eof), follow.get(l));
	assertEquals(set(a), follow.get(r));
	assertEquals(set(a), follow.get(r2));
	assertEquals(set(b), follow.get(q));
	assertEquals(set(b), follow.get(q2));
	}
	@Test
	public void testLFirstPlus () {
	Map < Rule , Set < Term >> firstp = GLL.getFirstp ();
	List < Rule > lR = LG.getRules(l);
	List < Rule > rR = LG.getRules(r);
	List < Rule > r2R = LG.getRules(r2);
	List < Rule > qR = LG.getRules(q);
	List < Rule > q2R = LG.getRules(q2);
	assertEquals(set(a, c), firstp . get (lR.get(0)));
	assertEquals(set(b), firstp . get (lR.get(1)));
	assertEquals(set(a), firstp . get (rR.get(0)));
	assertEquals(set(c), firstp . get (rR.get(1)));
	assertEquals(set(b), firstp . get (r2R.get(0)));
	assertEquals(set(a, empty), firstp . get (r2R.get(1)));
	assertEquals(set(b), firstp . get (qR.get(0)));
	assertEquals(set(b), firstp . get (q2R.get(0)));
	assertEquals(set(c), firstp . get (q2R.get(1)));
	}
	
	@Test
	public void testLLL1 () {
	assertTrue(GLL.isLL1());
	}
	
	
	
	
	/** Tests the LL-calculator for the Sentence grammar. */
	@Test
	public void testSentenceOrigLL1() {
		// Without the last (recursive) rule, the grammar is LL-1
		assertTrue(createCalc(sentenceG).isLL1());
	}
	

	@Test
	public void testSentenceXFirst() {
		Map<Symbol, Set<Term>> first = sentenceXLL.getFirst();
		assertEquals(set(adj, noun), first.get(sent));
		assertEquals(set(adj, noun), first.get(subj));
		assertEquals(set(adj, noun), first.get(obj));
		assertEquals(set(adj), first.get(mod));
	}
	
	@Test
	public void testSentenceXFollow() {
		// FOLLOW sets
		Map<NonTerm, Set<Term>> follow = sentenceXLL.getFollow();
		assertEquals(set(Symbol.EOF), follow.get(sent));
		assertEquals(set(verb), follow.get(subj));
		assertEquals(set(end), follow.get(obj));
		assertEquals(set(noun, adj), follow.get(mod));
	}
	
	@Test
	public void testSentenceXFirstPlus() {
		// Test per rule
		Map<Rule, Set<Term>> firstp = sentenceXLL.getFirstp();
		List<Rule> subjRules = sentenceXG.getRules(subj);
		assertEquals(set(noun), firstp.get(subjRules.get(0)));
		assertEquals(set(adj), firstp.get(subjRules.get(1)));
	}
	
	@Test
	public void testSentenceXLL1() {
		assertFalse(sentenceXLL.isLL1());
	}

	/** Creates an LL1-calculator for a given grammar. */
	private LLCalc createCalc(Grammar g) {
		return new MyllCalc(g);
	}

	@SuppressWarnings("unchecked")
	private <T> Set<T> set(T... elements) {
		return new HashSet<>(Arrays.asList(elements));
	}
}
