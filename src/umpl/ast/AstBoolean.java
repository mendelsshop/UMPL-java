package umpl.ast;

import misc.Result.Ok;
import misc.Result.Result;
import parser_combinator.Parser;
import parser_combinator.Parsers;
import umpl.analyzer.Anaylzer;
import umpl.evaluation.Evaluator;
import umpl.evaluation.EvaluatorError;

public class AstBoolean extends Ast {
    public AstBoolean(BooleanType val) {
        this.val = val;
    }

    public enum BooleanType {
        True,
        False,
        Maybe,
    }

    BooleanType val;

    @Override
    public String toString() {
        return "Boolean [val=" + val + "]";
    }

    public static final Parser<Ast> parser = Parsers.AnyOf("&|?").Map(c -> {
        switch (c) {
            case '&':
                return new AstBoolean(BooleanType.True);
            case '|':
                return new AstBoolean(BooleanType.False);
            case '?':
                return new AstBoolean(BooleanType.Maybe);
            default:
                return null;

        }
    });

    @Override
    public Result<Result<Ast, AstControlFlow>, EvaluatorError> evaluate(Evaluator state) {
        return new Ok<>(new Ok<>(this));
    }

    @Override
    public void analyze_links(Anaylzer analyzer) {
    }

    @Override
    public void analyze_labels(Anaylzer analyzer) {
    }

}
