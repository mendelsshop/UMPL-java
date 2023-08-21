package umpl.ast;

import misc.Result.Result;
import parser_combinator.Parser;
import parser_combinator.Parsers;
import umpl.analyzer.Anaylzer;
import umpl.evaluation.Evaluator;
import umpl.evaluation.EvaluatorError;

public class AstLabel extends Ast {
    public static final Parser<String> lableParser = AstIdent.identParser.KeepRight(Parsers.Matches('@'));

    public AstLabel(String val) {
        this.val = val;
    }

    String val;

    public static final Parser<Ast> parser = lableParser
            .Map(c -> new AstLabel(c));

    @Override
    public String toString() {
        return "Label [val=" + val + "]";
    }

    @Override
    public Result<Result<Ast, AstControlFlow>, EvaluatorError> evaluate(Evaluator state) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'evaluate'");
    }

    @Override
    public void analyze_links(Anaylzer analyzer) {

    }

    @Override
    public void analyze_labels(Anaylzer analyzer) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'analyze_labels'");
    }

}
