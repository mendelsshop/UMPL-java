package umpl.ast;

import parser_combinator.Parser;
import parser_combinator.Parsers;

public class AstHempty extends Ast {
    public AstHempty() {

    }

    @Override
    public String toString() {
        return "Hempty";
    }

    public static Parser<Ast> parser = Parsers.Matches("hempty").Map((i) -> new AstHempty());

}
