package umpl.evaluation;

import misc.Result.Result;
import umpl.ast.Ast;

public interface IEvaluator {
    // TODO: the inner result, shouldn't really be a result in name, but result does the same thing as an `Either` data structure
    // Also maybe it should not be Result<Ast, Stopper>, but rather EvalOk
    // where EvalOk, would not use Ast, but instead some smaller subset of Ast + list + thunk ...
    public Result<Result<Ast, Stopper>, EvaluatorError> evaluate(Evaluator state);
}
