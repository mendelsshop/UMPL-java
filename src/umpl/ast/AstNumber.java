package umpl.ast;

import java.util.Optional;

import misc.Result.Result;

import misc.Result.Ok;
import parser_combinator.Parser;
import parser_combinator.Parsers;
import umpl.analyzer.Anaylzer;
import umpl.evaluation.Evaluator;
import umpl.evaluation.EvaluatorError;

public class AstNumber extends Ast {
    public AstNumber(double val) {
        this.val = val;
    }

    double val;
    private static final Parser<Character> digit = Parsers.Digit();
    private static final Parser<Character> hexadigit = digit.Alt(Parsers.AnyOf("abcdef"));
    public static final Parser<Ast> parser = hexadigit.Many1()
            .Chain(hexadigit.Many1().KeepRight(Parsers.Matches('%')).Opt())
            .KeepRight(Parsers.Matches("0x"))
            .Alt(digit.Many1().Chain(digit.Many1().KeepRight(Parsers.Matches('%')).Opt())).Map(c -> {
                String number = String.format("0x%s.", Parsers.listToString(c.getFirst()));
                Optional<String> fp = c.getSecond().map(Parsers::listToString);
                if (fp.isPresent()) {
                    number = String.format("%s%sp0", number, fp.get());
                } else {
                    number = String.format("%sp0", number);
                }
                return new AstNumber(Double.parseDouble(number));
            });

    @Override
    public String toString() {
        return "Number [val=" + val + "]";
    }

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