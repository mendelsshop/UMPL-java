package umpl.ast;

import parser_combinator.Parser;
import parser_combinator.Parsers;

public class AstString extends Ast {


    public AstString(String val) {
        this.val = val;
    }

    String val;

    @Override
    public String toString() {
        return "String [val=" + val + "]";
    }

    public static Parser<Ast> parser = Parsers.NotMatches('.').Many1().InBetween(Parsers.Matches('.'), Parsers.Matches('.'))
                .Map((i) -> new AstString(Parsers.listToString(i)));
    

}
