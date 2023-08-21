package umpl.ast;

import java.util.List;

import misc.Result.Result;
import parser_combinator.Parser;
import parser_combinator.Parsers;
import umpl.analyzer.Anaylzer;
import umpl.evaluation.Evaluator;
import umpl.evaluation.EvaluatorError;

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
    public Result<Result<Ast, AstControlFlow>, EvaluatorError> evaluate(Evaluator state) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'evaluate'");
    }

    @Override
    public void analyze_links(Anaylzer analyzer) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'analyze_links'");
    }

    @Override
    public void analyze_labels(Anaylzer analyzer) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'analyze_labels'");
    }

}
