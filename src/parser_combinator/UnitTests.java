package parser_combinator;

import static org.junit.Assert.*;

import org.junit.Test;

public class UnitTests {
        public enum Unit {
                a,
                c,
        }

        @Test
        public void test_basic() throws Exception {
                var parser = Parsers.Matches('c')
                                .Alt(Parsers.Matches('b'))
                                .Many1();
                var r = parser.parse("bcbc").Unwrap();
                System.out.println(r);
        }
}
