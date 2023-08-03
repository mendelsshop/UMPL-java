package umpl.ast;

import java.util.Arrays;

import parser_combinator.Parser;
import parser_combinator.Parsers;

public class AstIdent extends Ast {
    public AstIdent(String val) {
        this.val = val;
    }

    String val;

    public static Parser<String> identParser = Parsers.Satisfy(c->!((Arrays.asList(Ast.call_start)).contains(c.toString()) || Arrays.asList(Ast.call_end).contains(c.toString())|| Arrays.asList(Ast.special_char).contains(c.toString()))).Many1().Map(Parsers::listToString);
    public static Parser<Ast> parser = identParser.Map(c->new AstIdent(c));

    @Override
    public String toString() {
        return "Ident [val=" + val + "]";
    }
    
}
