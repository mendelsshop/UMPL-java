package umpl.ast;

import java.util.List;

import misc.Result.Result;
import parser_combinator.Parser;
import parser_combinator.Parsers;
import umpl.analyzer.Anaylzer;
import umpl.evaluation.Evaluator;
import umpl.evaluation.EvaluatorError;

public class AstIf extends Ast {
    Ast condition;
    List<Ast> consequent;
    List<Ast> alternative;

    public AstIf(Ast condition, List<Ast> consequent, List<Ast> alternative) {
        this.condition = condition;
        this.consequent = consequent;
        this.alternative = alternative;
    }

    public static final Parser<Ast> parser = Ast.parser.KeepRight(Parsers.Matches("if"))
            .Chain(AstScope.parser.KeepRight(
                    Parsers.Matches("do").InBetween(Ast.whitespacecommentParser, Ast.whitespacecommentParserOpt)))
            .Chain(AstScope.parser.KeepRight(
                    Parsers.Matches("otherwise").InBetween(Ast.whitespacecommentParser,
                            Ast.whitespacecommentParserOpt)))
            .Map(c -> new AstIf(c.getFirst().getFirst(), c.getFirst().getSecond(), c.getSecond()));

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
        alternative = alternative.stream().<Ast>map(c->c.analyze_links(analyzer)).toList();
        consequent = consequent.stream().<Ast>map(c->c.analyze_links(analyzer)).toList();
        condition = condition.analyze_links(analyzer);
        return this;
    }

    @Override
    public Ast analyze_labels(Anaylzer analyzer) {
        alternative = alternative.stream().<Ast>map(c->c.analyze_labels(analyzer)).toList();
        consequent = consequent.stream().<Ast>map(c->c.analyze_labels(analyzer)).toList();
        condition = condition.analyze_labels(analyzer);
        return this;
    }
}
