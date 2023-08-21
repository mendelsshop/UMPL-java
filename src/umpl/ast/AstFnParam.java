package umpl.ast;

import misc.Result.Err;
import misc.Result.Ok;
import misc.Result.Result;
import parser_combinator.Parser;
import parser_combinator.Parsers;
import umpl.analyzer.Anaylzer;
import umpl.evaluation.Evaluator;
import umpl.evaluation.EvaluatorError;
import umpl.evaluation.EvaluatorError.Reason;

// maybe should extend AstIdent
public class AstFnParam extends Ast {
    public AstFnParam(int val) {
        this.val = val;
    }

    int val;

    public static final Parser<Ast> parser = Parsers.Digit().Many1()
            .InBetween(Parsers.Matches('\'').Alt(Parsers.Matches('\"')),
                    Parsers.Matches('\'').Alt(Parsers.Matches('\"')).Opt())
            .Map(c -> new AstFnParam(Integer.parseInt(Parsers.listToString(c))));

    @Override
    public String toString() {
        return "FnParamAccesor [val=" + val + "]";
    }

    @Override
    public Result<Result<Ast, AstControlFlow>, EvaluatorError> evaluate(Evaluator state) {
        return (state.get(String.valueOf(val)).map(
                c -> (Result<Result<Ast, AstControlFlow>, EvaluatorError>) new Ok<Result<Ast, AstControlFlow>, EvaluatorError>(
                        new Ok<>(c))))
                .orElse(new Err<>(new EvaluatorError(Reason.VariableNotFound)));

    }

    @Override
    public Ast analyze_links(Anaylzer analyzer) {
        return this;
    }

    @Override
    public Ast analyze_labels(Anaylzer analyzer) {
        return this;
    }
}
