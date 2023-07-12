package umpl_beta;

import java.util.Optional;

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

    public Optional<T> Ok() {
        switch (type) {
            case Ok: return Optional.of(((OkResult<T>) this).getResult() ); 
            case Error: return Optional.empty();
            default: return Optional.empty();
        }
    } 


}
