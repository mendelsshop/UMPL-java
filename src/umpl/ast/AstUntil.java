package umpl.ast;

import java.util.List;

import misc.Result.Result;
import parser_combinator.Parser;
import parser_combinator.Parsers;
import umpl.analyzer.Anaylzer;
import umpl.evaluation.Evaluator;
import umpl.evaluation.EvaluatorError;

public class AstUntil extends Ast {
    @Override
    public String toString() {
        return "AstUntil [Condition=" + Condition + ", loopScope=" + loopScope + "]";
    }

    public AstUntil(Ast condition, List<Ast> loopScope) {
        Condition = condition;
        this.loopScope = loopScope;
    }

    Ast Condition;
    List<Ast> loopScope;

    public static final Parser<Ast> parser = Ast.parser.KeepRight(Parsers.Matches("until"))
            .Chain(AstScope.parser.KeepRight(
                    Parsers.Matches("then").InBetween(Ast.whitespacecommentParser, Ast.whitespacecommentParserOpt)))
            .Map(c -> new AstUntil(c.getFirst(), c.getSecond()));

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
