package umpl_beta;

import java.util.Optional;
import java.util.function.Function;

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

    public static Function<Context, Result<Character>> Matches(char c) {
        return Parsers.Satisfy((cin) -> cin == c);
    }

    public static <T> Function<Context, Result<T>> Fail() {
        return (ctx) -> new ErrorResult<T>(ErrorReason.Fail, ctx);
    }
}
