package umpl_beta;

public class OkResult<T> extends Result<T> {
    private T result;

    public T getResult() {
        return result;
    }

    public OkResult(T result, Context context) {
        this.result = result;
        this.context = context;
        type = ResultKind.Ok;
    }

}