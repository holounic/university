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


    private int readInput() {
        while (true) {
            String s = in.next();
            try {
                return Integer.parseInt(s);
            } catch (NumberFormatException e) {
                out.println("Hey, stupid piece of shit, your input is not a number, fuck you!");
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
