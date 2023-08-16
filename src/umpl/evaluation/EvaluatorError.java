package umpl.evaluation;

public class EvaluatorError {
    public enum Reason {
        VariableNotFound
    }

    Reason reason;

    public EvaluatorError(Reason reason) {
        this.reason = reason;
    }
}
