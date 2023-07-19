package umpl.ast;

public class AstLabel extends Ast {
    public AstLabel(String val) {
        this.val = val;
    }

    String val;

    @Override
    public String toString() {
        return "Label [val=" + val + "]";
    }
    
}
