package misc.Result;

public class Ok<T, U> extends Result<T, U> {
    private T data;

    public Ok(T data) {
        this.data = data;
        type = ResultKind.Ok;
    }

    T getResult() {
        return data;
    };

}
