package umpl.ast;

import java.util.List;

import parser_combinator.Parser;
import parser_combinator.Parsers;

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

}
