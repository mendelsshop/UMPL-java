package umpl.ast;

import java.util.Optional;

import parser_combinator.Parser;
import parser_combinator.Parsers;

public class AstNumber extends Ast {
    public AstNumber(double val) {
        this.val = val;
    }

    double val;
    private static Parser<Character> digit = Parsers.Digit();
    private static Parser<Character> hexadigit = digit.Alt(Parsers.AnyOf("abcdef"));
    public static Parser<Ast> parser = hexadigit.Many1().Chain(hexadigit.Many1().KeepRight(Parsers.Matches('%')).Opt())
            .KeepRight(Parsers.Matches("0x"))
            .Alt(digit.Many1().Chain(digit.Many1().KeepRight(Parsers.Matches('%')).Opt())).Map(c -> {
                String number = String.format("0x%s.", Parsers.listToString(c.getFirst()));
                Optional<String> fp = c.getSecond().map(Parsers::listToString);
                if (fp.isPresent()) {
                    number = String.format("%s%sp0", number, fp.get());
                } else {
                    number = String.format("%sp0", number);
                }
                return new AstNumber(Double.parseDouble(number));
            });

    @Override
    public String toString() {
        return "Number [val=" + val + "]";
    }

}