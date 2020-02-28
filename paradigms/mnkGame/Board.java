package mnkGame;

public abstract class Board {
    protected abstract Position getPosition();
    protected abstract Cell getCell();
    protected abstract Result makeMove(Move move);
}
