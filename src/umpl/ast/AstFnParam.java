package umpl.ast;

import parser_combinator.Parser;
import parser_combinator.Parsers;

// maybe should extend AstIdent
public class AstFnParam extends Ast {
    public AstFnParam(int val) {
        this.val = val;
    }

    int val;

    public static final Parser<Ast> parser = Parsers.Digit().Many1()
            .InBetween(Parsers.Matches('\'').Alt(Parsers.Matches('\"')),
                    Parsers.Matches('\'').Alt(Parsers.Matches('\"')).Opt())
            .Map(c -> new AstFnParam(Integer.parseInt(Parsers.listToString(c))));

    @Override
    public String toString() {
        return "FnParamAccesor [val=" + val + "]";
    }
}
