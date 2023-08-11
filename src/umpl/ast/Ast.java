package umpl.ast;

import java.util.Arrays;

import misc.Unit;
import parser_combinator.Parser;
import parser_combinator.Parsers;

public class Ast {

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
                                        // ident parser should be placed last so it doesn't interfere with other
                                        // statements like: if, stop,..
                                        .Alt(AstIdent.parser).parse(c))
                        .KeepRight(whitespacecommentParserOpt);

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
}

// pub enum UMPL2Expr {
// Bool(Boolean),done
// Number(f64), done
// String(RC<str>), done
// Scope(Vec<UMPL2Expr>), done
// Ident(RC<str>), done
// // second 2 are scopes
// If(Box<If>), done
// // second 2 are scopes
// Unless(Box<Unless>), done
// Stop(Box<UMPL2Expr>),done
// Skip, done
// // last one is scope
// Until(Box<Until>),
// // last one is scope
// GoThrough(Box<GoThrough>),
// // last one is scope
// ContiueDoing(Vec<UMPL2Expr>),
// // last one is scope
// Fanction(Fanction),
// Application(Application), done
// Quoted(Box<UMPL2Expr>), done
// Label(RC<str>), done
// FnParam(usize), done
// Hempty, done
// Link(RC<str>, Vec<RC<str>>), done
// Let(RC<str>, Box<UMPL2Expr>), done
// }