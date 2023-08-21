package umpl.ast;

import java.util.Arrays;
import java.util.Optional;

import misc.Result.Err;
import misc.Result.Ok;
import misc.Result.Result;
import parser_combinator.Parser;
import parser_combinator.Parsers;
import umpl.analyzer.Anaylzer;
import umpl.evaluation.Evaluator;
import umpl.evaluation.EvaluatorError;

import umpl.evaluation.EvaluatorError.Reason;

public class AstIdent extends Ast {
    public AstIdent(String name) {
        this.name = name;
    }

    String name;

    public static final Parser<String> identParser = Parsers
            .Satisfy(c -> !((Arrays.asList(Ast.call_start)).contains(c.toString())
                    || Arrays.asList(Ast.call_end).contains(c.toString())
                    || Arrays.asList(Ast.special_char).contains(c.toString())))
            .Many1().Map(Parsers::listToString);
    public static final Parser<Ast> parser = identParser.Map(c -> new AstIdent(c));

    @Override
    public String toString() {
        return "Ident [val=" + name + "]";
    }

    @Override
    public Result<Result<Ast, AstControlFlow>, EvaluatorError> evaluate(Evaluator state) {
        Optional<Ast> value = state.get(name);
        if (value.isPresent()) {
            return new Err<Result<Ast, AstControlFlow>, EvaluatorError>(new EvaluatorError(Reason.VariableNotFound));
        }
        return new Ok<>(new Ok<>(value.get()));
    }

    @Override
    public Ast analyze_links(Anaylzer analyzer) {
        return this;
    }

    @Override
    public Ast analyze_labels(Anaylzer analyzer) {
        return this;
    }

}
