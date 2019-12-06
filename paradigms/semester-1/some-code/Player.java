package mnkGame;

public interface Player {
    Move makeMove(Position position, Cell cell);
    int getId();
}
