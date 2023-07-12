package umpl_beta;

public abstract class Result<T> {
    protected Context context;
    protected ResultKind type;

    public ResultKind getType() {
        return type;
    }

    public enum ResultKind {
        Ok,
        Error
    }

    public Context getContext() {
        return context;
    }


}
