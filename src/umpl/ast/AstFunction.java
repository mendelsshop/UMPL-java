package umpl.ast;

import java.util.List;
import java.util.Optional;

import com.vdurmont.emoji.EmojiManager;

import misc.Result.Ok;
import misc.Result.Result;
import parser_combinator.Parser;
import parser_combinator.Parsers;
import umpl.evaluation.Evaluator;
import umpl.evaluation.EvaluatorError;


public class AstFunction extends Ast {
    Optional<Character> name;
    int paramCount;
    Optional<Variadic> params;
    List<Ast> scope;

    public AstFunction(Optional<Character> name, int paramCount, Optional<Variadic> params, List<Ast> scope) {
        this.name = name;
        this.paramCount = paramCount;
        this.params = params;
        this.scope = scope;
    }

    public enum Variadic {
        AtLeast1,
        AtLeast0,
    }

    public static final Parser<Ast> parse = Parsers.Satisfy(c -> EmojiManager.isEmoji(c.toString())).Opt()
            .KeepRight(Parsers.Matches("fanction").Chain(Ast.whitespacecommentParser))
            .Chain(Parsers.Integer().Opt().KeepRight(Ast.whitespacecommentParser).Map(c -> c.orElse(0)))
            .Chain(Parsers
                    .Matches('*').Map(c -> Variadic.AtLeast0).Alt(Parsers.Matches('+').Map(c -> Variadic.AtLeast1))
                    .KeepRight(Ast.whitespacecommentParserOpt).Opt())
            .Chain(AstScope.parser.KeepRight(Ast.whitespacecommentParser))
            .Map(c -> new AstFunction(c.getFirst().getFirst().getFirst(), c.getFirst().getFirst().getSecond(),
                    c.getFirst().getSecond(), c.getSecond()));

    @Override
    public String toString() {
        return "AstFunction [name=" + name + ", paramCount=" + paramCount + ", params=" + params + ", scope=" + scope
                + "]";
    }

    @Override
    public Result<Result<Ast, AstControlFlow>, EvaluatorError> evaluate(Evaluator state) {
        // if it's a function (has a name) then we insert it into scope (with no name)
        if (name.isPresent()) {
            state.insert(name.get().toString(), new AstFunction(Optional.empty(), paramCount, params, scope));
            return new Ok<>(new Ok<>(new AstHempty()));
        }
        // otherwise just return it
        return new Ok<>(new Ok<>(this));
    }
}
