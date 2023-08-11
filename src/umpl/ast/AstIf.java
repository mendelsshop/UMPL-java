package umpl.ast;

import java.util.List;

import parser_combinator.Parser;
import parser_combinator.Parsers;

public class AstIf extends Ast {
    Ast condition;
    List<Ast> consequent;
    List<Ast> alternative;

    public AstIf(Ast condition, List<Ast> consequent, List<Ast> alternative) {
        this.condition = condition;
        this.consequent = consequent;
        this.alternative = alternative;
    }

    public static final Parser<Ast> parser = Ast.parser.KeepRight(Parsers.Matches("if"))
            .Chain(AstScope.parser.KeepRight(
                    Parsers.Matches("do").InBetween(Ast.whitespacecommentParser, Ast.whitespacecommentParserOpt)))
            .Chain(AstScope.parser.KeepRight(
                    Parsers.Matches("otherwise").InBetween(Ast.whitespacecommentParser,
                            Ast.whitespacecommentParserOpt)))
            .Map(c -> new AstIf(c.getFirst().getFirst(), c.getFirst().getSecond(), c.getSecond()));

    @Override
    public String toString() {
        return "AstIf [condition=" + condition + ", consequent=" + consequent + ", alternative=" + alternative + "]";
    }
}
