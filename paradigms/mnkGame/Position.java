package mnkGame;

public interface Position {
    boolean isValid(Move move);
    Cell getCell(int row, int column);
}
