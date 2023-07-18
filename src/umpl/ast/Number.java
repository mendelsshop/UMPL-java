package umpl.ast;

public class Number extends Ast {
    public Number(double val) {
        this.val = val;
    }

    double val;

    @Override
    public String toString() {
        return "Number [val=" + val + "]";
    }
    
}
