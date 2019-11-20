package mnkGame;

import java.io.PrintStream;
import java.util.Scanner;

public class HumanPlayer implements Player {
    private final PrintStream out;
    private final Scanner in;

    public HumanPlayer() {
        this.out = System.out;
        this.in = new Scanner(System.in);
    }

    private boolean isInteger(String s) {
        for (int  i = 0; i < s.length(); ++i) {
            if (s.charAt(i) - '0' < 0 || s.charAt(i) - '0' > 9) {
                return false;
            }
        }
        return true;
    }

    private int readInput() {
        while (true) {
            String s = in.next();
            if (isInteger(s)) {
                return Integer.parseInt(s);
            }
        }
    }

    @Override
    public Move makeMove(final Position position, final Cell cell) {
        while (true) {

            final Move move = new Move(readInput(), readInput(), cell);
            if (position.isValid(move)) {
                return move;
            }
            out.println("Move " + move + " is invalid");
        }
    }
}
