package umpl_beta;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

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
}