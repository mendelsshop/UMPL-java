package umpl.ast;

import java.util.List;
import java.util.Optional;

import misc.Result.Ok;
import misc.Result.Result;
import parser_combinator.Parser;
import parser_combinator.Parsers;
import umpl.analyzer.Anaylzer;
import umpl.evaluation.Evaluator;
import umpl.evaluation.EvaluatorError;

public class AstString extends Ast {

    public AstString(String val) {
        this.val = val;
    }

    String val;

    @Override
    public String toString() {
        return "String [val=" + val + "]";
    }

    public static final Parser<Ast> parser = Parsers.NotMatches('.').Many()
            .InBetween(Parsers.Matches('.'), Parsers.Matches('.'))
            .Map((Optional<List<Character>> i) -> new AstString(i.map(Parsers::listToString).orElse("")));

    @Override
    public Result<Result<Ast, AstControlFlow>, EvaluatorError> evaluate(Evaluator state) {
        return new Ok<>(new Ok<>(this));
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
