package umpl.ast;

import static org.junit.Assert.*;



import org.junit.Test;

public class UnitTests {
        public enum Unit {
                a,
                c,
        }

        @Test
        public void test_basic() throws Exception {
                System.out.println(AstControlFlow.parser.parse("stop aa").Unwrap());
        }
}
