package mnkGame;

import java.util.Map;

public class MnkBoard extends Board implements Position, PrivateBoard {
    private static final Map<Cell, Character> SYMBOLS = Map.of(
            Cell.X, 'X',
            Cell.O, 'O',
            Cell.E, '.'
    );

    private final Cell[][] cells;
    private final int target;
    private Cell turn;
    private int blankCells;

    public MnkBoard(int m, int n, int k) {
        if (m <= 0 || n <= 0 || k <= 0) {
            throw new IllegalArgumentException("Dimensions must be represented by positive numbers");
        }
        if (k > Math.max(m, n)) {
            throw new IllegalArgumentException("Unreal game");
        }
        this.target = k;
        cells = new Cell[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                cells[i][j] = Cell.E;
            }
        }
        turn = Cell.X;
        blankCells = m * n;
    }

    private void nextTurn() {
        if (this.turn  == Cell.X) {
            this.turn = Cell.O;
        } else {
            this.turn = Cell.X;
        }
    }

    @Override
    public Position getPosition() {
        return this;
    }

    @Override
    public  Cell getCell() {
        return turn;
    }

    @Override
    public Cell getCell(final int row, final int column) {
        return cells[row][column];
    }

    @Override
    public boolean isValid(Move move) {
        int row = move.getRow();
        int column = move.getColumn();
        return row >= 0 && column >= 0 && row < cells.length
                && column < cells[0].length && cells[row][column] == Cell.E;
    }

    @Override
    public Result makeMove(final Move move) {
        if (!isValid(move)) {
            return Result.LOSE;
        }

        cells[move.getRow()][move.getColumn()] = move.getValue();
        blankCells--;
        nextTurn();
        return checkResult(move);
    }

    private Result multiChecker(Cell value, int i, int iEnd, int j, int jEnd) {
        int count = 0;
        boolean iStatic = (i == iEnd);
        boolean jStatic = (j == jEnd);

        while ((i != iEnd || iStatic ) && (j != jEnd || jStatic)) {
            if (cells[i][j] == value) {
                count++;
            } else {
                count = 0;
            }
            if (count == this.target) {
                this.clear();
                return Result.WIN;
            }
            if (i < iEnd) {
                i++;
            }
            if (i > iEnd) {
                i--;
            }
            if (j < jEnd) {
                j++;
            }
            if (j > jEnd) {
                j--;
            }

        }
        return Result.UNKNOWN;
    }

    public Result checkResult(final Move move) {
        int row = move.getRow();
        int column = move.getColumn();
        Cell value = move.getValue();

        if (blankCells == 0) {
            clear();
            return Result.DRAW;
        }

        Result horizontal = multiChecker(value, Math.max(0, row - this.target), Math.min(row + target, cells.length), column, column);
        if (horizontal == Result.WIN) {
            clear();
            return horizontal;
        }

        Result verticle = multiChecker(value, row, row, Math.max(0, column - this.target), Math.min(column + target, cells[0].length));
        if (verticle == Result.WIN) {
            clear();
            return verticle;
        }

        Result mainDiagonal = multiChecker(value, Math.max(0, row - this.target + 1), Math.min(cells.length, row + target), Math.max(
                0, column - this.target + 1), Math.min(cells[0].length, row + target));
        if (mainDiagonal == Result.WIN) {
            clear();
            return mainDiagonal;
        }

        Result adversDiagonal = multiChecker(value, Math.max(0, row - this.target), Math.min(cells.length, row + target),
                Math.min(cells[0].length - 1, row + target), Math.max(0, column - target) - 1);
        if (adversDiagonal == Result.WIN) {
            clear();
            return adversDiagonal;
        }

        return Result.UNKNOWN;
    }

    private void clear() {
        log(this.toString());
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[0].length; j++) {
                cells[i][j] = Cell.E;
                blankCells = cells.length * cells[0].length;
            }
        }
    }

    private void log(String s) {
        System.out.println(s);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("  ");
        for (int j = 0; j < cells[0].length; j++) {
            builder.append(j).append(" ");
        }
        builder.append('\n');
        for (int i = 0; i < cells.length; i++) {
            builder.append(i).append(" ");
            for (int j = 0; j < cells[i].length; j++) {
                builder.append(SYMBOLS.get(cells[i][j])).append(" ");
            }
            builder.append('\n');
        }
        return builder.toString();
    }
}
