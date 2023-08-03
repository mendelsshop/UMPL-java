package umpl.ast;

import parser_combinator.Parser;
import parser_combinator.Parsers;

public class AstQuoted extends Ast {
    public AstQuoted(Ast val) {
        this.val = val;
    }

    Ast val;

    public static Parser<Ast> parser = Ast.parser.KeepRight(Parsers.Matches(';')).Map(c-> new AstQuoted(c));

    @Override
    public String toString() {
        return "Quote [val=" + val + "]";
    }
}
