package umpl.ast;

public class AstNumber extends Ast {
    public AstNumber(double val) {
        this.val = val;
    }

    double val;

    @Override
    public String toString() {
        return "Number [val=" + val + "]";
    }
    
}
