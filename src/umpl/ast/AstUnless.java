package umpl.ast;

import java.util.List;

import parser_combinator.Parser;
import parser_combinator.Parsers;

// unless gets its own class b/c unless or if might have slightly different runtime semantics
public class AstUnless extends Ast {
    Ast condition;
    List<Ast> consequent;
    List<Ast> alternative;

    public AstUnless(Ast condition, List<Ast> consequent, List<Ast> alternative) {
        this.condition = condition;
        this.consequent = consequent;
        this.alternative = alternative;
    }

    public static final Parser<Ast> parser = Ast.parser.KeepRight(Parsers.Matches("unless"))
            .Chain(AstScope.parser.KeepRight(
                    Parsers.Matches("than").InBetween(Ast.whitespacecommentParser, Ast.whitespacecommentParserOpt)))
            .Chain(AstScope.parser.KeepRight(
                    Parsers.Matches("else").InBetween(Ast.whitespacecommentParser,
                            Ast.whitespacecommentParserOpt)))
            .Map(c -> new AstUnless(c.getFirst().getFirst(), c.getFirst().getSecond(), c.getSecond()));

    @Override
    public String toString() {
        return "AstIf [condition=" + condition + ", consequent=" + consequent + ", alternative=" + alternative + "]";
    }
}
