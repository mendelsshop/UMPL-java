package misc.Result;

import java.util.Optional;
import java.util.function.Function;

public abstract class Result<T, U> {
    protected ResultKind type;

    public ResultKind getType() {
        return type;
    }

    public enum ResultKind {
        Ok,
        Error
    }

    public Optional<T> Ok() {
        switch (type) {
            case Ok:
                return Optional.of(((Ok<T, U>) this).getResult());
            case Error:
                return Optional.empty();
            default:
                return Optional.empty();
        }
    }

    public Optional<U> Err() {
        switch (type) {
            case Ok:
                return Optional.empty();
            case Error:
                return Optional.of(((Err<T, U>) this).getResult());
            default:
                return Optional.empty();
        }
    }

    public T UnwrapUnsafe() {
        return ((Ok<T, U>) this).getResult();
    }

    public U UnwrapErrUnsafe() {
        return ((Err<T, U>) this).getResult();
    }

    public <V> Result<V, U> Map(Function<T, V> map) {
        if (type == ResultKind.Error) {
            return CastErrorUnsafe();
        }
        return new Ok<>(map.apply(UnwrapUnsafe()));
    }

    public <V> Result<V, U> CastError() throws Exception {
        var newError = UnwrapErr();
        return new Err<V, U>(newError);
    }

    public <V> Result<V, U> CastErrorUnsafe() {
        var newError = UnwrapErrUnsafe();
        return new Err<V, U>(newError);
    }

    public T Unwrap() throws Exception {
        switch (type) {
            case Error:
                throw new Exception(((Err<T, U>) this).getResult().toString());
            case Ok:
                return ((Ok<T, U>) this).getResult();
            default:
                throw new Exception("unwrapped error value");
        }
    }

    public U UnwrapErr() throws Exception {
        switch (type) {
            case Error:
                return ((Err<T, U>) this).getResult();
            case Ok:
                throw new Exception(((Err<T, U>) this).getResult().toString());
            default:
                throw new Exception("unwrapped error value");
        }
    }

}
