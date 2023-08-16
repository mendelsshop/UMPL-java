package umpl.ast;

import java.util.Optional;

import misc.Result.Result;
import parser_combinator.Parser;
import parser_combinator.Parsers;
import umpl.evaluation.Evaluator;
import umpl.evaluation.EvaluatorError;
import umpl.evaluation.Stopper;

public class AstControlFlow extends Ast {
    private Optional<Ast> val;
    private ControlFlowType kind;

    public AstControlFlow(ControlFlowType kind) {
        // if (kind == ControlFlowType.Stop) {
        // throw new Exception("tried to make stop node but not given value");
        // }
        this.kind = kind;
        this.val = Optional.empty();
    }

    public AstControlFlow(Ast val, ControlFlowType kind) {
        // if (kind == ControlFlowType.Skip) {
        // throw new Exception("tried to make skip node but given value" + val);
        // }
        this.val = Optional.of(val);
        this.kind = kind;
    }

    private static final Parser<Ast> skip_parser = Parsers.Matches("skip")
            .Map(c -> new AstControlFlow(ControlFlowType.Skip));

    private static final Parser<Ast> stop_parser = Ast.parser
            .KeepRight(Parsers.Matches("stop").KeepLeft(Ast.whitespacecommentParser))
            .Map(t -> new AstControlFlow(t, ControlFlowType.Stop));

    public static final Parser<Ast> parser = stop_parser.Alt(skip_parser);

    public AstControlFlow() {
    }

    public Boolean isStop() {
        return kind == ControlFlowType.Stop;
    }

    public Boolean isSkip() {
        return kind == ControlFlowType.Skip;
    }

    public enum ControlFlowType {
        Stop,
        Skip
    }

    @Override
    public String toString() {
        if (isStop()) {
            return "AstControlFlow [val=" + val + ", kind=" + kind + "]";
        } else {
            return "AstControlFlow [kind=" + kind + "]";
        }
    }

    @Override
    public Result<Result<Ast, Stopper>, EvaluatorError> evaluate(Evaluator state) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'evaluate'");
    }

}
