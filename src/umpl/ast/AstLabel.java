package umpl.ast;

import parser_combinator.Parser;
import parser_combinator.Parsers;

public class AstLabel extends Ast {
    public static final Parser<String> lableParser = AstIdent.identParser.KeepRight(Parsers.Matches('@'));

    public AstLabel(String val) {
        this.val = val;
    }

    String val;

    public static final Parser<Ast> parser = lableParser
            .Map(c -> new AstLabel(c));

    @Override
    public String toString() {
        return "Label [val=" + val + "]";
    }

}
