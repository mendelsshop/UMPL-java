package umpl.ast;

import java.util.ArrayList;
import java.util.List;

import parser_combinator.Parser;
import parser_combinator.Parsers;

public class AstScope {
    public static final Parser<List<Ast>> parser = Ast.parser.Sep(Ast.whitespacecommentParser)
            .InBetween(Parsers.Matches("\u169C").KeepLeft(Ast.whitespacecommentParserOpt),
                    Parsers.Matches("\u169B").KeepRight(Ast.whitespacecommentParserOpt).Opt())
            .Map((c -> c.orElse(new ArrayList<Ast>())));
}
