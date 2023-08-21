package umpl.ast;

import java.util.List;

import misc.Result.Result;
import parser_combinator.Parser;
import parser_combinator.Parsers;
import umpl.analyzer.Anaylzer;
import umpl.evaluation.Evaluator;
import umpl.evaluation.EvaluatorError;

public class AstLink extends Ast {
    String GotoLabel;
    List<String> ComeFromLabels;

    public AstLink(String gotoLabel, List<String> comeFromLabels) {
        GotoLabel = gotoLabel;
        ComeFromLabels = comeFromLabels;
    }

    public static final Parser<Ast> parser = AstLabel.lableParser
            .KeepRight(Parsers.Matches("link").Chain(Ast.whitespacecommentParser))
            .Chain(AstLabel.lableParser.Sep1(Ast.whitespacecommentParser).KeepRight(Ast.whitespacecommentParser))
            .Map(c -> new AstLink(c.getFirst(), c.getSecond()));

    @Override
    public String toString() {
        return "AstLink [GotoLabel=" + GotoLabel + ", ComeFromLabels=" + ComeFromLabels + "]";
    }

    @Override
    public Result<Result<Ast, AstControlFlow>, EvaluatorError> evaluate(Evaluator state) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'evaluate'");
    }

    @Override
    public Ast analyze_links(Anaylzer analyzer) {
        analyzer.pushLink(GotoLabel, ComeFromLabels);
        return new AstHempty();
    }

    @Override
    public Ast analyze_labels(Anaylzer analyzer) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'analyze_labels'");
    }

}
