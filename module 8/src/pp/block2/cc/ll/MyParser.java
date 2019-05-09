package pp.block2.cc.ll;

import static pp.block2.cc.ll.L.A;
import static pp.block2.cc.ll.L.B;
import static pp.block2.cc.ll.L.C;

import java.util.List;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Token;

import pp.block2.cc.AST;
import pp.block2.cc.NonTerm;
import pp.block2.cc.ParseException;
import pp.block2.cc.Parser;
import pp.block2.cc.SymbolFactory;

public class MyParser implements Parser {
	public MyParser() {
		this.fact = new SymbolFactory(L.class);
	}

	private final SymbolFactory fact;

	@Override
	public AST parse(Lexer lexer) throws ParseException {
		this.tokens = lexer.getAllTokens();
		this.index = 0;
		return parseL();
	}

	private List<? extends Token> tokens;

	private AST parseL() throws ParseException {
		AST result = new AST(L);
		Token next = peek();
		switch (next.getType()) {
		case A:
		case C:
			result.addChild(parseR());
			result.addChild(parseToken(A));
			break;
		case B:
			result.addChild(parseQ());
			result.addChild(parseToken(B));
			result.addChild(parseToken(A));
			break;
        default:
            throw unparsable(L);
		}
		return result;
	}
		
	private AST parseR() throws ParseException {
		AST result = new AST(R);
		Token next = peek();
		switch (next.getType()) {
		case C:
			result.addChild(parseToken(C));
		case A:
			result.addChild(parseToken(A));
			result.addChild(parseToken(B));
			result.addChild(parseToken(A));
			result.addChild(parseR2());
			break;
        default:
            throw unparsable(R);
		}
		return result;
	}
	
	private AST parseR2() throws ParseException {
		AST result = new AST(R2);
		Token next = peek();
		switch (next.getType()) {
		case B:
			result.addChild(parseToken(B));
			result.addChild(parseToken(C));
			result.addChild(parseR2());
			break;
		case A:
			break;
        default:
            throw unparsable(R2);
		}
		return result;
	}
	
	private AST parseQ() throws ParseException {
		AST result = new AST(Q);
		Token next = peek();
		result.addChild(parseToken(B));
		result.addChild(parseQ2());
		return result;
	}
	
	private AST parseQ2() throws ParseException {
		AST result = new AST(Q2);
		Token next = peek();
		switch (next.getType()) {
		case B:
			result.addChild(parseToken(B));
		case C:
			result.addChild(parseToken(C));
			break;
        default:
            throw unparsable(Q2);
		}
		return result;
	}
	
	



	private ParseException unparsable(NonTerm nt) {
		try {
			Token next = peek();
			return new ParseException(String.format(
					"Line %d:%d - could not parse '%s' at token '%s'",
					next.getLine(), next.getCharPositionInLine(), nt.getName(),
					this.fact.get(next.getType())));
		} catch (ParseException e) {
			return e;
		}
	}


	/** Creates an AST based on the expected token type. */
	private AST parseToken(int tokenType) throws ParseException {
		Token next = next();
		if (next.getType() != tokenType) {
			throw new ParseException(String.format(
					"Line %d:%d - expected token '%s' but found '%s'",
					next.getLine(), next.getCharPositionInLine(),
					this.fact.get(tokenType), this.fact.get(next.getType())));
		}
		return new AST(this.fact.getTerminal(tokenType), next);
	}

	/** Returns the next token, without moving the token index. */
	private Token peek() throws ParseException {
		if (this.index >= this.tokens.size()) {
			throw new ParseException("Reading beyond end of input");
		}
		return this.tokens.get(this.index);
	}

	/** Returns the next token and moves up the token index. */
	private Token next() throws ParseException {
		Token result = peek();
		this.index++;
		return result;
	}

	private int index;

	private static final NonTerm L = new NonTerm("L");
	private static final NonTerm R = new NonTerm("R");
	private static final NonTerm R2 = new NonTerm("R2");
	private static final NonTerm Q = new NonTerm("Q");
	private static final NonTerm Q2 = new NonTerm("Q2");

	public static void main(String[] args) {
		args = new String[] {"abaa", "cababcbca", "bbcca", "bbcba"};
		if (args.length == 0) {
			System.err.println("Usage: [text]+");
		} else {
			for (String text : args) {
				String ret = "";
				for (char k: text.toCharArray()) {
					ret = ret + k + " ";
				}
				CharStream stream = CharStreams.fromString(ret);
				Lexer lexer = new L(stream);
				try {
					System.out.printf("Parse tree: %n%s%n",
							new MyParser().parse(lexer));
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
