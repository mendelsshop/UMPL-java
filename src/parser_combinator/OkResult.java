package parser_combinator;

import misc.Result.Result;

public class OkResult<T> {
    private T result;
    private Context context;

    public Context getContext() {
        return context;
    }

    public T getResult() {
        return result;
    }

    public OkResult(T result, Context context) {
        this.result = result;
        this.context = context;
    }

    public static <T> Result<OkResult<T>, ParseError> Ok(T result, Context context) {
        return new misc.Result.Ok<>(new OkResult<>(result, context));
    }

}