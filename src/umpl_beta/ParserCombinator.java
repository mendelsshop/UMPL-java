package umpl_beta;

import java.util.function.Function;

import umpl_beta.Result.ResultKind;

public class ParserCombinator {
    public static <T> Function<Context, Result<T>> Alt(Function<Context, Result<T>> p1, Function<Context, Result<T>> p2) {
      return  (ctx) -> {
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
    public static <T, U>  Function<Context, Result<Tuple<T,U>>> Chain(Function<Context, Result<T>> p1, Function<Context, Result<U>> p2) {
        return (ctx) -> {
            Result<T> r1 = p1.apply(ctx);
            if (r1.getType() == ResultKind.Error) {
                return (Result<Tuple<T,U>>) r1;
            }
            OkResult<T> v1 = (OkResult<T>) r1;
            Result<U> r2 = p2.apply(v1.getContext());
            if (r2.getType() == ResultKind.Error) {
                return (Result<Tuple<T,U>>) r2;
            }
            OkResult<U> v2 = (OkResult<U>) r2;
            return new OkResult<Tuple<T,U>>(new Tuple<T, U>(v1.getResult(), v2.getResult()), r2.getContext());
        };
    }
}