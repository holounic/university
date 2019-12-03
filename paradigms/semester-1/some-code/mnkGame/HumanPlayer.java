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

    @Override
    public Move makeMove(final Position position, final Cell cell) {
        int x, y;
        while (true) {
            while (true) {
                if (in.hasNextInt()) {
                    x = in.nextInt();
                } else {
                    in.next();
                    in.next();
                    out.println("Hey, stupid piece of shit, your input is not a number, fuck you!");
                    continue;
                }
                if (in.hasNextInt()) {
                    y = in.nextInt();
                    break;
                } else {
                    in.next();
                    out.println("Hey, stupid piece of shit, your input is not a number, fuck you!");
                }
            }
            final Move move = new Move(x, y, cell);
            if (position.isValid(move)) {
                return move;
            }
            out.println("Move " + move + " is invalid");
        }
    }
}
