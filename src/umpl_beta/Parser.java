package umpl_beta;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class Parser<T> {
    private Function<Context, Result<T>> parser_function;

    public Function<Context, Result<T>> getParser() {
        return parser_function;
    }

    public Parser(Function<Context, Result<T>> parser_function) {
        this.parser_function = parser_function;
    }

    public Parser<T> Alt(Function<Context, Result<T>> p1) {
        return new Parser<T>(ParserCombinators.Alt(parser_function, p1));
    }

    public <U> Parser<U> Map(Function<T, U> mapper) {
        return new Parser<U>(ParserCombinators.Map(parser_function, mapper));
    }

    public Result<T> parse(String input) {
        return parser_function.apply(new Context(input));
    }

    public <U> Parser<Tuple<T, U>> Chain(Function<Context, Result<U>> p2) {
        return new Parser<Tuple<T, U>>(ParserCombinators.Chain(parser_function, p2));
    }

    public Parser<Optional<T>> Opt() {
        return new Parser<Optional<T>>(ParserCombinators.Opt(parser_function));
    }

    public Parser<Optional<List<T>>> Many() {
        return new Parser<Optional<List<T>>>(ParserCombinators.Many(parser_function));
    }

    public Parser<List<T>> Many1() {
        return new Parser<List<T>>(ParserCombinators.Many1(parser_function));
    }

    public <U> Parser<Optional<List<T>>> Sep(Function<Context, Result<U>> delim) {
        return new Parser<>(ParserCombinators.Sep(parser_function, delim));
    }

    public <U> Parser<List<T>> Sep1(Function<Context, Result<U>> delim) {
        return new Parser<>(ParserCombinators.Sep1(parser_function, delim));
    }

    public <U> Parser<T> KeepRight(Function<Context, Result<U>> left) {
        return new Parser<>(ParserCombinators.KeepRight(left, parser_function));
    }

    public <U> Parser<T> KeepLeft(Function<Context, Result<U>> right) {
        return new Parser<>(ParserCombinators.KeepLeft(parser_function, right));
    }

    public <U, V> Parser<T> InBetween(Function<Context, Result<U>> left, Function<Context, Result<V>> right) {
        return new Parser<>(ParserCombinators.InBetween(left, parser_function, right));
    }

    public Parser<List<T>> Seq(List<Function<Context, Result<T>>> parsers) {
        parsers.add(0, parser_function);
        return new Parser<>(ParserCombinators.Seq(parsers));
    }

    public Parser<T> Choice(List<Function<Context, Result<T>>> parsers) {
        parsers.add(0, parser_function);
        return new Parser<>(ParserCombinators.Choice(parsers));
    }
}
