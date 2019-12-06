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

    public Result coolChecker(Move move, int iBegin, int iEnd, int jBegin, int jEnd) {
        int count = 0;
        Cell value = move.getValue();

        while (iBegin < iEnd && jBegin < jEnd) {
            if (cells[iBegin][jBegin] == value) {
                count++;
            } else {
                count = 0;
            }
            if (count == this.target) {
                this.clear();
                return Result.WIN;
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

        int count = 0;
        for (int i = Math.max(0, row - this.target); i < Math.min(row + target, cells.length); i++) {
            if (cells[i][column] == value) {
                count++;
            } else {
                count = 0;
            }
            if (count == this.target) {
                clear();
                return Result.WIN;
            }
        }

        count = 0;
        for (int j = Math.max(0, column - this.target); j < Math.min(column + target, cells[0].length); j++) {
            if (cells[row][j] == value) {
                count++;
            } else {
                count = 0;
            }
            if (count == this.target) {
                clear();
                return Result.WIN;
            }
        }

        count = 0;

        for (int i = Math.max(0, row - this.target + 1), j = Math.max(
                0, column - this.target + 1); i < Math.min(cells.length, row + target) && j < Math.min(
                        cells[0].length, row + target); i++, j++) {
            if (cells[i][j] == value) {
                count++;
            } else {
                count = 0;
            }
            if (count == this.target) {
                clear();
                return Result.WIN;
            }
        }

        count = 0;

        for (int i = Math.max(0, row - this.target), j = Math.min(
                cells[0].length - 1, row + target); i < Math.min(
                        cells.length, row + target) && j >= Math.max(0, column - target); i++, j--) {
            if (cells[i][j] == value) {
                count++;
            } else {
                count = 0;
            }
            if (count == this.target) {
                clear();
                return Result.WIN;
            }
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
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[i].length; j++) {
                builder.append(SYMBOLS.get(cells[i][j]));
                builder.append(" ");
            }
            builder.append('\n');
        }
        return builder.toString();
    }
}