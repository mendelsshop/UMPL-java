package umpl.ast;

import java.util.Optional;

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
        type = Type.Normal;
    }

    enum Type {
        Normal, ComeTo
    }

    String val;
    Type type;

    private AstLabel(String val, Type type) {
        this.val = val;
        this.type = type;
    }

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
    public Ast analyze_links(Anaylzer analyzer) {
        return this;
    }

    @Override
    public Ast analyze_labels(Anaylzer analyzer) {
        // TODO: this might not be needed as we can just figure out what the label does
        // at eval time
        Optional<Optional<Ast>> replace = analyzer.getLinks().entrySet().stream().<Optional<Ast>>map(c -> {
            if (c.getKey() == val) {
                return Optional.of(new AstLabel(val, Type.ComeTo));
            } else if (c.getValue().contains(val)) {
                return Optional.ofNullable(new AstLabel(c.getKey()));
            } else {
                return Optional.empty();

            }
        }).findAny();
        if (replace.isPresent()) {
            return replace.get().get();
        } else {
            return this;
        }
    }

}
