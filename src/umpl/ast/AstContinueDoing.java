package umpl.ast;

import java.util.List;

import misc.Result.Result;
import parser_combinator.Parser;
import parser_combinator.Parsers;
import umpl.evaluation.Evaluator;
import umpl.evaluation.EvaluatorError;
import umpl.evaluation.Stopper;

public class AstContinueDoing extends Ast {
    @Override
    public String toString() {
        return "AstContinueDoing [loopScope=" + loopScope + "]";
    }

    List<Ast> loopScope;

    public AstContinueDoing(List<Ast> loopScope) {
        this.loopScope = loopScope;
    }

    public static final Parser<Ast> parser = AstScope.parser.KeepRight(
            Parsers.Matches("continue-doing").KeepLeft(Ast.whitespacecommentParser))
            .Map(c -> new AstContinueDoing(c));

    @Override
    public Result<Result<Ast, Stopper>, EvaluatorError> evaluate(Evaluator state) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'evaluate'");
    }

}
