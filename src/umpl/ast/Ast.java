package umpl.ast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Function;

import misc.Unit;
import parser_combinator.Parser;
import parser_combinator.Parsers;
import umpl.analyzer.IAnalyzer;
import umpl.ast.AstApplication.PrintType;
import umpl.evaluation.IEvaluator;

public abstract class Ast implements IEvaluator, IAnalyzer {
        protected static String[] whitespace = new String[] {
                        " ", "\n", "\t"

        };

        public static final Parser<Unit> commentParser = Parsers.Matches('!').Chain(Parsers.NotMatches('\n').Many())
                        .Chain(Parsers.Matches("\n")).Map(c -> new Unit());

        public static final Parser<Unit> whitespaceParser = Parsers
                        .Satisfy(c -> Arrays.asList(whitespace).contains(c.toString()))
                        .Map(c -> new Unit());
        public static final Parser<Unit> whitespacecommentParser = commentParser.Alt(whitespaceParser).Many1()
                        .Map(c -> new Unit());
        public static final Parser<Unit> whitespacecommentParserOpt = commentParser.Alt(whitespaceParser).Many()
                        .Map(c -> new Unit());
        // needs to be its own new closure so that we don't have infinite recursion
        // while creating the parser (so we add a level of indirection)
        // also wih java if the a static thing is not fully initialized we get nullptr
        // exceptions when calling this parser from other parsers
        public static final Parser<Ast> parser = new Parser<>(
                        (c) -> AstHempty.parser.Alt(AstBoolean.parser).Alt(AstApplication.parser)
                                        .Alt(AstString.parser)
                                        .Alt(AstNumber.parser)
                                        .Alt(AstLabel.parser)
                                        .Alt(AstControlFlow.parser)
                                        .Alt(AstQuoted.parser)
                                        .Alt(AstFnParam.parser)
                                        .Alt(AstIf.parser)
                                        .Alt(AstUnless.parser)
                                        .Alt(AstLink.parser)
                                        .Alt(AstLet.parser)
                                        .Alt(AstGoThrough.parser)
                                        .Alt(AstContinueDoing.parser)
                                        .Alt(AstUntil.parser)
                                        .Alt(AstFunction.parser)
                                        // ident parser should be placed last so it doesn't interfere with other
                                        // statements like: if, stop,..
                                        .Alt(AstIdent.parser).parse(c))
                        .KeepRight(whitespacecommentParserOpt)
                        .Chain(Parsers.Matches("car").Alt(Parsers.Matches("cdr").Alt(Parsers.Matches("cgr")))
                                        .KeepRight(Parsers.Matches('^')).Many())
                        .Map(c -> {
                                var expr = c.getFirst();
                                for (String accesor : c.getSecond().orElse(new ArrayList<>())) {
                                        expr = make_accces(expr, accesor);
                                }
                                return expr;
                        });

        protected static String[] special_char = new String[] {
                        "!", " ", "᚜", "᚛", ".", "&", "|", "?", "*", "+", "@", "\"", "\'", ";", "\n", "\t", "<",
                        ">", "^",
        };

        protected static String[] call_start = new String[] {
                        "(", "༺", "༼", "⁅", "⁽", "₍", "⌈", "⌊", "❨", "❪", "❬", "❮", "❰", "❲", "❴", "⟅", "⟦", "⟨",
                        "⟪", "⟬", "⟮", "⦃", "⦅", "⦇", "⦉", "⦋", "⦍", "⦏", "⦑", "⦓", "⦕", "⦗", "⧘", "⧚", "⸢", "⸤",
                        "⸦", "⸨", "⹕", "⹗", "⹙", "⹛", "〈", "《", "「", "『", "【",
                        "〔", "〖", "〘", "〚", "﹙", "﹛", "﹝", "（", "［", "｛", "｟", "｢", "{", "[",
        };

        protected static String[] call_end = new String[] {
                        ")", "༻", "༽", "⁆", "⁾", "₎", "⌉", "⌋", "❩", "❫", "❭", "❯", "❱", "❳", "❵", "⟆", "⟧", "⟩",
                        "⟫", "⟭", "⟯", "⦄", "⦆", "⦈", "⦊", "⦌", "⦎", "⦐", "⦒", "⦔", "⦖", "⦘", "⧙", "⧛", "⸣", "⸥",
                        "⸧", "⸩", "⹖", "⹘", "⹚", "⹜", "〉", "》", "」", "』", "】",
                        "〕", "〗", "〙", "〛", "﹚", "﹜", "﹞", "）", "］", "｝", "｠", "｣", "}", "]",
        };

        public static void main(String[] args) throws Exception {
                Ast ast = Ast.parser.parse("link @a @b @c a").Unwrap();
                System.out.println(ast);
        }

        private static Ast make_accces(Ast expr, String accesor) {
                return new AstApplication(Arrays.asList(new Ast[] { new AstIdent(accesor), expr }), PrintType.None);
        }

}

// pub enum UMPL2Expr {
// Bool(Boolean),done c
// Number(f64), done c
// String(RC<str>), done c
// Scope(Vec<UMPL2Expr>), done c
// Ident(RC<str>), done c
// // second 2 are scopes
// If(Box<If>), done c
// // second 2 are scopes
// Unless(Box<Unless>), done c
// Stop(Box<UMPL2Expr>),done c
// Skip, done c
// // last one is scope
// Until(Box<Until>), done c
// // last one is scope
// GoThrough(Box<GoThrough>), done c
// // last one is scope
// ContiueDoing(Vec<UMPL2Expr>), done c
// // last one is scope
// Fanction(Fanction),
// Application(Application), done c
// Quoted(Box<UMPL2Expr>), done c
// Label(RC<str>), done c
// FnParam(usize), done
// Hempty, done c
// Link(RC<str>, Vec<RC<str>>), done c
// Let(RC<str>, Box<UMPL2Expr>), done c
// }