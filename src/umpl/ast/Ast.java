package umpl.ast;

import java.util.Arrays;

import misc.Unit;
import parser_combinator.Parser;
import parser_combinator.Parsers;

public class Ast {
    public static Parser<Ast> parser = AstHempty.parser.Alt(AstBoolean.parser)
            .Alt(AstString.parser)
            .Alt(AstNumber.parser)
            .Alt(AstIdent.parser)
            .Alt(AstLabel.parser)
    // .Alt(AstControlFlow.parser)
    // .Alt(AstQuoted.parser)
    ;

    protected static String[] whitespace = new String[] {
            " ", "\n", "\t"

    };

    public static Parser<Unit> commentParser = Parsers.Matches('!').Chain(Parsers.NotMatches('\n').Many())
            .Chain(Parsers.Matches("\n")).Map(c -> new Unit());

    public static Parser<Unit> whitespaceParser = Parsers.Satisfy(c -> Arrays.asList(whitespace).contains(c.toString()))
            .Map(c -> new Unit());
    public static Parser<Unit> whitespacecommentParser = commentParser.Alt(whitespaceParser).Many()
            .Map(c -> new Unit());
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
        Ast ast = Ast.parser.parse("skip").Unwrap();
        System.out.println(ast);
    }
}

// pub enum UMPL2Expr {
// Bool(Boolean),done
// Number(f64), done
// String(RC<str>), done
// Scope(Vec<UMPL2Expr>),
// Ident(RC<str>), done
// // second 2 are scopes
// If(Box<If>),
// // second 2 are scopes
// Unless(Box<Unless>),
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
// Application(Application),
// Quoted(Box<UMPL2Expr>), done
// Label(RC<str>), done
// FnParam(usize),
// Hempty, done
// Link(RC<str>, Vec<RC<str>>),
// Let(RC<str>, Box<UMPL2Expr>),
// }