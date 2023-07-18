package parser_combinator;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import parser_combinator.ErrorResult.ErrorReason;

public class Parsers {
    public static Parser<Character> Satisfy(Function<Character, Boolean> satisfies) {
        return new Parser<>(ctx -> {
            Optional<Character> maybeChar = ctx.currentChar();
            if (maybeChar.isPresent()) {
                char c = maybeChar.get();
                if (satisfies.apply(c)) {
                    return new OkResult<Character>(c, ctx.IncrementIndex());
                }
                return new ErrorResult<Character>(ErrorReason.MisMatch, ctx);
            }
            return new ErrorResult<Character>(ErrorReason.EOF, ctx);
        });
    }

    public static Parser<Character> Digit() {
        return Parsers.AnyOf("0123456789");
    }

    public static Parser<Integer> Integer() {
        return Parsers.Digit().Many1().Map(int_p -> Integer.parseInt(listToString(int_p)));
    }

    public static Parser<String> WhiteSpace() {
        return Parsers.AnyOf(" \n\t").Many1().Map(ws -> listToString(ws));
    }

    private static String listToString(List<Character> list) {
        return (list).stream().map(c -> c.toString()).collect(Collectors.joining());
    }

    public static Parser<Character> Take() {
        return Parsers.Satisfy((cin) -> true);
    }

    public static Parser<Character> Matches(char c) {
        return Parsers.Satisfy((cin) -> cin == c);
    }

    public static Parser<Character> NotMatches(char c) {
        return Parsers.Satisfy((cin) -> cin != c);
    }

    public static <T> Parser<T> Fail() {
        return new Parser<>((ctx) -> new ErrorResult<T>(ErrorReason.Fail, ctx));
    }

    public static Parser<String> Matches(String s) {
        return ParserCombinators.Map(
                ParserCombinators.Seq(s.chars().mapToObj((c) -> (char) c).map(c -> Matches(c)).toList()),
                (s_list) -> listToString(s_list));
    }

    public static Parser<Character> AnyOf(String s) {
        return AnyOf(s.chars().mapToObj((c) -> (char) c).toList());
    }

    private static Parser<Character> AnyOf(List<Character> list) {
        return Satisfy((s) -> list.contains(s));
    }

    public static Parser<Character> NotAnyOf(String s) {
        return NotAnyOf(s.chars().mapToObj((c) -> (char) c).toList());
    }

    private static Parser<Character> NotAnyOf(List<Character> list) {
        return Satisfy((s) -> !list.contains(s));
    }
}
