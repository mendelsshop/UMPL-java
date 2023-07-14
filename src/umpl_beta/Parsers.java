package umpl_beta;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import umpl_beta.ErrorResult.ErrorReason;

public class Parsers {
    public static Function<Context, Result<Character>> Satisfy(Function<Character, Boolean> satisfies) {
        return ctx -> {
            Optional<Character> maybeChar = ctx.currentChar();
            if (maybeChar.isPresent()) {
                char c = maybeChar.get();
                if (satisfies.apply(c)) {
                    return new OkResult<Character>(c, ctx.IncrementIndex());
                }
                return new ErrorResult<Character>(ErrorReason.MisMatch, ctx);
            }
            return new ErrorResult<Character>(ErrorReason.EOF, ctx);
        };
    }

    public static Function<Context, Result<Character>> Digit() {
        return Parsers.AnyOf("0123456789");
    }

    public static Function<Context, Result<Integer>> Integer() {
        return new Parser<>(Parsers.Digit()).Many1().Map(int_p -> Integer.parseInt(listToString(int_p))).getParser();
    }

    public static Function<Context, Result<String>> WhiteSpace() {
        return new Parser<>(Parsers.AnyOf(" \n\t")).Many1().Map(ws -> listToString(ws)).getParser();
    }

    private static String listToString(List<Character> list) {
        return (list).stream().map(c -> c.toString()).collect(Collectors.joining());
    }

    public static Function<Context, Result<Character>> Take() {
        return Parsers.Satisfy((cin) -> true);
    }

    public static Function<Context, Result<Character>> Matches(char c) {
        return Parsers.Satisfy((cin) -> cin == c);
    }

    public static Function<Context, Result<Character>> NotMatches(char c) {
        return Parsers.Satisfy((cin) -> cin != c);
    }

    public static <T> Function<Context, Result<T>> Fail() {
        return (ctx) -> new ErrorResult<T>(ErrorReason.Fail, ctx);
    }

    public static Function<Context, Result<String>> Matches(String s) {
        return ParserCombinators.Map(
                ParserCombinators.Seq(s.chars().mapToObj((c) -> (char) c).map(c -> Matches(c)).toList()),
                (s_list) -> listToString(s_list));
    }

    public static Function<Context, Result<Character>> AnyOf(String s) {
        return AnyOf(s.chars().mapToObj((c) -> (char) c).toList());
    }

    private static Function<Context, Result<Character>> AnyOf(List<Character> list) {
        return Satisfy((s) -> list.contains(s));
    }

    public static Function<Context, Result<Character>> NotAnyOf(String s) {
        return NotAnyOf(s.chars().mapToObj((c) -> (char) c).toList());
    }

    private static Function<Context, Result<Character>> NotAnyOf(List<Character> list) {
        return Satisfy((s) -> !list.contains(s));
    }
}
