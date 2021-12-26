import java.text.ParseException;

/*
S -> VAR T(A);
A -> P
A -> P, A
P -> CONST VAR T
P -> VAR T
P -> BLANK
T -> VAR
T -> *T
T -> &T
VAR = [a-zA-Z_][a-zA-Z_0-9]*
*/
public class Parser {
    private Lexer lexer;

    private static final String S = "S";
    private static final String T = "T";
    private static final String A = "A";
    private static final String P = "P";

    public Tree parse(String text) throws ParseException {
        lexer = new Lexer(text);
        return S();
    }

    private Tree T() throws ParseException {
        if (lexer.getCurToken() == Token.AMPERSAND) {
            lexer.nextToken();
            return new Tree(T, new Tree("AMPERSAND"), T());
        }
        if (lexer.getCurToken() == Token.ASTERISK) {
            lexer.nextToken();
            return new Tree( T, new Tree("ASTERISK"), T());
        }
        if (lexer.getCurToken() == Token.VAR) {
            Tree t = new Tree(T, "VAR");
            lexer.nextToken();
            return t;
        }
        throw getParseException("*, & or variable/type name");
    }

    private Tree P() throws ParseException {
        if (lexer.getCurToken() == Token.CONST) {
            lexer.nextToken();
            if (lexer.getCurToken() == Token.VAR) {
                lexer.nextToken();
                return new Tree(P, new Tree("CONST"), new Tree("VAR"), T());
            }
            throw new ParseException("type name", lexer.getPosition());
        }

        if (lexer.getCurToken() == Token.VAR) {
            lexer.nextToken();
            return new Tree(P, new Tree("VAR"), T());
        }
        if (lexer.getCurToken() == Token.R_BRACKET) {
            return new Tree(P, new Tree(""));
        }
        throw new ParseException("type name", lexer.getPosition());
    }

    private Tree A() throws ParseException {
        Tree p = P();
        if (lexer.getCurToken() == Token.COMMA) {
            lexer.nextToken();
            return new Tree(A, p, new Tree("COMMA"), A());
        }
        return new Tree(A, p);
    }

    private Tree S() throws ParseException {
        lexer.nextToken();
        if (lexer.getCurToken() == Token.VAR) {
            lexer.nextToken();
            Tree t = T();
            if (lexer.getCurToken() == Token.L_BRACKET) {
                lexer.nextToken();
                Tree a = A();
                if (lexer.getCurToken() == Token.R_BRACKET) {
                    lexer.nextToken();
                    if (lexer.getCurToken() == Token.SEMICOLON) {
                        lexer.nextToken();
                        if (lexer.getCurToken() == Token.END) {
                            return new Tree(S, new Tree("VAR"), t, new Tree("L_BRACKET"), a, new Tree("R_BRACKET"), new Tree("SEMICOLON"));
                        }
                        throw getParseException("end of input");
                    }
                    throw getParseException("semicolon <;>");
                }
                throw getParseException("right bracket <)>");
            }
            throw getParseException("left bracket <(>");
        }
        throw getParseException("type name");
    }

    private ParseException getParseException(String message) {
        return new ParseException("Expected " + message, lexer.getPosition());
    }

}
