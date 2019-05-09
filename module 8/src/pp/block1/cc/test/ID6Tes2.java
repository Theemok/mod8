package pp.block1.cc.test;

import org.junit.Test;

import pp.block1.cc.antlr.gchar;

import pp.block1.cc.antlr.pli;;


@SuppressWarnings("javadoc")
public class ID6Tes2 {
	private static LexerTester tester = new LexerTester(pli.class); // TODO fill in once you have the ID6 grammar

	@Test
	public void testcases() {
        tester.correct("\" aaaaaa 34aabS \"\" ads ASD55ASDD x \"");
        tester.wrong("\"ALKSJDLKAJSDLKAasdasdad");
        tester.wrong("\" asda qw A \"\" Das dqw 123 ");
	}
}
	
