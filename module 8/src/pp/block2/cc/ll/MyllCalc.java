package pp.block2.cc.ll;

import pp.block2.cc.NonTerm;
import pp.block2.cc.Symbol;
import pp.block2.cc.Term;

import java.util.*;

/**
 * Created by Rick on 10-5-2015.
 */
public class MyllCalc implements LLCalc {
    private Grammar grammar;
    private Map<Symbol, Set<Term>> first;
    private Map<NonTerm, Set<Term>> follow;
    private Map<Rule, Set<Term>> firstp;


    public MyllCalc(Grammar grammar) {
        this.grammar = grammar;
        calcFirst();
        calcFollow();
        calcFirstp();
    }


    @Override
    public Map<Symbol, Set<Term>> getFirst() {
        return first;
    }

    @Override
    public Map<NonTerm, Set<Term>> getFollow() {
        return follow;
    }

    @Override
    public Map<Rule, Set<Term>> getFirstp() {
        return firstp;
    }

    @Override
    public boolean isLL1() {
        for (NonTerm nonTerm : grammar.getNonterminals()) {
            for (Rule rule : grammar.getRules(nonTerm)) {
                for (Rule rule1 : grammar.getRules(nonTerm)) {
                    if (rule != rule1) {
                        Set<Term> intersection = new HashSet<>(firstp.get(rule));
                        intersection.retainAll(firstp.get(rule1));
                        if (!intersection.isEmpty()) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    private void calcFirst() {
        first = new HashMap<>();

        //first(a) <- (T union eof union epsilon)
        //adds 'eof' to first set.
        Set<Term> eofSet = new HashSet<>();
        eofSet.add(Symbol.EOF);
        first.put(Symbol.EOF, eofSet);

        //adds 'epsilon' to the first set.
        Set<Term> emptySet = new HashSet<>();
        emptySet.add(Symbol.EMPTY);
        first.put(Symbol.EMPTY, emptySet);

        //add terminals to first
        for (Term term : grammar.getTerminals()) {
            Set<Term> s = new HashSet<>();
            s.add(term);
            first.put(term, s);
        }

        //first(A) <- empty set
        for (NonTerm nonTerm : grammar.getNonterminals()) {
            first.put(nonTerm, new HashSet<Term>());
        }

        boolean changing = true;
        while (changing) {
            changing = false;

            for (Rule rule : grammar.getRules()) {
                List<Symbol> beta = rule.getRHS();

                //First(beta1) - {epsilon}
                Set<Term> rhs = new HashSet<>(first.get(beta.get(0)));
                rhs.remove(Symbol.EMPTY);

                int i = 0;
                while (i < beta.size() - 1 && first.get(beta.get(i)).contains(Symbol.EMPTY)) {
                    rhs.addAll(first.get(beta.get(i + 1)));
                    rhs.remove(Symbol.EMPTY);
                    i++;
                }

                if (i == beta.size() - 1 && first.get(beta.get(i)).contains(Symbol.EMPTY)) {
                    rhs.add(Symbol.EMPTY);
                }

                int oldLength = first.get(rule.getLHS()).size();
                first.get(rule.getLHS()).addAll(rhs);
                changing = changing || oldLength < first.get(rule.getLHS()).size();
            }
        }
    }

    private void calcFollow() {
        follow = new HashMap<>();

        //follow(A) <- empty set
        for (NonTerm nonTerm : grammar.getNonterminals()) {
            follow.put(nonTerm, new HashSet<Term>());
        }

        //follow(S) <- {eof]
        Set<Term> eofSet = new HashSet<>();
        eofSet.add(Symbol.EOF);
        follow.put(grammar.getStart(), eofSet);

        boolean changing = true;
        while (changing) {
            changing = false;

            for (Rule r : grammar.getRules()) {
                Set<Term> trailer = new HashSet<>(follow.get(r.getLHS()));

                List<Symbol> beta = r.getRHS();
                for (int i = beta.size() - 1; i >= 0; i--) {
                    if (beta.get(i) instanceof NonTerm) {
                        int oldLength = follow.get(beta.get(i)).size();
                        follow.get(beta.get(i)).addAll(trailer);
                        changing = changing || oldLength < follow.get(beta.get(i)).size();
                        if (first.get(beta.get(i)).contains(Symbol.EMPTY)) {
                            trailer.addAll(first.get(beta.get(i)));
                            trailer.remove(Symbol.EMPTY);
                        } else {
                            trailer = new HashSet<>(first.get(beta.get(i)));
                        }
                    } else {
                        trailer = new HashSet<>(first.get(beta.get(i)));
                    }
                }
            }
        }
    }

    public void calcFirstp() {
        firstp = new HashMap<>();
        for (Rule rule : grammar.getRules()) {
            List<Symbol> beta = rule.getRHS();
            Set<Term> rhs = new HashSet<>(first.get(beta.get(0)));
            int i = 1;
            while (i < beta.size() - 1 && first.get(beta.get(i)).contains(Symbol.EMPTY)) {
                rhs.addAll(first.get(beta.get(i)));
                i++;
            }

            firstp.put(rule, rhs);
            if (rhs.contains(Symbol.EMPTY)) {
                firstp.get(rule).addAll(follow.get(rule.getLHS()));
            }
        }
    }
}