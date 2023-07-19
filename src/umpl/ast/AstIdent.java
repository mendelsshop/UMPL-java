package umpl.ast;

public class AstIdent extends Ast {
    public AstIdent(String val) {
        this.val = val;
    }

    String val;

    @Override
    public String toString() {
        return "Ident [val=" + val + "]";
    }
    
}
