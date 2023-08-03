package umpl.ast;

import java.util.List;
import java.util.Optional;

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

    public static Parser<Ast> parser = Parsers.NotMatches('.').Many()
            .InBetween(Parsers.Matches('.'), Parsers.Matches('.'))
            .Map((Optional<List<Character>> i) -> new AstString(i.map(Parsers::listToString).orElse("")));

}
