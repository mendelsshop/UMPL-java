package umpl_beta;

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
                Parser<Tuple<Unit, Optional<Character>>> combinator = new Parser<Character>(Parsers.Matches('c'))
                                .Alt(Parsers.Matches('a')).Map((c) -> {
                                        switch (c) {
                                                case 'a':
                                                        return Unit.a;
                                                case 'c':
                                                        return Unit.c;
                                                default:
                                                        throw new IllegalArgumentException();
                                        }
                                }).Chain(ParserCombinators.Opt(Parsers.Matches('d')));
                OkResult<Tuple<Unit, Optional<Character>>> result = (OkResult<Tuple<Unit, Optional<Character>>>) combinator
                                .parse("adaa");
                System.out.println(result.getResult().getFirst() + " " + result.getResult().getSecond() + " "
                                + result.getContext().getInput() + " " + result.getContext().getIndex());
        }

        @Test
        public void t1() throws Exception {
                var p = new Parser<>(Parsers.Matches('s')).Many1();
                var r = p.parse("ssssss");
                switch (r.getType()) {
                        case Ok:
                                var v = (OkResult<List<Character>>) r;
                                System.out.println(v.getContext().getInput() + " " + v.getContext().getIndex() + " "
                                                + v.getResult());
                                break;
                        case Error: {
                                var e = (ErrorResult<?>) r;
                                System.out.println(e.getContext().getInput() + " " + e.getContext().getIndex() + " "
                                                + e.getReason());
                        }
                                ;
                }
        }
}
