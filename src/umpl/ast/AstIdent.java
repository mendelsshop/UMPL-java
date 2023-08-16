package umpl.ast;

import java.util.Arrays;

import misc.Result.Result;
import parser_combinator.Parser;
import parser_combinator.Parsers;
import umpl.evaluation.Evaluator;
import umpl.evaluation.EvaluatorError;
import umpl.evaluation.Stopper;

public class AstIdent extends Ast {
    public AstIdent(String val) {
        this.val = val;
    }

    String val;

    public static final Parser<String> identParser = Parsers
            .Satisfy(c -> !((Arrays.asList(Ast.call_start)).contains(c.toString())
                    || Arrays.asList(Ast.call_end).contains(c.toString())
                    || Arrays.asList(Ast.special_char).contains(c.toString())))
            .Many1().Map(Parsers::listToString);
    public static final Parser<Ast> parser = identParser.Map(c -> new AstIdent(c));

    @Override
    public String toString() {
        return "Ident [val=" + val + "]";
    }

    @Override
    public Result<Result<Ast, Stopper>, EvaluatorError> evaluate(Evaluator state) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'evaluate'");
    }

}
