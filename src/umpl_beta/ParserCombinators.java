package umpl_beta;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import umpl_beta.ErrorResult.ErrorReason;
import umpl_beta.Result.ResultKind;

public class ParserCombinators {
    public static <T> Function<Context, Result<T>> Alt(Function<Context, Result<T>> p1,
            Function<Context, Result<T>> p2) {
        return (ctx) -> {
            Result<T> result = p1.apply(ctx);
            if (result.getType() == ResultKind.Ok) {
                return result;
            }
            return p2.apply(ctx);
        };
    }

    @SuppressWarnings("unchecked")
    public static <T, U> Function<Context, Result<U>> Map(Function<Context, Result<T>> p, Function<T, U> mapper) {
        return (ctx) -> {
            Result<T> result = p.apply(ctx);
            if (result.getType() == ResultKind.Error) {
                return (Result<U>) result;
            }
            OkResult<T> value = (OkResult<T>) result;
            return new OkResult<U>(mapper.apply(value.getResult()), result.getContext());
        };
    }

    @SuppressWarnings("unchecked")
    public static <T, U> Function<Context, Result<Tuple<T, U>>> Chain(Function<Context, Result<T>> p1,
            Function<Context, Result<U>> p2) {
        return (ctx) -> {
            Result<T> r1 = p1.apply(ctx);
            if (r1.getType() == ResultKind.Error) {
                return (Result<Tuple<T, U>>) r1;
            }
            OkResult<T> v1 = (OkResult<T>) r1;
            Result<U> r2 = p2.apply(v1.getContext());
            if (r2.getType() == ResultKind.Error) {
                return (Result<Tuple<T, U>>) r2;
            }
            OkResult<U> v2 = (OkResult<U>) r2;
            return new OkResult<Tuple<T, U>>(new Tuple<T, U>(v1.getResult(), v2.getResult()), r2.getContext());
        };
    }

    public static <T> Function<Context, Result<Optional<T>>> Opt(Function<Context, Result<T>> p) {
        return (ctx) -> {
            Result<T> result = p.apply(ctx);
            Optional<T> ret = result.Ok();
            return new OkResult<Optional<T>>(ret, result.getContext());
        };
    }

    public static <T> Function<Context, Result<Optional<List<T>>>> Many(Function<Context, Result<T>> p) {
        return (ctx) -> {
            List<T> results = new ArrayList<T>();
            while (true) {
                Result<T> result = p.apply(ctx);
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
        };
    }

    @SuppressWarnings("unchecked")
    public static <T> Function<Context, Result<List<T>>> Many1(Function<Context, Result<T>> p) {
        return (ctx) -> {
            Result<Optional<List<T>>> result = ParserCombinators.Many(p).apply(ctx);
            if (result.getType() == ResultKind.Error) {
                var error = (ErrorResult<?>) result;
                return (Result<List<T>>) error;
            }
            var value = (OkResult<Optional<List<T>>>) result;
            if (value.getResult().isEmpty()) {
                return new ErrorResult<>(ErrorReason.NoMatch, ctx);
            }
            return new OkResult<List<T>>(value.getResult().get(), value.getContext());
        };
    }

    @SuppressWarnings("unchecked")
    public static <T> Function<Context, Result<List<T>>> Seq(List<Function<Context, Result<T>>> parsers) {
        return (ctx) -> {
            List<T> results = new ArrayList<T>();
            for (var parser : parsers) {
                Result<T> result = parser.apply(ctx);
                if (result.getType() == ResultKind.Error) {
                    // TODO: return error with consumed context
                    return (Result<List<T>>) result;
                }
                var value = (OkResult<T>) result;
                ctx = value.getContext();
                results.add(value.getResult());
            }
            return new OkResult<>(results, ctx);
        };
    }

    public static <T> Function<Context, Result<T>> Choice(List<Function<Context, Result<T>>> parsers) {
        return (ctx) -> {
            for (var parser : parsers) {
                Result<T> result = parser.apply(ctx);
                if (result.getType() == ResultKind.Ok) {
                    return result;
                }
            }
            return new ErrorResult<>(ErrorReason.NoMatch, ctx);
        };
    }

    public static <T, U> Function<Context, Result<Optional<List<T>>>> Sep(Function<Context, Result<T>> p,
            Function<Context, Result<U>> delim) {
        var rest_p = ParserCombinators.Many(ParserCombinators.KeepRight(delim, p));
        return (ctx) -> {
            var first_r = p.apply(ctx);
            if (first_r.getType() == ResultKind.Error) {
                return new OkResult<>(Optional.empty(), ctx);
            }
            var first_v = (OkResult<T>) first_r;
            var rest_r = rest_p.apply(first_v.getContext());
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

        };
    }

    @SuppressWarnings("unchecked")
    public static <T, U> Function<Context, Result<List<T>>> Sep1(Function<Context, Result<T>> p,
            Function<Context, Result<U>> delim) {
        return (ctx) -> {
            Result<Optional<List<T>>> result = ParserCombinators.Sep(p, delim).apply(ctx);
            if (result.getType() == ResultKind.Error) {
                var error = (ErrorResult<?>) result;
                return (Result<List<T>>) error;
            }
            var value = (OkResult<Optional<List<T>>>) result;
            if (value.getResult().isEmpty()) {
                return new ErrorResult<>(ErrorReason.NoMatch, ctx);
            }
            return new OkResult<List<T>>(value.getResult().get(), value.getContext());
        };
    }

    public static <T, U> Function<Context, Result<T>> KeepLeft(Function<Context, Result<T>> p_left,
            Function<Context, Result<U>> p_right) {
        return ParserCombinators.Map(ParserCombinators.Chain(p_left, p_right), (r) -> r.getFirst());
    }

    public static <T, U> Function<Context, Result<U>> KeepRight(Function<Context, Result<T>> p_left,
            Function<Context, Result<U>> p_right) {
        return ParserCombinators.Map(ParserCombinators.Chain(p_left, p_right), (r) -> r.getSecond());
    }

    public static <T, U, V> Function<Context, Result<U>> InBetween(Function<Context, Result<T>> p_left,
            Function<Context, Result<U>> p_mid, Function<Context, Result<V>> p_right) {
        return ParserCombinators.KeepLeft(ParserCombinators.KeepRight(p_left, p_mid), p_right);
    }
}