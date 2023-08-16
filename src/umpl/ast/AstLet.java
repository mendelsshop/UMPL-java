package umpl.ast;

import misc.Result.Result;
import parser_combinator.Parser;
import parser_combinator.Parsers;
import umpl.evaluation.Evaluator;
import umpl.evaluation.EvaluatorError;
import umpl.evaluation.Stopper;

public class AstLet extends Ast {
    String name;
    Ast value;

    public AstLet(String name, Ast value) {
        this.name = name;
        this.value = value;
    }

    public static final Parser<Ast> parser = AstIdent.identParser
            .KeepRight(Parsers.Matches("let").Chain(Ast.whitespacecommentParser))
            .Chain(Ast.parser)
            .Map(c -> new AstLet(c.getFirst(), c.getSecond()));

    @Override
    public String toString() {
        return "AstLet [name=" + name + ", value=" + value + "]";
    }

    @Override
    public Result<Result<Ast, Stopper>, EvaluatorError> evaluate(Evaluator state) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'evaluate'");
    }
}
