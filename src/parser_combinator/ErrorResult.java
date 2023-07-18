package parser_combinator;

public class ErrorResult<T> extends Result<T> {
    private ErrorReason reason;

    public ErrorResult(ErrorReason reason, Context context) {
        this.reason = reason;
        this.context = context;
        type = ResultKind.Error;
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
}