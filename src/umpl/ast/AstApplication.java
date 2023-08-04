package umpl.ast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import parser_combinator.Parser;
import parser_combinator.Parsers;

public class AstApplication extends Ast {
    public enum PrintType {
        None,
        Print,
        PrintLN,
    }

    private static final Parser<PrintType> printParser = Parsers.Matches(">>").Map(c -> PrintType.Print)
            .Alt(Parsers.Matches('>').Map(c -> PrintType.PrintLN)).Alt(Parsers.Matches('<').Map(c -> PrintType.None))
            .KeepRight(Ast.whitespacecommentParserOpt);
    private List<Ast> arguements;
    private PrintType print;
    public static final Parser<Ast> parser = Ast.parser.Sep(Ast.whitespacecommentParser)
            .InBetween(Parsers.Satisfy(c -> Arrays.asList(Ast.call_start).contains(c.toString())),
                    Parsers.Satisfy(c -> Arrays.asList(Ast.call_end).contains(c.toString())))
            .Chain(printParser).Map(c -> new AstApplication(c.getFirst().orElse(new ArrayList<>()), c.getSecond()));

    public AstApplication(List<Ast> arguements, PrintType print) {
        this.arguements = arguements;
        this.print = print;
    }

    @Override
    public String toString() {
        return "AstApplication [arguements=" + arguements + ", print=" + print + "]";
    }
}