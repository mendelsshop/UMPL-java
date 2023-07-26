package parser_combinator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import parser_combinator.ErrorResult.ErrorReason;
import parser_combinator.Result.ResultKind;

public class ParserCombinators {
    public static <T> Parser<T> Alt(Parser<T> p1,
            Parser<T> p2) {
        return new Parser<>((ctx) -> {
            Result<T> result = p1.parse(ctx);
            if (result.getType() == ResultKind.Ok) {
                return result;
            }
            return p2.parse(ctx);
        });
    }

    @SuppressWarnings("unchecked")
    public static <T, U> Parser<U> Map(Parser<T> p, Function<T, U> mapper) {
        return new Parser<>((ctx) -> {
            Result<T> result = p.parse(ctx);
            if (result.getType() == ResultKind.Error) {
                return (Result<U>) result;
            }
            OkResult<T> value = (OkResult<T>) result;
            return new OkResult<U>(mapper.apply(value.getResult()), result.getContext());
        });
    }

    @SuppressWarnings("unchecked")
    public static <T, U> Parser<Tuple<T, U>> Chain(Parser<T> p1,
            Parser<U> p2) {
        return new Parser<>((ctx) -> {
            Result<T> r1 = p1.parse(ctx);
            if (r1.getType() == ResultKind.Error) {
                return (Result<Tuple<T, U>>) r1;
            }
            OkResult<T> v1 = (OkResult<T>) r1;
            Result<U> r2 = p2.parse(v1.getContext());
            if (r2.getType() == ResultKind.Error) {
                return (Result<Tuple<T, U>>) r2;
            }
            OkResult<U> v2 = (OkResult<U>) r2;
            return new OkResult<Tuple<T, U>>(new Tuple<T, U>(v1.getResult(), v2.getResult()), r2.getContext());
        });
    }

    public static <T> Parser<Optional<T>> Opt(Parser<T> p) {
        return new Parser<>((ctx) -> {
            Result<T> result = p.parse(ctx);
            Optional<T> ret = result.Ok();
            return new OkResult<Optional<T>>(ret, result.getContext());
        });
    }

    public static <T> Parser<Optional<List<T>>> Many(Parser<T> p) {
        return new Parser<>((ctx) -> {
            List<T> results = new ArrayList<T>();
            while (true) {
                Result<T> result = p.parse(ctx);
                if (result.getType() == ResultKind.Error) {
                    if (results.isEmpty()) {
                        return new OkResult<Optional<List<T>>>(Optional.empty(), ctx);
                    } else {
                        return new OkResult<Optional<List<T>>>(Optional.of(results), ctx);
                    }
                }
                OkResult<T> value = (OkResult<T>) result;
                results.add(value.getResult());
                ctx = value.getContext();
            }
        });
    }

    @SuppressWarnings("unchecked")
    public static <T> Parser<List<T>> Many1(Parser<T> p) {
        return new Parser<>((ctx) -> {
            Result<Optional<List<T>>> result = ParserCombinators.Many(p).parse(ctx);
            if (result.getType() == ResultKind.Error) {
                var error = (ErrorResult<?>) result;
                return (Result<List<T>>) error;
            }
            var value = (OkResult<Optional<List<T>>>) result;
            if (value.getResult().isEmpty()) {
                return new ErrorResult<>(ErrorReason.NoMatch, ctx);
            }
            return new OkResult<List<T>>(value.getResult().get(), value.getContext());
        });
    }

    @SuppressWarnings("unchecked")
    public static <T> Parser<List<T>> Seq(Parser<T>[] parsers) {
        return new Parser<>((ctx) -> {
            List<T> results = new ArrayList<T>();
            for (var parser : parsers) {
                Result<T> result = parser.parse(ctx);
                if (result.getType() == ResultKind.Error) {
                    // TODO: return error with consumed context
                    return (Result<List<T>>) result;
                }
                var value = (OkResult<T>) result;
                ctx = value.getContext();
                results.add(value.getResult());
            }
            return new OkResult<>(results, ctx);
        });
    }

    @SuppressWarnings("unchecked")
    public static <T> Parser<List<T>> Seq(List<Parser<T>>parsers) {
        return new Parser<>((ctx) -> {
            List<T> results = new ArrayList<T>();
            for (var parser : parsers) {
                Result<T> result = parser.parse(ctx);
                if (result.getType() == ResultKind.Error) {
                    // TODO: return error with consumed context
                    return (Result<List<T>>) result;
                }
                var value = (OkResult<T>) result;
                ctx = value.getContext();
                results.add(value.getResult());
            }
            return new OkResult<>(results, ctx);
        });
    }

    public static <T> Parser<T> Choice(Parser<T>[] parsers) {
        return new Parser<>((ctx) -> {
            for (var parser : parsers) {
                Result<T> result = parser.parse(ctx);
                if (result.getType() == ResultKind.Ok) {
                    return result;
                }
            }
            return new ErrorResult<>(ErrorReason.NoMatch, ctx);
        });
    }

    public static <T> Parser<T> Choice(List<Parser<T>> parsers) {
        return new Parser<>((ctx) -> {
            for (var parser : parsers) {
                Result<T> result = parser.parse(ctx);
                if (result.getType() == ResultKind.Ok) {
                    return result;
                }
            }
            return new ErrorResult<>(ErrorReason.NoMatch, ctx);
        });
    }

    public static <T, U> Parser<Optional<List<T>>> Sep(Parser<T> p,
            Parser<U> delim) {
        var rest_p = ParserCombinators.Many(ParserCombinators.KeepRight(delim, p));
        return new Parser<Optional<List<T>>>((ctx) -> {
            var first_r = p.parse(ctx);
            if (first_r.getType() == ResultKind.Error) {
                return new OkResult<>(Optional.empty(), ctx);
            }
            var first_v = (OkResult<T>) first_r;
            var rest_r = rest_p.parse(first_v.getContext());
            var res = new ArrayList<T>();
            res.add(first_v.getResult());
            if (rest_r.getType() == ResultKind.Error) {
                return new OkResult<Optional<List<T>>>(Optional.of(res), first_v.getContext());
            }
            var rest_v = (OkResult<Optional<List<T>>>) rest_r;
            if (rest_v.getResult().isEmpty()) {

                return new OkResult<Optional<List<T>>>(Optional.of(res), first_v.getContext());
            }
            return new OkResult<Optional<List<T>>>(
                    Optional.of(Stream.concat(res.stream(), rest_v.getResult().get().stream())
                            .collect(Collectors.toList())),
                    rest_v.getContext());

        });
    }

    @SuppressWarnings("unchecked")
    public static <T, U> Parser<List<T>> Sep1(Parser<T> p,
            Parser<U> delim) {
        return new Parser<>((ctx) -> {
            Result<Optional<List<T>>> result = ParserCombinators.Sep(p, delim).parse(ctx);
            if (result.getType() == ResultKind.Error) {
                var error = (ErrorResult<?>) result;
                return (Result<List<T>>) error;
            }
            var value = (OkResult<Optional<List<T>>>) result;
            if (value.getResult().isEmpty()) {
                return new ErrorResult<>(ErrorReason.NoMatch, ctx);
            }
            return new OkResult<List<T>>(value.getResult().get(), value.getContext());
        });
    }

    public static <T, U> Parser<T> KeepLeft(Parser<T> p_left,
            Parser<U> p_right) {
        return ParserCombinators.Map(ParserCombinators.Chain(p_left, p_right), (r) -> r.getFirst());
    }

    public static <T, U> Parser<U> KeepRight(Parser<T> p_left,
            Parser<U> p_right) {
        return ParserCombinators.Map(ParserCombinators.Chain(p_left, p_right), (r) -> r.getSecond());
    }

    public static <T, U, V> Parser<U> InBetween(Parser<T> p_left,
            Parser<U> p_mid, Parser<V> p_right) {
        return ParserCombinators.KeepLeft(ParserCombinators.KeepRight(p_left, p_mid), p_right);
    }
}