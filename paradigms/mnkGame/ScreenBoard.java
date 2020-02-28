package mnkGame;

public class ScreenBoard extends MnkBoard {
    private ScreenBoard(int m, int n, int k) {
        super(m, n, k);
    }
    public ScreenBoard(Cell[][] cells) {
        super(1, 1, 1);
        this.cells = new Cell[cells.length][cells[0].length];
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[0].length; j++) {
                this.cells[i][j] = cells[i][j];
            }
        }
    }
}
