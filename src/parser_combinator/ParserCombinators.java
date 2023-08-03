package parser_combinator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import misc.Tuple;
import misc.Result.Result;
import misc.Result.Result.ResultKind;
import parser_combinator.ParseError.ErrorReason;

public class ParserCombinators {
    public static <T> Parser<T> Alt(Parser<T> p1,
            Parser<T> p2) {
        return new Parser<>((ctx) -> {
            Result<OkResult<T>, ParseError> result = p1.parse(ctx);
            if (result.getType() == ResultKind.Ok) {
                return result;
            }
            return p2.parse(ctx);
        });
    }

    public static <T, U> Parser<U> Map(Parser<T> p, Function<T, U> mapper) {
        return new Parser<>((ctx) -> {
            Result<OkResult<T>, ParseError> result = p.parse(ctx);
            if (result.getType() == ResultKind.Error) {
                return result.CastErrorUnsafe();
            }
            OkResult<T> value = result.UnwrapUnsafe();
            return OkResult.Ok(mapper.apply(value.getResult()), value.getContext());
        });
    }

    public static <T, U> Parser<Tuple<T, U>> Chain(Parser<T> p1,
            Parser<U> p2) {
        return new Parser<>((ctx) -> {
            Result<OkResult<T>, ParseError> r1 = p1.parse(ctx);
            if (r1.getType() == ResultKind.Error) {
                return r1.CastErrorUnsafe();
            }
            OkResult<T> v1 = r1.UnwrapUnsafe();
            Result<OkResult<U>, ParseError> r2 = p2.parse(v1.getContext());
            if (r2.getType() == ResultKind.Error) {
                return r2.CastErrorUnsafe();
            }
            OkResult<U> v2 = r2.UnwrapUnsafe();
            return OkResult.Ok(new Tuple<T, U>(v1.getResult(), v2.getResult()), v2.getContext());
        });
    }

    private static <T> Context getContext(Result<OkResult<T>, ParseError> result) {
        if (result.getType() == ResultKind.Error) {
            return result.UnwrapErrUnsafe().getContext();
        } else {
            return result.UnwrapUnsafe().getContext();
        }
    }

    public static <T> Parser<Optional<T>> Opt(Parser<T> p) {
        return new Parser<Optional<T>>((ctx) -> {
            Result<OkResult<T>, ParseError> result = p.parse(ctx);
            Optional<T> ret = result.Ok().map(c -> c.getResult());
            return OkResult.Ok(ret, getContext(result));
        });
    }

    public static <T> Parser<Optional<List<T>>> Many(Parser<T> p) {
        return new Parser<>((ctx) -> {
            List<T> results = new ArrayList<T>();
            while (true) {
                Result<OkResult<T>, ParseError> result = p.parse(ctx);
                if (result.getType() == ResultKind.Error) {
                    if (results.isEmpty()) {
                        return OkResult.Ok(Optional.empty(), ctx);
                    } else {
                        return OkResult.Ok(Optional.of(results), ctx);
                    }
                }
                OkResult<T> value = result.UnwrapUnsafe();
                results.add(value.getResult());
                ctx = value.getContext();
            }
        });
    }

    public static <T> Parser<List<T>> Many1(Parser<T> p) {
        return new Parser<>((ctx) -> {
            Result<OkResult<Optional<List<T>>>, ParseError> result = ParserCombinators.Many(p).parse(ctx);
            if (result.getType() == ResultKind.Error) {
                return result.CastErrorUnsafe();
            }
            var value = (OkResult<Optional<List<T>>>) result.UnwrapUnsafe();
            if (value.getResult().isEmpty()) {
                return ParseError.Err(ErrorReason.NoMatch, ctx);
            }
            return OkResult.Ok(value.getResult().get(), value.getContext());
        });
    }

    public static <T> Parser<List<T>> Seq(Parser<T>[] parsers) {
        return new Parser<List<T>>((ctx) -> {
            List<T> results = new ArrayList<T>();
            for (var parser : parsers) {
                Result<OkResult<T>, ParseError> result = parser.parse(ctx);
                if (result.getType() == ResultKind.Error) {
                    // TODO: return error with consumed context
                    return result.CastErrorUnsafe();
                }
                var value = (OkResult<T>) result.UnwrapUnsafe();
                ctx = value.getContext();
                results.add(value.getResult());
            }
            return OkResult.Ok(results, ctx);
        });
    }

    public static <T> Parser<List<T>> Seq(List<Parser<T>> parsers) {
        return new Parser<>((ctx) -> {
            List<T> results = new ArrayList<T>();
            for (var parser : parsers) {
                Result<OkResult<T>, ParseError> result = parser.parse(ctx);
                if (result.getType() == ResultKind.Error) {
                    // TODO: return error with consumed context
                    return result.CastErrorUnsafe();
                }
                var value = (OkResult<T>) result.UnwrapUnsafe();
                ctx = value.getContext();
                results.add(value.getResult());
            }
            return OkResult.Ok(results, ctx);
        });
    }

    public static <T> Parser<T> Choice(Parser<T>[] parsers) {
        return new Parser<>((ctx) -> {
            for (var parser : parsers) {
                Result<OkResult<T>, ParseError> result = parser.parse(ctx);
                if (result.getType() == ResultKind.Ok) {
                    return result;
                }
            }
            return ParseError.Err(ErrorReason.NoMatch, ctx);
        });
    }

    public static <T> Parser<T> Choice(List<Parser<T>> parsers) {
        return new Parser<>((ctx) -> {
            for (var parser : parsers) {
                Result<OkResult<T>, ParseError> result = parser.parse(ctx);
                if (result.getType() == ResultKind.Ok) {
                    return result;
                }
            }
            return ParseError.Err(ErrorReason.NoMatch, ctx);
        });
    }

    public static <T, U> Parser<Optional<List<T>>> Sep(Parser<T> p,
            Parser<U> delim) {
        var rest_p = ParserCombinators.Many(ParserCombinators.KeepRight(delim, p));
        return new Parser<Optional<List<T>>>((ctx) -> {
            var first_r = p.parse(ctx);
            if (first_r.getType() == ResultKind.Error) {
                return OkResult.Ok(Optional.empty(), ctx);
            }
            var first_v = (OkResult<T>) first_r.UnwrapUnsafe();
            var rest_r = rest_p.parse(first_v.getContext());
            var res = new ArrayList<T>();
            res.add(first_v.getResult());
            if (rest_r.getType() == ResultKind.Error) {
                return OkResult.Ok(Optional.of(res), first_v.getContext());
            }
            var rest_v = (OkResult<Optional<List<T>>>) rest_r.UnwrapUnsafe();
            if (rest_v.getResult().isEmpty()) {

                return OkResult.Ok(Optional.of(res), first_v.getContext());
            }
            return OkResult.Ok(
                    Optional.of(Stream.concat(res.stream(), rest_v.getResult().get().stream())
                            .collect(Collectors.toList())),
                    rest_v.getContext());

        });
    }

    public static <T, U> Parser<List<T>> Sep1(Parser<T> p,
            Parser<U> delim) {
        return new Parser<>((ctx) -> {
            Result<OkResult<Optional<List<T>>>, ParseError> result = ParserCombinators.Sep(p, delim).parse(ctx);
            if (result.getType() == ResultKind.Error) {
                return result.CastErrorUnsafe();
            }
            var value = (OkResult<Optional<List<T>>>) result.UnwrapUnsafe();
            if (value.getResult().isEmpty()) {
                return ParseError.Err(ErrorReason.NoMatch, ctx);
            }
            return OkResult.Ok(value.getResult().get(), value.getContext());
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