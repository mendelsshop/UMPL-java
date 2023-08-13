package umpl.ast;

import java.util.List;
import java.util.Optional;

import com.vdurmont.emoji.EmojiManager;

import parser_combinator.Parser;

public class AstFunction extends Ast {
    Optional<String> name;
    int paramCount;
    Optional<Variadic> params;
    List<Ast> scope;

    public AstFunction(Optional<String> name, int paramCount, Optional<Variadic> params, List<Ast> scope) {
        this.name = name;
        this.paramCount = paramCount;
        this.params = params;
        this.scope = scope;
    }

    public static final Parser<Ast> parse = null;

    public enum Variadic {
        AtLeast1,
        AtLeast0,
    }

    @Override
    public String toString() {
        return "AstFunction [name=" + name + ", paramCount=" + paramCount + ", params=" + params + ", scope=" + scope
                + "]";
    }
}
