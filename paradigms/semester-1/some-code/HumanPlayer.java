package mnkGame;

import java.io.PrintStream;
import java.util.Random;
import java.util.Scanner;

public class HumanPlayer implements Player {
    private final PrintStream out;
    private final Scanner in;
    private final int id;

    public HumanPlayer(int id) {
        this.out = System.out;
        this.in = new Scanner(System.in);
        this.id = id;
    }

    public HumanPlayer() {
        this(new Random().nextInt());
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
                    out.println("Hey, that's not a number, buddy!!");
                    continue;
                }
                if (in.hasNextInt()) {
                    y = in.nextInt();
                    break;
                } else {
                    in.next();
                    out.println("Hey, u better stop making fun of me! Please manage to input NUMBERS!");
                }
            }
            final Move move = new Move(x, y, cell);
            if (position.isValid(move)) {
                return move;
            }
            out.println("Move " + move + " is invalid");
        }
    }

    @Override
    public int getId() {
        return id;
    }
}
