package misc.Result;

public class Err<T, U> extends Result<T, U> {
    private U error;

    public Err(U error) {
        this.error = error;
        type = ResultKind.Error;
    }

    U getResult() {
        return error;
    };
}
