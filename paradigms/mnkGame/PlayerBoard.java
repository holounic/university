package mnkGame;

public class PlayerBoard extends Board {
    private MnkBoard board;

    public PlayerBoard(int m, int n, int k) {
        board = new MnkBoard(m, n, k);
    }

    public PlayerBoard() {
        board = new MnkBoard(3, 3, 3);
    }

    public Result makeMove(final Move move) {
        return this.board.makeMove(move);
    }

    public Position getPosition() {
        return board.getPosition();
    }
    public Cell getCell() {
        return board.getCell();
    }
    @Override
    public String toString() {
        return board.toString();
    }


}
