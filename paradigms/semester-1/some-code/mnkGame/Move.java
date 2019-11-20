package mnkGame;

public final class Move {
    private final int row;
    private final int column;
    private final Cell value;

    public Move(int row, int column, Cell value) {
        this.value = value;
        this.row = row;
        this.column = column;
    }

    public int getRow() {
        return row;
    }
    public int getColumn() {
        return column;
    }
    public Cell getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "row = " + row + " column = " + column + " value = " + value;
    }

}
