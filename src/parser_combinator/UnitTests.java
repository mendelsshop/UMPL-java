package parser_combinator;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Optional;

import org.junit.Test;

public class UnitTests {
        public enum Unit {
                a,
                c,
        }

        @Test
        public void test_basic() throws Exception {
                Parser<List<Character>> parser = Parsers.Matches('c').Alt(Parsers.Matches('b')).Many1();
                List<Character> r = parser.parse("bcc").Unwrap();
                System.out.println(r);
        }
}
