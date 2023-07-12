package umpl_beta;

import static org.junit.Assert.*;

import org.junit.Test;



public class UnitTests {
        public enum Unit {
                a,
                c,
        }
        
        @Test
        public void test_basic() throws Exception {
                Parser<Tuple<Unit, Character>> combinator = new Parser<Character>(Parsers.Matches('c')).Alt(Parsers.Matches('a')).Map((c) -> {
                        switch (c) {
                        case 'a': return Unit.a;
                        case 'c': return Unit.c;
                        default: throw new IllegalArgumentException();
                        }
                }).Chain(Parsers.Matches('d'));
                OkResult<Tuple<Unit, Character>> result = (OkResult<Tuple<Unit, Character>>) combinator.parse("adaa");
                System.out.println(result.getResult().getFirst() + " "+ result.getResult().getSecond() + " " + result.getContext().getInput() + " " + result.getContext().getIndex());
        }
}
