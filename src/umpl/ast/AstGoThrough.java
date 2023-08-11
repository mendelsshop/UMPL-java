package umpl.ast;

import java.util.List;

import parser_combinator.Parser;
import parser_combinator.Parsers;

public class AstGoThrough extends Ast {
    Ast iterValue;
    String iterName;
    List<Ast> loopScope;

    public AstGoThrough(String iterName, Ast iterValue, List<Ast> loopScope) {
        this.iterValue = iterValue;
        this.iterName = iterName;
        this.loopScope = loopScope;
    }

    public static final Parser<Ast> parser = AstIdent.identParser
            .Chain(Ast.parser.KeepRight(Parsers.Matches("go-through")))
            .Chain(AstScope.parser.KeepRight(
                    Parsers.Matches("then").InBetween(Ast.whitespacecommentParser, Ast.whitespacecommentParserOpt)))
            .Map(c -> new AstGoThrough(c.getFirst().getFirst(), c.getFirst().getSecond(), c.getSecond()));

    @Override
    public String toString() {
        return "AstGoThrough [iterName=" + iterName + ", iterValue=" + iterValue + ", loopScope=" + loopScope + "]";
    }
}
