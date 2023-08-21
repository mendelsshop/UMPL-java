package umpl.ast;

import misc.Result.Result;
import parser_combinator.Parser;
import parser_combinator.Parsers;
import umpl.analyzer.Anaylzer;
import umpl.evaluation.Evaluator;
import umpl.evaluation.EvaluatorError;

public class AstQuoted extends Ast {
    public AstQuoted(Ast val) {
        this.val = val;
    }

    Ast val;

    public static final Parser<Ast> parser = Ast.parser.KeepRight(Parsers.Matches(';')).Map(c -> new AstQuoted(c));

    @Override
    public String toString() {
        return "Quote [val=" + val + "]";
    }

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
