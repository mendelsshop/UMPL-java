package umpl.ast;

import parser_combinator.Parser;
import parser_combinator.Parsers;

public class AstBoolean extends Ast {
    public AstBoolean(BooleanType val) {
        this.val = val;
    }

    public enum BooleanType {
        True,
        False,
        Maybe,
    }

    BooleanType val;

    @Override
    public String toString() {
        return "Boolean [val=" + val + "]";
    }

    public static Parser<Ast> parser = Parsers.AnyOf("&|?").Map(c -> {
        switch (c) {
            case '&':
                return new AstBoolean(BooleanType.True);
            case '|':
                return new AstBoolean(BooleanType.False);
            case '?':
                return new AstBoolean(BooleanType.Maybe);
            default:
                return null;

        }
    });

}
