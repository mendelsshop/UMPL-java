package umpl.ast;

public class AstString extends Ast {
    public AstString(String val) {
        this.val = val;
    }

    String val;

    @Override
    public String toString() {
        return "String [val=" + val + "]";
    }
    
}
