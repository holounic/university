package expression.parser;

import expression.TripleExpression;

public interface Parser {
    TripleExpression parse(String source) throws ParsingException;
}
