package parser_combinator;

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

    public Parser<T> Alt(Parser<T> p1) {
        return ParserCombinators.Alt(this, p1);
    }

    public <U> Parser<U> Map(Function<T, U> mapper) {
        return ParserCombinators.Map(this, mapper);
    }

    public Result<T> parse(String input) {
        return parser_function.apply(new Context(input));
    }

    public Result<T> parse(Context input) {
        return parser_function.apply(input);
    }

    public <U> Parser<Tuple<T, U>> Chain(Parser<U> p2) {
        return ParserCombinators.Chain(this, p2);
    }

    public Parser<Optional<T>> Opt() {
        return (ParserCombinators.Opt(this));
    }

    public Parser<Optional<List<T>>> Many() {
        return (ParserCombinators.Many(this));
    }

    public Parser<List<T>> Many1() {
        return (ParserCombinators.Many1(this));
    }

    public <U> Parser<Optional<List<T>>> Sep(Parser<U> delim) {
        return (ParserCombinators.Sep(this, delim));
    }

    public <U> Parser<List<T>> Sep1(Parser<U> delim) {
        return (ParserCombinators.Sep1(this, delim));
    }

    public <U> Parser<T> KeepRight(Parser<U> left) {
        return (ParserCombinators.KeepRight(left, this));
    }

    public <U> Parser<T> KeepLeft(Parser<U> right) {
        return (ParserCombinators.KeepLeft(this, right));
    }

    public <U, V> Parser<T> InBetween(Parser<U> left, Parser<V> right) {
        return (ParserCombinators.InBetween(left, this, right));
    }

    public Parser<List<T>> Seq(List<Parser<T>> parsers) {
        parsers.add(0, this);
        return (ParserCombinators.Seq(parsers.stream().map((p) -> p).toList()));
    }

    public Parser<T> Choice(List<Parser<T>> parsers) {
        parsers.add(0, this);
        return (ParserCombinators.Choice( parsers.stream().map((p) -> p).toList()));
    }
}
