package umpl.ast;

import misc.Result.Err;
import misc.Result.Ok;
import misc.Result.Result;
import misc.Result.Result.ResultKind;
import parser_combinator.Parser;
import parser_combinator.Parsers;
import umpl.analyzer.Anaylzer;
import umpl.evaluation.Evaluator;
import umpl.evaluation.EvaluatorError;

import umpl.evaluation.EvaluatorError.Reason;

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
    public Result<Result<Ast, AstControlFlow>, EvaluatorError> evaluate(Evaluator state) {
        var valWrapped = value.evaluate(state);
        if (valWrapped.getType() == ResultKind.Error) {
            return valWrapped;
        } else if (valWrapped.UnwrapUnsafe().getType() == ResultKind.Error) {
            return valWrapped;
        }
        if (state.set(name, valWrapped.UnwrapUnsafe().UnwrapUnsafe())) {
            return new Ok<>(new Ok<>(new AstHempty()));
        }
        return new Err<Result<Ast, AstControlFlow>, EvaluatorError>(new EvaluatorError(Reason.VariableNotFound));
    }

    @Override
    public Ast analyze_links(Anaylzer analyzer) {
       value = value.analyze_links(analyzer);
       return this;
    }

    @Override
    public Ast analyze_labels(Anaylzer analyzer) {
        value = value.analyze_labels(analyzer);
        return this;
    }
}
