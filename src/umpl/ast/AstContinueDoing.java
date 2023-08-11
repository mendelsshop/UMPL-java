package umpl.ast;

import java.util.List;

import parser_combinator.Parser;
import parser_combinator.Parsers;

public class AstContinueDoing extends Ast {
    @Override
    public String toString() {
        return "AstContinueDoing [loopScope=" + loopScope + "]";
    }

    List<Ast> loopScope;

    public AstContinueDoing(List<Ast> loopScope) {
        this.loopScope = loopScope;
    }

    public static final Parser<Ast> parser = AstScope.parser.KeepRight(
            Parsers.Matches("continue-doing").KeepLeft(Ast.whitespacecommentParser))
            .Map(c -> new AstContinueDoing(c));

}
