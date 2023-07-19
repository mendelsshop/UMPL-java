package umpl.ast;

import java.util.Optional;

public class AstControlFlow extends Ast {
    private Optional<Ast> val;
    private ControlFlowType kind;

    public AstControlFlow(Optional<Ast> val, ControlFlowType kind) throws Exception {
        if ((!val.isPresent() &&  kind == ControlFlowType.Stop) || (val.isPresent() &&  kind == ControlFlowType.Skip) ) {
            throw new Exception("");
        }
        this.val = val;
        this.kind = kind;
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
        return "AstControlFlow [val=" + val + ", kind=" + kind + "]";
    }
    
}
