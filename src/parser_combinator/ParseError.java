package parser_combinator;

import misc.Result.Result;

public class ParseError {
    private ErrorReason reason;
    private Context context;

    public Context getContext() {
        return context;
    }

    public ParseError(ErrorReason reason, Context context) {
        this.reason = reason;
        this.context = context;

    }

    public ErrorReason getReason() {
        return reason;
    }

    public enum ErrorReason {
        EOF,
        MisMatch,
        Fail,
        NoMatch,
    }

    public static <T> Result<OkResult<T>, ParseError> Err(ErrorReason reason, Context context) {
        return new misc.Result.Err<OkResult<T>, ParseError>(new ParseError(reason, context));
    }
}