import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class LexerTest {
    final String NO_ARGS_INPUT = "type name( )   ;";
    final String TWO_ARGS_INPUT = "type         name(t1 arg1, t2 arg2);";
    final String ASTERISK_TYPE_ARG_INPUT = "char char_func(int *unicorn);";
    final String DOUBLE_ASTERISK_TYPE_ARG_INPUT = "char char_func(int **unicorn);";
    final String STUPID_CPP = "    char * * n()   ; ";
    final String CONST_ARG = "int func(const int x);";

    @Test
    public void constArgTest() {
        List<Token> tokens = extractTokens(CONST_ARG);
        assertEquals(List.of(Token.VAR, Token.VAR, Token.L_BRACKET, Token.CONST, Token.VAR, Token.VAR, Token.R_BRACKET, Token.SEMICOLON), tokens);
    }

    @Test
    public void testStupid() {
        List<Token> tokens = extractTokens(STUPID_CPP);
        assertEquals(List.of(Token.VAR, Token.ASTERISK, Token.ASTERISK, Token.VAR, Token.L_BRACKET, Token.R_BRACKET, Token.SEMICOLON), tokens);
    }

    @Test
    public void testNoArgsInput() {
        List<Token> tokens = extractTokens(NO_ARGS_INPUT);
        assertEquals(List.of(Token.VAR, Token.VAR, Token.L_BRACKET, Token.R_BRACKET, Token.SEMICOLON), tokens);
    }

    @Test
    public void testTwoArgsInput() {
        List<Token> tokens = extractTokens(TWO_ARGS_INPUT);
        assertEquals(
                List.of(Token.VAR, Token.VAR, Token.L_BRACKET, Token.VAR,
                        Token.VAR, Token.COMMA, Token.VAR,
                        Token.VAR, Token.R_BRACKET, Token.SEMICOLON), tokens);
    }

    @Test
    public void testAsteriskTypeInput() {
        List<Token> tokens = extractTokens(ASTERISK_TYPE_ARG_INPUT);
        assertEquals(
                List.of(Token.VAR, Token.VAR, Token.L_BRACKET, Token.VAR,
                        Token.ASTERISK, Token.VAR, Token.R_BRACKET, Token.SEMICOLON), tokens);
    }

    @Test
    public void testDoubleAsteriskTypeInput() {
        List<Token> tokens = extractTokens(DOUBLE_ASTERISK_TYPE_ARG_INPUT);
        assertEquals(
                List.of(Token.VAR, Token.VAR, Token.L_BRACKET, Token.VAR, Token.ASTERISK,
                        Token.ASTERISK, Token.VAR, Token.R_BRACKET, Token.SEMICOLON), tokens);
    }

    private List<Token> extractTokens(String input) {
        List<Token> tokens = new ArrayList<>();
        Lexer lexer = new Lexer(input);
        lexer.nextToken();
        while (lexer.getCurToken() != Token.END) {
            tokens.add(lexer.getCurToken());
            lexer.nextToken();
        }
        return tokens;
    }

}
