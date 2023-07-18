package umpl;

import parser_combinator.Parsers;
import parser_combinator.Result;
import umpl.ast.Ast;

public class Parser {
    public static Result<Ast> parse(String input) {
        return Parsers.Integer().Map(n -> (Ast) new umpl.ast.Number(n)).parse(input);
    }
    public static void main(String[] args) throws Exception {
        Ast a = parse("1113").Unwrap();
        System.out.println(a);
    }
}