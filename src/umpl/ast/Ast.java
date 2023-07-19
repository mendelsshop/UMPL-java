package umpl.ast;

public class Ast {
    
}

// pub enum UMPL2Expr {
//     Bool(Boolean),done
//     Number(f64), done
//     String(RC<str>), done 
//     Scope(Vec<UMPL2Expr>),
//     Ident(RC<str>), done
//     // second 2 are scopes
//     If(Box<If>),
//     // second 2 are scopes
//     Unless(Box<Unless>),
//     Stop(Box<UMPL2Expr>),done
//     Skip, done 
//     // last one is scope
//     Until(Box<Until>),
//     // last one is scope
//     GoThrough(Box<GoThrough>),
//     // last one is scope
//     ContiueDoing(Vec<UMPL2Expr>),
//     // last one is scope
//     Fanction(Fanction),
//     Application(Application),
//     Quoted(Box<UMPL2Expr>), done
//     Label(RC<str>), done
//     FnParam(usize),
//     #[default]
//     Hempty, done
//     Link(RC<str>, Vec<RC<str>>),
//     Tree(Tree),
//     FnKW(FnKeyword),
//     Let(RC<str>, Box<UMPL2Expr>),
// }