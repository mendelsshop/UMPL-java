package umpl.ast;

public class AstQuoted extends Ast {
    public AstQuoted(Ast val) {
        this.val = val;
    }

    Ast val;

    @Override
    public String toString() {
        return "Quote [val=" + val + "]";
    }
    
}
