package umpl.ast;

import parser_combinator.Parser;
import parser_combinator.Parsers;

public class AstLabel extends Ast {
    public AstLabel(String val) {
        this.val = val;
    }

    String val;

    public static final Parser<Ast> parser = AstIdent.identParser.KeepRight(Parsers.Matches('@'))
            .Map(c -> new AstLabel(c));

    @Override
    public String toString() {
        return "Label [val=" + val + "]";
    }

}
