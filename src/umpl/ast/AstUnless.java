package umpl.ast;

import java.util.List;

import misc.Result.Result;
import parser_combinator.Parser;
import parser_combinator.Parsers;
import umpl.analyzer.Anaylzer;
import umpl.evaluation.Evaluator;
import umpl.evaluation.EvaluatorError;

// unless gets its own class b/c unless or if might have slightly different runtime semantics
public class AstUnless extends Ast {
    Ast condition;
    List<Ast> consequent;
    List<Ast> alternative;

    public AstUnless(Ast condition, List<Ast> consequent, List<Ast> alternative) {
        this.condition = condition;
        this.consequent = consequent;
        this.alternative = alternative;
    }

    public static final Parser<Ast> parser = Ast.parser.KeepRight(Parsers.Matches("unless"))
            .Chain(AstScope.parser.KeepRight(
                    Parsers.Matches("than").InBetween(Ast.whitespacecommentParser, Ast.whitespacecommentParserOpt)))
            .Chain(AstScope.parser.KeepRight(
                    Parsers.Matches("else").InBetween(Ast.whitespacecommentParser,
                            Ast.whitespacecommentParserOpt)))
            .Map(c -> new AstUnless(c.getFirst().getFirst(), c.getFirst().getSecond(), c.getSecond()));

    @Override
    public String toString() {
        return "AstIf [condition=" + condition + ", consequent=" + consequent + ", alternative=" + alternative + "]";
    }

    @Override
    public Result<Result<Ast, AstControlFlow>, EvaluatorError> evaluate(Evaluator state) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'evaluate'");
    }

    @Override
    public Ast analyze_links(Anaylzer analyzer) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'analyze_links'");
    }

    @Override
    public Ast analyze_labels(Anaylzer analyzer) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'analyze_labels'");
    }
}
