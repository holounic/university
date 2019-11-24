package mnkGame;

public class PlayerBoard extends MnkBoard {

    public PlayerBoard(int m, int n, int k) {
        super(m, n, k);
    }

    public PlayerBoard() {
        super(3, 3, 3);
    }

    @Override
    public Result makeMove(final Move move) {
        return super.makeMove(move);
    }

    @Override
    public Position getPosition() {
        return super.getPosition();
    }
    @Override
    public Cell getCell() {
        return super.getCell();
    }

}
