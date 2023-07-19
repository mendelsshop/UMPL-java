package umpl.ast;


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
    
}
