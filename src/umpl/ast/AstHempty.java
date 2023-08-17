package umpl.ast;

import misc.Result.Ok;
import misc.Result.Result;
import parser_combinator.Parser;
import parser_combinator.Parsers;
import umpl.evaluation.Evaluator;
import umpl.evaluation.EvaluatorError;


public class AstHempty extends Ast {
    public AstHempty() {

    }

    @Override
    public String toString() {
        return "Hempty";
    }

    public static final Parser<Ast> parser = Parsers.Matches("hempty").Map((i) -> new AstHempty());

    @Override
    public Result<Result<Ast, AstControlFlow>, EvaluatorError> evaluate(Evaluator state) {
        return new Ok<>(new Ok<>(this));
    }
}
