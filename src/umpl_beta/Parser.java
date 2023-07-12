package umpl_beta;

import java.util.function.Function;

public class Parser<T> {
    private Function<Context, Result<T>> parser_function;

    public Parser(Function<Context, Result<T>> parser_function) {
        this.parser_function = parser_function;
    }

    public Parser<T> Alt(Function<Context, Result<T>> p1) {
        return new Parser<T>(ParserCombinator.Alt(parser_function, p1));
    }

    public <U> Parser<U> Map(Function<T, U> mapper) {
        return new Parser<U>(ParserCombinator.Map(parser_function, mapper));
    }

    public Result<T> parse(String input) {
        return parser_function.apply(new Context(input));
    }

    public <U> Parser<Tuple<T, U>> Chain(Function<Context, Result<U>> p2) {
        return new Parser<Tuple<T, U>>(ParserCombinator.Chain(parser_function, p2));
    }
}
